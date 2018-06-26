package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;

import java.util.List;

/**
 * sProject的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISProjectService extends IService<SProject>  {

    List<SProject> selectProjectAll();
}

