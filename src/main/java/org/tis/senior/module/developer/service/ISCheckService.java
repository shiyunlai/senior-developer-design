package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.SCheck;
import org.tis.senior.module.developer.entity.enums.CheckStatus;
import org.tis.senior.module.developer.entity.enums.ConfirmStatus;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckMergeDetail;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tmatesoft.svn.core.SVNException;

import java.util.List;

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
    void process(String deliveryGuid, DeliveryResult result, String desc, String userId) throws SVNException;

    /**
     * 确认核对清单状态
     * @param checkListGuid 核对清单id
    * @param status 状态
     */
    void confirm(String checkListGuid, ConfirmStatus status);

    /**
     * 确认核对清单添加到投放申请
     * @param checkListGuid 核对清单id
     * @param deliveryId 申请id
     * @param patchType
     * @param deployWhere
     */
    void confirmToDelivery(String checkListGuid, String deliveryId, String patchType, String deployWhere);

    /**
     * 查看环境及窗口下的申请合并详情
     * @param profileId
     * @param what
     * @return
     */
    List<CheckMergeDetail> getMergeList(String profileId, PackTime what);

    /**
     * 完成核对
     * @param id 核对ID
     * @param status 核对状态 成功或失败
     */
    void completeCheck(String id, CheckStatus status);
}

