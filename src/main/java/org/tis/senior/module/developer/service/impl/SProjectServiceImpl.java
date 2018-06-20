package org.tis.senior.module.developer.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.dao.SProjectMapper;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.service.ISProjectService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sProject的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProjectServiceImpl extends ServiceImpl<SProjectMapper, SProject> implements ISProjectService {

}

