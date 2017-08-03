package com.transam.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.transam.system.entity.Resources;
import com.transam.system.entity.Role;
import com.transam.system.entity.User;
import com.transam.system.service.ResourcesService;
import com.transam.system.service.RoleService;
import com.transam.system.service.UserService;
import com.transam.utils.Common;

import javax.annotation.Resource;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Autowired
	private RoleService roleService;
    @Resource
    private ResourcesService resourcesService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String roleIds = SecurityUtils.getSubject().getSession().getAttribute("roleSessionId").toString();
		List<Resources> rs = resourcesService.findResByRoleIds(roleIds);
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for(Resources resources: rs){
        	if(!StringUtils.isBlank(resources.getResKey())){
        		info.addStringPermission(resources.getResKey());
        	}
        }
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        User user = userService.findByAccountName(username).get(0);
        if(user==null) throw new UnknownAccountException();
        if ("1".equals(user.getLocked())) {
			throw new LockedAccountException(); // 帐号锁定
		}
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, // 用户名
				user.getPassword(), // 密码
				ByteSource.Util.bytes(username + "" + user.getCredentialsSalt()),// salt=username+salt
				getName() // realm name
		);
        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSession", user);
        session.setAttribute("userSessionId", user.getId());
        
        List<Role> roleList = roleService.findRoleByUserId(user.getId());
		if(roleList.size() > 0 && roleList != null){
			String roleSessionId = "";
			String roleSessionName = "";
			for(Role role : roleList){
				roleSessionId += role.getId() + ",";
				roleSessionName += role.getName() + ",";
			}
			session.setAttribute("roleSessionName", Common.trimComma(roleSessionName));
			session.setAttribute("roleSessionId", Common.trimComma(roleSessionId));
		}else{
			throw new AccountException();
		}
        
        return authenticationInfo;
    }
}
