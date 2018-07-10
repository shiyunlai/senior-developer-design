package org.tis.senior.module.developer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.core.config.SvnProperties;
import org.tis.senior.module.developer.entity.enums.CommitType;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.service.ISSvnKitService;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_NONE;

@Service
@Transactional(rollbackFor = Exception.class)
public class SSvnKitServiceImpl implements ISSvnKitService {

    @Autowired
    private SvnProperties svnProperties;

    private ISVNAuthenticationManager svnAuthenticationManager;

    /**
     * 获取svn的提交历史记录
     *
     * @return
     */
    @Override
    public List<SvnCommit> loadSvnHistory(String url, int startRevision) throws SVNException {

        List<SvnCommit> scList = new ArrayList<>();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        repository.setAuthenticationManager(this.svnAuthenticationManager);
        LinkedList logEntries = new LinkedList();
        repository.log(new String[]{""}, startRevision, -1, true, true,
                0, false, null, logEntry -> {
                    if (logEntry.getRevision() != -1) {
                        logEntries.add(logEntry);
                    }
                });
        for (Object logEntry1 : logEntries) {
            /*
             * gets a next SVNLogEntry
             */
            SVNLogEntry logEntry = (SVNLogEntry) logEntry1;

            SvnCommit svnCommit = new SvnCommit();
            svnCommit.setRevision((int) logEntry.getRevision());
            svnCommit.setAuthor(logEntry.getAuthor());
            svnCommit.setCommitDate(logEntry.getDate());
            svnCommit.setMessage(logEntry.getMessage());

            if (logEntry.getChangedPaths().size() > 0) {

                Set changedPathsSet = logEntry.getChangedPaths().keySet();

                for (Object aChangedPathsSet : changedPathsSet) {

                    SVNLogEntryPath entryPath = logEntry.getChangedPaths().get(aChangedPathsSet);

                    SvnPath svnPath = new SvnPath();

                    String type = svnPath.getType().toString();
                    svnPath.setPath(entryPath.getPath());
                    svnPath.setType(CommitType.what(type));
                    if (entryPath.getCopyPath() != null) {
                        svnPath.setCopyPath(entryPath.getCopyPath());
                        svnPath.setCopyRevision((int) entryPath.getCopyRevision());
                    }
                }
            }
            scList.add(svnCommit);
        }

        return scList;
    }

    @Override
    public int getLastRevision(String url) throws SVNException {
        return (int) getLastRevision(SVNURL.parseURIEncoded(url)).getCommittedRevision().getNumber();
    }

    @Override
    public List<SvnFile> getDiffStatus(String url, String startRevision) throws SVNException {
        SVNURL svnurl = SVNURL.parseURIEncoded(url);
        return doDiffStatus(svnurl,startRevision, null);
    }

    @Override
    public List<SvnFile> getBranchDiffStatus(String url, String startRevision) throws SVNException {
        List<SvnFile> list = new ArrayList<>();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        repository.setAuthenticationManager(this.svnAuthenticationManager);
        Collection entries = repository.getDir("", -1, null, (Collection) null);
        for (Object entry1 : entries) {
            SVNDirEntry entry = (SVNDirEntry) entry1;
            if (entry.getKind() == SVNNodeKind.DIR) {
                SVNRepository svnRepository = SVNRepositoryFactory.create(entry.getURL());
                svnRepository.setAuthenticationManager(this.svnAuthenticationManager);
                svnRepository.log(new String[] {""}, Long.valueOf(startRevision), repository.getLatestRevision(), true, true,
                        1, false, null, logEntry -> {
                    boolean isCopy = false;
                    if (logEntry.getChangedPaths().size() > 0) {
                        Set<String> changedPathsSet = logEntry.getChangedPaths().keySet();
                        for (String aChangedPathsSet : changedPathsSet) {
                            SVNLogEntryPath p = logEntry.getChangedPaths().get(aChangedPathsSet);
                            if (p.getCopyPath() != null) {
                                isCopy=true;
                                break;
                            }
                        }
                        if (isCopy) {
                            list.addAll(doDiffStatus(entry.getURL(), Long.toString(logEntry.getRevision()), null));
                        } else {
                            list.addAll(doDiffStatus(entry.getRepositoryRoot(),
                                    Long.toString(logEntry.getRevision() -1), entry.getURL().getPath()));
                        }
                    }
                });
            }
        }
        return list;
    }

    @PostConstruct
    private void svnAuthenticationManagerInit() {
        setupLibrary();
        this.svnAuthenticationManager = SVNWCUtil.createDefaultAuthenticationManager(
                svnProperties.getUserName(), svnProperties.getPassword().toCharArray()
        );
    }


    private SVNInfo getLastRevision(SVNURL url) {
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        SVNWCClient svnwcClient = new SVNWCClient(svnAuthenticationManager, defaultOptions);
        try {
            return svnwcClient.doInfo(url, SVNRevision.HEAD, SVNRevision.HEAD);
        } catch (SVNException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<SvnFile> doDiffStatus(SVNURL url, String startRevision, String filter) throws SVNException {
        List<SvnFile> files = new ArrayList<>();
        SVNRevision start = SVNRevision.create(Long.valueOf(startRevision));
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        SVNDiffClient svnDiffClient = new SVNDiffClient(svnAuthenticationManager, defaultOptions);
        SVNRepository repository = SVNRepositoryFactory.create(url);
        repository.setAuthenticationManager(svnAuthenticationManager);
        svnDiffClient.doDiffStatus(url, start, url, SVNRevision.HEAD, SVNDepth.INFINITY, false, diff -> {
            if (!diff.getModificationType().equals(STATUS_NONE) ) {
                if (StringUtils.isBlank(filter) || diff.getURL().getPath().startsWith(filter)) {
                    SvnFile svnFile = new SvnFile();
                    svnFile.setPath(diff.getURL().toString());
                    CommitType what = CommitType.what(diff.getModificationType().toString());
                    if (what != null) {
                        svnFile.setType(what);
                        svnFile.setNodeType(diff.getKind().toString());
                        files.add(svnFile);
                    }
                }
            }
        });
        return files;
    }


    private static void setupLibrary() {

        DAVRepositoryFactory.setup();

        SVNRepositoryFactoryImpl.setup();

        FSRepositoryFactory.setup();
    }
}
