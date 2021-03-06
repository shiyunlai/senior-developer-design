package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.dao.SPatchMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SPatch;
import org.tis.senior.module.developer.service.ISPatchService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sPatch的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SPatchServiceImpl extends ServiceImpl<SPatchMapper, SPatch> implements ISPatchService {

}

