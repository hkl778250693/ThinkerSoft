package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.UserDetailActivity;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.navigation.BNavigatorActivity;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

public class MapMeterAdapter extends BaseAdapter {
	private Context context;
	private Activity act;
	private List<UsersInfo> data;
	private LayoutInflater inflater;
	private CallBackItemListener mListener;
	private MapMHandler adapterhandler;
	private SharedPreferences preferences;
	
/*	private int clickPosition;
	private UsersInfo clickUsersInfo;
	private LinearLayout Cuturn_to_detail;*/
	public MapMeterAdapter(Context context,Activity act,List<UsersInfo> data){
		this.context = context;
		this.act = act;
		this.data = data;
		inflater = LayoutInflater.from(context);
		adapterhandler = new MapMHandler();
		MyApplication.mapmhandler = adapterhandler;
		preferences = context.getSharedPreferences("IP_PORT_DBNAME", 0);
	}
	
	public void updateAllData(List<UsersInfo> tempdata){
		this.data.clear(); 
		this.data.addAll(tempdata);
		notifyDataSetChanged();
	}
	
	public void updateOneData(UsersInfo usersInfo, int position){
		this.data.remove(position);
		this.data.add(position, usersInfo);
		notifyDataSetChanged();
	}
	
	public List<UsersInfo> getMapMeterAdapterData(){
		return data;
	}
	
	
	public CallBackItemListener getmListener() {
        return mListener;
    }
 
    public void setmListener(CallBackItemListener mListener) {
        this.mListener = mListener;
    }
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.map_meter_lv_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String name = data.get(position).getUSNAME();
		String id = data.get(position).getUSID();
		holder.getItemNum().setText(data.get(position).getDOMETERID());
		holder.getUsName().setText("����:  "+name);
		holder.getUsId().setText("���:  "+id);
		holder.getMeterId().setText("���:  "+data.get(position).getMETERID());
		holder.getUsAddress().setText("��ַ:  "+data.get(position).getUSADRESS());
		
		if(data.get(position).getLASTMONTH_DOSAGE()!=null&&!"".equals(data.get(position).getLASTMONTH_DOSAGE())){
			holder.getLastUseage().setText("����:  "+data.get(position).getLASTMONTH_DOSAGE());
		}else{
			holder.getLastUseage().setText("����:  "+"����");
		}
		
		if(data.get(position).getTHISMONTH_DOSAGE()!=null&&!"".equals(data.get(position).getTHISMONTH_DOSAGE())){
			holder.getThisUseage().setText("����:  "+data.get(position).getTHISMONTH_DOSAGE());
		}else{
			holder.getThisUseage().setText("����:  "+"����");
		}
		if(data.get(position).getUSDEBT()==null||"".equals(data.get(position).getUSDEBT())){

		}else{
			holder.getBadgeView().setText("Ƿ");
			holder.getBadgeView().setBackgroundResource(R.drawable.gps_background_red);
		}
		holder.getBadgeView().show();
		holder.getTurn_to_detail().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context,
						UserDetailActivity.class);
				intent.putExtra("action", "MapMeterActivity");
				intent.putExtra("usInfo", data.get(position));	
				intent.putExtra("position", position);
				context.startActivity(intent);
				//MyAplication.clickMarker = marker;//���б�activity�Ŀؼ������뼰ʱ�ͷš�
			}
		});

		holder.getNavigation().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String latlng = preferences.getString("sb", "");
				if(latlng!=null){
					int loc = latlng.indexOf("$");
					double CURRENTLATIUDE = Double.parseDouble(latlng.substring(0, loc-1));
					double CURRENTLONGITUDE = Double.parseDouble(latlng.substring(loc+1, latlng.length()));
					double PURPOSELONGITUDE = Double.parseDouble(data.get(position).getBASE_LONGITUDE());
					double PURPOSELATIUDE = Double.parseDouble(data.get(position).getBASE_LATIUDE());
					if(PURPOSELONGITUDE==0||PURPOSELATIUDE==0){
						Mytoast.showToast(context,
								"ȱ��Ŀ�ĵ����꣬�޷�����������", Toast.LENGTH_SHORT);
					}
					//�ж�SDCard
					if (Gadget.isSDCardExist()){
						launchNavigator(CURRENTLONGITUDE,
								CURRENTLATIUDE,
								PURPOSELONGITUDE, PURPOSELATIUDE);
					}else{
						Mytoast.showToast(context,
								"��⵽����ֻ�ȱ��SD��������ʹ�õ������ܣ�", Toast.LENGTH_SHORT);
					}
				}else{
					Mytoast.showToast(context,
							"���궨λʧ�ܣ�����ʹ�õ������ܣ�", Toast.LENGTH_SHORT);
				}
			}
		});
		//mListener.callBackItem(position, data.get(position));
		return convertView;
	}
	
	private void launchNavigator(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		BNaviPoint startPoint = new BNaviPoint(latitude1, longitude1, "���",
				BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(latitude2, longitude2, "�յ�",
				BNaviPoint.CoordinateType.BD09_MC);
		BaiduNaviManager.getInstance().launchNavigator(act,
				startPoint, // ��㣨��ָ������ϵ��
				endPoint, // �յ㣨��ָ������ϵ��
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,// ��·��ʽ
				true, // ��ʵ����
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
				new OnStartNavigationListener() {

					@Override
					public void onJumpToDownloader() {

					}

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(
								act,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						context.startActivity(intent);
					}
				} // ��ת����
				);
	}
	
	
	
	class ViewHolder{
		public TextView itemNum;
		public TextView usName;
		public TextView usId;
		public TextView meterId;
		public TextView usAddress;
		public TextView lastUseage;
		public TextView thisUseage;
		public LinearLayout turn_to_detail;
		public ImageView navigation;
		public BadgeView badgeView;
		public ViewHolder(View convertView){
			itemNum = (TextView) convertView.findViewById(R.id.map_meter_lv_item_userId);
			usName = (TextView) convertView.findViewById(R.id.map_meter_user_name);
			usId = (TextView) convertView.findViewById(R.id.map_meter_user_id);
			meterId = (TextView) convertView.findViewById(R.id.map_meter_user_meterId);
			usAddress = (TextView) convertView.findViewById(R.id.map_meter_user_addres);
			lastUseage = (TextView) convertView.findViewById(R.id.map_meter_lastuse);
			thisUseage = (TextView) convertView.findViewById(R.id.map_meter_thisuse);
			turn_to_detail = (LinearLayout) convertView.findViewById(R.id.map_meter_turn_to_detail);
			navigation = (ImageView) convertView.findViewById(R.id.map_meter__turntonav);
			badgeView = new BadgeView(context,usName);
		}
		public TextView getItemNum() {
			return itemNum;
		}
		public void setItemNum(TextView itemNum) {
			this.itemNum = itemNum;
		}
		public TextView getUsName() {
			return usName;
		}
		public void setUsName(TextView usName) {
			this.usName = usName;
		}
		public TextView getUsId() {
			return usId;
		}
		public void setUsId(TextView usId) {
			this.usId = usId;
		}
		public TextView getMeterId() {
			return meterId;
		}
		public void setMeterId(TextView meterId) {
			this.meterId = meterId;
		}
		public TextView getUsAddress() {
			return usAddress;
		}
		public void setUsAddress(TextView usAddress) {
			this.usAddress = usAddress;
		}
		public TextView getLastUseage() {
			return lastUseage;
		}
		public void setLastUseage(TextView lastUseage) {
			this.lastUseage = lastUseage;
		}
		public TextView getThisUseage() {
			return thisUseage;
		}
		public void setThisUseage(TextView thisUseage) {
			this.thisUseage = thisUseage;
		}
		public LinearLayout getTurn_to_detail() {
			return turn_to_detail;
		}
		public void setTurn_to_detail(LinearLayout turn_to_detail) {
			this.turn_to_detail = turn_to_detail;
		}
		public ImageView getNavigation() {
			return navigation;
		}
		public void setNavigation(ImageView navigation) {
			this.navigation = navigation;
		}
		public BadgeView getBadgeView() {
			return badgeView;
		}
		public void setBadgeView(BadgeView badgeView) {
			this.badgeView = badgeView;
		}		
	}
    public interface CallBackItemListener{
        void callBackItem(int clickPosition, UsersInfo info);
    }
    public class MapMHandler extends Handler{
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			switch (Integer.parseInt(bundle.getString("key"))) {
			case 1:
				//�����б�
				UsersInfo info = (UsersInfo) bundle.getSerializable("usInfo");
				int position = bundle.getInt("position");
				data.remove(position);
				data.add(position, info);
				notifyDataSetChanged();
				//�޸ı�ע��ɫ
				BitmapDescriptor descriptor = null;
				if(MyApplication.MARKERTYPEFARNEAR==0){//��
					descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding_blue);
				}else if(MyApplication.MARKERTYPEFARNEAR==1){//Զ
					descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark_pt_blue);
				}
				if (data.size() == 1) {
					MyApplication.clickMarkers.get(0).setIcon(descriptor);
					MyApplication.clickMarkers_icon.set(0, descriptor);
				} else if (data.size() > 1) {
					boolean isDone = false;
					for (int i = 0; i < data.size(); i++) {
						if (data.get(i).getTHISMONTH_RECORD() == null
								|| "".equals(data.get(i).getTHISMONTH_RECORD())) {
							isDone = true;
						}
					}
					if (isDone == false) {
						MyApplication.clickMarkers.get(0).setIcon(descriptor);
						MyApplication.clickMarkers_icon.set(0, descriptor);
					}
				}
				break;
			default:
				break;
			};
		};
    }

}
