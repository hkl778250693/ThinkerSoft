package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.BianHaoCXActivity;
import com.example.administrator.thinker_soft.meter_code.BiaoHaoCXActivity;
import com.example.administrator.thinker_soft.meter_code.CbbCXAction;
import com.example.administrator.thinker_soft.meter_code.MeterUserListviewActivity;
import com.example.administrator.thinker_soft.meter_code.YongHuMingCX;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class CustomQueryFragment extends Fragment {
    private View view;
    private final String filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String DBName;
    private LinearLayout by_met_book, by_user_name, by_user_num, by_user_met, by_user_minus;
    private MyActivityManager mam;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    by_user_minus.setEnabled(true);
                    break;
                case 1:
                    Toast.makeText(getActivity(), "��ѡ�񳭱�", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cx, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    //绑定控件
    private void bindView() {
        by_met_book = (LinearLayout) view.findViewById(R.id.by_met_book);
        by_user_name = (LinearLayout) view.findViewById(R.id.by_user_name);
        by_user_num = (LinearLayout) view.findViewById(R.id.by_user_num);
        by_user_met = (LinearLayout) view.findViewById(R.id.by_user_met);
        by_user_minus = (LinearLayout) view.findViewById(R.id.by_user_minus);
    }

    //初始化设置
    private void defaultSetting() {
        mam = MyActivityManager.getInstance();
        mam.pushOneActivity(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
    }

    //点击事件
    public void setViewClickListener() {
        by_met_book.setOnClickListener(onClickListener);
        by_user_name.setOnClickListener(onClickListener);
        by_user_num.setOnClickListener(onClickListener);
        by_user_met.setOnClickListener(onClickListener);
        by_user_minus.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.by_met_book:
                    Intent intent1 = new Intent(getActivity(), CbbCXAction.class);
                    startActivity(intent1);
                    break;
                case R.id.by_user_name:
                    Intent intent2 = new Intent(getActivity(), YongHuMingCX.class);
                    startActivity(intent2);
                    break;
                case R.id.by_user_num:
                    Intent intent3 = new Intent(getActivity(), BianHaoCXActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.by_user_met:
                    Intent intent4 = new Intent(getActivity(), BiaoHaoCXActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.by_user_minus:
                    by_user_minus.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            int signal = 10;
                            int con_signal = 10;
                            editor.putInt("signal", signal);
                            editor.putInt("con_signal", con_signal);
                            editor.commit();
                            DBName = sharedPreferences.getString("dbName", "");
                            if (DBName != null && !"".equals(DBName)) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), MeterUserListviewActivity.class);
                                startActivity(intent);
                            } else {
                                handler.sendEmptyMessage(1);
                                return;
                            }
                            handler.sendEmptyMessage(0);
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };
}
