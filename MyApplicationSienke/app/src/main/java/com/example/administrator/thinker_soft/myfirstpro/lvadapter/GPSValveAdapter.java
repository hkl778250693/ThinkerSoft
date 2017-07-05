package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.GpsMapCollectorActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserDetailActivity;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.entity.DeviceInfo;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

import java.util.List;

public class GPSValveAdapter extends BaseAdapter {
	private List<DeviceInfo> data;
	private Context context;
	private LayoutInflater inflater;
	private GPSValveHandler valveHandler;
	public GPSValveAdapter(Context context,List<DeviceInfo> data) {
		this.context = context;
		this.data = data;
		this.valveHandler = new GPSValveHandler();
		MyApplication.gpsvalveHandler = this.valveHandler;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void updateListData(List<DeviceInfo> data){
		this.data.clear();
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		if(contentView==null){
			contentView = inflater.inflate(R.layout.gps_valvefragment_item, null);
			holder = new ViewHolder(contentView);
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder) contentView.getTag();
		}
		holder.getGps_lv_item_valveId().setText(""+data.get(position).getN_EQUIPMENT_ID());//data.get(position).getC_EQUIPMENT_NUMBER()
		holder.getGps_valve_name().setText("".equals(data.get(position).getC_EQUIPMENT_NAME())?"����":data.get(position).getC_EQUIPMENT_NAME());
		holder.getGps_valve_address().setText("".equals(data.get(position).getC_EQUIPMENT_ADDRESS())?"����":data.get(position).getC_EQUIPMENT_ADDRESS());
		holder.getGps_valve_type().setText("��  ��:"+("".equals(data.get(position).getC_EQUIPMENT_TYPE_NAME())?"����":data.get(position).getC_EQUIPMENT_TYPE_NAME()));
		holder.getGps_valve_status().setText("״  ̬:"+("".equals(data.get(position).getN_EQUIPMENT_STATUS())?"����":("1".equals(data.get(position).getN_EQUIPMENT_STATUS())?"����":"ͣ��")));
		holder.getGps_valve_reamrk().setText("��   ע:"+("".equals(data.get(position).getC_EQUIPMENT_REMARK())?"����":data.get(position).getC_EQUIPMENT_REMARK()));
		holder.getGps_valve_lontitude().setText("��   ��:"+("".equals(data.get(position).getC_EQUIPMENT_X())?"����":data.get(position).getC_EQUIPMENT_X()));
		holder.getGps_valve_latitude().setText("γ   ��:"+("".equals(data.get(position).getC_EQUIPMENT_Y())?"����":data.get(position).getC_EQUIPMENT_Y()));
		if(data.get(position).getC_EQUIPMENT_X()==null||"".equals(data.get(position).getC_EQUIPMENT_X())||data.get(position).getC_EQUIPMENT_Y()==null||"".equals(data.get(position).getC_EQUIPMENT_Y())){
			holder.getBadgeView().setText("δ�ɼ�");
			holder.getBadgeView().setBackgroundResource(R.drawable.gps_background_red);
		}else{
			holder.getBadgeView().setText("�Ѳɼ�");
			holder.getBadgeView().setBackgroundResource(R.drawable.gps_background_blue);
		}
		holder.getBadgeView().show();
		holder.getValve_turn_to_detail().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, MeterUserDetailActivity.class);
				intent.putExtra("devInfo", data.get(position));
				context.startActivity(intent);
			}
		});
		holder.getGps_valveturntomap().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, GpsMapCollectorActivity.class);
				intent.setAction("GPSValveFragment");
				intent.putExtra("devInfo", data.get(position));
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		return contentView;
	}
	class ViewHolder{
		private LinearLayout valve_turn_to_detail;
		private TextView gps_lv_item_valveId;
		private TextView gps_valve_name;
		private TextView gps_valve_address;
		private TextView gps_valve_type;
		private TextView gps_valve_status;
		private TextView gps_valve_reamrk;
		private TextView gps_valve_lontitude;
		private TextView gps_valve_latitude;
		private ImageView gps_valveturntomap;
		private BadgeView badgeView;
		public ViewHolder(View contentView) {
			valve_turn_to_detail = (LinearLayout) contentView.findViewById(R.id.valve_turn_to_detail);
			gps_lv_item_valveId = (TextView) contentView.findViewById(R.id.gps_lv_item_valveId);
			gps_valve_name = (TextView) contentView.findViewById(R.id.gps_valve_name);
			gps_valve_address = (TextView) contentView.findViewById(R.id.gps_valve_address);
			gps_valve_type = (TextView) contentView.findViewById(R.id.gps_valve_type);
			gps_valve_status = (TextView) contentView.findViewById(R.id.gps_valve_status);
			gps_valve_reamrk = (TextView) contentView.findViewById(R.id.gps_valve_reamrk);
			gps_valve_lontitude = (TextView) contentView.findViewById(R.id.gps_valve_lontitude);
			gps_valve_latitude = (TextView) contentView.findViewById(R.id.gps_valve_latitude);
			gps_valveturntomap = (ImageView) contentView.findViewById(R.id.gps_valveturntomap);
			badgeView = new BadgeView(context,gps_valve_name);
		}
		public LinearLayout getValve_turn_to_detail() {
			return valve_turn_to_detail;
		}
		public void setValve_turn_to_detail(LinearLayout valve_turn_to_detail) {
			this.valve_turn_to_detail = valve_turn_to_detail;
		}
		public TextView getGps_valve_name() {
			return gps_valve_name;
		}
		public void setGps_valve_name(TextView gps_valve_name) {
			this.gps_valve_name = gps_valve_name;
		}
		public TextView getGps_valve_address() {
			return gps_valve_address;
		}
		public void setGps_valve_address(TextView gps_valve_address) {
			this.gps_valve_address = gps_valve_address;
		}
		public TextView getGps_valve_type() {
			return gps_valve_type;
		}
		public void setGps_valve_type(TextView gps_valve_type) {
			this.gps_valve_type = gps_valve_type;
		}
		public TextView getGps_valve_status() {
			return gps_valve_status;
		}
		public void setGps_valve_status(TextView gps_valve_status) {
			this.gps_valve_status = gps_valve_status;
		}
		public TextView getGps_valve_reamrk() {
			return gps_valve_reamrk;
		}
		public void setGps_valve_reamrk(TextView gps_valve_reamrk) {
			this.gps_valve_reamrk = gps_valve_reamrk;
		}
		public TextView getGps_lv_item_valveId() {
			return gps_lv_item_valveId;
		}
		public void setGps_lv_item_valveId(TextView gps_lv_item_valveId) {
			this.gps_lv_item_valveId = gps_lv_item_valveId;
		}
		public TextView getGps_valve_lontitude() {
			return gps_valve_lontitude;
		}
		public void setGps_valve_lontitude(TextView gps_valve_lontitude) {
			this.gps_valve_lontitude = gps_valve_lontitude;
		}
		public TextView getGps_valve_latitude() {
			return gps_valve_latitude;
		}
		public void setGps_valve_latitude(TextView gps_valve_latitude) {
			this.gps_valve_latitude = gps_valve_latitude;
		}
		public ImageView getGps_valveturntomap() {
			return gps_valveturntomap;
		}
		public void setGps_valveturntomap(ImageView gps_valveturntomap) {
			this.gps_valveturntomap = gps_valveturntomap;
		}
		public BadgeView getBadgeView() {
			return badgeView;
		}
		public void setBadgeView(BadgeView badgeView) {
			this.badgeView = badgeView;
		}
		
	}
    public class GPSValveHandler extends Handler{
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			switch (Integer.parseInt(bundle.getString("key"))) {
			case 1:
				 DeviceInfo info = (DeviceInfo) bundle.getSerializable("devInfo");
				int position = bundle.getInt("position");
				data.remove(position);
				data.add(position, info);
				notifyDataSetChanged();
				break;
			default:
				break;
			};

		};
    }
}
