package org.tis.senior.module.developer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.controller.request.DeliveryListAndDeliveryAddRequest;
import org.tis.senior.module.developer.controller.request.DeliveryProfileRequest;
import org.tis.senior.module.developer.controller.request.SDliveryAddRequest;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.CommitType;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.DeliveryType;
import org.tis.senior.module.developer.entity.enums.PatchType;
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

    @Override
    public List<DeliveryProjectDetail> assembleDelivery(String branchGuid) throws SVNException {

        SBranch branch = branchService.selectById(branchGuid);
        if (branch == null) {
            throw new DeveloperException("查询不到此分支！");
        }
        //查询所有的工程
        Map<String, SProject> projectMap = projectService.selectProjectAll().stream().
                collect(Collectors.toMap(SProject::getProjectName, p -> p));

        List<SvnFile> svnCommits = svnKitService.getDiffStatus(branch.getFullPath(), branch.getCurrVersion().toString());
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
                        project = projectMap.get("default");
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
                                    ecdSet.add(eoe);
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
                String programName = DeveloperUtils.getProgramName(svnFile.getPath());
                sdl.setProgramName(programName);
                String projectName = DeveloperUtils.getProjectName(svnFile.getPath());
                SProject sProject = projectMap.get(projectName);
                if (sProject == null) {
                    sProject = projectMap.get("default");
                }
                sdl.setPartOfProject(sProject.getProjectName());
                if ("S".equals(sProject.getProjectType())) {
                    String deployConfig = sProject.getDeployConfig();
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        String deployType = jsonObject.getString("deployType");
                        sdl.setPatchType(exportType);
                        sdl.setDeployWhere(deployType);
                    }
                } else {
                    String deployConfig = sProject.getDeployConfig();
                    JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(object.toString());
                        String exportType = jsonObject.getString("exportType");
                        String deployType = jsonObject.getString("deployType");
                        if (exportType.equals("ecd")) {
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
            });
        }
        return DeliveryProjectDetail.getDeliveryDetail(sdList, projectService.selectProjectAll());
    }

    @Override
    public void addDeliveryList(DeliveryListAndDeliveryAddRequest request, String userId) throws SVNException {

        String guidBranch = request.getGuidBranch();
        SBranch branch = branchService.selectById(guidBranch);
        EntityWrapper<SBranchMapping> sbmEntityWrapper = new EntityWrapper<>();
        sbmEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH, branch.getGuid());
        List<SBranchMapping> sbmList = branchMappingService.selectList(sbmEntityWrapper);
        if (sbmList.size() != 1) {
            throw new DeveloperException("根据分支guid获取的第三方的工作项为空或多条！");
        }
        SBranchMapping sbm = sbmList.get(0);

        List<SDelivery> deliveryList = new ArrayList<>();
        SDliveryAddRequest dliveryAddRequest = request.getDliveryAddRequest();
        List<DeliveryProfileRequest> guidPro = dliveryAddRequest.getProfiles();
        for (DeliveryProfileRequest req : guidPro) {
            //组装投放申请
            SDelivery delivery = new SDelivery();
            delivery.setApplyAlias(req.getApplyAlias());
            delivery.setGuidWorkitem(sbm.getGuidOfWhats());
            delivery.setGuidProfiles(Integer.parseInt(req.getGuidProfiles()));
            delivery.setDeliveryType(DeliveryType.GENERAL);
            delivery.setProposer(userId);
            delivery.setApplyTime(new Date());
            delivery.setPackTiming(req.getPackTiming());
            delivery.setDeliveryTime(dliveryAddRequest.getDeliveryTime());
            delivery.setDeliveryResult(DeliveryResult.APPLYING);
            deliveryList.add(delivery);
        }
        deliveryService.insertBatch(deliveryList);


        for (SDelivery sDelivery : deliveryList) {

            //组装投产代码清单
            for (SDeliveryList dlar : request.getDeliveryList()) {
                dlar.setGuid(null);
                dlar.setGuidDelivery(sDelivery.getGuid());
            }
            insertBatch(request.getDeliveryList());
        }

//        int revision = svnKitService.getLastRevision(branch.getFullPath());
//        SBranch sb = new SBranch();
//        sb.setCurrVersion(revision);
//        sb.setGuid(branch.getGuid());
//        branchService.updateById(sb);
    }

    @Override
    public List<SDeliveryList> selectDeliveryListOutPutExcel(String guidWorkitem, String guidProfiles) {

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, guidProfiles);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM, guidWorkitem);
        List<SDelivery> sdList = deliveryService.selectList(deliveryEntityWrapper);

        List<Integer> guidDelivery = new ArrayList<>();
        for (SDelivery delivery : sdList) {
            guidDelivery.add(delivery.getGuid());
        }

        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        if (guidDelivery == null) {
            throw new DeveloperException("查询到的投放申请的guid集合为空");
        }
        deliveryListEntityWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, guidDelivery);
        List<SDeliveryList> sdlList = selectList(deliveryListEntityWrapper);

        List<SDeliveryList> deliveryLists = new ArrayList<>();

        sdlList.stream().collect(Collectors.groupingBy(SDeliveryList::getPatchType)).forEach((p, list) -> {
            if (p.equals(PatchType.ECD.getValue())) {
                list.stream().collect(Collectors.groupingBy(SDeliveryList::getPartOfProject,
                        Collectors.groupingBy(dl -> DeveloperUtils.getModule(dl.getFullPath()))))
                        .forEach((pj, m) -> m.forEach((module, l) -> {
                            // 导出ecd 的同工程清单
                            String[] deployWhereSplit = l.get(0).getDeployWhere().split(",");
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
                    String[] deployWhereSplit = sd.getDeployWhere().split(",");
                    String[] patchTypeSplit = sd.getPatchType().split(",");
                    for (String patchType : patchTypeSplit) {
                        for (String deployWhere : deployWhereSplit) {
                            SDeliveryList sdl = new SDeliveryList();
                            sdl.setPatchType(patchType);
                            sdl.setDeployWhere(deployWhere);
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

    private List<SvnFile> getDiffStatus(String fullPath, String startRevision) {

//        List<SvnFile> svnCommits = svnKitService.getDiffStatus(branch.getFullPath(), branch.getCurrVersion().toString());
            return null;
    }



}

