package org.tis.senior.module.developer.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.dao.SProgramCommitMapper;
import org.tis.senior.module.developer.entity.SProgramCommit;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.service.ISProgramCommitService;

/**
 * sProgramCommit的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProgramCommitServiceImpl extends ServiceImpl<SProgramCommitMapper, SProgramCommit> implements ISProgramCommitService {

}

