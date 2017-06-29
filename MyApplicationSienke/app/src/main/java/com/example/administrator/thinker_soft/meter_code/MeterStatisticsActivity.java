package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

public class MeterStatisticsActivity extends Activity {
    private ImageView back;
    private TextView allUserNumber, doneNumber, undone_number, meterNumber, finishRate;
    private RadioButton allUserStatistics, bookStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaobiaotongji);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        allUserNumber = (TextView) findViewById(R.id.all_user_number);
        doneNumber = (TextView) findViewById(R.id.done_number);
        undone_number = (TextView) findViewById(R.id.undone_number);
        meterNumber = (TextView) findViewById(R.id.meter_number);
        finishRate = (TextView) findViewById(R.id.finish_rate);
        allUserStatistics = (RadioButton) findViewById(R.id.all_user_statistics);
        bookStatistics = (RadioButton) findViewById(R.id.book_statistics);
    }

    //初始化设置
    private void defaultSetting() {
        allUserStatistics.setChecked(true);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        allUserStatistics.setOnClickListener(clickListener);
        bookStatistics.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.all_user_statistics:

                    break;
                case R.id.book_statistics:

                    break;
                default:
                    break;
            }
        }
    };
}
