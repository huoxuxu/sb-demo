package com.hxx.sbConsole.jwt;

import com.hxx.sbcommon.common.basic.OftenUtil;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-16 13:47:10
 **/
public class GenerateRefreshTokenImpl implements IGenerateRefreshToken {
    @Override
    public String GenerateRefreshTokenStr() {
        return OftenUtil.getUUID();
    }
}
