package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.SCheck;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;

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
    CheckResultDetail check(String profileId, PackTime packTiming, String userId);
}

