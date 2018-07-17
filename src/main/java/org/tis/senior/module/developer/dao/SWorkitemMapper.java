package org.tis.senior.module.developer.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.vo.WorkitemBranchDetail;

import java.util.List;

/**
 * sWorkitem的Mapper类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface SWorkitemMapper extends BaseMapper<SWorkitem>  {

    List<WorkitemBranchDetail> selectWorkitemDetail(RowBounds rowBounds,
                                                    @Param("ew") Wrapper<WorkitemBranchDetail> wrapper);
}

