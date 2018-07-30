package org.tis.senior.module.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/25
 **/
@Component
@ConfigurationProperties(prefix = "tis.svn")
public class SvnProperties {

    /**
     * SVN用户账号
     */
    private String username;

    /**
     * SVN用户密码
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
