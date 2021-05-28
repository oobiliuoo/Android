package com.oobiliuoo.parkingtong.net;

import android.os.Handler;

import com.oobiliuoo.parkingtong.utils.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * 连接服务器
 * @author biliu
 */
public class TcpClient {

    private Socket mSocket;
    private String mIpAddress;
    private int mClientPort;
    private OutputStream mOutStream;
    private InputStream mInStream;
    private Handler mHandler;
    private String sendMsg;

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public TcpClient(Handler mHandler, String mIpAddress, int mClientPort) {
        this.mHandler = mHandler;
        this.mIpAddress = mIpAddress;
        this.mClientPort = mClientPort;
    }


    private boolean connect() {

        try {
            //指定ip地址和端口号
            // 1.创建一个客户端对象Socket,构造方法绑定服务器的IP地址和端口号
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(mIpAddress,mClientPort),5*1000);
            if (mSocket != null) {
                //获取输出流、输入流
                mOutStream = mSocket.getOutputStream();
                mInStream = mSocket.getInputStream();
                Utils.mLog1("TcpClient", "连接成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.mLog1("TcpClient", "连接失败");
            return false;
        }
        return true;
    }

    private void writeMsg() {
        if (sendMsg.length() == 0 || mOutStream == null) {
            return;
        }
        try {   //发送
            mOutStream.write(sendMsg.getBytes());
            mOutStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readMsg() {
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            len = mInStream.read(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes, 0, len);
    }

    public void closeConnection() {
        try {
            if (mOutStream != null) {
                mOutStream.close(); //关闭输出流
                mOutStream = null;
            }
            if (mInStream != null) {
                mInStream.close(); //关闭输入流
                mInStream = null;
            }
            if (mSocket != null) {
                mSocket.close();  //关闭socket
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void send() {

        TcpSendThread mTcpSendThread = new TcpSendThread();
        mTcpSendThread.start();

    }

    class TcpReceiveThread extends Thread {
        @Override
        public void run() {
            if (connect()) {
                Utils.sendMessage(mHandler, 1, "CC");
            } else {
                Utils.sendMessage(mHandler, 2, "CF");
            }

            String msg = readMsg();
            Utils.sendMessage(mHandler, 3, msg);

            closeConnection();
        }
    }

    class TcpSendThread extends Thread {
        @Override
        public void run() {
            if (connect()) {
                Utils.sendMessage(mHandler, 1, "CC");
            } else {
                Utils.sendMessage(mHandler, 2, "CF");
            }
            writeMsg();

            String msg = readMsg();
            Utils.sendMessage(mHandler, 3, msg);

            closeConnection();

        }
    }

}
