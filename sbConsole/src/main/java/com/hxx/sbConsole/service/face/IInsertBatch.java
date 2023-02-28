package com.hxx.sbConsole.service.face;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-28 17:26:13
 **/
public interface IInsertBatch {

    /**
     * 处理
     *
     * @param titles
     * @param data
     * @return
     */
    List<String> proc(List<String> titles, List<Object[]> data);

}
