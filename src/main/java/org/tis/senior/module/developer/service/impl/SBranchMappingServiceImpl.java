package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.dao.SBranchMappingMapper;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.springframework.stereotype.Service;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * sBranchMapping的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SBranchMappingServiceImpl extends ServiceImpl<SBranchMappingMapper, SBranchMapping> implements ISBranchMappingService {

}

