package com.example.administrator.thinker_soft.android_cbjactivity.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.ChaoBiaoBenTongjiActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.MeterSelectActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.LoginActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.ShowListviewActivity;
import com.example.administrator.thinker_soft.android_cbjactivity.WeiChaoBaoActivity;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class MeterHomePageFragment extends Fragment {
    private View view;
    private static final int CODE = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String filepath;
    private String DBName;
    private List<UsersInfo> list;
    private int signal = 0;// ��ʾ��־
    private int con_signal = 0;// ������ʾ��־
    private PopupWindow popupWindow;
    private int POPWINDOWWIDTH;
    private int POPWINDOWHEIGHT;
    private LinearLayout first_layout,second_layout,third_layout,fourth_layout,fifth_layout,sixth_layout;
    private MyActivityManager mam ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chaobiao, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    //绑定控件
    private void bindView() {
        first_layout  = (LinearLayout) view.findViewById(R.id.first_layout);
        second_layout = (LinearLayout) view.findViewById(R.id.second_layout);
        third_layout  = (LinearLayout) view.findViewById(R.id.third_layout);
        fourth_layout = (LinearLayout) view.findViewById(R.id.fourth_layout);
        fifth_layout  = (LinearLayout) view.findViewById(R.id.fifth_layout);
        sixth_layout = (LinearLayout) view.findViewById(R.id.sixth_layout);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(getActivity());
        filepath = Environment.getDataDirectory().getPath() + "/data/"
                + "com.example.android_cbjactivity" + "/databases/";
        sharedPreferences = getActivity().getSharedPreferences(
                "IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
    }

    //点击事件
    public void setViewClickListener() {
        first_layout.setOnClickListener(clickListener);
        second_layout.setOnClickListener(clickListener);
        third_layout.setOnClickListener(clickListener);
        fourth_layout.setOnClickListener(clickListener);
        fifth_layout.setOnClickListener(clickListener);
        sixth_layout.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.first_layout:
                    signal = 1;
                    editor = sharedPreferences.edit();
                    editor.putInt("signal", signal);
                    editor.apply();
                    DBName = sharedPreferences.getString("dbName", "");
                    Intent intent = new Intent(getActivity(), ShowListviewActivity.class);
                    startActivityForResult(intent, CODE);
                    break;
                case R.id.second_layout:
                    signal = 2;
                    con_signal = 2;
                    editor = sharedPreferences.edit();
                    editor.putInt("signal", signal);
                    editor.putInt("con_signal", con_signal);
                    editor.apply();
                    DBName = sharedPreferences.getString("dbName", "");
                    System.out.println("DBName:" + DBName);
                    Intent intent1 = new Intent(getActivity(), MeterSelectActivity.class);
                    intent1.putExtra("DBName", DBName);
                    startActivityForResult(intent1, CODE);
                    break;
                case R.id.third_layout:
                    signal = 3;
                    con_signal = 3;
                    editor = sharedPreferences.edit();
                    editor.putInt("signal", signal);
                    editor.putInt("con_signal", con_signal);
                    editor.apply();
                    // SharedPreferences sharedPreferences =
                    // getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
                    DBName = sharedPreferences.getString("dbName", "");
                    Intent intent2 = new Intent(getActivity(), ShowListviewActivity.class);
                    startActivityForResult(intent2, CODE);
                    break;
                case R.id.fourth_layout:
                    signal = 4;
                    con_signal = 4;
                    editor.putInt("signal", signal);
                    editor.putInt("con_signal", con_signal);
                    editor.commit();
                    DBName = sharedPreferences.getString("dbName", "");
                    Intent intent3 = new Intent(getActivity(), WeiChaoBaoActivity.class);
                    intent3.putExtra("DBName", DBName);
                    startActivityForResult(intent3, CODE);
                    break;
                case R.id.fifth_layout:
                    Intent intent4 = new Intent(getActivity(), ChaoBiaoBenTongjiActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.sixth_layout:
                    signal = 6;
                    editor.putInt("signal", signal);
                    editor.apply();
                    Intent intent5 = new Intent(getActivity(), MeterSelectActivity.class);
                    startActivity(intent5);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE == requestCode) {
            System.out.println("����");
        }
        if (requestCode == 1 && resultCode == 1) {
            getActivity().finish();
            System.exit(0);
        }
        if (requestCode == 1 && resultCode == 2) {
            Intent intent = new Intent(getActivity(),
                    LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
