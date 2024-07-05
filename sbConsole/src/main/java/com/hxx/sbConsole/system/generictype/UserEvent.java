package com.hxx.sbConsole.system.generictype;

import com.hxx.sbConsole.model.User;

public class UserEvent extends BaseEvent<User> {

    public UserEvent(User data) {
        super(data);
    }

}
