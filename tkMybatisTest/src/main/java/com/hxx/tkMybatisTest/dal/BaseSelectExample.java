package com.hxx.tkMybatisTest.dal;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-12-01 13:49:30
 **/
@Data
public class BaseSelectExample {
    /**
     * 排序
     */
    private String orderByCase;
    /**
     * 分组
     */
    private String groupByCase;

    /**
     * mysql分页时，limit 参数
     */
    private int limitStart;

    /**
     * 每页显示记录数
     */
    private int pageSize;
}
