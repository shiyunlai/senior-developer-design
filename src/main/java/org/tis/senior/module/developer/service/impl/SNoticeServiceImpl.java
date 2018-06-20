package org.tis.senior.module.developer.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SNotice;
import org.tis.senior.module.developer.dao.SNoticeMapper;
import org.tis.senior.module.developer.service.ISNoticeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * sNotice的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SNoticeServiceImpl extends ServiceImpl<SNoticeMapper, SNotice> implements ISNoticeService {

}

