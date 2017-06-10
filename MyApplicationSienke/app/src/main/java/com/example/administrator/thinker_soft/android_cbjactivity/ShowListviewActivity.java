package com.example.administrator.thinker_soft.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.paging.noMeterPageDataBuffer;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshBase;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.niftydialogeffects.NiftyDialogBuilder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshListView;
import com.example.administrator.thinker_soft.niftydialogeffects.Effectstype;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;


@SuppressLint("NewApi")
public class ShowListviewActivity extends Activity{
	//private ListView listView;
	private RefreshPullToRefreshListView mylistView;
	private List<UsersInfo> totalinfos;
	private List<UsersInfo> pageinfos;
	private Map<Integer,String> mMapContent;
	private LinearLayout layout;//����
	private EditText editText;//�����
	private TextView textView;//���
	//���ݿⷽ��
	private DBService dbService;
	private String dbName;
	private String filepath;
	//��ҳ
	private LinearLayout page_show_ll;
	private TextView up;
	private TextView show;
	private TextView down;
	private LinearLayout control_page_show;
	private int currentdatanum;//������ʾ����
	private int currentdatacount;//����������
	private int pagenum=0;//��ǰҳ  Ĭ�ϵ�һҳ
	private int pagecount;//��ҳ��
	//����
	private LayoutInflater layoutInflater;
	private View progressBar;
	private TextView tv_show;
	private static boolean isScrollLast;
	private MyHandler handler;
	private int UP_LOAD_SUCCESS = 1;
	private int DOWN_LOAD_SUCCESS = 2;
	//����
	private int indexpos;//�������λ��
	private int indexsign = -2;//�����ʾλ��
	private int indexmsg;//����������ʾλ��
	private int indexupdate;//��������λ��
	private int indexitem = -2;//�б�item���λ�ü�¼
	private LinearLayout currentItem;
	private StringBuffer result;
	private int location;//�����EditText��λ��
	NormalShouYeAdapter adapter;
	
	private static final int REQUESTCODE = 10;
	
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;	
	private String LONGITUDE;
	private String LATIUDE;//γ��
	private String sb;
	//����
	private int signal;//��ҳ���ܱ�־
	private int con_signal;//���������־
	private String bookName;
	private int con_position;//��������λ��
	private String usName;
	private String usId;
	private String meterId;
	private NiftyDialogBuilder dialogBuilder;
	private noMeterPageDataBuffer pageDataBuffer;
	private List<UsersInfo> tempList;
	private int continue_person;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shouyechaobiaos);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		handler = new MyHandler();
		mylistView = (RefreshPullToRefreshListView)findViewById(R.id.list_shouye);
		mylistView.setMode(RefreshPullToRefreshBase.Mode.BOTH);// ����ˢ��ģʽ ���� ���� Ĭ������
		mylistView.setOnRefreshListener(onRefreshListener2);
		mMapContent = new HashMap<Integer, String>();
		layout = (LinearLayout) findViewById(R.id.ll_Home_btn);
		page_show_ll = (LinearLayout) findViewById(R.id.page_show_ll);
		up = (TextView) findViewById(R.id.page_up);
		show = (TextView) findViewById(R.id.page_show);
		down = (TextView) findViewById(R.id.page_down);
		control_page_show = (LinearLayout) findViewById(R.id.control_page_show);	
		//������
		result = new StringBuffer("");

		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		dbName = sharedPreferences.getString("dbName", "");
		Log.v("ZT", "���ݿ�����"+dbName);
		signal = sharedPreferences.getInt("signal", 0);
		con_signal = sharedPreferences.getInt("con_signal", 0);
		bookName = sharedPreferences.getString("bookName", "");
		con_position = sharedPreferences.getInt("con_position", 0);
		usName = sharedPreferences.getString("usName", "");
		usId = sharedPreferences.getString("usId", "");
		meterId = sharedPreferences.getString("meterId", "");
		
		filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		Log.v("ZT", "�ļ�·����"+filepath);
		File file = new File(filepath+dbName);
		Log.v("ZT", "�ļ���С��"+file.length());
		currentdatanum = sharedPreferences.getInt("number", 0);
		if(currentdatanum==0){
			currentdatanum = 50;//Ĭ����ʾ50������
		}	
		if(initdata()==false){
			Toast.makeText(ShowListviewActivity.this, "û�г����¼", Toast.LENGTH_SHORT).show();
			finish();
		};

		if(pageinfos==null||pageinfos.size()<0){
			return;
		}
		adapter = new NormalShouYeAdapter(ShowListviewActivity.this, pageinfos);
		layoutInflater = getLayoutInflater();
		
		mylistView.setVerticalScrollBarEnabled(true);
		mylistView.setAdapter(adapter);
		if(continue_person!=0){
			mylistView.getRefreshableView().setSelection(con_position%currentdatanum);
		}
		control_page_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(page_show_ll.getVisibility()==View.GONE){
					page_show_ll.setVisibility(View.VISIBLE);
				}else{
					page_show_ll.setVisibility(View.GONE);
				}
			}
		});

		up.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pagenum>0){
					pagenum--;
					System.out.println("ҳ��"+pagenum);
					//pageUtil = new Pagination(totalinfos, pagenum,currentdatanum,getApplicationContext());
					//pageinfos = pageUtil.getPageDatas();
					System.out.println("ǰ���֣�"+pageinfos.get(0).getUSNAME());
					dealdata();
					System.out.println("�����֣�"+pageinfos.get(0).getUSNAME());
					adapter.updateAdapterData(pageinfos);
					show.setText(pagenum+1+"/"+pagecount);
					mylistView.getRefreshableView().setSelection(currentdatanum);
					indexsign=-2;
					location = 0;
					layout.setVisibility(LinearLayout.GONE);	
				}
				//down up show  currentdatanum currentdatacount pagenum
			}
		});
		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pagenum<pagecount-1){
					pagenum++;
					System.out.println("ҳ��"+pagenum);
					//pageUtil = new Pagination(totalinfos, pagenum,currentdatanum,getApplicationContext());
					//pageinfos = pageUtil.getPageDatas();
					System.out.println("ǰ���֣�"+pageinfos.size());
					dealdata();
					System.out.println("�����֣�"+pageinfos.size());
					adapter.updateAdapterData(pageinfos);
					show.setText(pagenum+1+"/"+pagecount);
					mylistView.getRefreshableView().setSelection(1);
					indexsign=-2;
					location = 0;
					layout.setVisibility(LinearLayout.GONE);	
				}
			}
		});

	}
	public void dealdata(){
		if(signal == 1){
			if(con_signal == 0){
				Toast.makeText(ShowListviewActivity.this, "û�г����¼", Toast.LENGTH_SHORT).show();
				return;
			}
			switch (con_signal) {
			case 2:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUserInfobybookname(bookName,currentdatanum,pagenum);
				break;
			case 3:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
				break;
			case 4:
				int meter_sg = sharedPreferences.getInt("meter_sg", 0);
				System.out.println("δ����־��"+meter_sg);
				if(meter_sg == 0){
					Toast.makeText(ShowListviewActivity.this, "û�г����¼", Toast.LENGTH_SHORT).show();
					return;
				}else if(meter_sg == 1){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryNoMeterInfosbyBookNamenoPaging(bookName);
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();	
				}else if(meter_sg == 2){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryAllOfNoMeterInfosnoPaging();
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();
				}
				break;
			case 7:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsName(usName, currentdatanum, pagenum);
				break;
			case 8:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsId(usId, currentdatanum, pagenum);
				break;
			case 9:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyMeterId(meterId, currentdatanum, pagenum);
				break;
			case 10:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInforofminus(currentdatanum, pagenum);
				break;
			default:
				break;
			}
			System.out.println("�����λ�ü�¼��"+con_position%currentdatanum);
			
			pagecount = currentdatacount/currentdatanum;
			if(currentdatacount%currentdatanum>0){
				pagecount = pagecount + 1;
			}
			
			show.setText(pagenum+1+"/"+pagecount);
		}else{
			if(signal == 3){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
			}if(signal == 2){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUserInfobybookname(bookName,currentdatanum, pagenum);
			}if(signal == 4){
				Intent intent = getIntent();
				int meter_sg = intent.getIntExtra("meter_sg", 0);
				if(meter_sg == 0){
					
					return ;
				}else if(meter_sg == 1){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryNoMeterInfosbyBookNamenoPaging(bookName);
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();					
				}else if(meter_sg == 2){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryAllOfNoMeterInfosnoPaging();
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();
				}
			}
			if(signal == 7){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsName(usName, currentdatanum, pagenum);
			}
			if(signal == 8){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsId(usId, currentdatanum, pagenum);
			}
			if(signal == 9){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyMeterId(meterId, currentdatanum, pagenum);
			}
			if(signal == 10){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInforofminus(currentdatanum, pagenum);				
			}
			pagecount = currentdatacount/currentdatanum;
			if(currentdatacount%currentdatanum>0){
				pagecount = pagecount + 1;
			}
			show.setText(pagenum+1+"/"+pagecount);
		}
	}
	public boolean initdata(){
		if(signal == 1){
			pagenum = con_position/currentdatanum;
			if(con_signal == 0){
				Toast.makeText(ShowListviewActivity.this, "û�г����¼", Toast.LENGTH_SHORT).show();
				return false;
			}
			switch (con_signal) {
			case 2:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUserInfobybookname(bookName,currentdatanum,pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryUserInfobybookNameCount(bookName);
				break;
			case 3:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryAllUsersInfoCount();
				break;
			case 4:
				int meter_sg = sharedPreferences.getInt("meter_sg", 0);
				System.out.println("δ����־��"+meter_sg);
				if(meter_sg == 0){
					Toast.makeText(ShowListviewActivity.this, "û�г����¼", Toast.LENGTH_SHORT).show();
					return false;
				}else if(meter_sg == 1){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryNoMeterInfosbyBookNamenoPaging(bookName);
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();	
				}else if(meter_sg == 2){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryAllOfNoMeterInfosnoPaging();
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();
				}
				break;	
			case 7:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsName(usName, currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryUserInfobyUsNameCount(usName);
				break;
			case 8:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsId(usId, currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryUserInfobyUsIdCount(usId);
				break;
			case 9:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyMeterId(meterId, currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount =  dbService.queryUserInfobyMeterIdCount(meterId);
				break;	
			case 10:
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInforofminus(currentdatanum, pagenum);
				System.out.println("pagenum++++++++++++++++++++++++++++++++++++++++++++++++++++++:"+pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount =  dbService.queryUsInforofminusCount();			
				break;
			default:
				break;
			}
			
			continue_person = con_position%currentdatanum;
			pagecount = currentdatacount/currentdatanum;
			if(currentdatacount%currentdatanum>0){
				pagecount = pagecount + 1;
			}
			show.setText(pagenum+1+"/"+pagecount);
		}else{
			if(signal == 3){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
				dbService.connclose();
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryAllUsersInfoCount();
				dbService.connclose();
			}if(signal == 2){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUserInfobybookname(bookName,currentdatanum, pagenum);
				dbService.connclose();
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryUserInfobybookNameCount(bookName);
				dbService.connclose();
			}if(signal == 4){
				Intent intent = getIntent();
				int meter_sg = intent.getIntExtra("meter_sg", 0);
				if(meter_sg == 0){
					
					return false;
				}else if(meter_sg == 1){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryNoMeterInfosbyBookNamenoPaging(bookName);
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();					
				}else if(meter_sg == 2){
					dbService = new DBService(filepath+dbName);
					tempList = dbService.queryAllOfNoMeterInfosnoPaging();
					pageDataBuffer = new noMeterPageDataBuffer(tempList);
					pageinfos = pageDataBuffer.getPageUsersbycount(currentdatanum, pagenum);
					currentdatacount = tempList.size();
				}
			}
			if(signal == 7){
					dbService = new DBService(filepath+dbName);
					pageinfos = dbService.queryUsInfobyUsName(usName, currentdatanum, pagenum);
					dbService = new DBService(filepath+dbName);
					currentdatacount = dbService.queryUserInfobyUsNameCount(usName);
			}
			if(signal == 8){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyUsId(usId, currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount = dbService.queryUserInfobyUsIdCount(usId);
			}
			if(signal == 9){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInfobyMeterId(meterId, currentdatanum, pagenum);
				dbService = new DBService(filepath+dbName);
				currentdatacount =  dbService.queryUserInfobyMeterIdCount(meterId);
			}
			if(signal == 10){
				dbService = new DBService(filepath+dbName);
				pageinfos = dbService.queryUsInforofminus(currentdatanum, pagenum);		
				dbService = new DBService(filepath+dbName);
				currentdatacount =  dbService.queryUsInforofminusCount();
				if(pageinfos==null||pageinfos.size()<0){
					finish();
					Toast.makeText(getApplicationContext(), "��ǰ�޴������û���Ϣ", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			if(pageinfos==null||pageinfos.size()<0){
				finish();
				Toast.makeText(getApplicationContext(), "��˶Բ�ѯ��Ϣ", Toast.LENGTH_SHORT).show();
				return true;
			}
			pagecount = currentdatacount/currentdatanum;
			if(currentdatacount%currentdatanum>0){
				pagecount = pagecount + 1;
			}
			show.setText(pagenum+1+"/"+pagecount);
		}
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int resultCode = 1;
		setResult(resultCode);
		editor = sharedPreferences.edit();
		editor.putInt("con_position", con_position);
		editor.commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(layout.getVisibility()==View.VISIBLE){
				layout.setVisibility(LinearLayout.GONE);
			}else{
				ShowListviewActivity.this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//TODO
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == resultCode){
			//textView.setBackgroundResource(R.drawable.user_numberss);
/*			service = new DBService(filepath+dbName);
			totalinfos = service.queryAllUsersInfo();
			service.connclose();
			
			pageUtil = new Pagination(totalinfos, 1);
			pageinfos = pageUtil.getPageDatas();
			adapter.notifyDataSetChanged();
			listView.invalidate();*/
			//con_position
			//UsersInfo userinfo = (UsersInfo) data.getSerializableExtra("userinfo");
			//totalinfos.set(con_position, userinfo);
			//pageUtil = new Pagination(totalinfos, pagenum,currentdatanum,getApplicationContext());
			//pageinfos = pageUtil.getPageDatas();"userinfo", usInfo
/*			UsersInfo usInfo = (UsersInfo) data.getSerializableExtra("userinfo");
			if(usInfo!=null){
				pageDataBuffer.updateoneOfUsersBuffer(usInfo,con_position);
			}*/
/*			Log.v("UsersInfo��������:", ""+tusInfo.getTHISMONTH_DOSAGE());
			Log.v("UsersInfo���¼�¼:", ""+tusInfo.getTHISMONTH_RECORD());
			Log.v("UsersInfo�����־:", ""+tusInfo.getDOMETERSIGNAL());*/
			dealdata();
			pagenum = con_position/currentdatanum;
			mylistView.getRefreshableView().setSelection(con_position%currentdatanum);
			pagecount = currentdatacount/currentdatanum;
			if(currentdatacount%currentdatanum>0){
				pagecount = pagecount + 1;
			}
			show.setText(pagenum+1+"/"+pagecount);
			//adapter.notifyDataSetChanged();
			adapter.updateAdapterData(pageinfos);
		}
	}
	/**
	 * ʵ�ּ̳���Adapter�ķ���
	 */
	public class NormalShouYeAdapter extends BaseAdapter{
		Context context;
		private List<UsersInfo> list;
		private LayoutInflater inflater;
		public NormalShouYeAdapter(Context context,
				List<UsersInfo> list) {
			this.context = context;
			this.list = list;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// ��䷽��
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
/*			if(position==0){
				convertView.setVisibility(convertView.GONE);
			}
			if(position == list.size()-1){
				convertView.setVisibility(convertView.GONE);
			}*/
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.shouyechaobiao_item,
						null);
				holder = new ViewHolder(getApplicationContext(),convertView);
				convertView.setTag(holder);
			} else {				
				holder = (ViewHolder) convertView.getTag();
			}
			holder.YHMC.setText("����:" + list.get(position).getUSNAME());
			double money =  Double.parseDouble(list.get(position).getUSDEBT());
			System.out.println("-----------------------STRName------------------------:"+list.get(position).getUSNAME());
			System.out.println("-----------------------STRmoney------------------------:"+list.get(position).getUSDEBT());
			System.out.println("-----------------------money------------------------:"+money);
			if(money>0.0&&!"0".equals(list.get(position).getUSDEBT())){
		        holder.debt.show();
			}else{
				holder.debt.hide();
			}
/*			double common =  Double.parseDouble(list.get(position).getUSDEBT());
			if(){
				
			}*/
			holder.BH.setText("���:" + list.get(position).getUSID());
			if("".equals(list.get(position).getMETERID())){
				holder.meterId.setText("���:����");
			}else{
				holder.meterId.setText("���:" + list.get(position).getMETERID());
			}
			holder.YHDZ.setText("�û���ַ:" + list.get(position).getUSADRESS());
			holder.SYYL_DS.setText("����:" + list.get(position).getLASTMONTH_RECORD()+"/"+list.get(position).getLASTMONTH_DOSAGE());
			if("".equals(list.get(position).getTHISMONTH_RECORD())&&"".equals(list.get(position).getTHISMONTH_DOSAGE())){
				holder.BYYL_DS.setText("����:�޼�¼");
			}else{
				holder.BYYL_DS.setText("����:" +list.get(position).getTHISMONTH_RECORD() +"/"+list.get(position).getTHISMONTH_DOSAGE());
			}
			holder.id.setText(""+list.get(position).getDOMETERID());//"" + (position + 1 + pagenum*currentdatanum) + ""
			holder.position = position;
			String temprecord = list.get(position).getTHISMONTH_RECORD();
			holder.ETEXT.setHint(" ");
			holder.ETEXT.setText(" ");
			if("".equals(temprecord)){
				//holder.ETEXT.setText("δ��");
				holder.id.setBackgroundResource(R.mipmap.showlist_item_head_bg);
				holder.ETEXT.setBackgroundResource(R.mipmap.showlist_item_none_bg);
				//System.out.println("δ��");
			}else{
				//holder.ETEXT.setText("�س�");
				holder.id.setBackgroundResource(R.mipmap.showlist_item_head_bg);
				holder.ETEXT.setBackgroundResource(R.mipmap.showlist_item_done_bg);
				//System.out.println("�س�");
			}
			LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.ll_tv.getLayoutParams();
			linearParams.weight = 2;
			//String tempresult = mMapContent.get(indexmsg);
			if(mMapContent!=null&&mMapContent.size()>0)
			holder.ETEXT.setText(mMapContent.get(indexmsg));
			if(mMapContent.get(indexmsg)!=null&&!"".equals(mMapContent.get(indexmsg))){
				holder.ETEXT.setSelection(mMapContent.get(indexmsg).length());
			}else{
				holder.ETEXT.setSelection(0);
			}
			/*holder.ETEXT.setInputType(InputType.TYPE_NULL);*/
			//..................................���´���Ϊ��ֹϵͳ���̵���.................................................
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
			int currentVersion = android.os.Build.VERSION.SDK_INT;
			String methodName = null;
			if(currentVersion >= 16){
				// 4.2
				methodName = "setShowSoftInputOnFocus";
			}
			else if(currentVersion >= 14){
				// 4.0
				methodName = "setSoftInputShownOnFocus";
			}
			
			if(methodName == null){
				holder.ETEXT.setInputType(InputType.TYPE_NULL);  
			}
			else{
				Class<EditText> cls = EditText.class;  
				Method setShowSoftInputOnFocus;  
				try {
					setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
					setShowSoftInputOnFocus.setAccessible(true);  
					setShowSoftInputOnFocus.invoke(holder.ETEXT, false); 
				} catch (NoSuchMethodException e) {
					editText.setInputType(InputType.TYPE_NULL);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			} 
			//.............................���ϴ���Ϊ��ֹϵͳ���̵���...............................................
			
			holder.ETEXT.setTextColor(getResources().getColor(R.color.text_font_color_white));
			holder.ETEXT.setOnTouchListener(new OnTouchListener(){
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == MotionEvent.ACTION_UP) {
						//editText = (EditText) v;      	   
						//editText
						//((TextView) v).setInputType(InputType.TYPE_NULL);	
						if(indexsign!=position){
							if(!"".equals(result.toString())){
								result.delete(0, result.length());
								System.out.println("ɾ��ǰ��"+result);
								result.append("");
								System.out.println("ɾ����"+result);
								indexpos = 0;
							}
						}
						indexsign = position;
						location = position;
						indexmsg = position;
						con_position = position + pagenum*currentdatanum;
						System.out.println("���λ�ã�"+con_position);
					}
					if(event.getAction() == MotionEvent.ACTION_UP) {
						//holder.ETEXT.requestFocus();
						//holder.ETEXT.setCursorVisible(true);
						adapter.notifyDataSetChanged();
						layout.setVisibility(LinearLayout.VISIBLE);
/*						editText.setText("");
						editText.requestFocus();
						editText.setCursorVisible(true);*/
                  }					
					return false;
				}
			});
			holder.linearLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					textView = holder.id;
					con_position = position + pagenum*currentdatanum;//��¼����λ��
					System.out.println("------------------�û�Ƿ�ѽ�"+pageinfos.get(position).getUSDEBT());
					Intent inent = new Intent();
					inent.setClass(ShowListviewActivity.this,
							UserDetailActivity.class);
					inent.putExtra("action", "ShowListviewActivity");
					inent.putExtra("usInfo", pageinfos.get(position));	
					startActivityForResult(inent, REQUESTCODE);
					if(layout.getVisibility()==LinearLayout.VISIBLE){
						result.delete(0, result.length());	
						indexpos=0;
						indexsign=-2;
						layout.setVisibility(LinearLayout.GONE);
					}
/*					if(indexitem!=position){
						if(currentItem!=null)
							currentItem.setBackground(getResources().getDrawable(R.drawable.usertable_cellbg2x));
					}
					currentItem = holder.linearLayout;
					indexitem = position;*/
					return;
				}
			});
/*			if(indexitem!=-1&&indexitem==position){
				holder.linearLayout.setBackground(getResources().getDrawable(R.drawable.usertable_cellbg_btn));
			}else{
				holder.linearLayout.setBackground(getResources().getDrawable(R.drawable.usertable_cellbg2x));
			}*/
			holder.ETEXT.clearFocus();
			holder.ETEXT.setCursorVisible(false);
			
			if(indexsign!= -1 && indexsign == position) {
	               // �����ǰ�����±�͵���¼��б����indexһ�£��ֶ�ΪEditText���ý��㡣
				//System.out.println("��ʾ���");
				//
				editText = holder.ETEXT;
				indexupdate = position;
				System.out.println("��������״̬1");
				if(!editText.isFocused()){
					linearParams.weight = 4;
					editText.setText("");
					editText.setHint("�������");
					editText.requestFocus();
					editText.setCursorVisible(true);
					editText.setWidth(30);
					editText.setTextColor(getResources().getColor(R.color.base_text_color));
					editText.setBackgroundResource(R.drawable.detail_edittext);
					editText.getBackground().setAlpha(40);
					if(!"".equals(result.toString()))
						editText.setText(result);
					System.out.println("��������״̬2");
				}		
	        }			
			return convertView;
		}
		
		public void updateAdapterData(List<UsersInfo> updatelist){
			list = updatelist;
			this.notifyDataSetChanged();
		}
	}

	class ViewHolder {
		// ��ţ���������ţ���ַ
		TextView id;
		TextView YHMC;
//		TextView SIGNAL;
		TextView BH;
		TextView meterId;
		TextView YHDZ;
		TextView SYYL_DS;
		TextView BYYL_DS;
		EditText ETEXT;
		int position;
		LinearLayout linearLayout;
		LinearLayout ll_tv;
		BadgeView debt;
		//BadgeView common;
		public ViewHolder(Context context,View v)
        {
			id = (TextView) v.findViewById(R.id.lv_item_userId);
			meterId =  (TextView) v.findViewById(R.id.user_meterId);
			YHMC = (TextView) v
					.findViewById(R.id.user_name);
/*			SIGNAL = (TextView) v
					.findViewById(R.id.user_signal);*/
			debt = new BadgeView(context, this.YHMC);
	        debt.setText("Ƿ");
	        debt.setBackgroundResource(R.drawable.tasks_background_red);
			//common = new BadgeView(context, this.YHMC);
			BH = (TextView) v.findViewById(R.id.user_id);
			YHDZ = (TextView) v
					.findViewById(R.id.user_addres);
			SYYL_DS = (TextView) v.findViewById(R.id.lastuse);
			BYYL_DS = (TextView) v.findViewById(R.id.thisuse);
			linearLayout = (LinearLayout) v.findViewById(R.id.turn_to_detail);
			ll_tv = (LinearLayout) v.findViewById(R.id.meter_enter_ll);
			ETEXT = (EditText) v.findViewById(R.id.meter_enter_id);
			
			ETEXT.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {	            	   
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					mMapContent.put(indexmsg, s.toString());
				}
			});					
			
        }
	}
	// ˢ�µ�ʱ��
	private RefreshPullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new RefreshPullToRefreshBase.OnRefreshListener2<View>() {
		
		@Override
		public void onPullDownToRefresh(
				RefreshPullToRefreshBase<View> refreshView) {
			//mylistView.setRefreshingLabel("pullingDown");
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(UP_LOAD_SUCCESS);						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		@Override
		public void onPullUpToRefresh(RefreshPullToRefreshBase<View> refreshView) {
			//mylistView.setRefreshingLabel("pullingUP");
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(DOWN_LOAD_SUCCESS);						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	};

	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==DOWN_LOAD_SUCCESS){
				if(pagenum<pagecount-1){
					pagenum++;
					System.out.println("ҳ��"+pagenum);
					//pageUtil = new Pagination(totalinfos, pagenum,currentdatanum,getApplicationContext());
					//pageinfos = pageUtil.getPageDatas();
					//progressBar.setVisibility(View.GONE);
					dealdata();
					adapter.updateAdapterData(pageinfos);
					mylistView.onRefreshComplete();
					mylistView.getRefreshableView().setSelection(1);
					show.setText(pagenum+1+"/"+pagecount);
					indexsign=-2;
					location = 0;
					layout.setVisibility(LinearLayout.GONE);	
				}else{
					mylistView.onRefreshComplete();
				}
			}
			if(msg.what==UP_LOAD_SUCCESS){
				if(pagenum>0){
					
					pagenum--;
					System.out.println("ҳ��"+pagenum);
					//pageUtil = new Pagination(totalinfos, pagenum,currentdatanum,getApplicationContext());
					//pageinfos = pageUtil.getPageDatas();
					//progressBar.setVisibility(View.GONE);
					dealdata();
					adapter.updateAdapterData(pageinfos);
					mylistView.onRefreshComplete();
					mylistView.getRefreshableView().setSelection(currentdatanum);	
					show.setText(pagenum+1+"/"+pagecount);
					indexsign=-2;
					location = 0;
					layout.setVisibility(LinearLayout.GONE);	
				}else{
					mylistView.onRefreshComplete();
				}
			}
		}
	}
	
	/**
	 * ���̰���
	 */
	public void onButton(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.HomeMeter_back:
			this.finish();
			break;
			case R.id.listone:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(1);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}
				break;
			case R.id.listtwo:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(2);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listthree:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(3);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listfour:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(4);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listfive:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(5);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listsix:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(6);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listseven:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(7);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}		
				break;
			case R.id.listeight:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(8);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listnight:
				if(result.length()<9&&editText!=null){
					if("0".equals(editText.getText().toString())){
						result.deleteCharAt(0);
					}
					result.append(9);
					editText.setText(result);
					indexpos++;
					editText.setSelection(indexpos);
				}			
				break;
			case R.id.listzero:
				if(editText!=null)
				if(editText.getText().toString()==null||"".equals(editText.getText().toString())){
					result.append(0);
					editText.setText(result);	
					break;
					}else{
						if("0".equals(editText.getText().toString())){
							break;
						}
						if(result.length()<9){
							result.append(0);
							editText.setText(result);
							indexpos++;
							editText.setSelection(indexpos);
						}
					}
				break;			
			case R.id.listdone:
				//....�ж��Ƿ��GPS...........
				if(editText == null){
					break;
				}
				LocationManager alm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);       
			       if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER))//&&!JaugeInternetState.isWifi(getApplicationContext())
			       {
			    	   dialogBuilder = new NiftyDialogBuilder(ShowListviewActivity.this,R.style.dialog_untran);
			    	   dialogBuilder
		                .withTitle("��ʾ")
		                .withTitleColor("#000000")
		                .withDividerColor("#999999")
		                .withMessage("���GPS�����������ڹ���������������ӣ�")
		                .withMessageColor("#000000")
		                .isCancelableOnTouchOutside(true)
		                .withDuration(700)
		                .withEffect(Effectstype.Slidetop)
		                .withButton1Text("ȡ������")
		                .withButton2Text("��������")
		                .setButton1Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		                    	dialogBuilder.dismiss();
		                    }
		                })
		                .setButton2Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		 				       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						       startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����
						       dialogBuilder.dismiss();
		                    }
		                })
		                .show();
			    	   return;
			      }

				sb = sharedPreferences.getString("sb", "");
				if(sb==null||"".equals(sb)){
					Toast.makeText(getApplicationContext(), "û�п��þ�γ��", Toast.LENGTH_SHORT).show();
					return;
				}
				int loc = sb.indexOf("$");
				LATIUDE = sb.substring(0, loc-1);
				LONGITUDE = sb.substring(loc+1, sb.length());

				final String thisRecord = editText.getText().toString();//���¶���
				String lastRecord = pageinfos.get(location).getLASTMONTH_RECORD();//���¶���
				if(thisRecord!=null&&!"".equals(thisRecord)&&!"".equals(lastRecord)){
					//��������
					final String thisDosage = String.valueOf(Integer.parseInt(thisRecord)-Integer.parseInt(lastRecord));
					if(Integer.parseInt(thisDosage)<0){
						Toast.makeText(ShowListviewActivity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();
						break;
					}
					String LOATRANGE = pageinfos.get(location).getFLOATRANGE();
					if("".equals(LOATRANGE)){
						LOATRANGE = "0";
					}
					if(Integer.parseInt(thisDosage)>Integer.parseInt(LOATRANGE)&&!"0".equals(LOATRANGE)){
						dialogBuilder= new NiftyDialogBuilder(ShowListviewActivity.this,R.style.dialog_untran);
				        dialogBuilder
		                .withTitle("��ʾ")
		                .withTitleColor("#000000")
		                .withDividerColor("#999999")
		                .withMessage("���µ���������"+LOATRANGE+"��������Χ")
		                .withMessageColor("#000000")
		                .isCancelableOnTouchOutside(true)
		                .withDuration(700)
		                .withEffect(Effectstype.RotateBottom)
		                .withButton1Text("ȡ��¼��")
		                .withButton2Text("ȷ��¼��")
		                .setButton1Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
/*								result.delete(0, result.length());
								indexpos=0;
								indexsign=-2;*/
								//layout.setVisibility(LinearLayout.GONE);
		                    	dialogBuilder.dismiss();

		                    }
		                })
		                .setButton2Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		                    	result.delete(0, result.length());
								//����ʱ��
								SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
								String date = dateformat.format(new Date());
								//�������ݿ�
								dbService = new DBService(filepath+dbName);
								dbService.modifyUserData(pageinfos.get(location).getUSID(), thisRecord, thisDosage, date, LONGITUDE,LATIUDE,"1");
								dbService.connclose();
								//ˢ��UI Listview
								pageinfos.get(location).setTHISMONTH_RECORD(thisRecord);
								pageinfos.get(location).setTHISMONTH_DOSAGE(thisDosage);
								pageinfos.get(location).setDOMETERDATE(date);
								pageinfos.get(location).setDOMETERSIGNAL("1");
								//mylistView.invalidate();								
								result.delete(0, result.length());	
								indexpos=0;
								if(indexsign==pageinfos.size()-1&&location==pageinfos.size()-1){
									indexsign=-2;
									location=0;	
									layout.setVisibility(View.GONE);
								}
								if(indexsign<pageinfos.size()&&location<pageinfos.size()){
									indexsign++;
									location++;	
									editText = null;
								}
								adapter.notifyDataSetChanged();
								if(indexsign-1<=0){
									mylistView.getRefreshableView().setSelection(indexsign);
								}else{
									mylistView.getRefreshableView().setSelection(indexsign-1);
								}
								Toast.makeText(ShowListviewActivity.this, "¼��ɹ�", Toast.LENGTH_SHORT).show();
								//layout.setVisibility(LinearLayout.GONE);
								dialogBuilder.dismiss();
		                    }
		                })
		                .show();
					}else{
						//����ʱ��
						SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
						String date = dateformat.format(new Date());
						//String[] showdate = date.split(" ");					
						//�������ݿ�
						//System.out.println("���ȣ�"+LONGITUDE);
						//System.out.println("γ�ȣ�"+LATIUDE);
						dbService = new DBService(filepath+dbName);
						dbService.modifyUserData(pageinfos.get(location).getUSID(), thisRecord, thisDosage, date, LONGITUDE,LATIUDE,"1");
						dbService.connclose();
						//ˢ��UI Listview
						pageinfos.get(location).setDOMETERDATE(date);
						pageinfos.get(location).setTHISMONTH_RECORD(thisRecord);
						pageinfos.get(location).setTHISMONTH_DOSAGE(thisDosage);
						pageinfos.get(location).setDOMETERSIGNAL("1");
						//mylistView.invalidate();
						result.delete(0, result.length());	
						indexpos=0;
						if(indexsign==pageinfos.size()-1&&location==pageinfos.size()-1){
							indexsign=-2;
							location=0;
							layout.setVisibility(View.GONE);
						}
						if(indexsign<pageinfos.size()&&location<pageinfos.size()){
							indexsign++;
							location++;	
							editText = null;
						}
						adapter.notifyDataSetChanged();
						if(indexsign-1<=0){
							mylistView.getRefreshableView().setSelection(indexsign);
						}else{
							mylistView.getRefreshableView().setSelection(indexsign-1);
						}
						/*Toast.makeText(ShowListviewActivity.this, "¼��ɹ�", Toast.LENGTH_SHORT).show();*/
						//layout.setVisibility(LinearLayout.GONE);
					}
				}else{
					Toast.makeText(ShowListviewActivity.this, "����ȷ��д��Ϣ", Toast.LENGTH_SHORT).show();	
					   //layout.setVisibility(LinearLayout.GONE);
				}
				break;
			case R.id.listdelete:
				if(editText==null){
					indexsign=-2;
					location = 0;
					adapter.notifyDataSetChanged();
					layout.setVisibility(LinearLayout.GONE);	
					break;
				}
				String tempstr = editText.getText().toString();
				//System.out.println("tempstr"+tempstr);
				if("".equals(tempstr)){
					indexsign=-2;
					location = 0;
					adapter.notifyDataSetChanged();
					layout.setVisibility(LinearLayout.GONE);	
				}
				if(tempstr!=null&&tempstr.length()>0){
					tempstr = tempstr.substring(0, tempstr.length()-1);
					result.replace(0, result.length(), tempstr);
					editText.setText(tempstr);
					if(indexpos>0)
						indexpos--;		
					editText.setSelection(indexpos);
				}
				break;			
							
		default:
			break;
		}
		
	}
}
