package com.wmh.shiro.service;

import com.wmh.shiro.pojo.UserOnline;

import java.util.List;

/**
 * Created by 周大侠
 * 2019-03-20 11:04
 */
public interface SessionService {
    List<UserOnline> list();
    boolean logout(String sessionId);
}
