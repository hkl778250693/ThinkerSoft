package com.example.administrator.thinker_soft.meter_code.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterStatisticsActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserContinueActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserListviewActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserUndoneActivity;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class MeterHomePageFragment extends Fragment {
    private View view;
    private LinearLayout first_layout,second_layout,third_layout,fourth_layout,fifth_layout,sixth_layout,rootLinearlayout,noData;
    private LayoutInflater layoutInflater;
    private PopupWindow fileWindow,bookWindow,undoneWindow;
    private View fileSelectView,bookView,undoneView;
    private ListView fileListView,bookListview;
    private MeterSingleSelectItem fileItem,bookItem;
    private List<MeterSingleSelectItem> fileList = new ArrayList<>();
    private List<MeterSingleSelectItem> bookList = new ArrayList<>();
    private MeterFileSelectListAdapter fileAdapter,bookAdapter;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login,sharedPreferences;
    private boolean bookMeterState,undoneMeterState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chaobiao, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookMeterState = false;
        undoneMeterState = false;
    }

    //绑定控件
    private void bindView() {
        first_layout  = (LinearLayout) view.findViewById(R.id.first_layout);
        second_layout = (LinearLayout) view.findViewById(R.id.second_layout);
        third_layout  = (LinearLayout) view.findViewById(R.id.third_layout);
        fourth_layout = (LinearLayout) view.findViewById(R.id.fourth_layout);
        fifth_layout  = (LinearLayout) view.findViewById(R.id.fifth_layout);
        sixth_layout = (LinearLayout) view.findViewById(R.id.sixth_layout);
        rootLinearlayout = (LinearLayout) view.findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(getActivity(), 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
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
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        if(!"".equals(sharedPreferences.getString("currentBookName",""))){
                            intent = new Intent(getActivity(), MeterUserContinueActivity.class);
                            intent.putExtra("fileName",sharedPreferences.getString("currentFileName",""));
                            intent.putExtra("bookName",sharedPreferences.getString("currentBookName",""));
                            intent.putExtra("bookID",sharedPreferences.getString("currentBookID",""));
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(),"请先选择抄表本！",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.second_layout:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        bookMeterState = true;
                        showBookSelectWindow();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                getBookInfo();
                            }
                        }.start();
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.third_layout:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        if(!"".equals(sharedPreferences.getString("currentBookName",""))){
                            intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                            intent.putExtra("fileName",sharedPreferences.getString("currentFileName",""));
                            intent.putExtra("bookName",sharedPreferences.getString("currentBookName",""));
                            intent.putExtra("bookID",sharedPreferences.getString("currentBookID",""));
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(),"请先选择抄表本！",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.fourth_layout:
                    if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                        undoneMeterState = true;
                        meterUndoneWindow();
                    }else {
                        Toast.makeText(getActivity(),"请先完成文件选择！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.fifth_layout:
                    intent = new Intent(getActivity(), MeterStatisticsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sixth_layout:
                    showFileSelectWindow();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            getFileInfo();
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 抄表本选择窗口
     */
    public void showBookSelectWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        bookView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        bookWindow = new PopupWindow(bookView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) bookView.findViewById(R.id.back);
        bookListview = (ListView) bookView.findViewById(R.id.list_view);
        TextView tips = (TextView) bookView.findViewById(R.id.tips);
        //设置点击事件
        tips.setText("请选择抄表本");
        bookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                bookItem = (MeterSingleSelectItem) bookAdapter.getItem(position);
                Log.i("meterHomePage","当前点击的item为："+bookItem.getName());
                sharedPreferences.edit().putString("currentBookName",bookItem.getName()).apply();
                sharedPreferences.edit().putString("currentBookID",bookItem.getID()).apply();
                bookWindow.dismiss();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = null;
                if(bookMeterState){
                    intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                }else if(undoneMeterState){
                    intent = new Intent(getActivity(), MeterUserUndoneActivity.class);
                    intent.putExtra("type","单个");
                }
                if (intent != null) {
                    intent.putExtra("bookName",bookItem.getName());
                    intent.putExtra("bookID",bookItem.getID());
                    intent.putExtra("fileName",sharedPreferences.getString("currentFileName",""));
                    startActivity(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookWindow.dismiss();
            }
        });
        bookWindow.update();
        bookWindow.setFocusable(true);
        bookWindow.setTouchable(true);
        bookWindow.setOutsideTouchable(true);
        bookWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        bookWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        bookWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        bookWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 未抄抄表选择窗口
     */
    public void meterUndoneWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        undoneView = layoutInflater.inflate(R.layout.popupwindow_meter_user_undone, null);
        undoneWindow = new PopupWindow(undoneView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) undoneView.findViewById(R.id.back);
        TextView tips = (TextView) undoneView.findViewById(R.id.tips);
        TextView singleBook = (TextView) undoneView.findViewById(R.id.single_book);
        TextView allBook = (TextView) undoneView.findViewById(R.id.all_book);
        //设置点击事件
        tips.setText("请选择方式");
        singleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
                showBookSelectWindow();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        getBookInfo();
                    }
                }.start();
            }
        });
        allBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), MeterUserUndoneActivity.class);
                intent.putExtra("type","所有");
                intent.putExtra("fileName",sharedPreferences.getString("currentFileName",""));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
            }
        });
        undoneWindow.update();
        undoneWindow.setFocusable(true);
        undoneWindow.setTouchable(true);
        undoneWindow.setOutsideTouchable(true);
        undoneWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        undoneWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        undoneWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        undoneWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 文件选择窗口
     */
    public void showFileSelectWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        fileSelectView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        fileWindow = new PopupWindow(fileSelectView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) fileSelectView.findViewById(R.id.back);
        fileListView = (ListView) fileSelectView.findViewById(R.id.list_view);
        TextView tips = (TextView) fileSelectView.findViewById(R.id.tips);
        noData = (LinearLayout) fileSelectView.findViewById(R.id.no_data);
        //设置点击事件
        tips.setText("请选择文件夹");
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                fileItem = (MeterSingleSelectItem) fileAdapter.getItem(position);
                Log.i("meterHomePage","当前点击的item为："+fileItem.getName());
                sharedPreferences.edit().putString("currentFileName",fileItem.getName()).apply();
                fileWindow.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileWindow.dismiss();
            }
        });
        fileWindow.update();
        fileWindow.setFocusable(true);
        fileWindow.setTouchable(true);
        fileWindow.setOutsideTouchable(true);
        fileWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        fileWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        fileWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        fileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    fileAdapter = new MeterFileSelectListAdapter(getActivity(),fileList,1);
                    fileListView.setAdapter(fileAdapter);
                    break;
                case 1:
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    bookAdapter = new MeterFileSelectListAdapter(getActivity(),bookList,0);
                    bookListview.setAdapter(bookAdapter);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //查询抄表文件信息
    public void getFileInfo() {
        fileList.clear();
        Cursor cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("meterHomePage", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("fileName")));
            fileList.add(item);
        }
        handler.sendEmptyMessage(0);
        cursor.close();
    }

    //查询抄表本信息
    public void getBookInfo() {
        bookList.clear();
        Cursor cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName","")});//查询并获得游标
        Log.i("meterHomePage", "抄表本个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setID(cursor.getString(cursor.getColumnIndex("bookId")));
            bookList.add(item);
        }
        handler.sendEmptyMessage(2);
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
