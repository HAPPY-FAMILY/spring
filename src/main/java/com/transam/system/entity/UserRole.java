package com.transam.system.entity;

import com.transam.persistence.BaseEntity;



public class UserRole extends BaseEntity<UserRole> {

	private static final long serialVersionUID = 1L;

    /** user_id */
    private Long userId;

    /** role_id */
    private Long roleId;



    /**
     * 获取 user_id 的值
     * @return Long
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置user_id 的值
     * @param Long userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取 role_id 的值
     * @return Long
     */
    public Long getRoleId() {
        return roleId;
    }
    
    /**
     * 设置role_id 的值
     * @param Long roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName());
        sb.append("; userId=" + (userId == null ? "null" : userId.toString()));
        sb.append("; roleId=" + (roleId == null ? "null" : roleId.toString()));

        return sb.toString();
    }
}