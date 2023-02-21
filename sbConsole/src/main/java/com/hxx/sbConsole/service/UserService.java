package com.hxx.sbConsole.service;

import com.hxx.sbConsole.commons.annotation.LogAction;
import com.hxx.sbConsole.model.User;


/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 9:29:12
 **/
public interface UserService {
    @LogAction(name = "selectById")
    User selectById(int id);

}
