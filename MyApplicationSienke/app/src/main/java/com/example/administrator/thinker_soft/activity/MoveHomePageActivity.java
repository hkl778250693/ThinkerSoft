package com.example.administrator.thinker_soft.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.adapter.GridviewHomePageAdapter;
import com.example.administrator.thinker_soft.android_cbjactivity.CBJActivity;
import com.example.administrator.thinker_soft.mobile_business.MobileBusinessActivity;
import com.example.administrator.thinker_soft.model.GridHomePageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */
public class MoveHomePageActivity extends Activity {
    private ImageView set;
    private GridView gridView;
    private GridviewHomePageAdapter adapter;
    private List<GridHomePageItem> gridHomePageItems = new ArrayList<>();
    private LinearLayout zsbg,gzl,qwx,ydaj,ydcx;
    private long exitTime = 0;//退出程序
    private LayoutInflater inflater; //转换器
    private View popupwindowView,quiteView;
    private Button cancelRb,saveRb;
    private PopupWindow popupWindow,quitePopup;
    private RadioButton settings, quite; //系统设置 退出应用
    private TextView tips;
    private RelativeLayout rootRelative;
    private LinearLayout rootLinearlayout;
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_homepage);

        bindView();//绑定控件
        defaultSetting();
        setViewClickListener();//设置点击事件
    }

    //绑定控件
    private void bindView() {
        set = (ImageView) findViewById(R.id.set);
        gridView = (GridView) findViewById(R.id.gridview);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        rootRelative = (RelativeLayout) findViewById(R.id.root_relative);
    }

    public void getData() {
        for (int i = 0; i < 5; i++) {
            GridHomePageItem item = new GridHomePageItem();
            if (i == 0) {
                item.setImageZsbg(R.mipmap.zsbg);
                item.setImageName("掌上办公");
            } else if (i == 1) {
                item.setImageZsbg(R.mipmap.gzl);
                item.setImageName("工作流");
            } else if (i == 2) {
                item.setImageZsbg(R.mipmap.qwx);
                item.setImageName("抢维修");
            } else if (i == 3) {
                item.setImageZsbg(R.mipmap.ydaj);
                item.setImageName("移动安检");
            } else if (i == 4) {
                item.setImageZsbg(R.mipmap.ydcx);
                item.setImageName("移动查询");
            }
            gridHomePageItems.add(item);
        }
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);  //退出登录以后需要这个备份记录是否更换账号
    }

    //点击事件
    private void setViewClickListener() {
        getData();
        set.setOnClickListener(clickListener);
        adapter = new GridviewHomePageAdapter(MoveHomePageActivity.this, gridHomePageItems);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridHomePageItem item = gridHomePageItems.get((int) parent.getAdapter().getItemId(position));
                if(item.getImageName().equals("移动安检")){
                    Intent intent = new Intent(MoveHomePageActivity.this,SecurityChooseActivity.class);
                    startActivity(intent);
                }else if(item.getImageName().equals("移动查询")){
                    Intent intent = new Intent(MoveHomePageActivity.this,QueryActivity.class);
                    startActivity(intent);
                }else if(item.getImageName().equals("掌上办公")){
                    Intent intent = new Intent(MoveHomePageActivity.this,CBJActivity.class);
                    startActivity(intent);
                }else if(item.getImageName().equals("工作流")){
                    Intent intent = new Intent(MoveHomePageActivity.this,BusinessWebviewActivity.class);
                    startActivity(intent);
                }else if (item.getImageName().equals("抢维修")){
                    Intent intent= new Intent(MoveHomePageActivity.this, MobileBusinessActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set:
                    createPopupwindow();
                    break;
            }
        }
    };

    //系统设置popupwindow
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createPopupwindow() {
        inflater = LayoutInflater.from(MoveHomePageActivity.this);
        popupwindowView = inflater.inflate(R.layout.popup_window_security, null);
        popupWindow = new PopupWindow(popupwindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        settings = (RadioButton) popupwindowView.findViewById(R.id.settings);//系统设置
        quite = (RadioButton) popupwindowView.findViewById(R.id.quite);//安全退出
        //设置点击事件
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.settings:
                        Intent intent = new Intent(MoveHomePageActivity.this, SystemSettingActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showQuitePopup();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_page_more_shape));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(rootRelative,0,0, Gravity.RIGHT);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //弹出退出登录前提示popupwindow
    public void showQuitePopup() {
        inflater = LayoutInflater.from(MoveHomePageActivity.this);
        quiteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        quitePopup = new PopupWindow(quiteView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) quiteView.findViewById(R.id.tips);
        cancelRb = (RadioButton) quiteView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) quiteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("退出后不会删除历史数据，下次登录依然可以使用本账号！");
        saveRb.setTextColor(getResources().getColor(R.color.red));
        saveRb.setText("退出登录");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
                Intent intent = new Intent(MoveHomePageActivity.this, MoveLoginActivity.class);
                startActivity(intent);
                sharedPreferences_login.edit().putBoolean("have_logined",false).apply();
                finish();
            }
        });
        quitePopup.update();
        quitePopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        quitePopup.setAnimationStyle(R.style.camera);
        quitePopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        quitePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MoveHomePageActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MoveHomePageActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MoveHomePageActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MoveHomePageActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p/>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
