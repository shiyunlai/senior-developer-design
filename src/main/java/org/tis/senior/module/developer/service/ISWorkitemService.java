package org.tis.senior.module.developer.service;

import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SWorkitem;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * sWorkitem的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISWorkitemService extends IService<SWorkitem>  {


    /**
     *
     *
     * @param userName
     * @return
     */
    List<SWorkitem> selectWorkitemByUser(String userName);

    SBranch selectBranchByWorkitemId(String workitemId) throws Exception;

    SWorkitem selectOneById(String workitemId);
}

