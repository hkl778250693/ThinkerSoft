package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    /**
     * 隐藏软键盘
     * @param context
     * @param editText
     */
    public static void hideSoftInput(Context context, EditText editText){
        InputMethodManager imm =(InputMethodManager)context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hidenOrShowInputMethod(View view, Context context) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 进行取反
        inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 显示输入法
    public static void showInputMethod(View view,Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //同时再使用该方法之前，view需要获得焦点，可以通过requestFocus()方法来设定。
        view.requestFocus();
        inputMethodManager.showSoftInput(view, inputMethodManager.SHOW_FORCED);
    }
    //隐藏输入法
    public static void hidenInputMethod(View view,Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //判断输入法是否已经打开
    public static boolean isInputMethodOpened(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive();
    }
}
