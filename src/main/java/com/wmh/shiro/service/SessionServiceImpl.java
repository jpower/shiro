package com.wmh.shiro.service;

import com.wmh.shiro.pojo.User;
import com.wmh.shiro.pojo.UserOnline;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 周大侠
 * 2019-03-20 11:06
 */
@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public List<UserOnline> list() {
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        List<UserOnline> res = new ArrayList<>();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();

            User user = new User();
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                userOnline.setUsername(user.getUsername());
                userOnline.setUserId(user.getId().toString());
            }
            userOnline.setHost(session.getHost());
            userOnline.setId((String) session.getId());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            userOnline.setStartTimestamp(session.getStartTimestamp());
            if(session.getTimeout() == 0) {
                userOnline.setStatus("离线");
            }else {
                userOnline.setStatus("在线");
            }
            userOnline.setTimeout(session.getTimeout());
            res.add(userOnline);
        }
        return res;
    }

    @Override
    public boolean logout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        return true;
    }
}
