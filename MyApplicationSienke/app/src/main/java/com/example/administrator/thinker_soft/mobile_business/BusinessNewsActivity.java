package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessNewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessNewsActivity extends Activity {

    private ImageView back;
    private ListView listView;
    private List<BusinessNewsItem> businessNewsItemList = new ArrayList<>();
    private BusinessNewsAdapter adapter;
    private BusinessNewsItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_news);//通知公告

        bindView();
        setViewClickListener();
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            item = new BusinessNewsItem();
            item.setName("张黎明");
            businessNewsItemList.add(item);
        }
    }

    public void setViewClickListener() {
        getData();
        adapter = new BusinessNewsAdapter(BusinessNewsActivity.this, businessNewsItemList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
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
