package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsUtils {

	private UserDetails details;

	private Collection<GrantedAuthority> authorities = new ArrayList<>();

	public UserDetailsUtils() {
		getDetails();
		getAuthorities();

	}

	public UserDetails getDetails() {
		if (details == null) {
			try {
				Object principal = SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

				if (principal instanceof UserDetails) {
					details = ((UserDetails) principal);
				}

			} catch (Exception e) {
			}
		}

		return details;
	}

	@SuppressWarnings("unchecked")
	public Collection<GrantedAuthority> getAuthorities() {
		if (authorities.size() == 0) {
			try {
				if (details != null) {
					authorities = (Collection<GrantedAuthority>) details
							.getAuthorities();
				}
			} catch (Exception e) {

			}
		}

		return authorities;
	}

	/**
	 * 检测用户组是否存在当前用户组中
	 * 
	 * @param group
	 * @return
	 */
	protected boolean ValidAuthorities(String group) {
		if (authorities.size() > 0) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if (grantedAuthority.getAuthority().equals(group)) {
					return true;
				}
			}
		}

		return false;
	}
}
