package org.tis.senior.module.core.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.tis.senior.module.core.aop.ShiroFormAuthenticationFilter;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 */
@Configuration
public class ShiroConfig {

    @Value("${tis.shiro.enable}")
    private boolean enable;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new LinkedHashMap<>(1);
        filterMap.put("authc2", new ShiroFormAuthenticationFilter());

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
//        filterChainDefinitionMap.put("/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/login", "anon");
        if (enable) {
            filterChainDefinitionMap.put("/**", "authc2");
        } else {
            filterChainDefinitionMap.put("/**", "anon");
        }
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/unauth");
        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 自定义sessionManager 会话管理器
     */
    @Bean
    public MySessionManager sessionManager() {
        MySessionManager sessionManager = new MySessionManager();
        // 设置全局会话超时时间，默认30分钟，即如果30 * 4 分钟内没有访问会话将过期 1000 * 60 * 30 * 4 = 7200000
        sessionManager.setGlobalSessionTimeout(7200000);
        // 删除失效的session
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启会话验证器，默认是开启的
        sessionManager.setSessionValidationSchedulerEnabled(true);
        /*Shiro提供了会话验证调度器，用于定期的验证会话是否已过期，如果过期将停止会话；
        出于性能考虑，一般情况下都是获取会话时来验证会话是否过期并停止会话的；
        但是如在web环境中，如果用户不主动退出是不知道会话是否过期的，因此需要定期的检测会话是否过期，
        Shiro提供了会话验证调度器SessionValidationScheduler来做这件事情*/
//        sessionManager.setSessionValidationScheduler(sessionValidationScheduler());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    /**
     * 会话DAO
     * @return
     */
    @Bean
    public EnterpriseCacheSessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }

    /**
     * 会话ID生成器
     *
     * @return
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     * 此处需要使用static方法，否则无法获取配件文件中的值
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
     */
    @Bean
    @ConditionalOnProperty(value = {"tis.shiro.enable"}, matchIfMissing = true)
    @DependsOn(value = "lifecycleBeanPostProcessor") //依赖其他bean的初始化
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
