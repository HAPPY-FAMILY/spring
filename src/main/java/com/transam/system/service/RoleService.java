package com.transam.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transam.persistence.BaseService;
import com.transam.system.entity.ResRole;
import com.transam.system.entity.Role;
import com.transam.system.entity.UserRole;
import com.transam.system.mapper.ResRoleMapper;
import com.transam.system.mapper.RoleMapper;
import com.transam.system.mapper.UserRoleMapper;
import com.transam.utils.Common;
import com.transam.utils.IdGen;
import com.transam.utils.Result;

@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService<RoleMapper, Role>{
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private ResRoleMapper resRoleMapper;
	
	public List<Role> findRoleByUserId(Long userId){
		return mapper.findRoleByUserId(userId);
	}
	
	public List<UserRole> findUserIdByRoleId(Long roleId){
		UserRole userRole = new UserRole();
		userRole.setRoleId(roleId);
		return userRoleMapper.findList(userRole);
	}
	
	public List<Role> findRoleByMap(Map<String,Object> map){
		return mapper.findRoleByMap(map);
	}
	
	public List<Role> findByRole(Role role){
		return mapper.findByRole(role);
	}
	
	
	@Transactional(readOnly=false)
	public Result saveRole(Role role,String resId, String txtUserSelect){
		if(role.getId() != null){
			role.setUpdateDate(new Date());
			mapper.update(role);
			if(!StringUtils.isBlank(txtUserSelect)){
				userRoleMapper.deleteOfBatch(txtUserSelect.split(","));
				pession(role.getId(),txtUserSelect);
			}
		}else{
			role.setId(IdGen.getId());
			role.setCreateDate(new Date());
			role.setUpdateDate(new Date());
			mapper.insert(role);
			pession(role.getId(),txtUserSelect);
		}
		if(!StringUtils.isBlank(resId)){
			String[] s = Common.trimComma(resId).split(",");
			List<ResRole> resRoleList = new ArrayList<ResRole>();
			for (String rid : s) {
				ResRole resRole = new ResRole();
				resRole.setRoleId(role.getId());
				resRoleMapper.delete(resRole);
				resRole.setResId(Long.parseLong(rid));
				resRoleList.add(resRole);
			}
			resRoleMapper.insertOfBatch(resRoleList);
		}
		return Result.ok();
	}
	
	/**
	 * 批量给用户添加角色
	 * @param roleId
	 * @param resId
	 * @param txtUserSelect
	 */
	public void pession(Long roleId,String txtUserSelect){	
		String[] txt = null;
		if (!StringUtils.isBlank(txtUserSelect)) {
			txt = txtUserSelect.split(",");
			List<UserRole> ulis = new ArrayList<UserRole>();
			for (String userId : txt) {
				UserRole userRole = new UserRole();
				List<UserRole> userRoleList = userRoleMapper.getByUserId(Long.parseLong(userId));
				if(userRoleList != null && userRoleList.size() > 0){
					for(UserRole ur : userRoleList){
						if(ur.getRoleId().longValue() == roleId.longValue()){
							continue;
						}else{
							userRole.setRoleId(roleId);
							userRole.setUserId(Long.parseLong(userId));
						}
					}
				}else{
					userRole.setRoleId(roleId);
					userRole.setUserId(Long.parseLong(userId));
				}
				ulis.add(userRole);
			}
			userRoleMapper.insertOfBatch(ulis);
		}
	}
	
	/**
	 * 批量删除角色与resRole
	 */
	@Transactional(readOnly=false)
	public Integer deleteOfBatch(String[] ids){
		resRoleMapper.deleteOfBatchByRoleId(ids);
		return mapper.deleteOfBatch(ids);
	}
}