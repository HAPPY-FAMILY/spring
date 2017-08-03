package com.transam.system.mapper;

import java.util.List;

import com.transam.persistence.BaseMapper;
import com.transam.system.entity.UserRole;


/**
 * 此类继承了BaseMapper类，已经拥有基本的增删改成操作
 * <其他请自行扩展>
 * @time 2016-09-29 22:31
 * @CodeFactoryGenerated
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    public List<UserRole> getByUserId(Long userIdd);
    //自行扩展
    
}