package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * sBranch的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISBranchService extends IService<SBranch>  {

    /**
     * 通过工作项ID或环境ID查询分支
     * @param forWhat
     * @param ids
     * @return
     */
    List<Map> selectListByForWhatIds(BranchForWhat forWhat, Collection ids);

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

