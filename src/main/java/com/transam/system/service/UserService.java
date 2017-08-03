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
import com.transam.system.entity.User;
import com.transam.system.entity.UserRole;
import com.transam.system.mapper.UserMapper;
import com.transam.system.mapper.UserRoleMapper;
import com.transam.utils.IdGen;
import com.transam.utils.PasswordHelper;
import com.transam.utils.Result;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseService<UserMapper, User>{
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	/**
	 * 根据账号查询用户
	 * @param accountName
	 * @return
	 */
	public List<User> findByAccountName(String accountName){
		return mapper.findByAccountName(accountName);
	}
	
	/**
	 * @param user
	 * @return
	 */
	public List<User> findByUser(User user){
		return mapper.findByUser(user);
	}
	
	/**
	 * 根据用户id删除用户角色关系及用户
	 */
	@Transactional(readOnly = false)
	public Integer deleteOfBatch(String[] ids){
		userRoleMapper.deleteOfBatch(ids);
		return mapper.deleteOfBatch(ids);
	}
	
	/**
	 * 添加用户
	 * @param txtRoleSelect
	 * @param user
	 * @return
	 */
	@Transactional(readOnly=false)
	public Result addUser(String txtRoleSelect,User user){
		mapper.deleteByAccount(user.getAccountName());
		//若密码为空，默认为"123456"
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword("123456");
		}
		PasswordHelper passwordHelper = new PasswordHelper();
		user.setId(IdGen.getId());
		user.setDelFlag(0);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		passwordHelper.encryptPassword(user);
		mapper.insert(user);
		if (!StringUtils.isBlank(txtRoleSelect)) {
			String[] txt = txtRoleSelect.split(",");
			List<UserRole> ulis = new ArrayList<UserRole>();
			for (String roleId : txt) {
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(Long.parseLong(roleId));
				ulis.add(userRole);
			}
			userRoleMapper.insertOfBatch(ulis);
		}
		return Result.ok();
	}
	
	/**
	 * 编辑用户
	 * @param txtRoleSelect
	 * @param user
	 * @return
	 */
	@Transactional(readOnly=false)
	public Result editUser(String txtRoleSelect,User user){
		if(!StringUtils.isBlank(user.getPassword())){
			PasswordHelper passwordHelper = new PasswordHelper();
			passwordHelper.encryptPassword(user);
		}
		user.setUpdateDate(new Date());
		mapper.update(user);
		if (!StringUtils.isBlank(txtRoleSelect)) {
			mapper.deleteUserRoleByUserId(user.getId());
			String[] txt = txtRoleSelect.split(",");
			List<UserRole> ulis = new ArrayList<UserRole>();
			for (String roleId : txt) {
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(Long.parseLong(roleId));
				ulis.add(userRole);
			}
			userRoleMapper.insertOfBatch(ulis);
		}else{
			mapper.deleteUserRoleByUserId(user.getId());
		}
		return Result.ok();
	}
	
	/**
	 * 更具roleId查询所属用户
	 * @param roleId
	 * @return
	 */
	public List<User> getByRoleId(Long roleId){
		return mapper.getByRoleId(roleId);
	}
	
	/**
	 * 根据map查询用户
	 * @param map
	 * @return
	 */
	public List<User> getUserbyMap(Map<String,Object> map){
		return mapper.getUserbyMap(map);
	}
}