package com.hxx.sbConsole.service.impl;

import com.hxx.sbConsole.commons.annotation.LogAction;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:17:13
 **/
@Service
public class DemoAnnotationService {
    @LogAction(name = "add")
    public void add() {
    }
}
