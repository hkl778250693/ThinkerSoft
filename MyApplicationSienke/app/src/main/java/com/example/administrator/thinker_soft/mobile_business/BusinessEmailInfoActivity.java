package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.EmailInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessEmailInfoActivity extends Activity {

    private ListView listViewEmail;
    private LinearLayout checked;
    private ImageView back;
    private List<BusinessEmailListviewItem> businessEmailListviewItemList = new ArrayList<>();
    private TextView check;
    private Button weidu,yidu,delete,cancel;
    private EmailInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email_info);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        listViewEmail = (ListView) findViewById(R.id.listview_email);
        check = (TextView) findViewById(R.id.check);
        weidu = (Button) findViewById(R.id.weidu);
        delete = (Button) findViewById(R.id.delete);
        cancel = (Button) findViewById(R.id.cancel);
        back = (ImageView) findViewById(R.id.back);
        checked = (LinearLayout) findViewById(R.id.checked);

    }

    //假数据
    public void getData(){
        for(int i=0;i<10;i++){
            BusinessEmailListviewItem item = new BusinessEmailListviewItem();
            item.setEmailAdress("thinkersoft@163.com"+i);
            businessEmailListviewItemList.add(item);
        }
    }

    public void setOnClickListener(){
        getData();
        adapter= new EmailInfoAdapter(BusinessEmailInfoActivity.this,businessEmailListviewItemList);
        listViewEmail.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        listViewEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };
}
