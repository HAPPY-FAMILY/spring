package com.transam.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transam.persistence.BaseService;
import com.transam.system.entity.Resources;
import com.transam.system.mapper.ResourcesMapper;
import com.transam.utils.Common;
import com.transam.utils.Result;

@Service
@Transactional(readOnly = true)
public class ResourcesService extends BaseService<ResourcesMapper, Resources>{
	
	public List<Resources> findResource(Resources resources,Long roleId){
		return mapper.findResource(resources,roleId);
	}
	
	public List<Resources> findBtn(Map<String,Object> map){
		return mapper.findBtn(map);
	}
	
	public List<Resources> findResByRoleIds(String roleIds){
		return mapper.findResByRoleIds(roleIds.split(","));
	}
	
	@Transactional(readOnly = false)
	public Result saveResources(Resources resources){
		if(resources.getId() != null){
			resources.setUpdateDate(new Date());
			resources.setLevel(Integer.parseInt(resources.getType()));
			mapper.update(resources);
		}else{
			resources.setCreateDate(new Date());
			resources.setUpdateDate(new Date());
			resources.setLevel(Integer.parseInt(resources.getType()));
			mapper.insert(resources);
		}
		return Result.ok();
	}
	
	@Transactional(readOnly = false)
	public Result addResRoleOfBatch(Long roleId,String resIds){
		mapper.deleteResRoleByRoleId(roleId);
		if(!StringUtils.isBlank(resIds)){
			List<Map<String,Object>> resRoleList = new ArrayList<Map<String,Object>>();
			String[] s = Common.trimComma(resIds).split(",");
			for (String rid : s) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("resId", rid);
				map.put("roleId", roleId);
				resRoleList.add(map);
			}
			mapper.addResRoleOfBatch(resRoleList);	
		}
		
		return Result.ok();
	}
}