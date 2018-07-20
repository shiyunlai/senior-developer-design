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
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.tis.senior.module.developer.util.DeveloperUtils;
import org.tmatesoft.svn.core.SVNException;

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

    @Override
    public List<DeliveryProjectDetail> assembleDelivery(String branchGuid){

        SBranch branch = branchService.selectById(branchGuid);
        if (branch == null) {
            throw new DeveloperException("查询不到此分支！");
        }
        //查询所有的工程
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
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
                String projectName = DeveloperUtils.getProjectName(f.getPath());
                if (StringUtils.isNotBlank(projectName)) {
                    SProject project = projectMap.get(projectName);
                    if (project == null) {
                        throw new DeveloperException("基础参数中没有"+ projectName +"此工程，如要整理清单，请在工程参数中添加此工程！");
                    }
                    JSONArray jsonArray = JSONArray.parseArray(project.getDeployConfig());
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        if ("ecd".equals(exportType)) {
                            String eoe = DeveloperUtils.getModule(f.getPath());
                            if (StringUtils.isNoneBlank(eoe) && f.getType().equals(CommitType.ADDED)) {

                                String path = f.getPath();
                                String subPath = path.substring(0, path.indexOf(path) + path.length());
                                if (subPath.equals(f.getPath())) {
                                    ecdSet.add(projectName+"/"+eoe);
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
                String projectName = DeveloperUtils.getProjectName(svnFile.getPath());
                SProject sProject = projectMap.get(projectName);
                if (sProject == null) {
                    throw new DeveloperException("基础参数中没有"+ projectName +"+此工程，如要整理清单，请在工程参数中添加此工程！");
                }
                sdl.setPartOfProject(sProject.getProjectName());
                String deployConfig = sProject.getDeployConfig();
                if(ProjectType.IBS.equals(sProject.getProjectType())) {
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    //here  跳出循环的标记
                    here:
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        String deployWhere = jsonObject.getString("deployWhere");
                        if (exportType.equals("ecd")) {
                            if (ecdSet.size() > 0) {
                                for (String ecd : ecdSet) {
                                    if (svnFile.getPath().contains(ecd)) {
                                        sdl.setPatchType(exportType);
                                        sdl.setDeployWhere(deployWhere);
                                        break here;
                                    }
                                }
                            }
                        } else {
                            sdl.setPatchType(exportType);
                            sdl.setDeployWhere(deployWhere);;
                        }
                    }
                }else{
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    String exportType = "";
                    Set<String> setStr = new HashSet<>();
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        if(exportType == ""){
                            exportType = jsonObject.getString("exportType");
                        }else{
                            exportType = exportType + "," + jsonObject.getString("exportType");
                        }
                        String deployType = jsonObject.getString("deployType");
                        Collections.addAll(setStr,deployType.split(","));
                    }
                    sdl.setPatchType(exportType);
                    String deploy = setStr.toString().replace("[","").replace("]","");
                    sdl.setDeployWhere(deploy);
                }
                sdList.add(sdl);
            });
        }
        return DeliveryProjectDetail.getDeliveryDetail(sdList, projectService.selectProjectAll());
    }

    @Override
    public List<SDelivery> addDeliveryList(DeliveryListAndDeliveryAddRequest request, String userId) throws SVNException {

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


        EntityWrapper<SBranchMapping> sbmEntityWrapper = new EntityWrapper<>();
        sbmEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH, request.getGuidBranch());
        List<SBranchMapping> sbmList = branchMappingService.selectList(sbmEntityWrapper);
        if (sbmList.size() != 1) {
            throw new DeveloperException("查询不到对应的关联数据！");
        }
        SBranchMapping sbm = sbmList.get(0);

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
        //创建已选择的环境的投放申请中的运行环境guid集合
        ArrayList<Integer> choiceProfileGuid = new ArrayList<>();
        for (DeliveryProfileRequest req : guidPro) {
            for (SDelivery delivery:deliverys){
                if(req.getGuidProfiles().equals(delivery.getGuidProfiles()) && request.getDeliveryList().size() == 0){
                    throw new DeveloperException("没有新的提交清单，只能向新环境投放申请！");
                }
            }
            //组装投放申请
            SDelivery delivery = new SDelivery();
            delivery.setApplyAlias(req.getApplyAlias());
            delivery.setGuidWorkitem(sbm.getGuidOfWhats());
            if(profilesService.selectById(req.getGuidProfiles()) == null){
                throw new DeveloperException("投放的环境不存在，请重新选择环境！");
            }
            //保存这次投放的环境guid
            choiceProfileGuid.add(req.getGuidProfiles());
            delivery.setGuidProfiles(req.getGuidProfiles());
            delivery.setDeliveryType(DeliveryType.GENERAL);
            delivery.setProposer(userId);
            delivery.setApplyTime(new Date());
            delivery.setPackTiming(req.getPackTiming());
            delivery.setDeliveryTime(dliveryAddRequest.getDeliveryTime());
            delivery.setDeliveryResult(DeliveryResult.APPLYING);
            deliveryList.add(delivery);
        }

        EntityWrapper<SDelivery> deliveryEntityWrapper1 = new EntityWrapper<>();
        deliveryEntityWrapper1.in(SDelivery.COLUMN_GUID_PROFILES,choiceProfileGuid);
        deliveryEntityWrapper1.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.CHECKING);
        deliveryEntityWrapper1.eq(SDelivery.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
        if(deliveryService.selectList(deliveryEntityWrapper1).size() > 0){
            throw new DeveloperException("你本次投放的环境有申请正在核对中，请等一等再申请！");
        }
        deliveryService.insertBatch(deliveryList);

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
                //获取工作项的标准清单记录
                EntityWrapper<SStandardList> standardListEntityWrapper = new EntityWrapper<>();
                standardListEntityWrapper.eq(SStandardList.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
                List<SStandardList> sStandardLists = standardListService.selectList(standardListEntityWrapper);

                if(sStandardLists.size() > 0){
                    //重新查询移除后已成功投放运行环境的投放申请
                    EntityWrapper<SDelivery> sDeliveryEntityWrapper = new EntityWrapper<>();
                    sDeliveryEntityWrapper.in(SDelivery.COLUMN_GUID_PROFILES,choiceProfileGuid);
                    sDeliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,request.getGuidWorkitem());
                    List<SDelivery> sdList = deliveryService.selectList(sDeliveryEntityWrapper);
                    //新增标准清单内容
                    List<SDeliveryList> sdlList = new ArrayList<>();
                    for(SDelivery sd:sdList){
                        for(SStandardList sStandard:sStandardLists){
                            SDeliveryList sdl = new SDeliveryList();
                            BeanUtils.copyProperties(sStandard,sdl);
                            sdl.setGuidDelivery(sd.getGuid());
                            sdl.setGuid(null);
                            sdl.setFromType(DeliveryListFromType.STANDARD);
                            sdlList.add(sdl);
                        }
                    }
                    insertBatch(sdlList);
                }

            }

        }

        //新增对应的投放申请的代码清单
        if(request.getDeliveryList().size() != 0) {
            for (SDelivery sDelivery : deliveryList) {
                List<SDeliveryList> deliveryLists = new ArrayList<>();
                //组装投产代码清单
                for (SDeliveryList dlar : request.getDeliveryList()) {
                    dlar.setGuidDelivery(sDelivery.getGuid());
                    dlar.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(
                            dlar.getPatchType(), projectMap.get(dlar.getPartOfProject()).getDeployConfig()));
                    deliveryLists.add(dlar);
                }
                insertBatch(deliveryLists);
            }
            branchService.recordBranchTempRevision(request.getGuidBranch());
        }
        return deliveryList;
    }

    @Override
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
    }

    @Override
    public List<SDeliveryList> selectDeliveryListExcel(Integer guidDelivery) {

        SDelivery delivery = deliveryService.selectById(guidDelivery);
        if(delivery == null){
            throw new DeveloperException("没有找到对应的投放申请！");
        }
        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY, delivery.getGuid());
        List<SDeliveryList> sdlList = selectList(deliveryListEntityWrapper);

        if(sdlList.size() == 0){
            throw new DeveloperException("此投放申请没有要导出的代码清单！");
        }
        return disposeExportExcel(sdlList);
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
        //追加投放申请代码清单
        List<SDeliveryList> deliveryLists = new ArrayList<>();
        for (Integer guidDelivery:request.getGuidDelivery()){
            for (SDeliveryList deliveryList:request.getDeliveryList()){
                deliveryList.setGuidDelivery(guidDelivery);
                deliveryList.setDeployWhere(DeliveryProjectDetail.generateDeployWhereString(
                        deliveryList.getPatchType(), projectMap.get(deliveryList.getPartOfProject()).getDeployConfig()));
                deliveryLists.add(deliveryList);
            }
        }
        insertBatch(deliveryLists);

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.in(SDelivery.COLUMN_GUID,request.getGuidDelivery());
        List<SDelivery> deliverys = deliveryService.selectList(deliveryEntityWrapper);

        branchService.recordBranchTempRevision(branchMappings.get(0).getGuidBranch());
        return deliverys;
    }

    @Override
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

    }

    private List<SDeliveryList> disposeExportExcel(List<SDeliveryList> sdlList){
        List<SDeliveryList> deliveryLists = new ArrayList<>();

        sdlList.stream().collect(Collectors.groupingBy(SDeliveryList::getPatchType)).forEach((p, list) -> {
            if (p.equals(PatchType.ECD.getValue())) {
                list.stream().collect(Collectors.groupingBy(SDeliveryList::getPartOfProject,
                        Collectors.groupingBy(dl -> DeveloperUtils.getModule(dl.getFullPath()))))
                        .forEach((pj, m) -> m.forEach((module, l) -> {
                            // 导出ecd 的同工程清单
                            JSONObject jsonObject = JSONObject.parseObject(l.get(0).getDeployWhere());
                            String [] deployWhereSplit = jsonObject.getString(l.get(0).getPatchType()).split(",");

                            for (String deployWhere : deployWhereSplit) {
                                SDeliveryList sdl = new SDeliveryList();
                                sdl.setPatchType(l.get(0).getPatchType());
                                sdl.setDeployWhere(deployWhere);
                                sdl.setPartOfProject(pj);
                                sdl.setFullPath(DeveloperUtils.getEcdPath(l.get(0).getFullPath()));
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
                            sdl.setFullPath(DeveloperUtils.getFilePath(sd.getFullPath()));
                            deliveryLists.add(sdl);
                        }
                    }

                }
            }
        });
        return deliveryLists;
    }



}

