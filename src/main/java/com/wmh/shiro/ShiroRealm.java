package com.wmh.shiro;

import com.wmh.shiro.dao.UserMapper;
import com.wmh.shiro.dao.UserPermissionMapper;
import com.wmh.shiro.dao.UserRoleMapper;
import com.wmh.shiro.pojo.Permission;
import com.wmh.shiro.pojo.Role;
import com.wmh.shiro.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SimpleTimeZone;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserPermissionMapper permissionMapper;

	@Autowired
	private UserRoleMapper roleMapper;
	/**
	 * 获取用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		// 获取当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo authorizationInfo  = new SimpleAuthorizationInfo ();
		// 获取当前用户所有角色
		List<Role> roles = roleMapper.findByUserName(user.getUsername());
		Set<String> roleSet = new HashSet<String>();
		for (Role r : roles) {
			roleSet.add(r.getName());
		}
		authorizationInfo.setRoles(roleSet);
		// 获取当前用户的所有权限
		List<Permission> permissions = permissionMapper.findByUserName(user.getUsername());
		Set<String> permissionSet = new HashSet<String>();
		for (Permission p : permissions) {
			permissionSet.add(p.getName());
		}
		authorizationInfo.setStringPermissions(permissionSet);
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String) token.getPrincipal();
		String password = new String((char[])token.getCredentials());
		System.out.println("用户" + userName + "认证-----ShiroRealm.doGetAuthenticationInfo");
		User user = userMapper.findByUserName(userName);

		if (user == null) {
			throw new UnknownAccountException("用户名或密码错误！");
		}
		if (!password.equals(user.getPassword())) {
			throw new IncorrectCredentialsException("用户名或密码错误！");
		}
		if (user.getStatus().equals("0")) {
			throw new LockedAccountException("账号已被锁定,请联系管理员！");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}
