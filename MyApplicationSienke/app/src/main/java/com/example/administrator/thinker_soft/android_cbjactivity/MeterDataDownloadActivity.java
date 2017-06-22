package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.PropertyInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.ProportionInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.BookDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeterDataDownloadActivity extends Activity {
	private ImageView back;
	private ListView booklistView;
	private ListView arealistView;
	private BookDataAdapter bookAdapter;
	private AreaDataAdapter areaAdapter;
	private List<BookInfo>  bookList;
	private List<AreaInfo> areaList;
	private List<PropertyInfo> propertyList;
	private List<ProportionInfo> proportionList;
	private List<UsersInfo> usList;
	private List<String> areaID;
	private List<String> bookID;
	private List<String> areaName;
	private List<String> bookName;
	private List<Integer> bookrecord;
	private List<Integer> arearecord;
/*	private TextView booktv ;
	private TextView areatv ;*/
	private LinearLayout areahrilayout;
	private LinearLayout arealayout;//�������
	private LinearLayout bookhrilayout;
	private LinearLayout booklayout;//�������	
	private LinearLayout book_scroll_ll;
	private LinearLayout area_scroll_ll;
	private EditText begianNum;
	private EditText endNum;
	private Dialog DownDialog;
	private Button downLoadbtn;
	private int areatvcount = 0;
	private int booktvcount = 0;
	private boolean booksignal = true;
	private boolean areasignal = true;
	int requestCode;
	private String ip;
	private String defaultPort;
	private String operName = "NANBU";
	private DBService dbservice;
	private String dbPath;
	private String version = "1";
	private String tempDBName;
	private String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	private Map<Integer,Boolean> dialogControl;
	private int clickCount;
	private ArrayList<String> meterBookList = new ArrayList<>();   //抄表本名称集合
	private ArrayList<String> meterAreaList = new ArrayList<>();   //抄表分区名称集合
	private List<BookInfo> bookInfoList = new ArrayList<>();      //抄表本集合
	private List<AreaInfo> areaInfoList = new ArrayList<>();   //抄表分区集合

	
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
		back=(ImageView) findViewById(R.id.back);
		booklistView = (ListView)findViewById(R.id.meter_book_lv);
		arealistView = (ListView)findViewById(R.id.meter_area_lv);
		begianNum = (EditText) findViewById(R.id.begain_num);
		endNum = (EditText) findViewById(R.id.end_num);
		downLoadbtn = (Button) findViewById(R.id.downLoadbtn);
	}

	//初始化设置
	private void defaultSetting() {
		Intent intent = getIntent();
		if(intent != null){
			Bundle meterData = intent.getExtras();
			if(meterData != null){
				Log.i("MeterDataDownload","数据接收成功");
				meterBookList = meterData.getStringArrayList("meterBookList");
				meterAreaList = meterData.getStringArrayList("meterAreaList");
				//初始化抄表本listview
				if (meterBookList != null) {
					for(int i = 0;i<meterBookList.size();i++){
						BookInfo item = new BookInfo();
						item.setBOOK(meterBookList.get(i));
						bookInfoList.add(item);
					}
					bookAdapter = new BookDataAdapter(MeterDataDownloadActivity.this,bookInfoList);
					booklistView.setAdapter(bookAdapter);
				}
				//初始化抄表分区listview
				if (meterAreaList != null) {
					for(int i = 0;i<meterAreaList.size();i++){
						AreaInfo item = new AreaInfo();
						item.setArea(meterAreaList.get(i));
						areaInfoList.add(item);
                    }
					areaAdapter = new AreaDataAdapter(MeterDataDownloadActivity.this,areaInfoList);
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

			}
		});
		arealistView.setOnItemClickListener(new OnItemClickListener() {   //抄表分区item点击事件
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	View.OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.back:
					finish();
					break;
				case R.id.downLoadbtn:
					if("".equals(begianNum.getText().toString())&&!"".equals(endNum.getText().toString())){
						Toast.makeText(MeterDataDownloadActivity.this, "����ȷ��д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
						begianNum.setFocusable(true);
					}else if(!"".equals(begianNum.getText().toString())&&"".equals(endNum.getText().toString())){
						Toast.makeText(MeterDataDownloadActivity.this, "����ȷ��д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
						endNum.setFocusable(true);
					}else{
						if((areaName!=null&&areaName.size()>0)||(bookName!=null&&bookName.size()>0)){
							Intent intent = new Intent(MeterDataDownloadActivity.this,dialog_loadname_activity.class);
							if(areaName!=null&&areaName.size()>0){
								intent.putExtra("areaName", (Serializable)areaName);
							}
							if(bookName!=null&&bookName.size()>0){
								intent.putExtra("bookName", (Serializable)bookName);
							}
							int requestCode = 1;
							startActivityForResult(intent, requestCode);
						}else{
							Toast.makeText(MeterDataDownloadActivity.this, "��ѡ����������", Toast.LENGTH_LONG).show();
						}
					}
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
