package org.tis.senior.module.developer.service;

import org.tis.senior.module.developer.entity.SDeliveryList;
import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;

import java.util.List;

/**
 * sDeliveryList的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISDeliveryListService extends IService<SDeliveryList>  {


    /**
     * 组装投放清单展示
     *
     * @return
     */
    List<DeliveryProjectDetail> assembleDelivery();

    void addDeliveryList();
}

