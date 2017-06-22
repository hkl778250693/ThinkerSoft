package com.example.administrator.thinker_soft.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.database.MyDBOpenHelper;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.PropertyInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.ProportionInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.BookDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;

import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi") public class MeterDataDownloadActivity extends Activity {
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
	private MyHandler dataHandler;
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
				meterBookList = meterData.getStringArrayList("meterBookList");
				meterAreaList = meterData.getStringArrayList("meterAreaList");
				//初始化抄表本listview
				if (meterBookList != null) {
					for(int i = 0;i<meterBookList.size();i++){
						BookInfo item = new BookInfo();
						item.setBOOK(meterBookList.get(i));
						bookInfoList.add(item);
					}
				}
				bookAdapter = new BookDataAdapter(MeterDataDownloadActivity.this,bookInfoList);
				booklistView.setAdapter(bookAdapter);
				//初始化抄表分区listview
				if (meterAreaList != null) {
					for(int i = 0;i<meterAreaList.size();i++){
						AreaInfo item = new AreaInfo();
						item.setArea(meterAreaList.get(i));
						areaInfoList.add(item);
                    }
				}
				areaAdapter = new AreaDataAdapter(MeterDataDownloadActivity.this,areaInfoList);
				arealistView.setAdapter(areaAdapter);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==resultCode){
			tempDBName = data.getAction();
			if("".equals(tempDBName)||tempDBName==null){
				Toast.makeText(this, "�ļ�����Ч", Toast.LENGTH_SHORT).show();
				return;
			}
			if(tempDBName.contains("/")){
				Toast.makeText(this, "������Ч�ַ�", Toast.LENGTH_SHORT).show();
				return;
			}
			tempDBName = tempDBName.trim();
			File file = new File(filepath+tempDBName);
			if(file.exists()){
				Toast.makeText(this, "�ļ��Ѵ���", Toast.LENGTH_SHORT).show();
				return;
			}else{
				if(!JaugeInternetState.isWifiEnabled(getApplicationContext())){
					Toast.makeText(this, "�����������WIFI", Toast.LENGTH_SHORT).show();
					return;
				}
				clickCount ++;
				dialogControl.put(clickCount, true);
				DownDialog = MyDialog.createLoadingDialog(MeterDataDownloadActivity.this, "�������� ���Ժ�");
				DownDialog.setCancelable(true);
				DownDialog.setCanceledOnTouchOutside(false);
				DownDialog.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
							File file = new File(filepath+tempDBName);
							if(file.exists()){
								file.delete();
							}
							if(file.getName().contains("journal"))
								file.deleteOnExit();
						}
						return false;
					}
				});
				DownDialog.setOnDismissListener(new OnDismissListener() {			
					@Override
					public void onDismiss(DialogInterface dialog) {
						dialogControl.put(clickCount, false);
					}
				});
				DownDialog.show();
				createDataBase(tempDBName);
				downLoadData();
			}
		}
	}

	private void createDataBase(String DBName) {
			MyDBOpenHelper myDB = new MyDBOpenHelper(this, DBName, null, Integer.parseInt(version));
			myDB.getWritableDatabase();
			myDB.close();
	}	
	private void downLoadData() {
		dataHandler  = new MyHandler();
		Thread thread = new Thread(){
			public void run() {
				int loc = clickCount;
				boolean signal = false;
				String parameter = AssembleUpmes.downLoadParameter(areaID, bookID,begianNum.getText().toString(), endNum.getText().toString());
				SocketInteraction userSocket= new SocketInteraction(getApplicationContext(),Integer.parseInt(defaultPort),ip,operName,parameter,dataHandler);
				signal = userSocket.DataDownLoadConn();
				userSocket.closeConn();
				if(dialogControl.size()>0){
					Log.v("ZHOUTAO","��ǰ�߳���ţ�"+loc);
					if(dialogControl.get(loc)==true){
						if(signal==false){
								dataHandler.post(new Runnable() {
									@SuppressLint("NewApi") @Override
									public void run() {
										// TODO Auto-generated method stub
										AlertDialog.Builder builder = null;
										builder = new AlertDialog.Builder(MeterDataDownloadActivity.this);
										builder.setMessage("��������ʧ��");
										builder.show();
										File file = new File(filepath+tempDBName);
										if(file.exists()){
											file.delete();
										}
										if(file.getName().contains("journal"))
											file.deleteOnExit();
										DownDialog.dismiss();
									}
								});
						}
					}
				}
			};
		};
		thread.start();
	}
	class AreaLVClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position,
				long arg3) {
			for(int i =0;i<arearecord.size();i++){
				if(arearecord.get(i) == position){
					return;
				}
			}
			arearecord.add(position);
			if(areatvcount==0){
				areahrilayout = new LinearLayout(MeterDataDownloadActivity.this);
				areahrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				areahrilayout.setOrientation(LinearLayout.HORIZONTAL);
				area_scroll_ll.addView(areahrilayout);
			}
			
			if(areatvcount!=0&&areatvcount%3==0){
				areahrilayout = new LinearLayout(MeterDataDownloadActivity.this);
				areahrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				areahrilayout.setOrientation(LinearLayout.HORIZONTAL);
				area_scroll_ll.addView(areahrilayout);
			}		
			arealayout = new LinearLayout(MeterDataDownloadActivity.this);
			arealayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
			TextView tv = (TextView) view.findViewById(R.id.textViewchaobiao);
			final TextView textView = new TextView(MeterDataDownloadActivity.this);
			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			textView.setText(tv.getText().toString());
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.mipmap.book_area_background);
			areatvcount++;
			arealayout.addView(textView);
			areahrilayout.addView(arealayout);
			areaID.add(areaList.get(position).getID());
			areaName.add(areaList.get(position).getArea());
			arealayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					areatvcount--;
					//ɾ������
					for(int i = 0;i<areaName.size();i++){
						String str = areaName.get(i);
						String edstr = ((TextView)((LinearLayout) v).getChildAt(0)).getText().toString();
						//edstr = edstr.substring(0, 1);
						if(edstr.equals(str)){
							areaName.remove(i);
							areaID.remove(i);
							break;
						}
					}
					//ɾ���ظ��б�
					for(int i = 0;i<arearecord.size();i++){
						int in = arearecord.get(i);
						if(in==position){
							arearecord.remove(i);
							break;
						}
					}						
					//ɾ�����
					int row = area_scroll_ll.getChildCount();	
					for(int x=0;x<row;x++){
						LinearLayout linearLayoutrow = (LinearLayout) area_scroll_ll.getChildAt(x);
						if(linearLayoutrow==null){
							area_scroll_ll.removeViewAt(x);
						}
						int col = linearLayoutrow.getChildCount();
						for(int y=0;y<col;y++){
							LinearLayout linearLayoutcol = (LinearLayout) linearLayoutrow.getChildAt(y);
							if(v.equals(linearLayoutcol)){
								linearLayoutrow.removeViewAt(y);
								return;
							}
						}
					}
				}
			});
		}
		
	}
	class BookLVClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3) {
			for(int i =0;i<bookrecord.size();i++){
				if(bookrecord.get(i) == position){
					return;
				}
			}
			if(booktvcount==0){//�״δ���
				bookhrilayout = new LinearLayout(MeterDataDownloadActivity.this);
				bookhrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				bookhrilayout.setOrientation(LinearLayout.HORIZONTAL);
				book_scroll_ll.addView(bookhrilayout);
			}
			if(booktvcount!=0&&booktvcount%3==0){
				bookhrilayout = new LinearLayout(MeterDataDownloadActivity.this);
				bookhrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				bookhrilayout.setOrientation(LinearLayout.HORIZONTAL);
				book_scroll_ll.addView(bookhrilayout);
			}
			bookrecord.add(position);//��¼����λ��,�ж��Ƿ��ظ�
			booklayout = new LinearLayout(MeterDataDownloadActivity.this);
			booklayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
			TextView tv = (TextView) view.findViewById(R.id.textViewchaobiao);
			final TextView textView = new TextView(MeterDataDownloadActivity.this);
			if(booktvcount==1){
				textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			}
			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			textView.setText(tv.getText().toString());
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.mipmap.book_area_background);
			booktvcount ++;
			booklayout.addView(textView);	
			bookhrilayout.addView(booklayout);
			bookID.add(bookList.get(position).getID());
			bookName.add(bookList.get(position).getBOOK());
			booklayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					booktvcount--;
					//areahrilayout.removeView((LinearLayout)v);
					for(int i = 0;i<bookName.size();i++){
						String str = bookName.get(i);
						String edstr = ((TextView)((LinearLayout) v).getChildAt(0)).getText().toString();
						//edstr = edstr.substring(0, 1);
						if(edstr.equals(str)){
							bookName.remove(i);
							bookID.remove(i);
							break;
						}
					}
					for(int i = 0;i<bookrecord.size();i++){
						int in = bookrecord.get(i);
						if(in==position){
							bookrecord.remove(i);
							break;
						}
					}
					int row = book_scroll_ll.getChildCount();	
					for(int x=0;x<row;x++){
						LinearLayout linearLayoutrow = (LinearLayout) book_scroll_ll.getChildAt(x);
						if(linearLayoutrow==null){
							book_scroll_ll.removeViewAt(x);
						}
						int col = linearLayoutrow.getChildCount();
						for(int y=0;y<col;y++){
							LinearLayout linearLayoutcol = (LinearLayout) linearLayoutrow.getChildAt(y);
							if(v.equals(linearLayoutcol)){
								linearLayoutrow.removeViewAt(y);
								return;
							}
						}
					}
				}
			});
		}
	}

    class MyHandler extends Handler {
    	/**
    	 * ��ȡsocket�߳�����
    	 */
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		int loc = clickCount;//��ǰ�̵߳����
    		Bundle bundle = msg.getData();
    		String data = bundle.getString("data");
    		String temp[] = data.split("��");
			if(dialogControl.size()>0){
				Log.v("ZHOUTAO","��ǰ�߳���ţ�"+loc);
				if(dialogControl.get(loc)==true){
		    		if(!"û������".equals(data)){
			    		for(int i=0;i<temp.length;i++){
							if(temp[i].contains("UserData")){
								try {
									usList = JsonAnalyze.analyszeUsersInfo(temp[i]);
								} catch (JSONException e) {
									DownDialog.dismiss();
									AlertDialog.Builder builder = null;
									builder = new AlertDialog.Builder(MeterDataDownloadActivity.this);
									builder.setMessage("���ݸ�ʽ���⣬�����Ի���ϵ������Ա��");
									builder.show();
									DownDialog.dismiss();
								}
								dbservice = new DBService(filepath+tempDBName);
								dbservice.insertUserData(usList);
								dbservice.connclose();
							}else if(temp[i].contains("UserProperties")){
								propertyList = JsonAnalyze.analyszeProperties(temp[i]);
								dbservice = new DBService(filepath+tempDBName);
								dbservice.insertPropertyData(propertyList);
								dbservice.connclose();
							}else if(temp[i].contains("BXData")){
								proportionList = JsonAnalyze.analyszeProportion(temp[i]);
								if(proportionList!=null&&proportionList.size()>0){
									dbservice = new DBService(filepath+tempDBName);
									dbservice.insertProportionData(proportionList);
									dbservice.connclose();
								}
							}											
			    		}
		    		}
		    		if(usList==null||usList.size()<0){
		    			File file = new File(filepath+tempDBName);
		    			if(file.exists()){
		    				file.delete();
		    			}
		    			AlertDialog.Builder builder = null;
		    			builder = new AlertDialog.Builder(MeterDataDownloadActivity.this);
		    			builder.setMessage("û�в�ѯ������û����ݣ�");
		    			builder.show();
		    			DownDialog.dismiss();
		    		}
					if(propertyList!=null&&usList!=null){
						DownDialog.dismiss();
						File file = new File(filepath);
						String[] files = file.list();
						for(String fi:files){
							if(fi.contains("journal")){
								File filetemp = new File(filepath+fi);
								filetemp.delete();
							}	
						}
						AlertDialog.Builder builder = null;
						builder = new AlertDialog.Builder(MeterDataDownloadActivity.this);
						builder.setMessage("�������سɹ�");
						builder.show();
					}
				}
			}
		}
    }	
}
