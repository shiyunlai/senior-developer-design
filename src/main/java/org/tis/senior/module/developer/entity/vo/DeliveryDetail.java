package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.enums.PatchType;

import java.util.List;
import java.util.Map;

/**
 * description:投放明细
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class DeliveryDetail  {

    public static final String TOTAL_PATCH = "补丁包数";

    public static final String TOTAL_FILE = "代码文件";

    public static final String TOTAL_SCRIPT = "脚本数";

    /**
     * 工作项信息
     */
    private List<SWorkitem> workitems;

    /**
     * 分支信息
     */
    private List<SBranch> branches;

    /**
     * 投放小计
     */
    private Map<PatchType, Integer> patchCount;

    /**
     * 投放合计
     */
    private Map<String, Integer> deliveryCount;

    /**
     * 投放清单
     */
    private List<DeliveryProjectDetail> detailList;

}
