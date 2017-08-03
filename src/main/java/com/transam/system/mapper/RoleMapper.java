package com.transam.system.mapper;

import java.util.List;
import java.util.Map;

import com.transam.persistence.BaseMapper;
import com.transam.system.entity.Role;


public interface RoleMapper extends BaseMapper<Role> {
	
	public List<Role> findRoleByUserId(Long userId);
	
	public List<Role> findRoleByMap(Map<String,Object> map);
	
	public List<Role> findByRole(Role role);
}
