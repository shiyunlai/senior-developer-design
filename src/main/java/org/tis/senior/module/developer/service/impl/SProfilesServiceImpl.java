package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.entity.SProfiles;
import org.springframework.stereotype.Service;
import org.tis.senior.module.developer.dao.SProfilesMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.service.ISProfilesService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sProfiles的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProfilesServiceImpl extends ServiceImpl<SProfilesMapper, SProfiles> implements ISProfilesService {

}

