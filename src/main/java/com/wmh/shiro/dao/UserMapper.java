package com.wmh.shiro.dao;

import com.wmh.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

@Mapper
public interface UserMapper {
	User findByUserName(String userName);
}
