package org.tis.senior.module.developer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.controller.request.DeliveryListAndDeliveryAddRequest;
import org.tis.senior.module.developer.controller.request.DeliveryListSuperadditionRequest;
import org.tis.senior.module.developer.controller.request.DeliveryProfileRequest;
import org.tis.senior.module.developer.controller.request.SDliveryAddRequest;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.DeliveryListStashListDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.tis.senior.module.developer.util.DeveloperUtils;
import org.tmatesoft.svn.core.SVNException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sDeliveryList的Service接口实现类
 *
 * @author lijh
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SDeliveryListServiceImpl extends ServiceImpl<SDeliveryListMapper, SDeliveryList> implements ISDeliveryListService {


    @Autowired
    private ISSvnKitService svnKitService;

    @Autowired
    private ISProjectService projectService;

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISDeliveryService deliveryService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Autowired
    private ISStandardListService standardListService;

    @Autowired
    private ISProfilesService profilesService;

    @Autowired
    private ISWorkitemService workitemService;

    @Autowired
    private ISCheckService checkService;

    @Autowired
    private ISStashListService stashListService;

    @Override
    public DeliveryListStashListDetail assembleDelivery(String branchGuid){

        SBranch branch = branchService.selectById(branchGuid);
        if (branch == null) {
            throw new DeveloperException("查询不到此分支！");
        }
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,branch.getGuid());
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(branchMappings.size() != 1){
            throw new DeveloperException("查询不到此"+branchGuid+"关联的工作项！");
        }
        SWorkitem workitem = workitemService.selectById(branchMappings.get(0).getGuidOfWhats());
        //查询所有的工程
        List<SProject> projectList = projectService.selectProjectAll();
        Map<String, SProject> projectMap = projectList.stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));

        List<SvnFile> svnCommits = null;
        try {
            svnCommits = svnKitService.getBranchDiffStatus(branch.getFullPath(), branch.getCurrVersion().toString());
        } catch (SVNException e) {
            throw new DeveloperException("分支的svnUrl不合法，无法获取提交记录！");
        }
        if (svnCommits.size() < 1) {
            throw new DeveloperException("该清单已被整理或没有最新的提交记录!");
        }
        List<SDeliveryList> sdList = new ArrayList<>();
        Map<String, List<SvnFile>> commitMap = svnCommits.stream().collect(Collectors.groupingBy(SvnFile::getNodeType));
        Set<String> ecdSet = new HashSet<>();
        if (commitMap.get("dir") != null) {
            commitMap.get("dir").forEach(f -> {
                String projectName = DeveloperUtils.getProjectName(f.getPath(),branch.getFullPath());
                if (StringUtils.isNotBlank(projectName)) {
                    SProject project = projectMap.get(projectName);
                    if (project == null) {
                        throw new DeveloperException("基础参数中没有"+ projectName +"此工程，如要整理清单，请联系rct组创建此工程！");
                    }
                    JSONArray jsonArray = JSONArray.parseArray(project.getDeployConfig());
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        if ("ecd".equals(exportType)) {
                            String module = DeveloperUtils.getModule(f.getPath(), branch.getFullPath());
                            if (StringUtils.isNoneBlank(module) && f.getType().equals(CommitType.ADDED)) {

                                String path = f.getPath();
                                String subPath = path.substring(0, path.indexOf(path) + path.length());
                                if (subPath.equals(f.getPath())) {
                                    ecdSet.add(projectName+"/"+module);
                                }
                            }
                        }
                    }
                }
            });
        }
        if (commitMap.get("file") != null) {
            commitMap.get("file").forEach(svnFile -> {
                SDeliveryList sdl = new SDeliveryList();
                sdl.setCommitType(svnFile.getType());
                sdl.setFullPath(DeveloperUtils.getPathUTF(svnFile.getPath()));
                sdl.setFromType(DeliveryListFromType.BRANCH);
                String programName = DeveloperUtils.getProgramName(svnFile.getPath());
                sdl.setProgramName(programName);
                String projectName = DeveloperUtils.getProjectName(svnFile.getPath(),branch.getFullPath());
                SProject sProject = projectMap.get(projectName);
                if (sProject == null) {
                    throw new DeveloperException("基础参数中没有"+ projectName +"此工程，如要整理清单，请联系rct组创建此工程！");
                }
                sdl.setPartOfProject(sProject.getProjectName());
                String deployConfig = sProject.getDeployConfig();
                if(ProjectType.IBS.equals(sProject.getProjectType())) {
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    //here  跳出循环的标记
                    here:
                    for (Object object : jsonArray) {
                        //解析json字符串获取导出类型和部署到那
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        if (exportType.equals("ecd")) {
                            if (ecdSet.size() > 0) {
                                for (String ecd : ecdSet) {
                                    if (svnFile.getPath().contains(ecd)) {
                                        sdl.setPatchType(exportType);
                                        sdl.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(exportType, deployConfig));
                                        break here;
                                    }
                                }
                            }
                        } else {
                            sdl.setPatchType(exportType);
                            sdl.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(exportType, deployConfig));
                        }
                    }
                }else{
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    String exportType = "";
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        if("".equals(exportType)){
                            exportType = jsonObject.getString("exportType");
                        }else{
                            exportType = exportType + "," + jsonObject.getString("exportType");
                        }
                    }
                    sdl.setPatchType(exportType);
                    sdl.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(exportType, deployConfig));
                }
                sdList.add(sdl);
            });
        }
        //保存需要删除的贮藏清单代码
        List<SStashList> deletesStashList = new ArrayList<>();
        //保存需要删除整理的清单代码
        List<SDeliveryList> deleteDeliveryLists = new ArrayList<>();
        //保存需要修改整理的清单代码
        List<SDeliveryList> updateDeliveryLists = new ArrayList<>();

        //查询贮藏清单列表
        EntityWrapper<SStashList> stashListEntityWrapper = new EntityWrapper<>();
        stashListEntityWrapper.eq(SStashList.COLUMN_GUID_WORKITEM, workitem.getGuid());
        List<SStashList> sStashLists = stashListService.selectList(stashListEntityWrapper);

        List<SDeliveryList> stashLists = new ArrayList<>();
        if(sStashLists.size() > 0){
            //遍历整理的清单代码
            for (SDeliveryList deliveryList:sdList){
                //遍历贮藏的清单代码
                for(SStashList stashList:sStashLists){
                    //判断贮藏清单和整理的清单的文件是否相同
                    if((stashList.getFullPath()).equals(deliveryList.getFullPath())){
                        //判断贮藏清单和整理的清单的文件提交类型是否相同
                        if(!deliveryList.getCommitType().equals(stashList.getCommitType())){
                            // 原-现—终 (修改类型的变化)
                            // D  A  M
                            // M  A  M 理论上不出现，如果发生抛出异常
                            // A  D  X 移除
                            // M  D  D
                            // A  M  A 不处理
                            // D  M  M 理论上不出现，如果发生抛出异常// D  A  M
                            // A  A  A 不处理
                            // M  M  M 不处理
                            // D  D  D 不处理
                            if(deliveryList.getCommitType().equals(CommitType.ADDED) &&
                                    stashList.getCommitType().equals(CommitType.DELETED)){
                                deliveryList.setCommitType(CommitType.MODIFIED);
                                updateDeliveryLists.add(deliveryList);
                            }else if(deliveryList.getCommitType().equals(CommitType.DELETED) &&
                                    stashList.getCommitType().equals(CommitType.ADDED)){
                                deleteDeliveryLists.add(deliveryList);
                            }else if(deliveryList.getCommitType().equals(CommitType.MODIFIED) &&
                                    stashList.getCommitType().equals(CommitType.ADDED)){
                                deliveryList.setCommitType(CommitType.ADDED);
                                updateDeliveryLists.add(deliveryList);
                            }else if(deliveryList.getCommitType().equals(CommitType.DELETED) &&
                                    stashList.getCommitType().equals(CommitType.MODIFIED)){

                            } else {
                                throw new DeveloperException("整理的清单与贮藏的清单提交类型不符合规则！");
                            }
                        }
                        //保存与整理清单相同的文件
                        deletesStashList.add(stashList);
                        //跳出到本次循环的标识
                        break;
                    }
                }
            }
            if(deletesStashList.size() > 0){
                //移除贮藏与整理出的清单相同的文件
                sStashLists.removeAll(deletesStashList);
            }
            if(sStashLists.size() > 0){
                //将SStashList对象值copy到SDeliveryList对象中
                sStashLists.forEach(stash ->{
                    SDeliveryList delivery = new SDeliveryList();
                    BeanUtils.copyProperties(stash,delivery);
                    stashLists.add(delivery);
                });
            }

        }
        //移除相同文件：整理出的清单提交类型为删除，贮藏清单为新增的相同文件。
        sdList.removeAll(deleteDeliveryLists);
        List<SDeliveryList> deliveryLists = new ArrayList<>();
        //遍历移除相同文件后的整理出的清单集合
        for(SDeliveryList sd:sdList){
            //遍历整理出的清单需要修改的文件
            for(SDeliveryList sdl:updateDeliveryLists){
                if(sdl.getFullPath().equals(sd.getFullPath())){
                    sd.setCommitType(sdl.getCommitType());
                    break;
                }
            }
            deliveryLists.add(sd);
        }
        DeliveryListStashListDetail detail = new DeliveryListStashListDetail();

        List<DeliveryProjectDetail> deliveryDetail = DeliveryProjectDetail.
                        getDeliveryDetail(deliveryLists, projectList);
        detail.setDeliveryDetail(deliveryDetail);
        if(stashLists.size() > 0){
            List<DeliveryProjectDetail> stashDetail = DeliveryProjectDetail.
                    getDeliveryDetail(stashLists, projectList);
            detail.setStashDetail(stashDetail);
        }else{
            detail.setStashDetail(null);
        }
        return detail;
    }

    @Override
    public List<SDelivery> addDeliveryList(DeliveryListAndDeliveryAddRequest request, String userId) throws SVNException, ParseException {
        if(request.getDliveryAddRequest().getProfiles().size() == 0){
            throw new DeveloperException("请选择要投放的环境及打包窗口再投放！");
        }
        //判断投放时间及投放窗口是否合理
        Date date = new Date();
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
        for(DeliveryProfileRequest profile:request.getDliveryAddRequest().getProfiles()){
            if (ymdFormat.format(profile.getDeliveryTime()).equals(ymdFormat.format(date))){
                //投放窗口的时间戳
                long time1 = hmFormat.parse(profile.getPackTiming()).getTime();
                //当前时间的时间戳
                long time2 = hmFormat.parse(hmFormat.format(date)).getTime();
                if(time2 > time1){
                    throw new DeveloperException("你投放的窗口已经过期，请选择下一个窗口投放！");
                }
            }else{
                //将投放时间转成时间戳
                long deliverTime1 = ymdFormat.parse(ymdFormat.format(profile.getDeliveryTime())).getTime();
                //当前时间的时间戳
                long time2 = ymdFormat.parse(ymdFormat.format(profile.getDeliveryTime())).getTime();
                if(time2 > deliverTime1){
                    throw new DeveloperException("你选择的投放日期已是过去日期，请重新投放时间！");
                }
            }
        }
        //查询所有工程
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));
        //判断是否是新投放
        EntityWrapper<SDelivery> deliverysEntityWrapper = new EntityWrapper<>();
        deliverysEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
        deliverysEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.APPLYING);
        List<SDelivery> sDeliveries = deliveryService.selectList(deliverysEntityWrapper);
        if(sDeliveries.size() > 0){
            throw new DeveloperException("你有投放申请正在申请中，如要投放，请追加投放！");
        }
        //创建已选择的环境的投放申请中的运行环境guid集合
        List<Integer> choiceProfileGuid = request.getDliveryAddRequest().getProfiles().stream().map(
                DeliveryProfileRequest::getGuidProfiles).collect(Collectors.toList());
        //获取投放日期以及具体打包窗口是否已经完成投放
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        int delIndex = 0;
        for (DeliveryProfileRequest profileRequest:request.getDliveryAddRequest().getProfiles()){
            deliveryWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.DELIVERED);
            deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                    new SimpleDateFormat("yyyy-MM-dd").format(profileRequest.getDeliveryTime()));
            deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING,profileRequest.getPackTiming());
            deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES,profileRequest.getGuidProfiles());
            delIndex++;
            if(delIndex != request.getDliveryAddRequest().getProfiles().size()){
                deliveryWrapper.orNew();
            }
        }
        List<SDelivery> deliveries = deliveryService.selectList(deliveryWrapper);
                if(deliveries.size() > 0){
            throw new DeveloperException("你选择的投放环境对应的打包窗口已完成投放，请选择其他时间投放！");
        }

        //获取已成功合并的投放申请
        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
        deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.DELIVERED);
        List<SDelivery> deliverys = deliveryService.selectList(deliveryEntityWrapper);
        if(deliverys.size() == 0 && request.getDeliveryList().size() == 0){
            throw new DeveloperException("没有要投放的代码！");
        }
        //新增投放申请列表
        List<SDelivery> deliveryList = new ArrayList<>();
        SDliveryAddRequest dliveryAddRequest = request.getDliveryAddRequest();
        List<DeliveryProfileRequest> guidPro = dliveryAddRequest.getProfiles();
        for (DeliveryProfileRequest req : guidPro) {
            for (SDelivery delivery:deliverys){
                if(req.getGuidProfiles().equals(delivery.getGuidProfiles()) && request.getDeliveryList().size() == 0){
                    throw new DeveloperException("没有新的提交清单，只能向新环境投放申请！");
                }
            }
            //组装投放申请
            SDelivery delivery = new SDelivery();
            delivery.setApplyAlias(req.getApplyAlias());
            delivery.setGuidWorkitem(request.getGuidWorkitem());
            if(profilesService.selectById(req.getGuidProfiles()) == null){
                throw new DeveloperException("投放的环境不存在，请重新选择环境！");
            }
            delivery.setGuidProfiles(req.getGuidProfiles());
            delivery.setDeliveryType(DeliveryType.GENERAL);
            delivery.setProposer(userId);
            delivery.setApplyTime(new Date());
            delivery.setPackTiming(req.getPackTiming());
            delivery.setDeliveryTime(req.getDeliveryTime());
            delivery.setDeliveryResult(DeliveryResult.APPLYING);
            deliveryList.add(delivery);
        }
        //集合添加核对中及核对成功状态，控制投放申请的环境是否可投放
        List<CheckStatus> checkStatuses = new ArrayList<>();
        checkStatuses.add(CheckStatus.WAIT);
        checkStatuses.add(CheckStatus.SUCCESS);
        //获取这个投放申请的运行环境是否有正在核对中
        EntityWrapper<SCheck> checkEntityWrapper = new EntityWrapper<>();
        int index = 0;
        for(DeliveryProfileRequest profileRequest:request.getDliveryAddRequest().getProfiles()){
            checkEntityWrapper.eq(SCheck.COLUMN_GUID_PROFILES, profileRequest.getGuidProfiles());
            checkEntityWrapper.in(SCheck.COLUMN_CHECK_STATUS, checkStatuses);
            checkEntityWrapper.eq(SCheck.COLUMN_PACK_TIMING, profileRequest.getPackTiming());
            checkEntityWrapper.eq("DATE_FORMAT(" + SCheck.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                    new SimpleDateFormat("yyyy-MM-dd").format(profileRequest.getDeliveryTime()));
            index++;
            if(index != request.getDliveryAddRequest().getProfiles().size()){
                checkEntityWrapper.orNew();
            }
        }
        if(checkService.selectList(checkEntityWrapper).size() > 0){
            throw new DeveloperException("你本次投放的环境窗口有申请正在核对中！");
        }
        //批量添加投放申请
        deliveryService.insertBatch(deliveryList);
        //添加标准清单的数组
        List<SDeliveryList> standardlList = new ArrayList<>();
        //获取分支详情
        SBranch branch= branchService.selectById(request.getGuidBranch());
        //判断此工作项是否有成功投放的申请记录
        if (deliverys.size() > 0) {
            List<Integer> achieveProfileGuid = new ArrayList<>();
            //创建工作项已成功投放申请的运行环境guid
            for (SDelivery delivery:deliverys) {
                achieveProfileGuid.add(delivery.getGuidProfiles());
            }
            //移除已成功投放运行环境的投放申请
            choiceProfileGuid.removeAll(achieveProfileGuid);
            if(choiceProfileGuid.size() > 0){
                //保存新投放代码清单的代码全路径
                List<String> fullPathList = request.getDeliveryList().stream().map(
                        SDeliveryList::getFullPath).collect(Collectors.toList());
                //获取工作项的标准清单记录
                EntityWrapper<SStandardList> standardListEntityWrapper = new EntityWrapper<>();
                standardListEntityWrapper.eq(SStandardList.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
                //去投放清单标准清单除本次与投放清单重复项
                if(fullPathList.size() > 0){
                    standardListEntityWrapper.notIn(SStandardList.COLUMN_FULL_PATH,fullPathList);
                }
                List<SStandardList> sStandardLists = standardListService.selectList(standardListEntityWrapper);
                if(sStandardLists.size() > 0){
                    //此次的投放申请选择的环境过滤掉已成功投放的环境后的投放申请集合
                    List<SDelivery> sDeliveries1 = deliveryList.stream().filter(sDelivery -> choiceProfileGuid.contains(
                            sDelivery.getGuidProfiles())).collect(Collectors.toList());
                    //组装并新增标准清单内容
                    for(SDelivery delivery:sDeliveries1){
                        for(SStandardList sStandard:sStandardLists){
                            SDeliveryList sdl = new SDeliveryList();
                            BeanUtils.copyProperties(sStandard,sdl);
                            sdl.setGuidDelivery(delivery.getGuid());
                            sdl.setGuid(null);
                            sdl.setFromType(DeliveryListFromType.STANDARD);
                            standardlList.add(sdl);
                        }
                    }
                    insertBatch(standardlList);
                }
            }
        }
        //新增对应的投放申请的代码清单
        if(request.getDeliveryList().size() != 0) {
            for (SDelivery sDelivery : deliveryList) {
                List<SDeliveryList> deliveryLists = new ArrayList<>();
                //组装投产代码清单
                for (SDeliveryList dlar : request.getDeliveryList()) {
                    if(standardlList.size() > 0){
                        for(SDeliveryList standardList:standardlList){
                            //判断标准清单里是否有代码清单导出为ECD
                            if(PatchType.ECD.equals(standardList.getPatchType())){
                                //获取截取路径到模块前面的字符
                                String subPath = standardList.getFullPath().substring(
                                        0,standardList.getFullPath().indexOf(DeveloperUtils.getModule(
                                                branch.getFullPath(), standardList.getFullPath())));
                                /*
                                    如果标准清单的工程模块导出为ecd，这次投放的代码清单也是标准清单模块下的文件，那么将
                                    这个投放代码的导出变更标准清单的导出类型
                                 */
                                if(dlar.getFullPath().contains(subPath) && PatchType.EPD.equals(dlar.getPatchType())){
                                    dlar.setPatchType(standardList.getPatchType());
                                }
                            }
                        }
                    }
                    dlar.setGuidDelivery(sDelivery.getGuid());
                    if(projectMap.get(dlar.getPartOfProject()).getProjectType().equals(ProjectType.SPECIAL)){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(dlar.getPatchType(),dlar.getDeployWhere());
                        dlar.setDeployWhere(jsonObject.toString());
                    }else{
                        dlar.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(
                                dlar.getPatchType(), projectMap.get(dlar.getPartOfProject()).getDeployConfig()));
                    }
                    deliveryLists.add(dlar);
                }
                //批量添加此次投放的代码清单
                insertBatch(deliveryLists);
            }
            //将版本号
            branchService.recordBranchTempRevision(request.getGuidBranch());
        }

        //先清除此工作项贮藏清单的所有内容
        EntityWrapper<SStashList> stashListEntityWrapper = new EntityWrapper<>();
        stashListEntityWrapper.eq(SStashList.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
        List<SStashList> sStashLists = stashListService.selectList(stashListEntityWrapper);
        if(sStashLists.size() > 0){
            List<Integer> guidStash = sStashLists.stream().map(SStashList::getGuid).collect(Collectors.toList());
            stashListService.deleteBatchIds(guidStash);
        }
        //保存未选择的代码清单
        if(request.getStashList().size() > 0){
            List<SStashList> sStashList = new ArrayList<>();
            for (SStashList stash:request.getStashList()){
                stash.setGuidWorkitem(request.getGuidWorkitem());
                sStashList.add(stash);
            }
            stashListService.insertBatch(sStashList);
        }
        return deliveryList;
    }

    /*@Override
    public List<SDeliveryList> selectDeliveryListOutPutExcel(Integer guidWorkitem, Integer guidProfiles) {

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, guidProfiles);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM, guidWorkitem);
        List<SDelivery> sdList = deliveryService.selectList(deliveryEntityWrapper);

        List<Integer> guidDelivery = new ArrayList<>();
        for (SDelivery delivery : sdList) {
            guidDelivery.add(delivery.getGuid());
        }

        if (guidDelivery.size() == 0) {
            throw new DeveloperException("此工作项查询不到对应的投放申请!");
        }

        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, guidDelivery);
        List<SDeliveryList> sdlList = selectList(deliveryListEntityWrapper);

        if(sdlList.size() == 0){
            throw new DeveloperException("此投放申请没有要导出的代码清单！");
        }
        return disposeExportExcel(sdlList);
    }*/

    @Override
    public List<SDeliveryList> selectDeliveryListExcel(Integer guidDelivery) {
        SDelivery delivery = deliveryService.selectById(guidDelivery);
        if(delivery == null){
            throw new DeveloperException("没有找到对应的投放申请！");
        }
        SWorkitem workitem = workitemService.selectById(delivery.getGuidWorkitem());
        if(workitem == null){
            throw new DeveloperException("没有找到对应的工作项信息！");
        }

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,workitem.getGuid());
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);

        SBranch branch = branchService.selectById(branchMappings.get(0).getGuidBranch());

        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY, delivery.getGuid());
        deliveryListEntityWrapper.ne(SDeliveryList.COLUMN_COMMIT_TYPE, CommitType.DELETED);
        List<SDeliveryList> sdlList = selectList(deliveryListEntityWrapper);

        if(sdlList.size() == 0){
            throw new DeveloperException("此投放申请没有要导出的代码清单！");
        }
        return disposeExportExcel(sdlList, branch.getFullPath());
    }

    @Override
    public List<SDelivery> addToDeliveryList(DeliveryListSuperadditionRequest request) throws SVNException {

        if(request.getDeliveryList().size() == 0){
            throw new DeveloperException("请选择需要追加的投放代码清单！");
        }
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,request.getGuidWorkitem());
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(branchMappings.size() == 0){
            throw new DeveloperException("此工作项没有分支信息，可能已取消关联！");
        }
        //查询所有工程
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));

        //获取追加申请前的代码清单
        EntityWrapper<SDeliveryList> sDeliveryListEntityWrapper = new EntityWrapper<>();
        sDeliveryListEntityWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, request.getGuidDelivery());
        List<SDeliveryList> deliveryLists = selectList(sDeliveryListEntityWrapper);
        if(deliveryLists.size() == 0){
            throw new DeveloperException("追加的投放申请正在被处理！");
        }
        // 接受需要新增或修改的投放清单
        List<SDeliveryList> insertOrUpdate = new ArrayList<>();
        // 接受需要删除的投放清单ID
        List<Integer> deletes = new ArrayList<>();
        for (Integer guidDelivery:request.getGuidDelivery()){
            //sd  跳出循环的标记
            sd:
            for (SDeliveryList deliveryList:request.getDeliveryList()){
                for(SDeliveryList dl:deliveryLists){
                    if(guidDelivery.equals(dl.getGuidDelivery())){
                        //判断文件是否是相同的
                        if(deliveryList.getFullPath().equals(dl.getFullPath())){
                            if(!deliveryList.getCommitType().equals(dl.getCommitType())){
                                // 原-现—终 (修改类型的变化)
                                // D  A  M
                                // M  A  M 理论上不出现，如果发生抛出异常
                                // A  D  X 移除
                                // M  D  D
                                // A  M  A 不处理
                                // D  M  M 理论上不出现，如果发生抛出异常// D  A  M
                                // A  A  A 不处理
                                // M  M  M 不处理
                                // D  D  D 不处理
                                if (dl.getCommitType().equals(CommitType.DELETED) &&
                                        deliveryList.getCommitType().equals(CommitType.ADDED)) {
                                    dl.setCommitType(CommitType.MODIFIED);
                                    insertOrUpdate.add(dl);
                                }  else if (dl.getCommitType().equals(CommitType.ADDED) &&
                                        deliveryList.getCommitType().equals(CommitType.DELETED)) {
                                    deletes.add(dl.getGuid());
                                } else if (dl.getCommitType().equals(CommitType.MODIFIED) &&
                                        deliveryList.getCommitType().equals(CommitType.DELETED)) {
                                    dl.setCommitType(CommitType.DELETED);
                                    insertOrUpdate.add(dl);
                                } else if (dl.getCommitType().equals(CommitType.ADDED) &&
                                        deliveryList.getCommitType().equals(CommitType.MODIFIED)) {
                                } else {
                                    throw new DeveloperException("追加投放发生了预料之外的异常！");
                                }
                            }
                            continue sd;
                        }
                    }
                }
                deliveryList.setGuidDelivery(guidDelivery);
                deliveryList.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(
                        deliveryList.getPatchType(), projectMap.get(deliveryList.getPartOfProject()).getDeployConfig()));
                insertOrUpdate.add(deliveryList);
            }
        }
        if(insertOrUpdate.size() > 0){
            insertOrUpdateBatch(insertOrUpdate);
        }
        if(deletes.size() > 0){
            deleteBatchIds(deletes);
        }
        branchService.recordBranchTempRevision(branchMappings.get(0).getGuidBranch());

        //获取本次追加投放申请的详情
        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.in(SDelivery.COLUMN_GUID,request.getGuidDelivery());
        List<SDelivery> deliverys = deliveryService.selectList(deliveryEntityWrapper);
        return deliverys;
    }

    /*@Override
    public void fillDeliveryList(String path, SDeliveryList deliveryList) {
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));

        String projectName = DeveloperUtils.getProjectName(path);

        SProject project = projectMap.get(projectName);
        if(project == null ){
            throw new DeveloperException("找不到此工程");
        }
        String exportType = "";
        String deployType = "";
        JSONArray jsonArray = JSONArray.parseArray(project.getDeployConfig());
        for (Object object : jsonArray) {
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            if(exportType == ""){
                exportType = jsonObject.getString("exportType");
            }else{
                exportType = exportType + "," + jsonObject.getString("exportType");
            }

            deployType = jsonObject.getString("deployType");
        }
        deliveryList.setPatchType(exportType);
        deliveryList.setDeployWhere(deployType);

    }*/

    private List<SDeliveryList> disposeExportExcel(List<SDeliveryList> sdlList, String branchPath){
        List<SDeliveryList> deliveryLists = new ArrayList<>();

        sdlList.stream().collect(Collectors.groupingBy(SDeliveryList::getPatchType)).forEach((p, list) -> {
            if (p.equals(PatchType.ECD.getValue())) {
                list.stream().collect(Collectors.groupingBy(SDeliveryList::getPartOfProject,
                        Collectors.groupingBy(dl -> DeveloperUtils.getModule(dl.getFullPath(), branchPath))))
                        .forEach((pj, m) -> m.forEach((module, l) -> {
                            // 导出ecd 的同工程清单
                            JSONObject jsonObject = JSONObject.parseObject(l.get(0).getDeployWhere());
                            String [] deployWhereSplit = jsonObject.getString(l.get(0).getPatchType()).split(",");

                            for (String deployWhere : deployWhereSplit) {
                                SDeliveryList sdl = new SDeliveryList();
                                sdl.setPatchType(l.get(0).getPatchType());
                                sdl.setDeployWhere(deployWhere);
                                sdl.setPartOfProject(pj);
                                sdl.setFullPath(DeveloperUtils.getEcdPath(l.get(0).getFullPath(), branchPath));
                                deliveryLists.add(sdl);
                            }
                        }));
            } else {
                for (SDeliveryList sd : list) {
                    JSONObject jsonObject = JSONObject.parseObject(sd.getDeployWhere());
                    String[] patchTypeSplit = sd.getPatchType().split(",");
                    for (String patchType : patchTypeSplit) {
                        String deployWhere = jsonObject.getString(patchType);
                        String[] deployWhereSplit = deployWhere.split(",");
                        for (String dw : deployWhereSplit) {
                            SDeliveryList sdl = new SDeliveryList();
                            sdl.setPatchType(patchType);
                            sdl.setDeployWhere(dw);
                            sdl.setPartOfProject(sd.getPartOfProject());
                            sdl.setFullPath(DeveloperUtils.getFilePath(sd.getFullPath(),branchPath));
                            deliveryLists.add(sdl);
                        }
                    }

                }
            }
        });
        return deliveryLists;
    }



}

