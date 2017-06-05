package com.example.administrator.thinker_soft.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.adapter.GridviewHomePageAdapter;
import com.example.administrator.thinker_soft.adapter.GridviewImageAdapter;
import com.example.administrator.thinker_soft.model.GridHomePageItem;
import com.example.administrator.thinker_soft.model.GridviewHomePageViewHolder;
import com.example.administrator.thinker_soft.model.NewTaskViewHolder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_homepage);

        bindView();//绑定控件
        setViewClickListener();//设置点击事件
    }

    //绑定控件
    private void bindView() {
        set = (ImageView) findViewById(R.id.set);
        gridView = (GridView) findViewById(R.id.gridview);
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
                    Intent intent1 = new Intent(MoveHomePageActivity.this,QueryActivity.class);
                    startActivity(intent1);
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set:
                    break;
            }
        }
    };

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
