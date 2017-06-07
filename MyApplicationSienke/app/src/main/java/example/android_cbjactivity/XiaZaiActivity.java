package example.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.thksoft.myfirstpro.database.MyDBOpenHelper;
import cc.thksoft.myfirstpro.entity.AreaInfo;
import cc.thksoft.myfirstpro.entity.BookInfo;
import cc.thksoft.myfirstpro.entity.PropertyInfo;
import cc.thksoft.myfirstpro.entity.ProportionInfo;
import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.lvadapter.AreaDataAdapter;
import cc.thksoft.myfirstpro.lvadapter.BookDataAdapter;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import cc.thksoft.myfirstpro.threadsocket.SocketInteraction;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.JaugeInternetState;
import cc.thksoft.myfirstpro.util.JsonAnalyze;
import cc.thksoft.myfirstpro.util.MyDialog;

@SuppressLint("NewApi") public class XiaZaiActivity extends Activity {
	
	private LinearLayout mImageView;
	private ListView arealistView;
	private ListView booklistView;	
	private AreaDataAdapter areaAdapter;
	private BookDataAdapter bookAdapter;
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
	private TextView booktv;
	private TextView areatv;
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
	//.............................
	//����ΪSoecket��������
	private String ip;
	private String defaultPort;
	private String operName = "NANBU";
	private MyHandler dataHandler;
	//�����ݿ��������
	private DBService dbservice;
	private String dbPath;
	//.............................
	//����Ϊ���ݿ����
	private String version = "1";
	private String tempDBName;
	private String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	
	private Map<Integer,Boolean> dialogControl;
	private int clickCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaozai);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		mImageView=(LinearLayout) findViewById(R.id.back_Button);
		arealistView = (ListView)findViewById(R.id.lv_data_area);
		booklistView = (ListView)findViewById(R.id.lv_data_book);
		bookID = new ArrayList<String>();
		areaID = new ArrayList<String>();
		areaName = new ArrayList<String>();
		bookName = new ArrayList<String>();
		bookrecord = new ArrayList<Integer>();
		arearecord = new ArrayList<Integer>();
		booktv = (TextView) findViewById(R.id.add_book);
		areatv = (TextView) findViewById(R.id.add_area);
		book_scroll_ll = (LinearLayout) findViewById(R.id.book_scroll_ll);
		area_scroll_ll = (LinearLayout) findViewById(R.id.area_scroll_ll);
		begianNum = (EditText) findViewById(R.id.begain_num);
		endNum = (EditText) findViewById(R.id.end_num);
		
		downLoadbtn = (Button) findViewById(R.id.downLoadbtn);
		
		SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		ip = sharedPreferences.getString("ip", "");
		defaultPort = sharedPreferences.getString("port", "");
		
		
		if("".equals(ip)||ip==null){
			Toast.makeText(XiaZaiActivity.this, "������IP", Toast.LENGTH_LONG).show();				
		}else if("".equals(defaultPort)||defaultPort==null){
			Toast.makeText(XiaZaiActivity.this, "�����ö˿�", Toast.LENGTH_LONG).show();
			finish();		
		}else{
			Intent intent = getIntent();
			bookList = (ArrayList<BookInfo>)intent.getSerializableExtra("bookList");
			areaList = (ArrayList<AreaInfo>)intent.getSerializableExtra("areaList");
			if(bookList!=null){
				bookAdapter = new BookDataAdapter(this,bookList);
				booklistView.setAdapter(bookAdapter);
				booklistView.setOnItemClickListener(new BookLVClickListener());
			}
			if(areaList!=null){
				areaAdapter = new AreaDataAdapter(this,areaList);
				arealistView.setAdapter(areaAdapter);
				arealistView.setOnItemClickListener(new AreaLVClickListener());
			}
		}
		dialogControl = new HashMap<Integer, Boolean>();
	}
/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(DownDialog!=null&&DownDialog.isShowing()){

			}
		}
		return super.onKeyDown(keyCode, event);
	}*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	public void onAction(View v) {
		int id=v.getId();
		
		switch (id) {
		case R.id.downLoadbtn:
			if("".equals(begianNum.getText().toString())&&!"".equals(endNum.getText().toString())){
				Toast.makeText(this, "����ȷ��д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
				begianNum.setFocusable(true);
			}else if(!"".equals(begianNum.getText().toString())&&"".equals(endNum.getText().toString())){
				Toast.makeText(this, "����ȷ��д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
				endNum.setFocusable(true);
			}else{
				if((areaName!=null&&areaName.size()>0)||(bookName!=null&&bookName.size()>0)){
					
					Intent intent = new Intent(XiaZaiActivity.this,dialog_loadname_activity.class);
					if(areaName!=null&&areaName.size()>0){			
						intent.putExtra("areaName", (Serializable)areaName); 
					}
					if(bookName!=null&&bookName.size()>0){
						intent.putExtra("bookName", (Serializable)bookName);   
					}
					int requestCode = 1;
					startActivityForResult(intent, requestCode);
				}else{
					Toast.makeText(this, "��ѡ����������", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.back_Button:
			finish();
		default:
			break;
		}
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
				DownDialog = MyDialog.createLoadingDialog(XiaZaiActivity.this, "�������� ���Ժ�");
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
				//�������ݿ�
				createDataBase(tempDBName);
				//��������
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
										builder = new AlertDialog.Builder(XiaZaiActivity.this);
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
			if(areatvcount==0){//�״δ���
				areahrilayout = new LinearLayout(XiaZaiActivity.this);
				areahrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				areahrilayout.setOrientation(LinearLayout.HORIZONTAL);
				area_scroll_ll.addView(areahrilayout);
			}
			
			if(areatvcount!=0&&areatvcount%3==0){
				areahrilayout = new LinearLayout(XiaZaiActivity.this);
				areahrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				areahrilayout.setOrientation(LinearLayout.HORIZONTAL);
				area_scroll_ll.addView(areahrilayout);
			}		
			arealayout = new LinearLayout(XiaZaiActivity.this);
			arealayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
			TextView tv = (TextView) view.findViewById(R.id.textViewchaobiao);
			final TextView textView = new TextView(XiaZaiActivity.this);
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
		public void onItemClick(AdapterView<?> arg0, View view, final int position,
				long arg3) {
			
			for(int i =0;i<bookrecord.size();i++){
				if(bookrecord.get(i) == position){
					return;
				}
			}
			if(booktvcount==0){//�״δ���
				bookhrilayout = new LinearLayout(XiaZaiActivity.this);
				bookhrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				bookhrilayout.setOrientation(LinearLayout.HORIZONTAL);
				book_scroll_ll.addView(bookhrilayout);
			}
			if(booktvcount!=0&&booktvcount%3==0){
				bookhrilayout = new LinearLayout(XiaZaiActivity.this);
				bookhrilayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				bookhrilayout.setOrientation(LinearLayout.HORIZONTAL);
				book_scroll_ll.addView(bookhrilayout);
			}
			bookrecord.add(position);//��¼����λ��,�ж��Ƿ��ظ�
			booklayout = new LinearLayout(XiaZaiActivity.this);
			booklayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
			TextView tv = (TextView) view.findViewById(R.id.textViewchaobiao);
			final TextView textView = new TextView(XiaZaiActivity.this);
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
									builder = new AlertDialog.Builder(XiaZaiActivity.this);
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
		    			builder = new AlertDialog.Builder(XiaZaiActivity.this);
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
						builder = new AlertDialog.Builder(XiaZaiActivity.this);
						builder.setMessage("�������سɹ�");
						builder.show();
					}
				}
			}
		}
    }	
}
