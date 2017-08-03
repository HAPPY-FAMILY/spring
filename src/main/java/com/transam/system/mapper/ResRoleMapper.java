package com.transam.system.mapper;

import com.transam.persistence.BaseMapper;
import com.transam.system.entity.ResRole;

/**
 * 此类继承了BaseMapper类，已经拥有基本的增删改成操作
 * <其他请自行扩展>
 * @time 2016-09-29 22:31
 * @CodeFactoryGenerated
 */
public interface ResRoleMapper extends BaseMapper<ResRole> {
    
    public Integer deleteOfBatchByRoleId(String[] ids);
    
}