package com.hxx.sbcommon.common.other;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-26 12:40:15
 **/
public class RamUsageUtil {

    public static void demo(){
        Map<String,String> map =new HashMap<>();
//        System.out.println("init"+RamUsageEstimator.sizeof(map));
        for (int i=0;i<100;i++){
            RandomStringUtils.randomAlphanumeric(100);
            map.put(RandomStringUtils.randomAlphanumeric(10),RandomStringUtils.randomAlphanumeric(10));
        }
//        System.out.println("init"+RamUsageEstimator.sizeof(map));
//        System.out.println("init"+RamUsageEstimator.humanSizeof(map));

    }

}
