package com.hxx.sbrest.service.impl;

import com.hxx.sbrest.common.config.MonsterConfig;
import com.hxx.sbrest.common.config.MyConfig;
import com.hxx.sbrest.service.ValueAttrService;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-09 9:22:58
 **/
@Service
public class ValueAttrServiceImpl implements ValueAttrService {
    // 自定义配置 my.properties
    @Autowired
    private MyConfig myConfig;
    // 自定义配置 my.properties
    @Autowired //自动创建对象
    private MonsterConfig monster;


    @Override
    public String GetValueProp() {
        String lever = myConfig.getLever();
        int atk = myConfig.getAtk();
        String desc = myConfig.getDesc();

        String name = monster.getName();
        int atk1 = monster.getAtk();

        FormattingTuple tuple = MessageFormatter.arrayFormat("{} {} {}", new Object[]{lever, atk, desc});
        FormattingTuple tuple2 = MessageFormatter.arrayFormat("{} {}", new Object[]{name, atk1});
        return tuple.getMessage() + " <br/> " + tuple2.getMessage();
    }
}
