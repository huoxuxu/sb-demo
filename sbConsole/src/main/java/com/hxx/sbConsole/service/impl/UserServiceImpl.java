package com.hxx.sbConsole.service.impl;

import com.hxx.sbConsole.commons.annotation.LogAction;
import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:15:15
 **/
@Service
public class UserServiceImpl implements UserService {

    @LogAction(name = "selectById")
    @Override
    public User selectById(int id) {
        User u = new User();
        {
            u.setId(id);
            u.setName("name" + id);
        }
        return u;
    }
}
