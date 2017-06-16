package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessEmailInfoActivity extends Activity {

    private ImageView back, more;
    private PopupWindow window;
    private RadioButton huifu, zhuanfa, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email_info);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessEmailInfoActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_business_email_inbox, null);
                window = new PopupWindow(popupView, 600, 400);
                window.setAnimationStyle(R.style.Popupwindow);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(more, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        more.setClickable(true);
                    }
                });

                huifu = (RadioButton) popupView.findViewById(R.id.huifu);
                zhuanfa = (RadioButton) popupView.findViewById(R.id.zhuanfa);
                detail = (RadioButton) popupView.findViewById(R.id.detail);

                huifu.setOnClickListener(clickListener);
                zhuanfa.setOnClickListener(clickListener);
                detail.setOnClickListener(clickListener);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.huifu:
                    Intent intent = new Intent(BusinessEmailInfoActivity.this, BusinessAnswerEmailActivity.class);
                    startActivity(intent);
                    window.dismiss();
                    break;
                case R.id.zhuanfa:
                    Intent intent1 = new Intent(BusinessEmailInfoActivity.this, BusinessIntransitEmailActivity.class);
                    startActivity(intent1);
                    window.dismiss();
                    break;
                case R.id.detail:
                    window.dismiss();
                    Intent intent2 = new Intent();
                    setResult(RESULT_OK,intent2);
                    finish();
                    break;
            }
        }
    };

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessEmailInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessEmailInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessEmailInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessEmailInfoActivity.this.getWindow().setAttributes(lp);
    }
}
