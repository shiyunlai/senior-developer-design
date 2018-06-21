package org.tis.senior.module.core.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.service.ISSvnAccountService;

import javax.annotation.Resource;
/**
 * Created by Administrator on 2017/12/11.
 * 自定义权限匹配和账号密码匹配
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private ISSvnAccountService accountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SSvnAccount userInfo = (SSvnAccount) principals.getPrimaryPrincipal();
        authorizationInfo.addRole(String.valueOf(userInfo.getRole().getValue()));
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        EntityWrapper<SSvnAccount> ew = new EntityWrapper<>();
        ew.eq(SSvnAccount.COLUMN_USER_ID,username);
        SSvnAccount userInfo = accountService.selectOne(ew);
        if (userInfo == null) {
            throw new UnknownAccountException("用户不存在！");
        }
        return new SimpleAuthenticationInfo(
                userInfo, // 用户名
                userInfo.getSvnPwd(), // 密码
                ByteSource.Util.bytes(userInfo.getSvnUser()), // salt=username+salt
                getName()  // realm name
        );
    }

    /**
     * 密码加密
     * @param password 加密前的密码
     * @param salt 盐值，可以想象成同一道菜加的盐不同，最后的味道也就不同，同理，相同的密码盐值不同，最后加密的密码也不同
     * @return
     */
    public static String generate(String password, String salt) {
        if(StringUtils.isBlank(password) && StringUtils.isBlank(salt)) {
            return null;
        }
        return new SimpleHash(
                "md5",
                password,
                ByteSource.Util.bytes(salt),
                2).toHex();
    }

//    public static void main(String[] args) {
//        System.out.println(generate("admin", "系统管理员"));
//        System.out.println(generate("zch", "赵春海"));
//        System.out.println(generate("ljh", "李俊华"));
//    }
}
