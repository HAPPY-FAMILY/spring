package com.transam.system.entity;

import com.transam.persistence.BaseEntity;

public class ResRole extends BaseEntity<ResRole> {

	private static final long serialVersionUID = 1L;

    /** res_id */
    private Long resId;

    /** role_id */
    private Long roleId;



    /**
     * 获取 res_id 的值
     * @return Long
     */
    public Long getResId() {
        return resId;
    }
    
    /**
     * 设置res_id 的值
     * @param Long resId
     */
    public void setResId(Long resId) {
        this.resId = resId;
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
        sb.append("; resId=" + (resId == null ? "null" : resId.toString()));
        sb.append("; roleId=" + (roleId == null ? "null" : roleId.toString()));

        return sb.toString();
    }
}