package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.dao.SSvnAccountMapper;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.service.ISSvnAccountService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * sSvnAccount的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SSvnAccountServiceImpl extends ServiceImpl<SSvnAccountMapper, SSvnAccount> implements ISSvnAccountService {

    private List<SSvnAccount> accounts;

    @Override
    public List<SSvnAccount> selectAllAccount() {
        return accounts;
    }

    @PostConstruct
    public void loadAccounts() {
        accounts = this.baseMapper.selectList(null).stream()
                .peek(s -> s.setSvnPwd(null)).collect(Collectors.toList());
    }
}

