package com.transam.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.transam.persistence.BaseMapper;
import com.transam.system.entity.Resources;

public interface ResourcesMapper extends BaseMapper<Resources>{
	public List<Resources> findResource(@Param("resources") Resources resources,@Param("roleId")Long roleId);
	
	public List<Resources> findBtn(Map<String,Object> map);
	
	public Integer deleteResRoleByRoleId(Long roleId);
	
	public Integer addResRoleOfBatch(List<Map<String,Object>> list);
	
	public List<Resources> findResByRoleIds(String[] roleIds);
}
