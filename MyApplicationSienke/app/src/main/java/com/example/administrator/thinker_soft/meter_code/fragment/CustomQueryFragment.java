package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private CardView bookCardView, userNameCardview, userIdCardview, meterNumberCardview;
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
        bookCardView = (CardView) view.findViewById(R.id.book_cardview);
        userNameCardview = (CardView) view.findViewById(R.id.user_name_cardview);
        userIdCardview = (CardView) view.findViewById(R.id.user_id_cardview);
        meterNumberCardview = (CardView) view.findViewById(R.id.meter_number_cardview);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        bookCardView.setOnClickListener(onClickListener);
        userNameCardview.setOnClickListener(onClickListener);
        userIdCardview.setOnClickListener(onClickListener);
        meterNumberCardview.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.book_cardview:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterBookQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.user_name_cardview:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterUserNameQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.user_id_cardview:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        intent = new Intent(getActivity(), MeterUserIDQueryActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.meter_number_cardview:
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
