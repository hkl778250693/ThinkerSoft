package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.CommunityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessCommunityActivity extends Activity {
    private ListView listview;
    private ImageView back;
    private TextView search;
    private List<CommunityListViewItem> communityListViewItemList = new ArrayList<>();
    private CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_community);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listview = (ListView) findViewById(R.id.listview);
        search = (TextView) findViewById(R.id.search);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            CommunityListViewItem item = new CommunityListViewItem();
            item.setPic(R.mipmap.eg);
            item.setTitle("爱迪生积分更好的");
            item.setNeirong("动画风格家公司电话防火阀");
            item.setTime("2017/06/23");
            item.setPingjia("0评");
            item.setHuifu("5回");
            item.setYuedu("34阅");
            communityListViewItemList.add(item);
        }
    }

    public void setOnClickListener() {
        getData();
        adapter = new CommunityAdapter(BusinessCommunityActivity.this, communityListViewItemList);
        listview.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        search.setOnClickListener(clickListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
