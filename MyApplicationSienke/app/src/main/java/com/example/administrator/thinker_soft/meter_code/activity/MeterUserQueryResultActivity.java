package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListviewAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/5 0005.
 */
public class MeterUserQueryResultActivity extends Activity {
    private ImageView back;
    private ListView listView;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private MeterUserListviewAdapter adapter;
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_query_result);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    //初始化设置
    private void defaultSetting() {
        Intent intent = getIntent();
        if(intent != null){
            userLists = intent.getParcelableArrayListExtra("meter_user_info");
            adapter = new MeterUserListviewAdapter(MeterUserQueryResultActivity.this, userLists);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                item = (MeterUserListviewItem) adapter.getItem(position);
                Intent intent = new Intent(MeterUserQueryResultActivity.this, MeterUserDetailActivity.class);
                intent.putExtra("user_id",item.getUserID());
                startActivityForResult(intent,currentPosition);
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserQueryResultActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == currentPosition){
                if(data != null){
                    item.setThisMonth(data.getStringExtra("this_month_dosage"));
                    item.setIfEdit(R.mipmap.userlist_gray);
                    item.setMeterState("已抄");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
