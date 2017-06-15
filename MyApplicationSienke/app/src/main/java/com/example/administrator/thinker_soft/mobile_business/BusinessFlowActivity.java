package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessFlowActivity extends Activity {

    private ImageView back;
    private ListView listView;
    private BusinessFlowAdapter adapter;
    private List<BusinessFlowListviewItem> businessFlowListviewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flow);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            BusinessFlowListviewItem item = new BusinessFlowListviewItem();
            item.setPic(R.mipmap.approval);
            businessFlowListviewItemList.add(item);
        }
    }

    public void setOnClickListener() {
        getData();
        adapter = new BusinessFlowAdapter(BusinessFlowActivity.this, businessFlowListviewItemList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BusinessFlowActivity.this,BusinessFlowInfoActivity.class);
                startActivity(intent);
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
            }
        }
    };
}
