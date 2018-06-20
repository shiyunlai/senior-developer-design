package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.dao.SDeliveryListMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.service.ISDeliveryListService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sDeliveryList的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SDeliveryListServiceImpl extends ServiceImpl<SDeliveryListMapper, SDeliveryList> implements ISDeliveryListService {

}

