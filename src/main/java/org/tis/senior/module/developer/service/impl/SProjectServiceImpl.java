package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.dao.SProjectMapper;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.service.ISProjectService;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.service.ISSvnKitService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * sProject的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProjectServiceImpl extends ServiceImpl<SProjectMapper, SProject> implements ISProjectService {

    private List<SProject> spList;

    @Override
    public List<SProject> selectProjectAll() {
        return this.spList;
    }

    @PostConstruct
    private void loadAllProject(){
        this.spList = selectList(null);
    }
}

