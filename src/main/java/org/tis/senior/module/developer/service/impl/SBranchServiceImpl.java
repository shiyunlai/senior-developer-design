package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.tis.senior.module.developer.service.ISBranchService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.dao.SBranchMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * sBranch的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SBranchServiceImpl extends ServiceImpl<SBranchMapper, SBranch> implements ISBranchService {

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Override
    public void deleteBranchAndMapping(Integer guidBranch) {
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sBranchMappings.size() > 0){
            SBranchMapping sbm = sBranchMappings.get(0);

            //删除对应的第三张关联表
            branchMappingService.deleteById(sbm.getGuid());
        }
        //删除分支guid对应的分支记录
        deleteById(guidBranch);
    }

    @Override
    public List<SBranch> selectNotAllotBranch() {
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(null);
        List<Integer> branchGuid = new ArrayList<>();
        for (SBranchMapping branchMapping:sBranchMappings){
            branchGuid.add(branchMapping.getGuidBranch());
        }
        if(branchGuid == null){
            throw new DeveloperException("没有查询到为被指配的分支");
        }
        EntityWrapper<SBranch> branchEntityWrapper = new EntityWrapper<>();
        branchEntityWrapper.notIn(SBranch.COLUMN_GUID,branchGuid);
        List<SBranch> branchList = selectList(branchEntityWrapper);
        return branchList;
    }
}

