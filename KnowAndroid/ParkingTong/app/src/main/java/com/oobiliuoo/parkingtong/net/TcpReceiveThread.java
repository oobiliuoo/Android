package com.oobiliuoo.parkingtong.net;

import android.util.Log;

import com.oobiliuoo.parkingtong.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpReceiveThread extends Thread{
    //Socket msg = null;//定义socket
    private OutputStream out_ip=null;//定义输出流（ip）
    OutputStream outputStream=null;
    private InputStream inputStream;
    private StringBuffer stringBuffer;
    private String ip;
    private int port;
    private String string;
    private boolean isRun = true;

    public TcpReceiveThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    @Override
    public void run(){
        Socket so = null;
        try {
            so = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": Hello");
       /*
        try {
            inputStream = so.getInputStream();
            out_ip = so.getOutputStream();
            Log.v("AndroidChat","开始连接服务器："+ip+"/"+port);
            sleep(1000);
        }
        catch (IOException | InterruptedException e) {
            Log.v("AndroidChat","连接服务器失败"+e.getMessage());
            e.printStackTrace();
            return;
        }

        */
        Log.v("AndroidChat","成功连接上服务器");
            /*
            下面是接收模块，你可以尝试探究如何将这个模块放在接收线程中。
            */

        /*
        try {
            inputStream = so.getInputStream();
            final byte[] buffer = new byte[1024];
            final int len = inputStream.read(buffer);
            System.out.println(new String(buffer,0,len));
            Log.v("AndroidChat","接收成功："+new String(buffer,0,len));
            string = new String(buffer,0,len);
            //上下两行会和MainActivity关联，是回调在显示屏的关键步骤。
            System.out.println(new String(buffer,0,len));
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }
}
