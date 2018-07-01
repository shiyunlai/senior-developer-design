package org.tis.senior.module.developer.util;


import com.alibaba.fastjson.JSON;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import java.io.UnsupportedEncodingException;

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
            try {
                pathSplit[10] = java.net.URLDecoder.decode(pathSplit[10],"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return pathSplit[10];
        } else {
            try {
                pathSplit[9] = java.net.URLDecoder.decode(pathSplit[9],"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return pathSplit[9];
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

    /**
     * 获取IBS工程的下级模块
     * @param path
     * @return
     */
    public static String isEpdOrEcd(String path){

        String[] pathSplit = path.split("/");

        if(pathSplit.length < 12) {
            return "";
        }
        if (pathSplit[8].equals("Feature") || pathSplit[8].equals("Hotfix")) {
            try {
                pathSplit[11] = java.net.URLDecoder.decode(pathSplit[11],"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return pathSplit[11];
        } else {
            try {
                pathSplit[10] = java.net.URLDecoder.decode(pathSplit[10],"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return pathSplit[10];
        }
    }
}
