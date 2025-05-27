package com.hxx.sbConsole.module.net.socket.model;

import lombok.Data;

import java.io.InputStream;

@Data
public class ReadyToSend {
    // 类型：0 字节流、1 数据流
    private byte type;
    // 待发送数据
    private byte[] data;
    // 待发送数据流
    private InputStream dataStream;

    public ReadyToSend(byte type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public ReadyToSend(byte type, InputStream dataStream) {
        this.type = type;
        this.dataStream = dataStream;
    }
}
