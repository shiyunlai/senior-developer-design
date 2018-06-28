package org.tis.senior.module.developer.util;


import com.alibaba.fastjson.JSON;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

public class DeveloperUtils {

    /**
     * 获取工程明
     *
     * @param path
     * @return
     */
    public static String getProjectName(String path) {

        String[] pathSplit = path.split("/");

        if (pathSplit[3].equals("Featrue") || pathSplit[3].equals("Hotfix")) {

            return pathSplit[5];
        } else {
            return pathSplit[4];
        }
    }

    /**
     * 获取代码名称
     *
     * @param svnPath
     * @return
     */
    public static String getProgramName(String svnPath) {

        String[] pathSplit = svnPath.split("/");
        return pathSplit[pathSplit.length - 1];
    }

    /**
     * 获取工程文件名
     * @param svnPath
     * @return
     */
    public static String getFilePath(String svnPath)  {
        return svnPath.substring(svnPath.indexOf(getProjectName(svnPath)));
    }

}
