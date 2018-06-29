package org.tis.senior.module.developer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.controller.request.DeliveryListAddRequest;
import org.tis.senior.module.developer.controller.request.DeliveryListAndDeliveryAddRequest;
import org.tis.senior.module.developer.controller.request.SDliveryAddRequest;
import org.tis.senior.module.developer.controller.request.SvnPathListRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.enums.ConfirmStatus;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.DeliveryType;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.service.*;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.util.DeveloperUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private ISDeliveryService deliveryService;

    @Override
    public List<DeliveryProjectDetail> assembleDelivery(String branchGuid) {

        SBranch branch = branchService.selectById(branchGuid);
        //查询所有的工程
        List<SProject> spList = projectService.selectProjectAll();

        List<SvnFile> svnCommits = svnKitService.getDiffStatus(branch.getFullPath(),branch.getCurrVersion().toString());
        List<SDeliveryList> sdList = new ArrayList<>();
        for (SvnFile svnFile:svnCommits){
            SDeliveryList sdl = new SDeliveryList();
            sdl.setAuthor(svnFile.getAuthor());
            sdl.setCommitDate(svnFile.getData());
            sdl.setDeliveryVersion(svnFile.getRevision().intValue());
            sdl.setCommitType(svnFile.getType());
            sdl.setFullPath(svnFile.getPath());
            String programName = DeveloperUtils.getProgramName(svnFile.getPath());
            sdl.setProgramName(programName);
            String project = DeveloperUtils.getProjectName(svnFile.getPath());
            for (SProject sProject:spList){
                if(project.equals(sProject.getProjectName())){
                    sdl.setPartOfProject(sProject.getProjectName());
                    if("com.primeton.ibs.config".equals(sProject.getProjectName())){

                    }else{
                        String deployConfig = sProject.getDeployConfig();
                        JSONArray jsonArray = JSONArray.parseArray(deployConfig);
                        for (Object object:jsonArray){
                            JSONObject jsonObject = JSONObject.parseObject(object.toString());
                            String exportType = jsonObject.getString("exportType");
                            String deployType = jsonObject.getString("deployType");
                            if(exportType.equals("ecd")){
                                if("dir".equals(svnFile.getNodeType())){
                                    String eoe = DeveloperUtils.isEpdOrEcd(svnFile.getPath());
                                    //如果是工程下的模块，以最后一个 . 截取，获取是否与工程名相等
                                    String module = StringUtils.substringBeforeLast(eoe,".");
                                    if(module.equals(project)){
                                        sdl.setPatchType(exportType);
                                        sdl.setDeployWhere(deployType);
                                    }
                                }
                            }else{
                                sdl.setPatchType(exportType);
                                sdl.setDeployWhere(deployType);
                            }
                        }
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

        List<SDelivery> deliveryList = new ArrayList<>();
        List<SDeliveryList> sDeliveryLists = new ArrayList<>();
        SDliveryAddRequest dliveryAddRequest =  request.getDliveryAddRequest();
        List<Integer> guidPro = dliveryAddRequest.getGuidProfiles();
        for (int guid:guidPro){
            //组装投放申请
            SDelivery delivery = new SDelivery();
            delivery.setGuidWorkitem(dliveryAddRequest.getGuidWorkitem());
            delivery.setGuidProfiles(guid);
            delivery.setDeliveryType(DeliveryType.GENERAL);
            delivery.setProposer(userId);
            delivery.setApplyTime(new Date());
            delivery.setDeliveryDesc(dliveryAddRequest.getDeliveryDesc());
            delivery.setPackTiming(dliveryAddRequest.getPackTiming());
            delivery.setDeliveryTime(dliveryAddRequest.getDeliveryTime());
            delivery.setDeliveryResult(DeliveryResult.APPLYING);
            deliveryList.add(delivery);
        }
        deliveryService.insertBatch(deliveryList);


        for (SDelivery sDelivery:deliveryList){
            EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
            deliveryEntityWrapper.eq(SDelivery.COLUMN_APPLY_TIME,sDelivery.getApplyTime());
            deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES,sDelivery.getGuidProfiles());
            List<SDelivery> sd = deliveryService.selectList(deliveryEntityWrapper);
            if(sd.size() > 1){
                throw new Exception("The object of the query does not exist or superfluous!");
            }
            SDelivery deli = sd.get(0);

            //组装投产代码清单
            for (DeliveryListAddRequest dlar:request.getDdliveryList()){
                List<SvnPathListRequest> pathListRequest = dlar.getFileList();
                for (SvnPathListRequest spr:pathListRequest){
                    SDeliveryList sdl = new SDeliveryList();
                    sdl.setGuidDelivery(deli.getGuid());
                    sdl.setProgramName(spr.getGrogramName());
                    sdl.setDeliveryVersion(spr.getRevision());
                    sdl.setPatchType(dlar.getPatchType());
                    sdl.setDeployWhere(dlar.getProfileWhere());
                    sdl.setFullPath(spr.getPath());
                    sdl.setAuthor(spr.getAuthor());
                    sdl.setCommitDate(spr.getDate());
                    sdl.setCommitType(spr.getCommitType());
                    sdl.setDeveloperConfirm(ConfirmStatus.WAIT);
                    sDeliveryLists.add(sdl);
                }
            }

        }
        insertBatch(sDeliveryLists);
        String guidBranch = request.getGuidBranch();
        SBranch branch = branchService.selectById(guidBranch);
        int revision = svnKitService.getLastRevision(branch.getFullPath());
        SBranch sb =  new SBranch();
        sb.setCurrVersion(revision);
        sb.setGuid(branch.getGuid());
        branchService.updateById(sb);
    }

}

