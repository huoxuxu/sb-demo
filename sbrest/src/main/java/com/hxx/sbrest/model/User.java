package com.hxx.sbrest.model;

import java.io.Serializable;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-18 15:27:36
 **/
@lombok.Data
public class User implements Serializable {
    private static final long serialVersionUID = -1709630087115308796L;

    private long id;
    private String code;
    private String name;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }
}
