package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.dao.SSvnAccountMapper;
import org.springframework.stereotype.Service;
import org.tis.senior.module.developer.service.ISSvnAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.springframework.transaction.annotation.Transactional;

/**
 * sSvnAccount的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SSvnAccountServiceImpl extends ServiceImpl<SSvnAccountMapper, SSvnAccount> implements ISSvnAccountService {

}

