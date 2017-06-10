package com.example.administrator.thinker_soft.myfirstpro.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.GPSMeterAdapter;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshBase;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshListView;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GPSMeterFragment extends Fragment {
	private Activity activity;
	private Context context;
	private RefreshPullToRefreshListView gps_fragment_lv;
	private LinearLayout gps_page_show_ll;
	private TextView gps_page_show;
	private EditText gps_page_search;
	private String et_content = "";
	private List<UsersInfo> uslist;
	private String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	private String dbName;
	private SharedPreferences sharedPreferences;
	private int currentdatanum;//������ʾ����
	private int currentdatacount;//����������
	private int pagenum=0;//��ǰҳ  Ĭ�ϵ�һҳ
	private int pagecount;//��ҳ��
	private List<UsersInfo> pageinfos;
	private static GPSMeterFragment gpsFragment;
	private View view;
	//...........HANDLER ��ʶ...........
	private int UP_LOAD_SUCCESS = 1;
	private int DOWN_LOAD_SUCCESS = 0;
	private int ETSEARCH_SUCCESS = 2;
	//...........��ѯ���� ��ʶ...........
	private int SEARCH_SIGNAL = 1;//1.��ѯ���� 2.������ѯ
	
	private DBService dbService;
	private GPSMeterAdapter adapter;
	private int position;//���λ��
	private UsersInfo info;//�����Item����
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			if(SEARCH_SIGNAL==1){
				if(key==UP_LOAD_SUCCESS){
					if(pagenum>0){
						
						pagenum--;
						System.out.println("ҳ��"+pagenum);
						dbService = new DBService(filepath+dbName);
						pageinfos = dbService.queryDataByCondition(et_content,currentdatanum, pagenum);
						if(pageinfos==null||pageinfos.size()<0){
							return;
						}
						dbService = new DBService(filepath+dbName);
						currentdatacount = dbService.getDataByConditionCount(et_content);
						pagecount = currentdatacount/currentdatanum;
						if(currentdatacount%currentdatanum>0){
							pagecount = pagecount + 1;
						}
						adapter.updateData(pageinfos);
						gps_fragment_lv.onRefreshComplete();
						gps_fragment_lv.getRefreshableView().setSelection(currentdatanum);	
						gps_page_show.setText(pagenum+1+"/"+pagecount);
					}else{
						gps_fragment_lv.onRefreshComplete();
					}

				}else if(key==DOWN_LOAD_SUCCESS){
					if(pagenum<pagecount-1){
						pagenum++;
						System.out.println("ҳ��"+pagenum);
						dbService = new DBService(filepath+dbName);
						pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
						if(pageinfos==null||pageinfos.size()<0){
							return;
						}
						dbService = new DBService(filepath+dbName);
						currentdatacount = dbService.queryAllUsersInfoCount();
						pagecount = currentdatacount/currentdatanum;
						if(currentdatacount%currentdatanum>0){
							pagecount = pagecount + 1;
						}
						adapter.updateData(pageinfos);
						gps_fragment_lv.onRefreshComplete();
						gps_fragment_lv.getRefreshableView().setSelection(1);
						gps_page_show.setText(pagenum+1+"/"+pagecount);
					}else{
						gps_fragment_lv.onRefreshComplete();
					}
				}
			}else if(SEARCH_SIGNAL==2){
				if(key==UP_LOAD_SUCCESS){
					if(pagenum>0){
						pagenum--;
						System.out.println("ҳ��"+pagenum);
						dbService = new DBService(filepath+dbName);
						pageinfos = dbService.queryDataByCondition(et_content,currentdatanum, pagenum);
						if(pageinfos==null||pageinfos.size()<0){
							return;
						}
						dbService = new DBService(filepath+dbName);
						currentdatacount = dbService.getDataByConditionCount(et_content);
						pagecount = currentdatacount/currentdatanum;
						if(currentdatacount%currentdatanum>0){
							pagecount = pagecount + 1;
						}
						adapter.updateData(pageinfos);
						gps_fragment_lv.onRefreshComplete();
						gps_fragment_lv.getRefreshableView().setSelection(currentdatanum);	
						gps_page_show.setText(pagenum+1+"/"+pagecount);
					}else{
						gps_fragment_lv.onRefreshComplete();
					}
				}else if(key==DOWN_LOAD_SUCCESS){
					if(pagenum<pagecount-1){
						pagenum++;
						System.out.println("ҳ��"+pagenum);
						dbService = new DBService(filepath+dbName);
						pageinfos = dbService.queryDataByCondition(et_content,currentdatanum, pagenum);
						if(pageinfos==null||pageinfos.size()<0){
							return;
						}
						dbService = new DBService(filepath+dbName);
						currentdatacount = dbService.getDataByConditionCount(et_content);
						pagecount = currentdatacount/currentdatanum;
						if(currentdatacount%currentdatanum>0){
							pagecount = pagecount + 1;
						}
						adapter.updateData(pageinfos);
						gps_fragment_lv.onRefreshComplete();
						gps_fragment_lv.getRefreshableView().setSelection(1);
						gps_page_show.setText(pagenum+1+"/"+pagecount);
					}else{
						gps_fragment_lv.onRefreshComplete();
					}
				}else if(key==ETSEARCH_SUCCESS){
					adapter.updateData(pageinfos);
					gps_fragment_lv.getRefreshableView().setSelection(currentdatanum);	
					gps_page_show.setText(pagenum+1+"/"+pagecount);
				}
			}
		};
	};
	
	public GPSMeterFragment(){
		
	}
	
	public static GPSMeterFragment getInstance(){
		if(gpsFragment==null){
			gpsFragment = new GPSMeterFragment();
		}
		return gpsFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		context = getActivity();
		guiwidaget();
		uslist = getDataBase();
		adapter = new GPSMeterAdapter(context, uslist);
		gps_fragment_lv.setAdapter(adapter);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
/*		if(data!=null)
		if(requestCode==10&&resultCode==10){
			MyGPSFragment.this.info = (UsersInfo) data.getSerializableExtra("usInfo");
			uslist.remove(position);
			uslist.add(position, MyGPSFragment.this.info);
			adapter.notifyDataSetChanged();
		}*/
	}
	
	private void guiwidaget() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_metercollector, (ViewGroup) activity.findViewById(R.id.gps_viewpager),false);
		gps_fragment_lv = (RefreshPullToRefreshListView) view.findViewById(R.id.gps_meter_fragment_lv);
		gps_fragment_lv.setMode(RefreshPullToRefreshBase.Mode.BOTH);// ����ˢ��ģʽ ���� ���� Ĭ������
		gps_fragment_lv.setOnRefreshListener(onRefreshListener2);
		gps_page_show = (TextView) view.findViewById(R.id.gps_page_show);
		gps_page_search = (EditText) view.findViewById(R.id.gps_page_search);
		gps_page_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(final Editable s) {
				if(s!=null&&!"".equals(s.toString())){
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						
						@Override
						public void run() {
							if(!s.toString().equals(et_content)){//1.��ʱ1���ѯ 2.���ν����ͬʱ �ŷ����ѯ
								SEARCH_SIGNAL = 2;
								et_content = s.toString();
								pagenum = 0;
								dbService = new DBService(filepath+dbName);
								pageinfos = dbService.queryDataByCondition(et_content, currentdatanum, pagenum);
								if(pageinfos==null||pageinfos.size()<0){
									return;
								}
								dbService = new DBService(filepath+dbName);
								currentdatacount = dbService.getDataByConditionCount(et_content);
								pagecount = currentdatacount/currentdatanum;
								if(currentdatacount%currentdatanum>0){
									pagecount = pagecount + 1;
								}
								handler.sendEmptyMessage(ETSEARCH_SUCCESS);
							}else{
								return;
							}
						}
					};
					timer.schedule(task, 1000, 1000);
					//���ö�ʱ����ÿ������һ�� Ҫ�ж������Ƿ��޸ģ�
					//��ȡ������
					//��ѯ���ݿ�
					//�����б�
				}else{
					SEARCH_SIGNAL = 1;
					dbService = new DBService(filepath+dbName);
					pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
					dbService = new DBService(filepath+dbName);
					currentdatacount = dbService.queryAllUsersInfoCount();
					if(pageinfos==null||pageinfos.size()<0){
						return;
					}
					pagecount = currentdatacount/currentdatanum;
					if(currentdatacount%currentdatanum>0){
						pagecount = pagecount + 1;
					}
					handler.sendEmptyMessage(ETSEARCH_SUCCESS);
				}
			}
		});
	}
	
	private List<UsersInfo> getDataBase() {
		sharedPreferences = context.getSharedPreferences("IP_PORT_DBNAME", 0);
		dbName = sharedPreferences.getString("dbName", "");
		dbService = new DBService(filepath+dbName);
		currentdatanum = sharedPreferences.getInt("number", 0);
		if(currentdatanum==0){
			currentdatanum = 50;//Ĭ����ʾ50������
			
		}
		pageinfos = dbService.queryAllUsersInfoPage(currentdatanum, pagenum);
		dbService = new DBService(filepath+dbName);
		currentdatacount = dbService.queryAllUsersInfoCount();
		if(pageinfos==null||pageinfos.size()<0){
			return null;
		}
		pagecount = currentdatacount/currentdatanum;
		if(currentdatacount%currentdatanum>0){
			pagecount = pagecount + 1;
		}
		gps_page_show.setText(pagenum+1+"/"+pagecount);
		return pageinfos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup  vg =  (ViewGroup) view.getParent();
		if(vg!=null){
			vg.removeAllViewsInLayout();
		}
		return view;
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
					super.run();
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(UP_LOAD_SUCCESS);						
					} catch (InterruptedException e) {
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
					super.run();
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(DOWN_LOAD_SUCCESS);						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	};	
}
