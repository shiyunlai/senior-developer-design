package org.tis.senior.module.developer.service;

import org.tis.senior.module.developer.entity.SProfiles;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * sProfiles的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISProfilesService extends IService<SProfiles>  {

    /**
     * 查询所有运行环境
     * @return
     */
    List<SProfiles> selectProfilesAll();

    SProfiles selectOneById(String guidProfile);
}

