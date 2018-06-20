package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.dao.SWorkitemMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sWorkitem的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SWorkitemServiceImpl extends ServiceImpl<SWorkitemMapper, SWorkitem> implements ISWorkitemService {

}

