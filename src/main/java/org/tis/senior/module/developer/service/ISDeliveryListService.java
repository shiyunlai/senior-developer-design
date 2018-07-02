package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.DeliveryListAndDeliveryAddRequest;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tmatesoft.svn.core.SVNException;

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
    List<DeliveryProjectDetail> assembleDelivery(String branchGuid) throws SVNException;

    /**
     * 添加投放申请和投产代码清单
     * @param request
     */
    void addDeliveryList(DeliveryListAndDeliveryAddRequest request, String proposer) throws Exception;

    /**
     * 查询此工作项下需要导出成Excel文件的清单代码
     *
     * @param guidWorkitem
     * @param guidProfiles
     * @return
     */
    List<SDeliveryList> selectDeliveryListOutPutExcel(String guidWorkitem, String guidProfiles);
}

