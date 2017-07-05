package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeterStatisticsActivity extends Activity {
    private ImageView back;
    private TextView allUserNumberTv, doneNumberTv, undoneNumberTv, meterCountTv, finishRateTv;
    private int allUserNumber = 0, doneNumber = 0, undoneNumber = 0, meterCount = 0;
    private String finishRate;
    private RadioButton areaRb,bookRb,allUserStatistics, singleStatistics,cancle,comfirm;
    private SQLiteDatabase db;  //数据库
    private Cursor areaAllCursor, areaSingleCursor, bookAllCursor,bookSingleCursor;
    private SharedPreferences sharedPreferences_login;
    private List<String> areaIDList = new ArrayList<>();  //存放分区ID的集合
    private List<String> bookIDList = new ArrayList<>();  //存放表册ID的集合
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private View view;
    private ListView listView;
    private TextView tips;
    private ImageView noData;
    private LinearLayout rootLinearlayout;
    private int selectPosition = -1;  //用于记录用户选择的变量
    private String currentID = "";  //单选时当前点击的item ID
    private List<MeterSingleSelectItem> singleSelectItems = new ArrayList<>();
    private MeterSingleSelectItem selectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_statistics);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        allUserNumberTv = (TextView) findViewById(R.id.all_user_number);
        doneNumberTv = (TextView) findViewById(R.id.done_number);
        undoneNumberTv = (TextView) findViewById(R.id.undone_number);
        meterCountTv = (TextView) findViewById(R.id.meter_number);
        finishRateTv = (TextView) findViewById(R.id.finish_rate);
        areaRb = (RadioButton) findViewById(R.id.area_rb);
        bookRb = (RadioButton) findViewById(R.id.book_rb);
        allUserStatistics = (RadioButton) findViewById(R.id.all_user_statistics);
        singleStatistics = (RadioButton) findViewById(R.id.single_statistics);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        areaRb.setChecked(true);
        allUserStatistics.setChecked(true);
        MySqliteHelper helper = new MySqliteHelper(MeterStatisticsActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        getAreaStatisticsData();  //获取分区统计数据
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        areaRb.setOnClickListener(clickListener);
        bookRb.setOnClickListener(clickListener);
        allUserStatistics.setOnClickListener(clickListener);
        singleStatistics.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.area_rb:
                    allUserStatistics.setText("统计分区所有用户");
                    singleStatistics.setText("按单个分区统计");
                    break;
                case R.id.book_rb:
                    allUserStatistics.setText("统计表册所有用户");
                    singleStatistics.setText("按单个表册统计");
                    break;
                case R.id.all_user_statistics:
                    getAreaStatisticsData();  //获取分区统计数据
                    break;
                case R.id.single_statistics:
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            if(areaRb.isChecked()){
                                getAreaInfo();
                            }else {
                                getBookInfo();
                            }
                            handler.sendEmptyMessage(1);
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取分区统计数据
     */
    public void getAreaStatisticsData(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                getAreaInfo();
                getBookInfo();
                allUserNumber = 0;
                doneNumber = 0;
                meterCount = 0;
                if(areaRb.isChecked()){
                    for(int i=0;i<areaIDList.size();i++){
                        getAreaAllUserData(areaIDList.get(i));   //获取分区所有用户数据信息
                    }
                }else {
                    for(int i=0;i<bookIDList.size();i++){
                        getBookAllUserData(bookIDList.get(i));   //获取表册所有用户数据信息
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    //选择单个分区或表册 Popup
    public void showSelectSinglePoup() {
        layoutInflater = LayoutInflater.from(MeterStatisticsActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_meter_statistics_single_select, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        cancle = (RadioButton) view.findViewById(R.id.cancel);
        comfirm = (RadioButton) view.findViewById(R.id.confirm);
        listView = (ListView) view.findViewById(R.id.listview);
        tips = (TextView) view.findViewById(R.id.tips);
        noData = (ImageView) view.findViewById(R.id.no_data);
        if(areaRb.isChecked()){
            tips.setText("请选择分区");
        }else {
            tips.setText("请选择表册");
        }
        //设置点击事件
        Log.i("MeterStatisticsActivity", "所有分区个数为：" + singleSelectItems.size());
        final MeterSingleSelectAdapter adapter = new MeterSingleSelectAdapter(MeterStatisticsActivity.this,singleSelectItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectPosition = position;
                adapter.notifyDataSetChanged();
                selectItem = (MeterSingleSelectItem) adapter.getItem(position);
                Log.i("MeterStatisticsActivity","当前点击的item为："+selectItem.getName());
                currentID = selectItem.getID();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(currentID)){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            allUserNumber = 0;
                            doneNumber = 0;
                            meterCount = 0;
                            if(areaRb.isChecked()){
                                getAreaSingleUserData(currentID);   //获取单个分区用户数据信息
                            }else {
                                getbookSingleUserData(currentID);   //获取单个表册用户数据信息
                            }
                            handler.sendEmptyMessage(2);
                        }
                    }.start();
                }else {
                    Toast.makeText(MeterStatisticsActivity.this,"请选择其中一个哦！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterStatisticsActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterStatisticsActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterStatisticsActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterStatisticsActivity.this.getWindow().setAttributes(lp);
    }

    public class MeterSingleSelectAdapter extends BaseAdapter {
        private Context context;
        private List<MeterSingleSelectItem> selectItems;
        private LayoutInflater layoutInflater;

        public MeterSingleSelectAdapter(Context context,List<MeterSingleSelectItem> selectItems) {
            this.selectItems = selectItems;
            this.context = context;
            if (context != null) {
                layoutInflater = LayoutInflater.from(context);
            }
        }

        @Override
        public int getCount() {
            return selectItems.size();
        }

        @Override
        public Object getItem(int position) {
            return selectItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.meter_statistics_single_select_listview_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)convertView.findViewById(R.id.name);
                viewHolder.ID = (TextView)convertView.findViewById(R.id.id);
                viewHolder.checkState = (RadioButton)convertView.findViewById(R.id.check_state);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            MeterSingleSelectItem item = selectItems.get(position);
            viewHolder.name.setText(item.getName());
            viewHolder.ID.setText(item.getID());
            if(selectPosition == position){
                viewHolder.checkState.setChecked(true);
            }
            else{
                viewHolder.checkState.setChecked(false);
            }
            return convertView;
        }
    }

    public class ViewHolder{
        TextView name;
        TextView ID;
        RadioButton checkState;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    setStatisticsData();  //设置统计数据
                    break;
                case 1:
                    showSelectSinglePoup();
                    break;
                case 2:
                    setStatisticsData();  //设置统计数据
                    popupWindow.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 设置统计数据
     */
    public void setStatisticsData(){
        allUserNumberTv.setText(String.valueOf(allUserNumber));
        doneNumberTv.setText(String.valueOf(doneNumber));
        undoneNumberTv.setText(String.valueOf(undoneNumber));
        meterCountTv.setText(String.valueOf(meterCount));
        finishRateTv.setText(String.valueOf(finishRate));
    }

    //查询所有分区ID
    public void getAreaInfo() {
        areaIDList.clear();
        singleSelectItems.clear();
        Cursor cursor = db.rawQuery("select * from MeterArea where login_user_id=? order by areaId", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("MeterStatisticsActivity", "所有分区ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            areaIDList.add(cursor.getString(cursor.getColumnIndex("areaId")));
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("areaName")));
            item.setID(cursor.getString(cursor.getColumnIndex("areaId")));
            singleSelectItems.add(item);
        }
        cursor.close();
    }

    //查询所有表册ID
    public void getBookInfo() {
        bookIDList.clear();
        singleSelectItems.clear();
        Cursor cursor = db.rawQuery("select * from MeterBook where login_user_id=? order by bookId", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("MeterStatisticsActivity", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            bookIDList.add(cursor.getString(cursor.getColumnIndex("bookId")));
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setID(cursor.getString(cursor.getColumnIndex("bookId")));
            singleSelectItems.add(item);
        }
        cursor.close();
    }

    //查询抄表分区所有用户数据信息
    public void getAreaAllUserData(String areaID) {
        areaAllCursor = db.rawQuery("select * from MeterUser where login_user_id=? and area_id=?", new String[]{sharedPreferences_login.getString("userId", ""),areaID});//查询并获得游标
        Log.i("MeterStatisticsActivity", "查询到所有分区有" + areaAllCursor.getCount() + "条数据！");
        allUserNumber += areaAllCursor.getCount();
        //如果游标为空，则显示没有数据图片
        if (areaAllCursor.getCount() == 0) {
            return;
        }
        while (areaAllCursor.moveToNext()) {
            if(areaAllCursor.getString(areaAllCursor.getColumnIndex("meterState")).equals("true")){
                doneNumber++;
                meterCount += Integer.parseInt(areaAllCursor.getString(areaAllCursor.getColumnIndex("this_month_dosage")));
            }
        }
        getStatisticsData(); //获取统计数据
    }

    /**
     * 获取统计数据
     */
    public void getStatisticsData(){
        undoneNumber = allUserNumber - doneNumber;
        if (allUserNumber != 0) {
            double haveDone = (double) doneNumber* 100;
            double totalCount = (double) allUserNumber;
            double finishingRate = haveDone/totalCount;  //完成率
            Log.i("getTotalUserNumber===>", "完成率=" + finishingRate + "%");
            DecimalFormat df = new DecimalFormat("0.0");
            finishRate = df.format(finishingRate);
            Log.i("getTotalUserNumber===>", "完成率=" + finishRate + "%");
        } else {
            finishRate = "0.0";
        }
    }

    //查询单个分区用户数据信息(传入分区ID)
    public void getAreaSingleUserData(String areaID) {
        doneNumber = 0;
        meterCount = 0;
        areaSingleCursor = db.rawQuery("select * from MeterUser where login_user_id=? and area_id=?", new String[]{sharedPreferences_login.getString("userId", ""),areaID});//查询并获得游标
        Log.i("MeterStatisticsActivity", "查询到单个分区有" + areaSingleCursor.getCount() + "条数据！");
        allUserNumber += areaSingleCursor.getCount();
        //如果游标为空，则显示没有数据图片
        if (areaSingleCursor.getCount() == 0) {
            return;
        }
        while (areaSingleCursor.moveToNext()) {
            if(areaSingleCursor.getString(areaSingleCursor.getColumnIndex("meterState")).equals("true")){
                doneNumber++;
                meterCount += Integer.parseInt(areaSingleCursor.getString(areaSingleCursor.getColumnIndex("this_month_dosage")));
            }
        }
        getStatisticsData(); //获取统计数据
    }

    //查询表册所有用户数据信息(传入所有表册ID)
    public void getBookAllUserData(String bookID) {
        bookAllCursor = db.rawQuery("select * from MeterUser where login_user_id=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""),bookID});//查询并获得游标
        Log.i("MeterStatisticsActivity", "查询到所有表册有" + bookAllCursor.getCount() + "条数据！");
        allUserNumber += bookAllCursor.getCount();
        //如果游标为空，则显示没有数据图片
        if (bookAllCursor.getCount() == 0) {
            return;
        }
        while (bookAllCursor.moveToNext()) {
            if(bookAllCursor.getString(bookAllCursor.getColumnIndex("meterState")).equals("true")){
                doneNumber++;
                meterCount += Integer.parseInt(bookAllCursor.getString(bookAllCursor.getColumnIndex("this_month_dosage")));
            }
        }
        getStatisticsData(); //获取统计数据
    }

    //查询单个表册用户数据信息(传入表册ID)
    public void getbookSingleUserData(String areaID) {
        doneNumber = 0;
        meterCount = 0;
        bookSingleCursor = db.rawQuery("select * from MeterUser where login_user_id=? and area_id=?", new String[]{sharedPreferences_login.getString("userId", ""),areaID});//查询并获得游标
        Log.i("MeterStatisticsActivity", "查询到单个分区有" + bookSingleCursor.getCount() + "条数据！");
        allUserNumber += bookSingleCursor.getCount();
        //如果游标为空，则显示没有数据图片
        if (bookSingleCursor.getCount() == 0) {
            return;
        }
        while (bookSingleCursor.moveToNext()) {
            if(bookSingleCursor.getString(bookSingleCursor.getColumnIndex("meterState")).equals("true")){
                doneNumber++;
                meterCount += Integer.parseInt(bookSingleCursor.getString(bookSingleCursor.getColumnIndex("this_month_dosage")));
            }
        }
        getStatisticsData(); //获取统计数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if (areaAllCursor != null) {
            areaAllCursor.close();
        }
        if (areaSingleCursor != null) {
            areaSingleCursor.close();
        }
        if (bookAllCursor != null) {
            bookAllCursor.close();
        }
        if (bookSingleCursor != null) {
            bookSingleCursor.close();
        }
        db.close();
    }
}
