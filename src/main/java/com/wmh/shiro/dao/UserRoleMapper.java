package com.wmh.shiro.dao;

import com.wmh.shiro.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
	
	List<Role> findByUserName(String userName);
}
