package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterBookQueryActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterNumberQueryActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserIDQueryActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserNameQueryActivity;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class CustomQueryFragment extends Fragment {
    private View view;
    private LinearLayout by_met_book, by_user_name, by_user_num, by_user_met;
    private SharedPreferences sharedPreferences_login,sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meter_query, null);

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
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        by_met_book.setOnClickListener(onClickListener);
        by_user_name.setOnClickListener(onClickListener);
        by_user_num.setOnClickListener(onClickListener);
        by_user_met.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.by_met_book:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterBookQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.by_user_name:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterUserNameQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.by_user_num:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterUserIDQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.by_user_met:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterNumberQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
