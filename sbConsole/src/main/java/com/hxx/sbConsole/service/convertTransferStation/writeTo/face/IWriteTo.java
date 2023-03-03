package com.hxx.sbConsole.service.convertTransferStation.writeTo.face;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-02 17:31:39
 **/
public interface IWriteTo {
    /**
     * 写入
     *
     * @param title
     * @param data
     * @return
     */
    int write(List<String> title, List<Object[]> data);
}
