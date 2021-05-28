package com.oobiliuoo.parkingtong.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class Utils {

    public static final String IP_ADDRESS = "192.168.43.239";
    public static final int IP_PORT = 8777;

    /** DIVISION: 分割符，根据这个拆分字符串 */
    public static final String DIVISION = "::";

    /** REQUEST_LOGIN: 请求登录*/
    public static final String REQUEST_LOGIN = "LOGIN::";

    /** RESPOND_ACCESS_LOGIN： 准许登陆 */
    public static final String RESPOND_ACCESS_LOGIN = "ACCESS_LOGIN";




    public static void mLog1(String text){
        Log.i("mLog", text);
    }

    public static void mLog1(String TAG ,String text){
        Log.i(TAG, text);
    }

    public static void showToast(Context context, String text){

        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

    }

    public static void sendMessage(Handler handler, int what, Object obj){
        Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }

}
