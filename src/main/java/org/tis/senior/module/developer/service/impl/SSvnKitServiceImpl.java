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
import org.tis.senior.module.developer.util.DeveloperUtils;
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
import java.util.concurrent.atomic.AtomicLong;

import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_NONE;

@Service
@Transactional(rollbackFor = Exception.class)
public class SSvnKitServiceImpl implements ISSvnKitService {

    @Autowired
    private SvnProperties svnProperties;

    private ISVNAuthenticationManager svnAuthenticationManager;

    private SVNClientManager svnClientManager;

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
        return doDiffStatus(svnurl, startRevision, true);
    }

    @Override
    public List<SvnFile> getDiffStatus(String url, String startRevision, boolean includeDir) throws SVNException {
        SVNURL svnurl = SVNURL.parseURIEncoded(url);
        return doDiffStatus(svnurl, startRevision, includeDir);
    }

    @Override
    public List<SvnFile> getBranchDiffStatus(String url, String startRevision) throws SVNException {
        List<SvnFile> list = new ArrayList<>();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        repository.setAuthenticationManager(this.svnAuthenticationManager);
        // 分支最新版本
        long branchLatestRevision = getLastRevision(url);
        if (branchLatestRevision <= Long.valueOf(startRevision)) {
            return list;
        }
        Collection entries = repository.getDir("", -1, null, (Collection) null);
        for (Object o : entries) {
            SVNDirEntry entry = (SVNDirEntry) o;
            if (entry.getKind() == SVNNodeKind.DIR) {
                // 如果当前工程最后变动版本小于此次查询的起始版本，说明没有任何变动
                if (entry.getRevision() <= Long.valueOf(startRevision)) {
                    continue;
                }
                SVNRepository svnRepository = SVNRepositoryFactory.create(entry.getURL());
                svnRepository.setAuthenticationManager(this.svnAuthenticationManager);
                // 获取当前工程第一次提交版本
                AtomicLong firstRevision = new AtomicLong(0L);
                svnRepository.log(new String[] {""}, 0, -1, false, true,
                        1, false, null, logEntry ->
                                firstRevision.set(logEntry.getRevision()));
                // 如果查询起始版本大于工程的第一次提交版本号,无需判断是否为新增工程
                if (Long.valueOf(startRevision) > firstRevision.get()) {
                    svnRepository.log(new String[] {""}, Long.valueOf(startRevision), branchLatestRevision,
                            false, true, 1, false, null, logEntry ->
                                list.addAll(doBranchDiffStatus(entry.getURL(),
                                        startRevision, null)));
                } else {
                    svnRepository.log(new String[]{""}, firstRevision.get(), branchLatestRevision,
                            true, true, 1, false, null, logEntry -> {
                                boolean isCopy = false;
                                if (logEntry.getChangedPaths().size() > 0) {
                                    Set<String> changedPathsSet = logEntry.getChangedPaths().keySet();
                                    for (String aChangedPathsSet : changedPathsSet) {
                                        SVNLogEntryPath p = logEntry.getChangedPaths().get(aChangedPathsSet);
                                        if (p.getCopyPath() != null) {
                                            isCopy = true;
                                            break;
                                        }
                                    }
                                    if (isCopy) {
                                        list.addAll(doBranchDiffStatus(entry.getURL(),
                                                Long.toString(logEntry.getRevision()), null));
                                    } else {
                                        list.addAll(doBranchDiffStatus(entry.getURL(),
                                                Long.toString(logEntry.getRevision() - 1), null));
                                    }
                                }
                            });
                }
            }
        }
        return list;
    }

    @Override
    public long doMkDir(String url, String commitMessage) throws SVNException {
        SVNURL svnurl = SVNURL.parseURIEncoded(url);
        SVNCommitInfo info = this.svnClientManager.getCommitClient().doMkDir(new SVNURL[]{svnurl}, commitMessage);
        return info.getNewRevision();
    }

    @Override
    public long doDelete(String url, String commitMessage) throws SVNException {
        SVNURL svnurl = SVNURL.parseURIEncoded(url);
        SVNCommitInfo info = this.svnClientManager.getCommitClient().doDelete(new SVNURL[]{svnurl}, commitMessage);
        return info.getNewRevision();
    }

    @Override
    public void doCopy(String[] sourceUrls, String destUrl, String commitMessage) throws SVNException {
        SVNCopyClient svnCopyClient = this.svnClientManager.getCopyClient();
        SVNCopySource[] copySources = new SVNCopySource[sourceUrls.length];
        for (int i = 0; i < sourceUrls.length; i++) {
            copySources[i] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, SVNURL.parseURIEncoded(sourceUrls[i]));
        }
        svnCopyClient.doCopy(copySources, SVNURL.parseURIEncoded(destUrl + "/"), false, false,
                false, commitMessage, null);

    }

    @Override
    public List<String> getDir(String url) throws SVNException {
        List<String> list = new ArrayList<>();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        repository.setAuthenticationManager(this.svnAuthenticationManager);
        Collection entries = repository.getDir("", -1, null, (Collection) null);
        for (Object o : entries) {
            SVNDirEntry entry = (SVNDirEntry) o;
            if (entry.getKind() == SVNNodeKind.DIR) {
                list.add(entry.getName());
            }
        }
        return list;
    }

    @PostConstruct
    private void svnAuthenticationManagerInit() {
        setupLibrary();
        this.svnAuthenticationManager = SVNWCUtil.createDefaultAuthenticationManager(
                svnProperties.getUsername(), svnProperties.getPassword().toCharArray()
        );
        this.svnClientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true),
                this.svnAuthenticationManager);
    }


    private SVNInfo getLastRevision(SVNURL url) throws SVNException {
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        SVNWCClient svnwcClient = new SVNWCClient(svnAuthenticationManager, defaultOptions);
        return svnwcClient.doInfo(url, SVNRevision.HEAD, SVNRevision.HEAD);

    }

    private List<SvnFile> doBranchDiffStatus(SVNURL url, String startRevision, String filter) throws SVNException {
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
                    svnFile.setPath(DeveloperUtils.getPathUTF(diff.getURL().toString()));
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

    private List<SvnFile> doDiffStatus(SVNURL svnurl, String startRevision, boolean includeDir) throws SVNException {
        List<SvnFile> files = new ArrayList<>();
        SVNRevision start = SVNRevision.create(Long.valueOf(startRevision));
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        SVNDiffClient svnDiffClient = new SVNDiffClient(svnAuthenticationManager, defaultOptions);
//        SVNDepth depth = includeDir ? SVNDepth.INFINITY : SVNDepth.FILES;
        svnDiffClient.doDiffStatus(svnurl, start, svnurl, SVNRevision.HEAD, SVNDepth.INFINITY, false, diff -> {
            SvnFile svnFile = new SvnFile();
            svnFile.setPath(DeveloperUtils.getPathUTF(diff.getURL().toString()));
            CommitType what = CommitType.what(diff.getModificationType().toString());
            if (what != null) {
                svnFile.setType(what);
                svnFile.setNodeType(diff.getKind().toString());
                files.add(svnFile);
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
