package com.transam.system.entity;

import com.transam.persistence.BaseEntity;
public class Role extends BaseEntity<Role>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String status;
	private String roleKey;
	private String name;
	private String description;
	
	private User user;//根据用户ID查询角色列表
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
