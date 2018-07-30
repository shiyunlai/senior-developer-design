package org.tis.senior.module.developer.service;

import org.tis.senior.module.developer.entity.vo.SvnCommit;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tmatesoft.svn.core.SVNException;

import java.util.List;

public interface ISSvnKitService {

    /**
     * 加载SVN LOG
     * @param url svn路径
     * @param startRevision 起始版本号
     * @return
     */
    List<SvnCommit> loadSvnHistory(String url, int startRevision) throws SVNException;

    /**
     * 获取版本间的差异
     * @param url svn路径
     * @param startRevision 起始版本号
     * @return
     */
    List<SvnFile> getDiffStatus(String url, String startRevision) throws SVNException;

    /**
     * 获取版本间的差异
     * @param url svn路径
     * @param startRevision 起始版本号
     * @return
     */
    List<SvnFile> getDiffStatus(String url, String startRevision, boolean includeDir) throws SVNException;

    /**
     * 获取分支路径下版本间的差异
     * @param url svn路径
     * @param startRevision 起始版本号
     * @return
     */
    List<SvnFile> getBranchDiffStatus(String url, String startRevision) throws SVNException;

    /**
     * 获取最新版本
     * @param url
     * @return
     */
    int getLastRevision(String url) throws SVNException;

    /**
     * 新建目录
     * @param url 分支路径
     * @param commitMessage 提交注释
     * @return 分支版本号
     */
    long doMkDir(String url, String commitMessage) throws SVNException;

    /**
     * 删除分支
     * @param url 分支路径
     * @param commitMessage 提交注释
     * @return 分支版本号
     */
    long doDelete(String url, String commitMessage) throws SVNException;

    /**
     * 复制分支
     * @param sourceUrls 源路径数组
     * @param destUrl 目标路径
     * @param commitMessage 提交注释
     * @throws SVNException
     */
    void doCopy(String[] sourceUrls, String destUrl, String commitMessage) throws SVNException;

    /**
     * 获取目录
     * @param url
     * @return
     * @throws SVNException
     */
    List<String> getDir(String url) throws SVNException;
}
