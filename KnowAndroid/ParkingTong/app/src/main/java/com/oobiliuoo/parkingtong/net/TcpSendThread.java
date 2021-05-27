package com.oobiliuoo.parkingtong.net;

import com.oobiliuoo.parkingtong.utils.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpSendThread extends Thread {
    private String send;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String ip;
    private int port;


    public TcpSendThread(String msg, int port, String ip) {
        this.send = msg;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            send = "客户端发来：" + send;
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            outputStream.write((send + "\n").getBytes("utf-8"));
            outputStream.flush();
            Utils.mLog1("TcpSendThread","发送成功"+send);
        } catch (Exception e) {
            Utils.mLog1("TcpSendThread","发送失败"+send+ "error" + e.getMessage());
            e.printStackTrace();
        }
    }
}