package org.tis.senior.module.developer.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.tis.senior.module.developer.entity.SBranch;

import java.util.List;
import java.util.Map;

/**
 * sBranch的Mapper类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface SBranchMapper extends BaseMapper<SBranch>  {

    List<Map> selectListByForWhatIds(@Param(value = "forWhat") String forWhat,
                                     @Param(value = "ids") String ids);

}

