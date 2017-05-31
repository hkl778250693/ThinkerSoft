package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/3/17.
 */
public class IpSettingActivity extends Activity {
    private ImageView back;
    private EditText ipEdit,portEdit;
    private Button confirmBtn,edit;
    private SharedPreferences public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private String ip,port;
    private LinearLayout rootLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_settings);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }
    //绑定控件
    private void bindView(){
        back= (ImageView) findViewById(R.id.back);
        ipEdit = (EditText) findViewById(R.id.ip_edit);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        edit = (Button) findViewById(R.id.edit);
        portEdit = (EditText) findViewById(R.id.port_edit);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }
    //点击事件
    private void setViewClickListener(){
        back.setOnClickListener(clickListener);
        edit.setOnClickListener(clickListener);
        confirmBtn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    IpSettingActivity.this.finish();
                    break;
                case R.id.edit:
                    ipEdit.setTextColor(getResources().getColor(R.color.text_black));
                    portEdit.setTextColor(getResources().getColor(R.color.text_black));
                    portEdit.setFocusable(true);
                    portEdit.setFocusableInTouchMode(true);
                    portEdit.requestFocus();
                    ipEdit.setFocusable(true);
                    ipEdit.setFocusableInTouchMode(true);
                    ipEdit.requestFocus();
                    ipEdit.setCursorVisible(true);
                    portEdit.setCursorVisible(true);
                    if(!public_sharedPreferences.getString("security_ip","").equals("")){
                        ipEdit.setSelection(public_sharedPreferences.getString("security_ip","").length());//将光标移至文字末尾
                    }else {
                        ipEdit.setSelection("192.168.2.201:".length());//将光标移至文字末尾
                    }
                    if(!public_sharedPreferences.getString("security_port","").equals("")){
                        portEdit.setSelection(public_sharedPreferences.getString("security_port","").length());//将光标移至文字末尾
                    }else {
                        portEdit.setSelection("8080".length());//将光标移至文字末尾
                    }
                    edit.setVisibility(View.GONE);
                    confirmBtn.setVisibility(View.VISIBLE);
                    break;
                case R.id.confirm_btn:
                    edit.setVisibility(View.VISIBLE);
                    confirmBtn.setVisibility(View.GONE);
                    ipEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    portEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    ipEdit.setCursorVisible(false);
                    portEdit.setCursorVisible(false);
                    ipEdit.setFocusable(false);
                    ipEdit.setFocusableInTouchMode(false);
                    portEdit.setFocusable(false);
                    portEdit.setFocusableInTouchMode(false);
                    ip = ipEdit.getText().toString().trim();
                    port = portEdit.getText().toString().trim();
                    editor.putString("security_ip",ip);
                    editor.putString("security_port",port);
                    editor.putBoolean("ip_port_changed",true);
                    editor.apply();
                    Toast.makeText(IpSettingActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //初始化设置
    private void defaultSetting() {
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = public_sharedPreferences.edit();
        ipEdit.setTextColor(getResources().getColor(R.color.text_gray));
        portEdit.setTextColor(getResources().getColor(R.color.text_gray));
        ipEdit.setFocusable(false);
        ipEdit.setFocusableInTouchMode(false);
        portEdit.setFocusable(false);
        portEdit.setFocusableInTouchMode(false);
        if(!public_sharedPreferences.getString("security_ip","").equals("")){
            ipEdit.setText(public_sharedPreferences.getString("security_ip",""));
        }else {
            ipEdit.setText("192.168.2.201:");
        }
        if(!public_sharedPreferences.getString("security_port","").equals("")){
            portEdit.setText(public_sharedPreferences.getString("security_port",""));
        }else {
            portEdit.setText("8080");
        }
    }
}

