package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
                    intent = new Intent(getActivity(), MeterBookQueryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.by_user_name:
                    intent = new Intent(getActivity(), MeterUserNameQueryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.by_user_num:
                    intent = new Intent(getActivity(), MeterUserIDQueryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.by_user_met:
                    intent = new Intent(getActivity(), MeterNumberQueryActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
