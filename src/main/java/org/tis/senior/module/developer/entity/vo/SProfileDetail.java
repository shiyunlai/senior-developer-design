package org.tis.senior.module.developer.entity.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SProfileDetail {

    private String applyAlias;

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guidProfile;

    /**
     * 环境名称
     */
    private String profilesName;

    /**
     * 是否勾选
     */
    private boolean isDelivered = false;

    /**
     * 投放时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deliveryTime;

    /**
     * 打包窗口
     */
    private List<PackTimeDetail> packTimeDetails;
}
