package com.hxx.sbweb.mapper;

import com.hxx.sbweb.domain.Demo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-08 10:34:35
 **/
public interface DemoMapper {

    List<Demo> selectDemo(Demo demo);

    int batchUpdate(List<Demo> demos);

    List<Demo> selectAll();

    Demo selectByCode(@Param("code") String code);
}
