package com.hxx.sbConsole.module.net.socket;

import com.hxx.sbConsole.module.net.socket.model.ReadyToSend;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpClient {
    private final String serverHost;
    private final int serverPort;

    private final String username;
    private final String password;

    private final BlockingQueue<ReadyToSend> sendQueue = new LinkedBlockingQueue<>();

    private Socket socket;
    private OutputStream socketOutputStream;
    private InputStream socketInputStream;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean isAuthenticated = new AtomicBoolean(false);

    private Thread senderThread;
    private Thread heartbeatThread;
    private Thread receiverThread;

    private static final int HEARTBEAT_INTERVAL = 5000; // 5秒心跳间隔

    public TcpClient(String serverHost, int serverPort, String username, String password) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;

        this.username = username;
        this.password = password;
    }

    public static void main(String[] args) {
        try {
            // 创建客户端并连接
            TcpClient client = new TcpClient("localhost", 7575, "testuser", "testpass");
            client.start();

            // 模拟发送业务数据
            for (int i = 0; i < 5; i++) {
                String message = "业务消息-" + i;
                client.enqueueData((byte) 0, message.getBytes());
                Thread.sleep(2000);
            }

            // 运行一段时间后停止
            Thread.sleep(30000);
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 启动客户端
    public void start() throws IOException {
        if (isRunning.get()) {
            return;
        }

        // 建立连接
        socket = new Socket(serverHost, serverPort);
        socketOutputStream = socket.getOutputStream();
        socketInputStream = socket.getInputStream();

        isRunning.set(true);

        // 启动发送线程
        senderThread = new Thread(this::sendLoop, "SenderThread");
        senderThread.start();

        // 启动接收线程
        receiverThread = new Thread(this::receiveLoop, "ReceiverThread");
        receiverThread.start();

        // 发送登录请求
        sendLoginRequest();
    }

    // 停止客户端
    public void stop() {
        isRunning.set(false);
        isAuthenticated.set(false);

        // 中断线程
        if (senderThread != null) senderThread.interrupt();
        if (heartbeatThread != null) heartbeatThread.interrupt();
        if (receiverThread != null) receiverThread.interrupt();

        // 关闭连接
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 发送登录请求
    private void sendLoginRequest() {
        try {
            // 构建登录数据包 (假设格式: LEN(4B) + TYPE(1B) + USERNAME + PASSWORD)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeByte(1); // 消息类型：登录
            dos.writeUTF(username);
            dos.writeUTF(password);

            byte[] loginData = baos.toByteArray();
            enqueueData((byte) 0, loginData);

            System.out.println("发送登录请求: " + username);
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    // 处理登录响应
    private void handleLoginResponse(byte[] data) {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            boolean success = dis.readBoolean();

            if (success) {
                System.out.println("登录成功");
                isAuthenticated.set(true);

                // 启动心跳线程
                startHeartbeat();
            } else {
                String errorMsg = dis.readUTF();
                System.out.println("登录失败: " + errorMsg);
                stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    // 启动心跳
    private void startHeartbeat() {
        if (heartbeatThread != null && heartbeatThread.isAlive()) {
            return;
        }

        heartbeatThread = new Thread(() -> {
            try {
                while (isRunning.get() && isAuthenticated.get()) {
                    // 发送心跳包 (假设格式: LEN(4B) + TYPE(1B) + DATA)
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);

                    dos.writeByte(2); // 消息类型：心跳
                    dos.writeLong(System.currentTimeMillis()); // 时间戳

                    byte[] heartbeatData = baos.toByteArray();
                    enqueueData((byte) 0, heartbeatData);

                    System.out.println("发送心跳包");
                    Thread.sleep(HEARTBEAT_INTERVAL);
                }
            } catch (InterruptedException e) {
                System.out.println("心跳线程被中断");
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }, "HeartbeatThread");

        heartbeatThread.start();
    }

    // 将数据放入发送队列
    public void enqueueData(byte type, byte[] data) {
        try {
            ReadyToSend rts = new ReadyToSend(type, data);

            sendQueue.put(rts);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 将数据流放入发送队列
    public void enqueueDataStream(byte type, InputStream dataStream) {
        try {
            ReadyToSend rts = new ReadyToSend(type, dataStream);

            sendQueue.put(rts);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 发送循环
    private void sendLoop() {
        try {
            while (isRunning.get()) {
                ReadyToSend rts = sendQueue.take();

                try {
                    if (rts.getType() == 0 && rts.getData() != null) {
                        // 发送字节流
                        socketOutputStream.write(rts.getData());
                        socketOutputStream.flush();
                    } else if (rts.getType() == 1 && rts.getDataStream() != null) {
                        // 发送数据流
                        copyStream(rts.getDataStream(), socketOutputStream);
                        socketOutputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 重新放入队列重试
                    sendQueue.put(rts);
                    throw e;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("发送线程被中断");
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    // 接收循环
    private void receiveLoop() {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while (isRunning.get() && (bytesRead = socketInputStream.read(buffer)) != -1) {
                byte[] receivedData = new byte[bytesRead];
                System.arraycopy(buffer, 0, receivedData, 0, bytesRead);

                // 解析消息类型 (假设前4字节为长度，第5字节为类型)
                if (bytesRead >= 5) {
                    int msgType = receivedData[4];

                    if (msgType == 1) { // 登录响应
                        handleLoginResponse(receivedData);
                    } else if (msgType == 2) { // 心跳响应
                        System.out.println("收到心跳响应");
                    } else {
                        // 处理其他类型的消息
                        handleMessage(msgType, receivedData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    // 处理其他消息
    private void handleMessage(int msgType, byte[] data) {
        System.out.println("收到消息类型: " + msgType + ", 长度: " + data.length);
        // 根据消息类型进行相应处理
    }

    // 复制流
    private void copyStream(InputStream input, OutputStream output) throws IOException {
        // 注意：这里需要根据实际的Stream类型实现具体的复制逻辑
        // 简化示例，实际应根据Stream类型处理
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
