package org.tis.senior.module.developer.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SProgram;
import org.tis.senior.module.developer.dao.SProgramMapper;
import org.tis.senior.module.developer.service.ISProgramService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sProgram的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProgramServiceImpl extends ServiceImpl<SProgramMapper, SProgram> implements ISProgramService {

}

