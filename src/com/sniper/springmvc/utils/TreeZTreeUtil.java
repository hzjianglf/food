package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;

import redis.clients.jedis.Jedis;

import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.security.MySecurityMetadataSource;

/**
 * 树形数据组装
 * 
 * @author sniper
 * 
 */
public class TreeZTreeUtil {

	private String treeNodesAll;
	private String treeNodes;
	private String contextPath;
	private String urlPath;
	private List<AdminRight> adminRights = new ArrayList<>();
	// 所有的url对应的用户组
	private Map<String, Collection<ConfigAttribute>> rightMap = new HashMap<>();
	{
		rightMap = MySecurityMetadataSource.getRightMap();
	}
	// 当前用户所拥有的用户组
	private Collection<GrantedAuthority> authorities = new ArrayList<>();

	public TreeZTreeUtil() {

	}

	public void setContextPath(String contentPath) {
		this.contextPath = contentPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public void setAdminRights(List<AdminRight> adminRights) {
		if (this.adminRights.size() == 0) {
			this.adminRights = adminRights;
		}
	}

	public List<AdminRight> getAdminRights() {
		return adminRights;
	}

	public void init() {

		RedisUtil redisUtil = new RedisUtil();
		Jedis jedis = redisUtil.getJedis();
		String keyName = RedisUtil.getKeyName(this.getClass(), getRedisKey());
		if (jedis.exists(keyName)) {
			try {
				this.treeNodes = jedis.get(keyName);
			} catch (Exception e) {
				// 释放redis对象
				redisUtil.returnBrokenResource(jedis);
				e.printStackTrace();
			} finally {
				redisUtil.returnResource(jedis);
			}

			return;
		}

		String isParent = "";
		String isHidden = "";
		String url = "";

		List<String> nodes = new ArrayList<>();

		for (AdminRight right : adminRights) {
			// 阻止没有权限的url显示
			if (!isUsed(right.getUrl())) {
				continue;
			}

			// 被隐藏的
			if (!right.isTheShow()) {
				isHidden = ",isHidden:true";
			} else {
				isHidden = "";
			}

			if (right.getUrl().endsWith("#")) {
				url = "";
			} else {
				url = ",url:\"" + this.contextPath + right.getUrl() + "\"";
			}

			// 是否是父级
			isParent = isParent(right);
			if (!"".equals(isParent)) {
				// 父级不能带有url
				url = "";
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append(right.getId());
			buffer.append(",pId:");
			buffer.append(right.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(right.getName());
			buffer.append("\"");
			buffer.append(url);
			buffer.append(isParent);
			// buffer.append(classStyle);
			buffer.append(",target:\"");
			buffer.append(right.getTarget());
			buffer.append("\",title:\"");
			buffer.append(right.getName().trim());
			buffer.append("\"");
			buffer.append(isHidden);
			buffer.append("}");

			nodes.add(buffer.toString());
		}

		if (nodes.size() != 0) {
			treeNodes = StringUtils.join(nodes, ",\r");
			try {
				jedis.set(keyName, treeNodes);
			} catch (Exception e) {
				// 释放redis对象
				redisUtil.returnBrokenResource(jedis);
				e.printStackTrace();
			} finally {
				redisUtil.returnResource(jedis);
			}

		}

	}

	/**
	 * 所有的
	 */
	public void initAll() {

		String isParent = "";
		String isOpen = "";

		List<String> nodes = new ArrayList<>();

		for (AdminRight right : adminRights) {

			// 是否是父级
			isParent = isParent(right);
			if (!"".equals(isParent)) {
				// 父级不能带有url
				// isOpen = ",open:true";
			} else {
				// isOpen = "";
			}

			if (right.getFid() == 0) {
				isOpen = ",open:true";
			} else {
				isOpen = "";
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append(right.getId());
			buffer.append(",pId:");
			buffer.append(right.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(right.getName());
			buffer.append("\"");
			buffer.append(isParent);
			buffer.append(isOpen);
			buffer.append("}");

			nodes.add(buffer.toString());
		}

		if (nodes.size() != 0) {
			treeNodesAll = StringUtils.join(nodes, ",\r");
		}

	}

	/**
	 * 获取当前url对象
	 * 
	 * @return
	 */
	public AdminRight getRightByUrl() {
		for (AdminRight right : adminRights) {
			if (right.getUrl().equalsIgnoreCase(urlPath)) {
				return right;
			}
		}
		return null;
	}

	private String isParent(AdminRight adminRight) {
		for (AdminRight right : adminRights) {
			if (adminRight.getId() == right.getFid()) {
				return ",isParent:true";
			}
		}

		return "";
	}

	/**
	 * 检测当前用户,当前url是否对当前用户可见
	 * 
	 * @return
	 */
	private boolean isUsed(String url) {

		if (rightMap.get(url) == null) {
			return false;
		}

		Collection<ConfigAttribute> configAttributes = rightMap.get(url);

		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		while (ite.hasNext()) {
			ConfigAttribute ca = ite.next();
			// 访问所请求资源所需要的权限
			String needPermission = ((SecurityConfig) ca).getAttribute();
			// 用户所拥有的权限authentication
			for (GrantedAuthority ga : authorities) {
				if (needPermission.equals(ga.getAuthority())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 得到当前的用户组列表
	 * 
	 * @param authorities
	 */
	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(String treeNodes) {
		this.treeNodes = treeNodes;
	}

	public String getTreeNodesAll() {
		return treeNodesAll;
	}

	/**
	 * 获取redis保存的key
	 * 
	 * @return
	 */
	public String getRedisKey() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("treeNodes_");
		if (authorities.size() == 0) {
			return "";
		}
		for (GrantedAuthority g : authorities) {
			buffer.append(g.getAuthority());
		}
		return buffer.toString();
	}

	public void clearCache() {

		Jedis jedis = new RedisUtil().getJedis();
		for (GrantedAuthority g : authorities) {
			String keyName = RedisUtil.getKeyName(this.getClass(), "treeNodes_"
					+ g.getAuthority());
			jedis.del(keyName);
		}
	}

}
