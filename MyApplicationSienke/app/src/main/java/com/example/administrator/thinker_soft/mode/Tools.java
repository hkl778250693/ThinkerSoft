package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Tools {

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //判断网络是否连接
    public static boolean NetIsAvilable(Context context){
        //获得网络管理
        ConnectivityManager cManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获得网络详情
        NetworkInfo networkInfo=cManager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isAvailable()){
            return false;
        }
        return true;
    }
}
