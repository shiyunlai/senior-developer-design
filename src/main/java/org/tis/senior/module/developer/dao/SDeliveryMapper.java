package org.tis.senior.module.developer.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.vo.DeliveryWorkitemDetail;

import java.util.List;

/**
 * sDelivery的Mapper类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface SDeliveryMapper extends BaseMapper<SDelivery>  {

    List<DeliveryWorkitemDetail> selectDeliveryWorkitemDetail(RowBounds rowBounds,
                                                        @Param("ew") Wrapper<DeliveryWorkitemDetail> wrapper);
}

