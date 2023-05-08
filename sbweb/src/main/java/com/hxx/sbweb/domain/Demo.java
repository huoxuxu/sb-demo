package com.hxx.sbweb.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-08 10:34:53
 **/
@lombok.Data
public class Demo implements Serializable {
    private static final long serialVersionUID = 7323579176823601958L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 成绩
     */
    private BigDecimal score;

    /**
     *
     */
    private LocalDateTime birthday;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

}
