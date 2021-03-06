package org.tis.senior.module.core.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tis.senior.module.core.web.controller.request.LoginRequest;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.entity.SSvnAccount;

/**
 * Created by Administrator on 2017/12/11.
 */
@RestController
public class ShiroController {

    /**
     * 登录方法
     * @param userInfo
     * @return
     */
    @PostMapping("/login")
    public ResultVO ajaxLogin(@RequestBody @Validated LoginRequest userInfo) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUserId(), userInfo.getPassword());
        subject.login(token);
        JSONObject jsonObject = new JSONObject();
        SSvnAccount principal = (SSvnAccount) SecurityUtils.getSubject().getPrincipal();
        principal.setSvnPwd(null);
        jsonObject.put("userInfo", principal);
        jsonObject.put("token", subject.getSession().getId());
        return ResultVO.success("登陆成功!", jsonObject);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResultVO logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultVO.success("退出登录成功！");
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    public ResultVO unauth() {
        throw new UnauthenticatedException();
    }
}
