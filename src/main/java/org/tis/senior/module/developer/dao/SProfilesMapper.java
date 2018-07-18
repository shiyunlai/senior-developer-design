package org.tis.senior.module.developer.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.vo.ProfileBranchDetail;

import java.util.List;

/**
 * sProfiles的Mapper类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface SProfilesMapper extends BaseMapper<SProfiles>  {

    List<ProfileBranchDetail> selectProfileDetail(RowBounds rowBounds,
                                                   @Param("ew") Wrapper<ProfileBranchDetail> wrapper);
}

