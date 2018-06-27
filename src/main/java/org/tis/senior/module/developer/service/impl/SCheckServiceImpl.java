package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.developer.dao.SCheckMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.developer.entity.SCheck;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * sCheck的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SCheckServiceImpl extends ServiceImpl<SCheckMapper, SCheck> implements ISCheckService {

    @Autowired
    private ISProfilesService profilesService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISSvnKitService svnKitService;

    @Override
    public DeliveryDetail check(String profileId, PackTime packTiming, String userId) {
        // 验证环境
        List<SProfiles> list = profilesService.selectProfilesAll().stream().filter(p ->
                StringUtils.equals(p.getGuid().toString(), profileId) &&
                        Arrays.asList(p.getPackTiming().split(",")).contains(packTiming.getValue().toString()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            throw new DeveloperException("环境或对应打包窗口不存在!");
        }
        SProfiles profiles = list.get(0);
        // 生成核对记录
        SCheck check = new SCheck();
        check.setCheckAlias(genCheckAlias(profiles, packTiming));
        check.setCheckDate(new Date());
        check.setCheckUser(userId);
        check.setGuidProfiles(Integer.valueOf(profileId));
        check.setPackTiming(packTiming.getValue().toString());
        // 获取分支信息
        EntityWrapper<SBranchMapping> bMWrapper = new EntityWrapper<>();
        bMWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.RELEASE.getValue());
        bMWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, profileId);
        SBranchMapping sBranchMapping = branchMappingService.selectOne(bMWrapper);
        EntityWrapper<SBranch> branchWrapper = new EntityWrapper<>();
        branchWrapper.eq(SBranch.COLUMN_GUID, sBranchMapping.getGuidBranch());
        SBranch sBranch = branchService.selectOne(branchWrapper);
        List<SvnCommit> svnCommits = svnKitService.loadSvnHistory(sBranch.getFullPath(), sBranch.getCurrVersion());

        return null;
    }

    private String genCheckAlias(SProfiles profiles, PackTime packTime) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 获取次数
        EntityWrapper<SCheck> wrapper = new EntityWrapper<>();
//        wrapper.eq("DATADIFF(" + SCheck.COLUMN_CHECK_DATE + ", NOW())", 0);
        wrapper.eq("to_days(" + SCheck.COLUMN_CHECK_DATE + ")", "to_days(now())");
        wrapper.eq(SCheck.COLUMN_GUID_PROFILES, profiles.getGuid());
        wrapper.eq(SCheck.COLUMN_PACK_TIMING, packTime.getValue());
        Integer count = this.baseMapper.selectCount(wrapper) + 1;
        return profiles.getProfilesName() + date + packTime.getValue() + profiles + "第" + count + "次核对";

    }
}

