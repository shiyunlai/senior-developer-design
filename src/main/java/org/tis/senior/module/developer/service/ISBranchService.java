package org.tis.senior.module.developer.service;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import org.tis.senior.module.developer.entity.SBranch;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * sBranch的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISBranchService extends IService<SBranch>  {


    /**
     * 删除分支及关联表
     * @param guidBranch
     */
    void deleteBranchAndMapping(Integer guidBranch);

    /**
     * 查询未被指配的分支
     * @return
     */
    List<SBranch> selectNotAllotBranch();
}

