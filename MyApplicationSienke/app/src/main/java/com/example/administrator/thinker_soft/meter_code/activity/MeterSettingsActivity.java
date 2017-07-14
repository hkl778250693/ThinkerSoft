package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.thinker_soft.R;

public class MeterSettingsActivity extends Activity {
    private ImageView back;
    private LinearLayout fileDelete,setPageCount,mapDonload,printNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_settings);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        fileDelete = (LinearLayout) findViewById(R.id.file_delete);
        setPageCount = (LinearLayout) findViewById(R.id.set_page_count);
        mapDonload = (LinearLayout) findViewById(R.id.map_donload);
        printNote = (LinearLayout) findViewById(R.id.print_note);
    }

    //初始化设置
    private void defaultSetting() {

    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        fileDelete.setOnClickListener(onClickListener);
        setPageCount.setOnClickListener(onClickListener);
        mapDonload.setOnClickListener(onClickListener);
        printNote.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterSettingsActivity.this.finish();
                    break;
                case R.id.file_delete:
                    Intent intent = new Intent(MeterSettingsActivity.this, MeterDeleteFileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set_page_count:
                    intent = new Intent(MeterSettingsActivity.this, MeterPageCountSettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.map_donload:
                    intent = new Intent(MeterSettingsActivity.this, MeterPageCountSettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.print_note:
                    intent = new Intent(MeterSettingsActivity.this, MeterPrintNoteActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
