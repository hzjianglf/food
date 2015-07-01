package com.sniper.springmvc.hibernate.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.utils.FilesUtil;

@Service("channelService")
public class ChannelServiceImpl extends BaseServiceImpl<Channel> implements
		ChannelService {

	@Resource(name = "channelDao")
	public void setDao(BaseDao<Channel> dao) {

		super.setDao(dao);
	}

	@Override
	public List<Channel> getChannelListById(Integer[] id) {

		String hql = "from Channel where id in(" + StringUtils.join(id, ",")
				+ ")";

		return this.findEntityByHQL(hql);
	}

	/**
	 * 根据类型读取不同列表的栏目
	 */
	@Override
	public List<Channel> getChannelListByType(Integer[] id) {

		String hql = "from Channel where showType in("
				+ StringUtils.join(id, ",") + ")  order by sort desc, id desc";

		return this.findEntityByHQL(hql);
	}

	@Override
	public List<Channel> getChannelListByType(Integer[] id, String sort) {
		String hql = "from Channel where showType in("
				+ StringUtils.join(id, ",") + ")  order by " + sort;

		return this.findEntityByHQL(hql);
	}

	@Override
	public List<Channel> getChannelListByType(Integer[] id, boolean enabled,
			String sort) {
		String hql = "from Channel where showType in("
				+ StringUtils.join(id, ",") + ")  order by " + sort;

		return this.findEntityByHQL(hql);
	}

	@Override
	public Map<String, String> getMapChannels(Integer[] id, boolean enabled,
			String sort) {
		Map<String, String> channels = new HashMap<>();

		List<Channel> list = this.getChannelListByType(id, enabled, sort);

		for (Channel channel : list) {
			channels.put(String.valueOf(channel.getId()), channel.getName());
		}

		return channels;
	}

	@Override
	public Map<String, String> getMapChannels(Integer[] id, String sort) {
		Map<String, String> channels = new HashMap<>();

		List<Channel> list = this.getChannelListByType(id, sort);

		for (Channel channel : list) {
			channels.put(String.valueOf(channel.getId()), channel.getName());
		}

		return channels;
	}

	@Override
	public String getChannelListByTypeForTree(Integer[] id, boolean enabled,
			String sort, String treeFidAttr) {
		List<Channel> channels = getChannelListByType(id, enabled, sort);
		// 查询所有fid

		Set<Integer> fids = new HashSet<>();
		for (Channel channel : channels) {
			if (channel.getFid() != 0) {
				fids.add(channel.getFid());
			}
		}

		List<String> nodes = new ArrayList<>();
		for (Channel c : channels) {

			StringBuffer buffer = new StringBuffer();

			buffer.append("{id:");
			buffer.append(c.getId());
			buffer.append(",pId:");
			buffer.append(c.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(c.getName());
			buffer.append("\"");

			Integer[] fidss = fids.toArray(new Integer[fids.size()]);

			if (c.getId() == 4) {
				buffer.append(",open:true, nocheck:true");
			} else if (Arrays.binarySearch(fidss, c.getId()) > -1) {
				// buffer.append(",open:true, nocheck:true");
				buffer.append(treeFidAttr);
			}

			buffer.append("}");
			//
			nodes.add(buffer.toString());
		}

		return StringUtils.join(nodes, ",\r");
	}

	@Override
	public List<Channel> getChannelListByType(Integer[] id, boolean enabled) {

		Map<String, Object> params1 = new HashMap<>();
		params1.put("status", enabled);

		String hql = "from Channel where showType in("
				+ StringUtils.join(id, ",")
				+ ") and status = :status order by sort desc, id desc";
		return this.findEntityByHQL(hql, params1);
	}

	@Override
	public Channel getChannelByName(String name) {
		String hql = "from Channel where name = :name order by sort desc, id desc";
		Map<String, Object> params1 = new HashMap<>();
		params1.put("name", name);

		Channel channel = (Channel) uniqueResult(hql, params1);
		if (channel != null) {
			Channel channel2 = getCEntity(channel.getFid());
			return channel2;
		}
		return null;
	}

	@Override
	public List<Channel> getChannelsByType(int type, boolean status) {
		String hql = "FROM Channel where showType=:type and status = :status order by sort desc,id desc";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		params.put("status", status);
		return this.findCEntityByHQL(hql, params);
	}

	/**
	 * 获取项目
	 */
	@Override
	public List<Channel> getChannelsByFidAndSelf(int id, boolean status) {
		String hql1 = "FROM Channel where (fid=:fid or id=:id) and status = :status order by sort desc,id desc";
		Map<String, Object> params1 = new HashMap<>();
		params1.put("fid", id);
		params1.put("id", id);
		params1.put("status", true);
		List<Channel> channels1 = this.findEntityByHQL(hql1, params1);
		return channels1;
	}

	@Override
	public List<Channel> getChannelsByFid(int fid, boolean status) {
		String hql = "FROM Channel where fid=:fid and status = :status order by sort desc, id desc";
		Map<String, Object> params = new HashMap<>();
		params.put("fid", fid);
		params.put("status", status);
		return this.findEntityByHQL(hql, params);
	}

	@Override
	public List<Channel> getChannelsByFid(int fid, boolean status, String sort) {
		String hql = "FROM Channel where fid=:fid and status =:status order by "
				+ sort;
		Map<String, Object> params = new HashMap<>();
		params.put("fid", fid);
		params.put("status", status);
		return this.findEntityByHQL(hql, params);
	}

	@Override
	public List<Channel> getChannelsByFid(int fid, boolean status,
			Boolean showHome, String sort) {
		String hql = "FROM Channel where fid=:fid and showHome = :showHome and status = :status order by "
				+ sort;
		Map<String, Object> params = new HashMap<>();
		params.put("fid", fid);
		params.put("showHome", showHome);
		params.put("status", status);
		return this.findEntityByHQL(hql, params);
	}

	@Override
	public List<String> getChannelList(int id, boolean status) {
		List<Channel> channels = this.getChannelsByFid(id, status);
		List<String> lists = new ArrayList<>();
		for (Channel c : channels) {
			lists.add(c.getName());
		}
		return lists;
	}

	@Override
	public String getTreeMenuByType(int id, boolean status) {
		List<String> nodes = new ArrayList<>();
		List<Channel> channels = this.getChannelsByType(id, status);
		return getTreeMenu(nodes, channels);
	}

	private String getTreeMenu(List<String> nodes, List<Channel> channels) {
		String depMenu;
		for (Channel c : channels) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append(c.getId());
			buffer.append(",pId:");
			buffer.append(c.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(c.getName());
			buffer.append("\"");
			buffer.append(",title:\"");
			buffer.append(c.getName().trim());
			buffer.append("\"");
			buffer.append("}");

			nodes.add(buffer.toString());
		}
		depMenu = StringUtils.join(nodes, ",");
		return depMenu;
	}

	@Override
	public String getTreeMenuByFid(int fid, boolean status) {
		List<String> nodes = new ArrayList<>();
		List<Channel> channels = this.getChannelsByFid(fid, status);
		return getTreeMenu(nodes, channels);
	}

	@Override
	public void deleteEntiry(Channel t) {

		if (!StringUtils.isAnyBlank(t.getAttachement())) {
			FilesUtil.delFileByPath(t.getAttachement());
		}
		super.deleteEntiry(t);
	}
}
