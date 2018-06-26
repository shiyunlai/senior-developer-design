package org.tis.senior.module.developer.service;



import org.tis.senior.module.developer.entity.vo.SvnCommit;

import java.util.List;

public interface ISSvnKitService {

    List<SvnCommit> loadSvnHistory(String url, int startRevision);
}
