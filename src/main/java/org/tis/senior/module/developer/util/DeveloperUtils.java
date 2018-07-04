package org.tis.senior.module.developer.util;


import com.alibaba.fastjson.JSON;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

public class DeveloperUtils {

    /**
     * 获取工程明
     *
     * @param path
     * @return
     */
    public static String getProjectName(String path) {

        String[] pathSplit = path.split("/");

        if(pathSplit.length < 11) {
            return "";
        }
        if (pathSplit[8].equals("Feature") || pathSplit[8].equals("Hotfix")) {

            return getPathUTF(pathSplit[10]);
        } else {

            return getPathUTF(pathSplit[9]);
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
        return getPathUTF(pathSplit[pathSplit.length - 1]);
    }

    /**
     * 获取工程文件名
     * @param svnPath
     * @return
     */
    public static String getFilePath(String svnPath)  {
        return getPathUTF(svnPath.substring(svnPath.indexOf(getProjectName(svnPath))));
    }

    /**
     * 获取IBS工程的下级模块
     * @param path
     * @return
     */
    public static String getModule(String path){

        String[] pathSplit = path.split("/");

        if(pathSplit.length < 12) {
            return "";
        }
        if (pathSplit[8].equals("Feature") || pathSplit[8].equals("Hotfix")) {

            return getPathUTF(pathSplit[11]);
        } else {
            return getPathUTF(pathSplit[10]);
        }
    }

    /**
     * 获取ECD的打包路径
     * @param fullPath
     * @return
     */
    public static String getEcdPath(String fullPath){
        String project = getProjectName(fullPath);
        String module = getModule(fullPath);
        return project+"/"+module+"/src";
    }

    /**
     * 转码
     * @param path
     * @return
     */
    public static String getPathUTF(String path){

        String svnPath = path;
        try {
            svnPath = java.net.URLDecoder.decode(svnPath,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return svnPath;
    }

}
