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

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
