package com.hxx.sbcommon.common.other.pageSeparate;

/**
 * 分页
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-15 11:28:54
 **/
@lombok.Data
public class Pageable {

    private int skip;
    private int take;

    public Pageable() {
    }

    public Pageable(int skip, int take) {
        this.skip = skip;
        this.take = take;
    }

}
