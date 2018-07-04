package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.SCheck;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tmatesoft.svn.core.SVNException;

/**
 * sCheck的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
public interface ISCheckService extends IService<SCheck>  {

    /**
     * 核对合并清单
     * @param profileId 环境ID
     * @param packTiming 打包窗口
     * @return
     */
    CheckResultDetail check(String profileId, PackTime packTiming, String userId) throws SVNException;

    /**
     * 查看核对结果
     * 用于已核对后查看结果
     * @param checkGuid
     * @return
     */
    CheckResultDetail detail(String checkGuid);

    /**
     * 处理投放申请
     * @param deliveryGuid
     * @param result
     * @param desc
     * @param userId
     */
    void process(String deliveryGuid, DeliveryResult result, String desc, String userId);

    /**
     * 确认文件
      * @param isDelivery 是否为投放清单，false则为合并清单
     * @param id 清单id
     */
    void confirm(boolean isDelivery, String id);
}

