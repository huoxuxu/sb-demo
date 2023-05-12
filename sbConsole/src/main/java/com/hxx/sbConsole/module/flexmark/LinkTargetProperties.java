package com.hxx.sbConsole.module.flexmark;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-11 10:27:34
 **/
@Configuration
@ConfigurationProperties(prefix = "markdown.link")
public class LinkTargetProperties{
    /**
     * 排除添加 target 属性的链接
     */
    private List<String> excludes;
    /**
     * target 属性的值
     */
    private String target = "_target";

    /**
     * 相对路径排除
     */
    private boolean relativeExclude = true;

    // get 和 set 方法省略
}
