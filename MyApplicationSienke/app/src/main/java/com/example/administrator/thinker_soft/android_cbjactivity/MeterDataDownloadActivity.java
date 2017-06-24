package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterAreaViewHolder;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterBookViewHolder;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.BookDataAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeterDataDownloadActivity extends Activity {
    private ImageView back;
    private ListView booklistView;
    private ListView arealistView;
    private BookDataAdapter bookAdapter;
    private AreaDataAdapter areaAdapter;
    private EditText begianNum;
    private EditText endNum;
    private Button downLoadBtn;
    private TextView selectBookNumber, selectAreaNumber;
    private TextView bookSelectAll, bookReverse, bookSelectCancel, areaSelectAll, areaReverse, areaSelectCancel;
    private List<BookInfo> bookInfoList = new ArrayList<>();      //抄表本集合
    private List<AreaInfo> areaInfoList = new ArrayList<>();   //抄表分区集合
    private int bookNum = 0; // 记录抄表本选中的条目数量
    private int areaNum = 0; // 记录抄表分区选中的条目数量
    private HashMap<String, Integer> bookIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表本ID
    private HashMap<String, Integer> areaIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表分区ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_data_download);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        selectBookNumber = (TextView) findViewById(R.id.select_book_number);
        selectAreaNumber = (TextView) findViewById(R.id.select_area_number);
        booklistView = (ListView) findViewById(R.id.meter_book_lv);
        arealistView = (ListView) findViewById(R.id.meter_area_lv);
        begianNum = (EditText) findViewById(R.id.begain_num);
        endNum = (EditText) findViewById(R.id.end_num);
        downLoadBtn = (Button) findViewById(R.id.downLoad_btn);
        bookSelectAll = (TextView) findViewById(R.id.book_select_all);
        bookReverse = (TextView) findViewById(R.id.book_reverse);
        bookSelectCancel = (TextView) findViewById(R.id.book_select_cancel);
        areaSelectAll = (TextView) findViewById(R.id.area_select_all);
        areaReverse = (TextView) findViewById(R.id.area_reverse);
        areaSelectCancel = (TextView) findViewById(R.id.area_select_cancel);
    }

    //初始化设置
    private void defaultSetting() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle meterData = intent.getExtras();
            if (meterData != null) {
                Log.i("MeterDataDownload", "数据接收成功");
                ArrayList<BookInfo> bookInfoArrayList = meterData.getParcelableArrayList("bookInfoArrayList");
                ArrayList<AreaInfo> areaInfoArrayList = meterData.getParcelableArrayList("areaInfoArrayList");
                //初始化抄表本listview
                if (bookInfoArrayList != null) {
                    for (int i = 0; i < bookInfoArrayList.size(); i++) {
                        bookInfoList.add(bookInfoArrayList.get(i));
                    }
                    bookAdapter = new BookDataAdapter(MeterDataDownloadActivity.this, bookInfoList);
                    booklistView.setAdapter(bookAdapter);
                }
                //初始化抄表分区listview
                if (areaInfoArrayList != null) {
                    for (int i = 0; i < areaInfoArrayList.size(); i++) {
                        areaInfoList.add(areaInfoArrayList.get(i));
                    }
                    areaAdapter = new AreaDataAdapter(MeterDataDownloadActivity.this, areaInfoList);
                    arealistView.setAdapter(areaAdapter);
                }
            }
        }
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        booklistView.setOnItemClickListener(new OnItemClickListener() {   //抄表本item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterBookViewHolder holder = (MeterBookViewHolder) view.getTag();
                holder.checkedState.toggle();
                BookDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                // 调整选定条目
                if (holder.checkedState.isChecked()) {
                    bookNum++;
                } else {
                    bookNum--;
                }
                //TextView显示
                selectBookNumber.setText("(" + bookNum + ")");
            }
        });
        arealistView.setOnItemClickListener(new OnItemClickListener() {   //抄表分区item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterAreaViewHolder holder = (MeterAreaViewHolder) view.getTag();
                holder.checkedState.toggle();
                AreaDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                if (holder.checkedState.isChecked()) {
                    areaNum++;
                } else {
                    areaNum--;
                }
                //TextView显示
                selectAreaNumber.setText("(" + areaNum + ")");
            }
        });
        bookSelectAll.setOnClickListener(onClickListener);
        bookReverse.setOnClickListener(onClickListener);
        bookSelectCancel.setOnClickListener(onClickListener);
        areaSelectAll.setOnClickListener(onClickListener);
        areaReverse.setOnClickListener(onClickListener);
        areaSelectCancel.setOnClickListener(onClickListener);
        downLoadBtn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.book_select_all:
                    bookSelectAll();
                    break;
                case R.id.book_reverse:
                    bookReverse();
                    break;
                case R.id.book_select_cancel:
                    bookSelectCancle();
                    break;
                case R.id.area_select_all:
                    areaSelectAll();
                    break;
                case R.id.area_reverse:
                    areaReverse();
                    break;
                case R.id.area_select_cancel:
                    areaSelectCancle();
                    break;
                case R.id.downLoad_btn:
                    saveBookInfo();
                    saveAreaInfo();
                    if(!"".equals(begianNum.getText().toString().trim()) && !"".equals(endNum.getText().toString().trim())){ //首先判断两个编号都不为空
                        if(bookIDMap.size() != 0 && areaIDMap.size() != 0){ //两个编号不为空，并且抄表本、抄表分区都已经选择
                            //此时会下载三个部分的数据

                        }else {  //要么抄表本、抄表分区其中一个已经选择，要么两个都没选择
                            if(bookIDMap.size() == 0 && areaIDMap.size() == 0){//两个编号不为空，但是抄表本、抄表分区都未选择
                                //此时只会下载 编号范围 部分的数据
                            }else if(bookIDMap.size() == 0){ //两个编号不为空，抄表分区已经选择，但是抄表本未选择
                                //此时会下载 编号范围、抄表分区 两个部分的数据
                            }else if(areaIDMap.size() == 0){ //两个编号不为空，抄表本已经选择，但是抄表分区未选择
                                //此时会下载 编号范围、抄表本 两个部分的数据
                            }
                        }
                    }else {  //要么两个编号都为空，要么其中一个为空
                        if("".equals(begianNum.getText().toString().trim()) && "".equals(endNum.getText().toString().trim())){ //两个编号都为空
                            if(bookIDMap.size() != 0 && areaIDMap.size() != 0){  //抄表本、抄表分区都已经选择
                                //此时会下载 抄表本、抄表分区 两个部分的数据
                            }else {
                                if(bookIDMap.size() == 0 && areaIDMap.size() == 0){ //两个编号都为空，并且抄表本、抄表分区都未选择
                                    //此时提示选择其中之一
                                    Toast.makeText(MeterDataDownloadActivity.this, "至少选择其中一个区域才能下载哦", Toast.LENGTH_SHORT).show();
                                }else if(bookIDMap.size() == 0){ //两个编号都为空，抄表分区已经选择，但是抄表本未选择
                                    //此时只会下载 抄表分区 部分的数据
                                }else if(areaIDMap.size() == 0){ //两个编号都为空，抄表本已经选择，但是抄表分区未选择
                                    //此时只会下载 抄表本 部分的数据
                                }
                                if(bookIDMap.size() == 0 && areaIDMap.size() == 0){ //两个编号都为空，并且抄表本、抄表分区都未选择
                                    Toast.makeText(MeterDataDownloadActivity.this, "至少选择其中一个区域才能下载哦", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else if("".equals(begianNum.getText().toString().trim())){ //开始编号为空
                            Toast.makeText(MeterDataDownloadActivity.this, "请您将区间填写完整！", Toast.LENGTH_SHORT).show();
                        }else if("".equals(endNum.getText().toString().trim())){ //结束编号为空
                            Toast.makeText(MeterDataDownloadActivity.this, "请您将区间填写完整！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 刷新抄表本listview和TextView的显示
    private void bookDataChanged() {
        // 通知listView刷新
        bookAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectBookNumber.setText("(" + bookNum + ")");
    }

    // 刷新抄表分区listview和TextView的显示
    private void areaDataChanged() {
        // 通知listView刷新
        areaAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectAreaNumber.setText("(" + areaNum + ")");
    }

    //抄表本全选
    public void bookSelectAll() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            BookDataAdapter.getIsCheck().put(i, true);
        }
        bookNum = bookInfoList.size();
        bookDataChanged();
    }

    //抄表本反选
    public void bookReverse() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--;//数量减一
            } else {
                BookDataAdapter.getIsCheck().put(i, true);
                bookNum++;//数量加一
            }
        }
        bookDataChanged();
    }

    //抄表本取消选择
    public void bookSelectCancle() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--; //数量减一
            }
        }
        bookDataChanged();
    }

    //抄表分区全选
    public void areaSelectAll() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            AreaDataAdapter.getIsCheck().put(i, true);
        }
        areaNum = areaInfoList.size();
        areaDataChanged();
    }

    //抄表分区反选
    public void areaReverse() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            } else {
                AreaDataAdapter.getIsCheck().put(i, true);
                areaNum++;
            }
        }
        areaDataChanged();
    }

    //抄表分区取消选择
    public void areaSelectCancle() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            }
        }
        areaDataChanged();
    }

    //保存选中的抄表本ID信息
    public void saveBookInfo() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookInfo bookInfo = bookInfoList.get((int) bookAdapter.getItemId(i));
                bookIDMap.put("bookID" + i, Integer.parseInt(bookInfo.getID()));
                Log.i("bookID=========>", "这次被勾选第" + i + "个，抄表本ID为：" + bookInfo.getID());
            }
        }
    }

    //保存选中的抄表分区ID信息
    public void saveAreaInfo() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                AreaInfo areaInfo = areaInfoList.get((int) areaAdapter.getItemId(i));
                areaIDMap.put("areaID" + i, Integer.parseInt(areaInfo.getID()));
                Log.i("areaID=========>", "这次被勾选第" + i + "个，抄表分区ID为：" + areaInfo.getID());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
