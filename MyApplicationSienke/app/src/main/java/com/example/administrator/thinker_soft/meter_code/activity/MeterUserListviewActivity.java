package com.example.administrator.thinker_soft.meter_code.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterTypeListviewAdaper;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListviewAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterTypeListviewItem;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class MeterUserListviewActivity extends Activity {
    private ImageView back;
    private LinearLayout selectPage;
    private RelativeLayout title;
    private ListView listview, areaListView, bookListView;
    private TextView noData, lastPage, nextPage, currentPageTv, totalPageTv, bookName;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private Cursor totalCountCursor, userLimitCursor;
    private SharedPreferences sharedPreferences_login;
    private int dataStartCount = 0;   //用于分页查询，表示从第几行开始
    private int currentPage = 1;  //当前页数
    private int totalPage;    //总页数
    private MeterUserListviewAdapter adapter;
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置
    private LayoutInflater layoutInflater;
    private PopupWindow typeSelectPopup;   //分区和表册选择框
    private View view;
    private LinearLayout rootLinearlayout, areaLayout, bookLayout;
    private MeterTypeListviewAdaper areaAdapter, bookAdapter;
    private String areaCurrentID = "";  //记录用户选择的分区item ID
    private String bookCurrentID = "";  //记录用户选择的表册item ID
    private List<MeterTypeListviewItem> areaItemList = new ArrayList<>();
    private List<MeterTypeListviewItem> bookItemList = new ArrayList<>();
    private List<String> bookIDList = new ArrayList<>();  //存放表册ID的集合
    private String bookID,book_name,fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_listview);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        title = (RelativeLayout) findViewById(R.id.title);
        areaLayout = (LinearLayout) findViewById(R.id.area_layout);
        bookLayout = (LinearLayout) findViewById(R.id.book_layout);
        back = (ImageView) findViewById(R.id.back);
        bookName = (TextView) findViewById(R.id.book_name);
        noData = (TextView) findViewById(R.id.no_data);
        selectPage = (LinearLayout) findViewById(R.id.select_page);
        listview = (ListView) findViewById(R.id.listview);
        lastPage = (TextView) findViewById(R.id.last_page);
        nextPage = (TextView) findViewById(R.id.next_page);
        currentPageTv = (TextView) findViewById(R.id.current_page_tv);
        totalPageTv = (TextView) findViewById(R.id.total_page_tv);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserListviewActivity.this, 1);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if(intent != null){
            fileName = intent.getStringExtra("fileName");
            bookID = intent.getStringExtra("bookID");
            book_name = intent.getStringExtra("bookName");
            bookName.setText("当前："+book_name);
            if(!"".equals(bookID) && !"".equals(fileName)){
                Log.i("meter_user","");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getTotalUserCount();
                        getMeterUserData(fileName, bookID, dataStartCount);   //默认读取本地的抄表分区用户数据
                        handler.sendEmptyMessage(0);
                    }
                }.start();
            }
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        lastPage.setOnClickListener(clickListener);
        nextPage.setOnClickListener(clickListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                item = (MeterUserListviewItem) adapter.getItem(position);
                Intent intent = new Intent(MeterUserListviewActivity.this, MeterUserDetailActivity.class);
                intent.putExtra("user_id", item.getUserID());
                startActivityForResult(intent, currentPosition);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserListviewActivity.this.finish();
                    break;
                case R.id.last_page:
                    lastPage.setClickable(false);
                    if (currentPageTv.getText().equals("1")) {
                        Toast.makeText(MeterUserListviewActivity.this, "已经是第一页哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) - 1));
                        dataStartCount -= 50;
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                if (!"".equals(areaCurrentID)) {
                                    getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表分区用户数据
                                } else {
                                    if (!"".equals(bookCurrentID)) {
                                        getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                                    }
                                }
                                handler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                    break;
                case R.id.next_page:
                    nextPage.setClickable(false);
                    if (currentPageTv.getText().equals(totalPageTv.getText())) {
                        Toast.makeText(MeterUserListviewActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) + 1));
                        dataStartCount += 50;
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                                handler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                    Log.i("MeterUserLVActivity", "总页数为：" + totalPageTv.getText());
                    Log.i("MeterUserLVActivity", "开始行数是：" + dataStartCount);
                    Log.i("MeterUserLVActivity", "当前页数是：" + currentPageTv.getText());
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new MeterUserListviewAdapter(MeterUserListviewActivity.this, userLists);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    currentPageTv.setText(String.valueOf(currentPage));
                    if (totalCountCursor.getCount() % 50 != 0) {
                        totalPage = totalCountCursor.getCount() / 50 + 1;
                    } else {
                        if (totalCountCursor.getCount() <= 50) {
                            totalPage = 1;
                        } else {
                            totalPage = totalCountCursor.getCount() / 50;
                        }
                    }
                    totalPageTv.setText(String.valueOf(totalPage));
                    break;
                case 1:
                    adapter = new MeterUserListviewAdapter(MeterUserListviewActivity.this, userLists);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    lastPage.setClickable(true);
                    nextPage.setClickable(true);
                    break;
                case 2:
                    areaAdapter = new MeterTypeListviewAdaper(MeterUserListviewActivity.this, areaItemList,0);
                    areaAdapter.notifyDataSetChanged();
                    areaListView.setAdapter(areaAdapter);
                    break;
                case 3:
                    bookAdapter = new MeterTypeListviewAdaper(MeterUserListviewActivity.this, bookItemList,1);
                    bookAdapter.notifyDataSetChanged();
                    bookListView.setAdapter(bookAdapter);
                    break;
                case 4:
                    if (noData.getVisibility() == View.GONE) {
                        noData.setVisibility(View.VISIBLE);
                    }
                    selectPage.setVisibility(View.GONE);
                    break;
                case 5:
                    if (noData.getVisibility() == View.VISIBLE) {
                        noData.setVisibility(View.GONE);
                    }
                    selectPage.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * viewGroup出来动画
     */
    public void viewGroupOutAnimation(ViewGroup viewGroup) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.list_out_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * viewGroup返回动画
     */
    public void viewGroupBackAnimation(ViewGroup viewGroup) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);
        viewGroup.setLayoutAnimation(controller);
    }

    //选择分区和表册 Popup
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void selectAreaAndBookPoup() {
        layoutInflater = LayoutInflater.from(MeterUserListviewActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_meter_area_select, null);
        typeSelectPopup = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        areaListView = (ListView) view.findViewById(R.id.area_list_view);
        bookListView = (ListView) view.findViewById(R.id.book_list_view);
        //设置点击事件
        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                areaAdapter.setSelectedPosition(position);
                areaAdapter.notifyDataSetInvalidated();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //获取点击的Item
                        MeterTypeListviewItem item = (MeterTypeListviewItem) areaAdapter.getItem(position);
                        Log.i("MeterUserListview", "当前点击分区的item为：" + item.getName());
                        areaCurrentID = item.getId();
                        getBookInfo(areaCurrentID);
                    }
                }.start();
            }
        });
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //获取点击的Item
                        MeterTypeListviewItem item = (MeterTypeListviewItem) bookAdapter.getItem(position);
                        Log.i("MeterUserListview", "当前点击抄表本的item为：" + item.getName());
                        bookCurrentID = item.getId();
                        if (!"".equals(bookCurrentID)) {
                            getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                        }
                    }
                }.start();
            }
        });
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.update();
        typeSelectPopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.meter_area_user_layout_shape));
        typeSelectPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        typeSelectPopup.showAsDropDown(title,15, 15);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterUserListviewActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterUserListviewActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterUserListviewActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterUserListviewActivity.this.getWindow().setAttributes(lp);
    }

    //查询抄表用户总数
    public void getTotalUserCount() {
        totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName,bookID});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        Log.i("MeterUserLVActivity", "总的查询到" + totalCountCursor.getCount() + "条数据！");
        if (totalCountCursor.getCount() == 0) {
            return;
        }
        while (totalCountCursor.moveToNext()) {

        }
        totalCountCursor.close();
    }

    //查询所有表册ID
    public void getBookInfo(String areaID) {
        bookIDList.clear();
        bookItemList.clear();
        Cursor cursor = db.rawQuery("select * from MeterBook where login_user_id=? and areaId=? order by bookId", new String[]{sharedPreferences_login.getString("userId", ""), areaID});//查询并获得游标
        Log.i("MeterStatisticsActivity", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            bookIDList.add(cursor.getString(cursor.getColumnIndex("bookId")));
            MeterTypeListviewItem item = new MeterTypeListviewItem();
            item.setName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setId(cursor.getString(cursor.getColumnIndex("bookId")));
            bookItemList.add(item);
        }
        handler.sendEmptyMessage(3);
        cursor.close();
    }

    //读取本地的抄表分区用户数据
    public void getMeterUserData(String fileName,String bookID,int dataStartCount) {
        userLists.clear();
        userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + ",50", new String[]{sharedPreferences_login.getString("userId", ""), fileName,bookID});//查询并获得游标
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (userLimitCursor.getCount() == 0) {
            handler.sendEmptyMessage(4);
            return;
        }
        handler.sendEmptyMessage(5);
        while (userLimitCursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));
            item.setUserName(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));
            item.setUserID(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));
            if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")).equals("null")) {
                item.setMeterNumber(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));
            } else {
                item.setMeterNumber("无");
            }
            item.setLastMonth(userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));
            item.setThisMonth(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));
            item.setAddress(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));
            if (userLimitCursor.getString(userLimitCursor.getColumnIndex("meterState")).equals("false")) {
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.userlist_red);
            } else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.userlist_gray);
            }
            userLists.add(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                if (data != null) {
                    item.setThisMonth(data.getStringExtra("this_month_dosage"));
                    item.setIfEdit(R.mipmap.userlist_gray);
                    item.setMeterState("已抄");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if (totalCountCursor != null) {
            totalCountCursor.close();
        }
        if (userLimitCursor != null) {
            userLimitCursor.close();
        }
        db.close();
    }
}
