package org.tis.senior.module.developer.service.impl;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        List<SvnFile> files = new ArrayList<>();
        SVNURL svnurl = SVNURL.parseURIEncoded(url);
        SVNRevision start = SVNRevision.create(Long.valueOf(startRevision));
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        SVNDiffClient svnDiffClient = new SVNDiffClient(svnAuthenticationManager, defaultOptions);
        List<SVNDiffStatus> list = new LinkedList<>();
        svnDiffClient.doDiffStatus(svnurl, start, svnurl, SVNRevision.HEAD, SVNDepth.INFINITY, false, list::add);
        list.forEach(diff -> {
            SVNInfo lastRevision = getLastRevision(diff.getURL());
            SvnFile svnFile = new SvnFile();
            svnFile.setAuthor(lastRevision.getAuthor());
            svnFile.setData(lastRevision.getCommittedDate());
            svnFile.setPath(diff.getURL().toString());
            svnFile.setRevision(lastRevision.getCommittedRevision().getNumber());
            svnFile.setType(CommitType.what(diff.getModificationType().toString()));
            svnFile.setNodeType(diff.getKind().toString());
            files.add(svnFile);
        });
        return files;
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


    private static void setupLibrary() {

        DAVRepositoryFactory.setup();

        SVNRepositoryFactoryImpl.setup();

        FSRepositoryFactory.setup();
    }
}
