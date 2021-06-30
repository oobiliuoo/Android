package com.oobiliuoo.intelligentgarageapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


/**
 * @author biliu
 */
public class Utils {

    /**服务器IP地址*/
    public static final String IP_ADDRESS = "192.168.43.131";
    /**服务器开放的端口号*/
    public static final int IP_PORT = 8080;




    /**
     *  相当于 log.i("mLog1",text)
     *  text: 想要显示的文字
     * */
    public static void mLog1(String text){
        Log.i("mLog", text);
    }
    /**
     *  相当于 log.i(TAG,text)
     *  TAG: TAG
     *  text: 想要显示的文字
     * */
    public static void mLog1(String TAG ,String text){
        Log.i(TAG, text);
    }

    /**
     *  显示Toast
     * */
    public static void showToast(Context context, String text){

        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    /**
     *  通过Handler传递消息
     * */
    public static void sendMessage(Handler handler, int what, Object obj){
        Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }

    /**
     *  判断是否为纯数字
     *  str:想判断的字符串
     *  return: boolean
     * */
    public static boolean isNumeric(String str) {
        for(int i =0;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     *  从SharePreferences中读取当前登录帐号
     *  context: 当前活动上下文
     *  return: 当前帐号
     * */
    public static String readCurrentUser(Context context){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String tel = pref.getString("tel","");
        return tel;
    }

}
