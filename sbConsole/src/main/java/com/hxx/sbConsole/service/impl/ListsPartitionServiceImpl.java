package com.hxx.sbConsole.service.impl;

import com.google.common.collect.Lists;
import com.hxx.sbConsole.model.DemoCls;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-09 18:03:24
 **/
public class ListsPartitionServiceImpl {

    public static void main(String[] args) {

        List<DemoCls> demos = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            DemoCls demo = new DemoCls();
            {
                demo.setId(i);
                demo.setCode("codecodecodecodecodecodecode" + i);
                demo.setName("namenamenamenamenamenamename" + i);
            }
            demos.add(demo);
        }
        System.out.println("size:" + demos.size());
        List<List<DemoCls>> lists = Lists.partition(demos, 100);
        System.out.println("size2:" + lists.size());

//        {
//            List<DemoCls> demos = new ArrayList<>();
//            for (int i = 0; i < 1000000; i++) {
//                DemoCls demo = new DemoCls();
//                {
//                    demo.setId(i);
//                    demo.setCode("codecodecodecodecodecodecode" + i);
//                    demo.setName("namenamenamenamenamenamename" + i);
//                }
//                demos.add(demo);
//            }
//            System.out.println("size:" + demos.size());
//        }

        System.out.println("ok1");
        Scanner input = new Scanner(System.in);
        input.next();
        System.out.println("ok");
    }
}
