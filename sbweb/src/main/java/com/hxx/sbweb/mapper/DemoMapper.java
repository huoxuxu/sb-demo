package com.hxx.sbweb.mapper;

import com.hxx.sbweb.domain.Demo;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-08 10:34:35
 **/
public interface DemoMapper {

    int batchUpdate(List<Demo> demos);
}
