package com.hxx.sbcommon.common.other.pageSeparate;

/**
 * 可传入页码和页大小的分页类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-15 12:32:13
 **/
@lombok.Data
public class GeneralPageable extends Pageable {
    /**
     * @param pageNum  页码，从1开始
     * @param pageSize 页大小
     */
    public GeneralPageable(int pageNum, int pageSize) {
        super((pageNum - 1) * pageSize, pageSize);
    }

}
