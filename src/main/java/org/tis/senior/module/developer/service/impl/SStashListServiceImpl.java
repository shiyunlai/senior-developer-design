package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.dao.SStashListMapper;
import org.tis.senior.module.developer.entity.SStashList;
import org.tis.senior.module.developer.service.ISStashListService;

/**
 * sStashList的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/07/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SStashListServiceImpl extends ServiceImpl<SStashListMapper, SStashList> implements ISStashListService {

}

