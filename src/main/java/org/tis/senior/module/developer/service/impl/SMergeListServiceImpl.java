package org.tis.senior.module.developer.service.impl;

import org.tis.senior.module.developer.entity.SMergeList;
import org.springframework.stereotype.Service;
import org.tis.senior.module.developer.dao.SMergeListMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.service.ISMergeListService;

/**
 * sMergeList的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SMergeListServiceImpl extends ServiceImpl<SMergeListMapper, SMergeList> implements ISMergeListService {

}

