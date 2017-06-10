package mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessEmailInfoActivity extends Activity {

    private ListView listViewEmail;
    private ImageView back;
    private BusinessEmailListviewItem item;
    private List<BusinessEmailListviewItem> businessEmailListviewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email_info);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        listViewEmail = (ListView) findViewById(R.id.listview_email);
        back = (ImageView) findViewById(R.id.back);
    }

    public void setOnClickListener(){
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
