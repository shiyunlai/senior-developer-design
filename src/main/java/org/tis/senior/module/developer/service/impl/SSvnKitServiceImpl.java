package org.tis.senior.module.developer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.core.config.SvnProperties;
import org.tis.senior.module.developer.entity.enums.SvnPathType;
import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnPath;
import org.tis.senior.module.developer.service.ISSvnKitService;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SSvnKitServiceImpl implements ISSvnKitService {

    @Autowired
    private SvnProperties svnProperties;
    /**
     * 获取svn的提交历史记录
     *
     * @return
     */
    @Override
    public List<SvnCommit> loadSvnHistory(String url, Long startRevision) {

        List<SvnCommit> scList = new ArrayList<>();

        String name = svnProperties.getUserName();
        String password = svnProperties.getPassword();
        //HEAD (the latest) revision
        long endRevision = -1;

        setupLibrary();

        SVNRepository repository = null;

        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            System.err.println("error while creating an SVNRepository for the location '" + url + "': " + svne.getMessage());
        }

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password.toCharArray());
        repository.setAuthenticationManager(authManager);

        try {
            endRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
        }

        Collection logEntries = null;
        try {

            logEntries = repository.log(new String[] {""}, null, startRevision, endRevision, true, true);

        } catch (SVNException svne) {
            System.out.println("error while collecting log information for '" + url + "': " + svne.getMessage());
        }
        for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
            /*
             * gets a next SVNLogEntry
             */
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();

            SvnCommit svnCommit = new SvnCommit();
            svnCommit.setRevision(logEntry.getRevision());
            svnCommit.setAuthor(logEntry.getAuthor());
            svnCommit.setDate(logEntry.getDate());
            svnCommit.setMessage(logEntry.getMessage());

            if (logEntry.getChangedPaths().size() > 0) {

                Set changedPathsSet = logEntry.getChangedPaths().keySet();

                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {

                    SVNLogEntryPath entryPath = logEntry.getChangedPaths().get(changedPaths.next());

                    SvnPath svnPath = new SvnPath();

                    String type = svnPath.getType().toString();
                    svnPath.setPath(entryPath.getPath());
                    //变动类型
                    if("A".equals(type)){
                        svnPath.setType(SvnPathType.ADDED);
                    }else if("D".equals(type)){
                        svnPath.setType(SvnPathType.DELETED);
                    }else if("M".equals(type)){
                        svnPath.setType(SvnPathType.MODIFIED);
                    }else if("R".equals(type)){
                        svnPath.setType(SvnPathType.REPLACED);
                    }
                    if(entryPath.getCopyPath() != null){
                        svnPath.setCopyPath(entryPath.getCopyPath());
                        svnPath.setCopyRevision(entryPath.getCopyRevision());
                    }
                }
            }
            scList.add(svnCommit);
        }

        return scList;
    }

    private static void setupLibrary() {

        DAVRepositoryFactory.setup();

        SVNRepositoryFactoryImpl.setup();

        FSRepositoryFactory.setup();
    }
}
