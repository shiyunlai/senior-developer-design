package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.DeliveryListAndDeliveryAddRequest;
import org.tis.senior.module.developer.controller.request.DeliveryListSuperadditionRequest;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.vo.DeliveryListStashListDetail;
import org.tmatesoft.svn.core.SVNException;

import java.text.ParseException;
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
    DeliveryListStashListDetail assembleDelivery(String branchGuid) throws SVNException;

    /**
     * 添加投放申请和投产代码清单
     * @param request
     */
    List<SDelivery> addDeliveryList(DeliveryListAndDeliveryAddRequest request, String proposer) throws SVNException, ParseException;

    /**
     * 查询此工作项下需要导出成Excel文件的清单代码
     *
     * @param guidWorkitem 工作项guid
     * @param guidProfiles 运行环境guid
     * @return
     */
//    List<SDeliveryList> selectDeliveryListOutPutExcel(Integer guidWorkitem, Integer guidProfiles);

    /**
     * 根本投放申请guid查询对应的代码清单
     * @param guidDelivery
     * @return
     */
    List<SDeliveryList> selectDeliveryListExcel(Integer guidDelivery);

    /**
     * 追加投放申请
     * @param request
     */
    List<SDelivery> addToDeliveryList(DeliveryListSuperadditionRequest request) throws SVNException;

    /**
     * 填充对象
     * @param path
     * @param deliveryList
     */
//    void fillDeliveryList(String path, SDeliveryList deliveryList);
}

