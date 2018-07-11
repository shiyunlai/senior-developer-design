package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.WorkitemAndBranchAddRequest;
import org.tis.senior.module.developer.controller.request.WorkitemBranchDetailRequest;
import org.tis.senior.module.developer.dao.SWorkitemMapper;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;
import org.tis.senior.module.developer.entity.enums.BranchMappingStatus;
import org.tis.senior.module.developer.entity.enums.ItemStatus;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.tis.senior.module.developer.service.ISBranchService;
import org.tis.senior.module.developer.service.ISWorkitemService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sWorkitem的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SWorkitemServiceImpl extends ServiceImpl<SWorkitemMapper, SWorkitem> implements ISWorkitemService {

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    private Map<String, SWorkitem> workitems = new HashMap<>();

    @Override
    public List<SWorkitem> selectWorkitemByUser(String userId) {

        EntityWrapper<SWorkitem> sWorkitemEntityWrapper = new EntityWrapper<>();
        sWorkitemEntityWrapper.like(SWorkitem.COLUMN_OWNER,userId);
        sWorkitemEntityWrapper.eq(SWorkitem.COLUMN_ITEM_STATUS,ItemStatus.DEVELOP);
        List<SWorkitem> swList = selectList(sWorkitemEntityWrapper);
        return swList;
    }

    @Override
    public SBranch selectBranchByWorkitemId(String workitemId) throws Exception {

        EntityWrapper<SBranchMapping> sbmEntityWrapper = new EntityWrapper<>();
        sbmEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, workitemId)
                .eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM.getValue());
        List<SBranchMapping> sbmList = branchMappingService.selectList(sbmEntityWrapper);
        if(sbmList.size() != 1){
            throw new Exception("The object of the query does not exist or superfluous");
        }
        SBranchMapping branchMapping = sbmList.get(0);

        SBranch sBranch = branchService.selectById(branchMapping.getGuidBranch());
        return sBranch;
    }

    @Override
    public SWorkitem selectOneById(String workitemId) {

        SWorkitem workitem = this.workitems.get(workitemId);
        if(workitem != null){
            return workitem;
        }
        workitem = selectById(workitemId);
        this.workitems.put(workitem.getGuid().toString(),workitem);
        return workitem;
    }

    @Override
    public void insertWorkitemAndBranch(WorkitemAndBranchAddRequest request) {

        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request.getWorkitemUpdateRequest(),workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        insert(workitem);

        SBranch branch = new SBranch();
        BeanUtils.copyProperties(request.getBranchAddRequest(),branch);
        branchService.insert(branch);

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(branch.getGuid());
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(workitem.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void insertWorkitemAndBranchMapping(WorkitemAddAndUpdateRequest request, Integer guidBranch) {
        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request,workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        insert(workitem);

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() > 0){
            throw new DeveloperException("次分支已被指配，请重新选择分支！");
        }

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(guidBranch);
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(workitem.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void deleteWorkitemAndBranchMapping(Integer guidWorkitem) {

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidWorkitem);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if (sBranchMappings.size() > 0) {
            SBranchMapping sbm = sBranchMappings.get(0);

            //删除对应的第三张关联表
            branchMappingService.deleteById(sbm.getGuid());
        }
        //删除guid对应的工作项
        deleteById(guidWorkitem);
    }

    @Override
    public WorkitemBranchDetailRequest workitemDetail(Integer guidWorkitem) {
        WorkitemBranchDetailRequest request = new WorkitemBranchDetailRequest();

        SWorkitem sWorkitem = selectById(guidWorkitem);
        request.setWorkitem(sWorkitem);

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,sWorkitem.getGuid());
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() != 1){
            request.setBranch(null);
        }else {
            SBranchMapping branchMapping = sbmList.get(0);
            SBranch branch = branchService.selectById(branchMapping.getGuidBranch());
            request.setBranch(branch);
        }
        return request;
    }

}

