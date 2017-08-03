package com.transam.system.mapper;

import java.util.List;
import java.util.Map;

import com.transam.persistence.BaseMapper;
import com.transam.system.entity.User;


public interface UserMapper extends BaseMapper<User> {
	public List<User> findByAccountName(String accountName);
	
	public List<User> findByUser(User user);
	
	public Integer deleteUserRoleByUserId(Long userId);
	
	public List<User> getByRoleId(Long roleId);
	
	public List<User> getUserbyMap(Map<String,Object> map);
	
	public Integer deleteByAccount(String accountName);
}
