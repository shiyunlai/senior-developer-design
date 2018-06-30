package org.tis.senior.module.developer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.controller.request.*;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.enums.CommitType;
import org.tis.senior.module.developer.entity.enums.ConfirmStatus;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.DeliveryType;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.util.DeveloperUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sDeliveryList的Service接口实现类
 * 
 * @author Auto Generate Tools
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
    private ISWorkitemService workitemService;

    @Override
    public List<DeliveryProjectDetail> assembleDelivery(String branchGuid) throws Exception {

        SBranch branch = branchService.selectById(branchGuid);
        //查询所有的工程
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));

        List<SvnFile> svnCommits = svnKitService.getDiffStatus(branch.getFullPath(),branch.getCurrVersion().toString());
        if(svnCommits.size() < 1){
            throw  new DeveloperException("该清单已被整理或没有最新的提交记录!");
        }
        List<SDeliveryList> sdList = new ArrayList<>();
        Map<String, List<SvnFile>> commitMap = svnCommits.stream().collect(Collectors.groupingBy(SvnFile::getNodeType));
        Set<String> ecdSet = new HashSet<>();
        if (!commitMap.get("dir").isEmpty()) {
            for (SvnFile f:commitMap.get("dir")){
                String projectName = DeveloperUtils.getProjectName(f.getPath());
                if (StringUtils.isNotBlank(projectName)) {
                    SProject project = projectMap.get(projectName);
                    if(project == null){
                        continue;
                    }
                    JSONArray jsonArray = JSONArray.parseArray(project.getDeployConfig());
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        if ("ecd".equals(exportType)) {
                            String eoe = DeveloperUtils.isEpdOrEcd(f.getPath());
                            if (StringUtils.isNoneBlank(eoe) && f.getType().equals(CommitType.ADDED)) {
                                //如果是工程下的模块，以最后一个 . 截取，获取是否与工程名相等
                                String module = StringUtils.substringBeforeLast(eoe, ".");
                                if (module.equals(DeveloperUtils.getProjectName(f.getPath()))) {
                                    ecdSet.add(eoe);
                                }
                            }
                        }
                    }
                }
            }
        }
        for(SvnFile svnFile :commitMap.get("file")){
            SDeliveryList sdl = new SDeliveryList();
            sdl.setAuthor(svnFile.getAuthor());
            sdl.setCommitDate(svnFile.getData());
            sdl.setDeliveryVersion(svnFile.getRevision().intValue());
            sdl.setCommitType(svnFile.getType());
            sdl.setFullPath(svnFile.getPath());
            String programName = DeveloperUtils.getProgramName(svnFile.getPath());
            sdl.setProgramName(programName);
            String projectName = DeveloperUtils.getProjectName(svnFile.getPath());
            SProject sProject = projectMap.get(projectName);
            if(sProject == null){
                continue;
            }
            sdl.setPartOfProject(sProject.getProjectName());
            if("com.primeton.ibs.config".equals(sProject.getProjectName())){

            }else{
                String deployConfig = sProject.getDeployConfig();
                JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                for (Object object:jsonArray){
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    String exportType = jsonObject.getString("exportType");
                    String deployType = jsonObject.getString("deployType");
                    if(exportType.equals("ecd")) {
                        if (ecdSet.size() > 0) {
                            for (String ecd : ecdSet) {
                                if (svnFile.getPath().contains(ecd)) {
                                    sdl.setPatchType(exportType);
                                    sdl.setDeployWhere(deployType);
                                }
                            }
                        }
                    } else {
                        sdl.setPatchType(exportType);
                        sdl.setDeployWhere(deployType);
                    }
                }
            }
            sdList.add(sdl);
        }
        List<DeliveryProjectDetail> dpdLst = DeliveryProjectDetail.getDeliveryDetail(sdList);
        return dpdLst;
    }

    @Override
    public void addDeliveryList(DeliveryListAndDeliveryAddRequest request, String userId) throws Exception {

        String guidBranch = request.getGuidBranch();
        SBranch branch = branchService.selectById(guidBranch);
        EntityWrapper<SBranchMapping> sbmEntityWrapper = new EntityWrapper<>();
        sbmEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,branch.getGuid());
        List<SBranchMapping> sbmList = branchMappingService.selectList(sbmEntityWrapper);
        if(sbmList.size() != 1){
            throw new Exception("根据分支guid获取的第三方的工作项为空或多条！");
        }
        SBranchMapping sbm = sbmList.get(0);

        List<SDelivery> deliveryList = new ArrayList<>();
        SDliveryAddRequest dliveryAddRequest =  request.getDliveryAddRequest();
        List<DeliveryProfileRequest> guidPro = dliveryAddRequest.getDeliveryProfileRequest();
        for (DeliveryProfileRequest req:guidPro){
            //组装投放申请
            SDelivery delivery = new SDelivery();
            delivery.setApplyAlias(dliveryAddRequest.getApplyAlias());
            delivery.setGuidWorkitem(sbm.getGuidOfWhats());
            delivery.setGuidProfiles(Integer.parseInt(req.getGuidProfiles()));
            delivery.setDeliveryType(DeliveryType.GENERAL);
            delivery.setProposer(userId);
            delivery.setApplyTime(new Date());
            delivery.setDeliveryDesc(dliveryAddRequest.getDeliveryDesc());
            delivery.setPackTiming(req.getPackTiming());
            delivery.setDeliveryTime(dliveryAddRequest.getDeliveryTime());
            delivery.setDeliveryResult(DeliveryResult.APPLYING);
            deliveryList.add(delivery);
        }
        deliveryService.insertBatch(deliveryList);


        for (SDelivery sDelivery:deliveryList){

            //组装投产代码清单
            for (SDeliveryList dlar:request.getDdliveryList()){
                dlar.setGuid(null);
                dlar.setGuidDelivery(sDelivery.getGuid());
                dlar.setDeveloperConfirm(ConfirmStatus.WAIT);
            }
            insertBatch(request.getDdliveryList());
        }

        int revision = svnKitService.getLastRevision(branch.getFullPath());
        SBranch sb =  new SBranch();
        sb.setCurrVersion(revision);
        sb.setGuid(branch.getGuid());
        branchService.updateById(sb);
    }

}

