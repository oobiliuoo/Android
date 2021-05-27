package com.oobiliuoo.parkingtong.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    public static void mLog1(String text){
        Log.i("mLog", text);
    }

    public static void mLog1(String TAG ,String text){
        Log.i(TAG, text);
    }

    public static void showToast(Context context, String text){

        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

    }

}
