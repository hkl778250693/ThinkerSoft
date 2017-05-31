package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.adapter.GridviewHomePagaAdapter;
import com.example.administrator.thinker_soft.adapter.TaskChooseAdapter;
import com.example.administrator.thinker_soft.model.HomePageItem;
import com.example.administrator.thinker_soft.model.HomePageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public class MoveHomePageActivity extends Activity {
    private ImageView Menu;
    private ImageView Set;
    private Button OkByt;
    private GridviewHomePagaAdapter adapter;
    private GridView gridviewHomepage;
    private List<HomePageItem> homePageItems = new ArrayList<>();
    private String defaul = "";//默认的全部不勾选
    private PopupWindow popupWindow,quitePopup;
    private LayoutInflater inflater; //转换器
    private View popupwindowView,quiteView;
    private Button cancelRb,saveRb;
    private TextView name, userName,tips;
    private LinearLayout rootLinearlayout;
    private ImageView security_check_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_homepage);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//设置点击事件
    }


    //绑定控件ID
    private void bindView() {
        Menu = (ImageView) findViewById(R.id.menu);
        Set = (ImageView) findViewById(R.id.set);
        OkByt = (Button) findViewById(R.id.Ok_byt);
        gridviewHomepage = (GridView) findViewById(R.id.gridview_homepage);
    }


    //初始化
    private void defaultSetting() {
        for (int i = 0; i < 5; i++) {
            HomePageItem item = new HomePageItem();
            if (i == 0) {
                item.setImage(R.mipmap.zsbg);
                item.setText("掌上办公");
            } else if (i == 1) {
                item.setImage(R.mipmap.gzl);
                item.setText("工作流");
            } else if (i == 2) {
                item.setImage(R.mipmap.qwx);
                item.setText("抢维修");
            } else if (i == 3) {
                item.setImage(R.mipmap.ydaj);
                item.setText("移动安检");
            } else if (i == 4) {
                item.setImage(R.mipmap.ydcx);
                item.setText("移动查询");
            }
            homePageItems.add(item);
        }
        //初始化勾选框信息，默认都是以未勾选为单位
        for (int i = 0; i < homePageItems.size(); i++) {
            defaul = defaul + "0";
        }

    }




    //设置点击事件
    private void setViewClickListener() {
        OkByt.setOnClickListener(clickListener);
        Set.setOnClickListener(clickListener);


        adapter = new GridviewHomePagaAdapter(MoveHomePageActivity.this, homePageItems);
        gridviewHomepage.setAdapter(adapter);
        gridviewHomepage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomePageViewHolder holder = (HomePageViewHolder) view.getTag();
                holder.checkBox.toggle();
                TaskChooseAdapter.getIsCheck().put(position, holder.checkBox.isChecked());
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set:
                    security_check_go.setClickable(false);
                    createPopupwindow();
                    Log.i("createPopupwindow===>", "true");
                    security_check_go.setClickable(true);
                    break;
                case R.id.Ok_byt:
                    break;
            }
        }
    };

    public void createPopupwindow(){
        Set.setOnClickListener(new View.OnClickListener() {
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
        popupWindow.showAsDropDown(security_check_go, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

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

    private void exitApp() {
        finish();
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
}

