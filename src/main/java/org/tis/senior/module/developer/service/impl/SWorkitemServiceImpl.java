package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.dao.SWorkitemMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.tis.senior.module.developer.service.ISBranchService;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.springframework.transaction.annotation.Transactional;

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
        sWorkitemEntityWrapper.like(SWorkitem.COLUMN_DEVELOPERS,userId);
        sWorkitemEntityWrapper.eq(SWorkitem.COLUMN_ITEM_STATUS,"0");
        List<SWorkitem> swList = selectList(sWorkitemEntityWrapper);
        return swList;
    }

    @Override
    public SBranch selectBranchByWorkitemId(String workitemId) throws Exception {

        EntityWrapper<SBranchMapping> sbmEntityWrapper = new EntityWrapper<>();
        sbmEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,workitemId);
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

}

