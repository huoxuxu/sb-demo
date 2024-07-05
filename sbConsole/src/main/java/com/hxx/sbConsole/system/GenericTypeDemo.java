package com.hxx.sbConsole.system;

import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.system.generictype.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.ResolvableType;

@Slf4j
public class GenericTypeDemo {

    public static void main(String[] args) {
        try {
            UserEvent userEvent = new UserEvent(new User());
            ResolvableType resolvableType = userEvent.getResolvableType();
            System.out.println("UserEvent Generic Type：" + resolvableType);

        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }
}
