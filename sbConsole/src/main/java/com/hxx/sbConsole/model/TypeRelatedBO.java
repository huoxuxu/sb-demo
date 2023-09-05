package com.hxx.sbConsole.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-05 9:18:23
 **/
@Data
public class TypeRelatedBO {

    private String name;
    private Class type;

    private String relatedName;
    private Class relatedType;

    public TypeRelatedBO() {
    }

    public TypeRelatedBO(String name, String relatedName, Class relatedType) {
        this.name = name;
        this.relatedName = relatedName;
        this.relatedType = relatedType;
    }
}
