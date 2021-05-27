package com.oobiliuoo.parkingtong.net;

import com.oobiliuoo.parkingtong.utils.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.Socket;

public class TcpClient {

    private Socket mSocket;
    private String mIpAddress;
    private int mClientPort;
    private OutputStream mOutStream ;
    private InputStream mInStream ;

    public TcpClient(String mIpAddress, int mClientPort) {
        this.mIpAddress = mIpAddress;
        this.mClientPort = mClientPort;
    }


    public boolean connect (){

        try {

            Utils.mLog1("TcpClient","connect ");
            //指定ip地址和端口号
            // 1.创建一个客户端对象Socket,构造方法绑定服务器的IP地址和端口号
            InetAddress address = InetAddress.getByName(mIpAddress);
            mSocket = new Socket( address,mClientPort);
            if(mSocket != null){
                //获取输出流、输入流
                mOutStream = mSocket.getOutputStream();
                mInStream = mSocket.getInputStream();
                Utils.mLog1("TcpClient","connect 2 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Utils.mLog1("TcpClient","connect success");
        return true;
    }

    public void writeMsg(String msg){
        if(msg.length() == 0 || mOutStream == null)
            return;
        try {   //发送
            mOutStream.write(msg.getBytes());
            mOutStream.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readMsg(){
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            len = mInStream.read(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String(bytes,0,len);
    }

    public void closeConnection(){
        try {
            if (mOutStream != null) {
                mOutStream.close(); //关闭输出流
                mOutStream = null;
            }
            if (mInStream != null) {
                mInStream.close(); //关闭输入流
                mInStream = null;
            }
            if(mSocket != null){
                mSocket.close();  //关闭socket
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
