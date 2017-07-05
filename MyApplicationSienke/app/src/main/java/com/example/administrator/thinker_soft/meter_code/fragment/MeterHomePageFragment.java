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
import com.example.administrator.thinker_soft.meter_code.activity.MeterSelectActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterStatisticsActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserListviewActivity;
import com.example.administrator.thinker_soft.meter_code.WeiChaoBaoActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserContinueActivity;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class MeterHomePageFragment extends Fragment {
    private View view;
    private LinearLayout first_layout,second_layout,third_layout,fourth_layout,fifth_layout,sixth_layout;

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
            Intent intent;
            switch (v.getId()){
                case R.id.first_layout:
                    intent = new Intent(getActivity(), MeterUserContinueActivity.class);
                    startActivity(intent);
                    break;
                case R.id.second_layout:
                    intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.third_layout:
                    intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.fourth_layout:
                    intent = new Intent(getActivity(), WeiChaoBaoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.fifth_layout:
                    intent = new Intent(getActivity(), MeterStatisticsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sixth_layout:
                    intent = new Intent(getActivity(), MeterSelectActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

}
