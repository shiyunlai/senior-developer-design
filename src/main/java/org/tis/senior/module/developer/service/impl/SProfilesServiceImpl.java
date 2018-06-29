package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.tis.senior.module.developer.entity.SProfiles;
import org.springframework.stereotype.Service;
import org.tis.senior.module.developer.dao.SProfilesMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.service.ISProfilesService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * sProfiles的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProfilesServiceImpl extends ServiceImpl<SProfilesMapper, SProfiles> implements ISProfilesService {

    private List<SProfiles> spList;

    @Override
    public List<SProfiles> selectProfilesAll() {
        return this.spList;
    }

    @PostConstruct
    public void selectAll(){
        EntityWrapper<SProfiles> spEntityWrapper = new EntityWrapper<>();
        spEntityWrapper.eq(SProfiles.COLUMN_IS_ALLOW_DELIVERY,"1");
        this.spList = selectList(spEntityWrapper);
    }
}

