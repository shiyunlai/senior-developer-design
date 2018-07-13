package org.tis.senior.module.developer.util;


import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class DeveloperUtils {

    private static final String PLACE_HOLDER = "{%s}";

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
     * 获取模块全路径
     * @param path
     * @return
     */
    public static String getModulePath(String path) {
        String[] pathSplit = path.split("/");

        if(pathSplit.length < 12) {
            return "";
        }
        if (pathSplit[8].equals("Feature") || pathSplit[8].equals("Hotfix")) {
            return getPathUTF(Arrays.stream(pathSplit).limit(12)
                    .reduce("", (r, t) -> "".equals(r) ?t:(r + "/" + t)));
        } else {
            return getPathUTF(Arrays.stream(pathSplit).limit(11)
                    .reduce("", (r, t) -> "".equals(r) ?t:(r + "/" + t)));
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

    /**
     * 下划线命名转为驼峰命名
     *
     * @param para
     * @return
     */
    public static String UnderlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * <p>
     * 获取in表达式
     * </p>
     *
     * @param value  集合
     */
    public static String inExpression(Collection<?> value) {

        if (CollectionUtils.isNotEmpty(value)) {
            StringBuilder inSql = new StringBuilder();
            inSql.append("(");
            int size = value.size();
            for (int i = 0; i < size; i++) {
                inSql.append(String.format(PLACE_HOLDER, i));
                if (i + 1 < size) {
                    inSql.append(",");
                }
            }
            inSql.append(")");
            return inSql.toString();
        }
        return "('')";
    }

    /**
     * 格式化日期到天
     * @param date 如果date为null, 则使用当前时间
     * @return
     */
    public static String getDayFormat(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

}
