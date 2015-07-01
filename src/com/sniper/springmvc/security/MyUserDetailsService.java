package com.sniper.springmvc.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sniper.springmvc.hibernate.service.impl.AdminUserService;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 用户读取用户的信息角色信息，账号是否过期 如果是前台用户重新定义一个这个方法即可
 * 
 * @author laolang
 * 
 */
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminUserService adminUserService;

	public MyUserDetailsService() {

	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		AdminUser adminUser = adminUserService.validateByName(username);
		if (adminUser == null) {
			throw new UsernameNotFoundException(username);
		}

		Collection<GrantedAuthority> authorities = obtionGrantedAuthorities(adminUser);

		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonEnabled = false;
		if (ValidateUtil.isValid(adminUser.getEnabled())) {
			accountNonEnabled = adminUser.getEnabled();
		}
		boolean accountNonLocked = true;
		if (adminUser.getLocked() == true) {
			accountNonLocked = false;
		}

		Date date = new Date();
		if (adminUser.getUsernameExpired() != null) {

			if (date.after(adminUser.getUsernameExpired())) {
				accountNonExpired = false;
			}
		}

		if (adminUser.getPasswordExpired() != null) {
			if (date.after(adminUser.getPasswordExpired())) {
				credentialsNonExpired = false;
			}
		}

		// 在数据库中获取信息之后赋值给他们
		User user = new User(adminUser.getName(), adminUser.getPassword(),
				accountNonEnabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);

		return user;
	}

	/**
	 * 取得用户的权限 获取当前用户可以查看的地址列表
	 * 
	 * @param adminUser
	 * @return
	 */
	private Collection<GrantedAuthority> obtionGrantedAuthorities(
			AdminUser adminUser) {

		Set<GrantedAuthority> authSet = new HashSet<>();
		Set<AdminGroup> groups = adminUser.getAdminGroup();

		for (AdminGroup adminGroup : groups) {
			authSet.add(new SimpleGrantedAuthority(adminGroup.getValue()));
		}
		return authSet;
	}

}
