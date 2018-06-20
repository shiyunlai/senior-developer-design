package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.service.ISBranchService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.dao.SBranchMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * sBranch的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SBranchServiceImpl extends ServiceImpl<SBranchMapper, SBranch> implements ISBranchService {

}

