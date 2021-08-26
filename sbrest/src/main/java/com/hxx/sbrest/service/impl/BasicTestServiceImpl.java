package com.hxx.sbrest.service.impl;

import com.hxx.sbrest.service.BasicTestService;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 16:33:07
 **/
@Service
public class BasicTestServiceImpl implements BasicTestService {
    @Override
    public String switchTest(String str) {
        // 整形变量（表达式）或字符型变量（表达式）
        // byte,shot,char都可以隐含的转换为int
        switch (str) {
            case "1":
                str += "999";
                break;
            case "11":
                str += "777";
                break;
            default:
                break;
        }

        return str;
    }


}
