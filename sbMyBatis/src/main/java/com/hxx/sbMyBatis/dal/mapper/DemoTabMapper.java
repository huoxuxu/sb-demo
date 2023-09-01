package com.hxx.sbMyBatis.dal.mapper;

import com.hxx.sbMyBatis.dal.entity.DemoTab;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public interface DemoTabMapper {
    /**
     * 查询全部
     *
     * @return
     */
    List<DemoTab> selectAll();

    List<Map<String, String>> selectMap();

}