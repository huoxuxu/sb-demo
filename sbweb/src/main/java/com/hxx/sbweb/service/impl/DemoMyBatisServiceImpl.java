package com.hxx.sbweb.service.impl;

import com.hxx.sbweb.domain.Demo;
import com.hxx.sbweb.mapper.DemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-13 10:04:30
 **/
@Slf4j
@Service
public class DemoMyBatisServiceImpl {
    @Autowired
    private DemoMapper demoMapper;


    public String demo() {
        List<Demo> ls = new ArrayList<>();
        {
            {
                Demo m = new Demo();
                {
                    m.setId(1L);
                    m.setName("哈哈");
                }
                ls.add(m);
            }
            {
                Demo m = new Demo();
                {
                    m.setId(2L);
                    m.setName("呵呵");
                }
                ls.add(m);
            }
        }
        int ret = demoMapper.batchUpdate(ls);
        return ret + "";
    }

}
