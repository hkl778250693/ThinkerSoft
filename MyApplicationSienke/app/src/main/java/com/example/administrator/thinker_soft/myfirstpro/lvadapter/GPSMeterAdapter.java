package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.administrator.thinker_soft.R;

import java.util.List;

import com.example.administrator.thinker_soft.meter_code.MeterUserDetailActivity;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.meter_code.GpsMapCollectorActivity;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

public class GPSMeterAdapter extends BaseAdapter {
	private Context context;
	private List<UsersInfo> data;
	private LayoutInflater inflater;
	private CallBackItemListener mListener;
	private GPSMeterHandler adapterhandler;
	public GPSMeterAdapter(Context context,List<UsersInfo> data){
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
		adapterhandler = new GPSMeterHandler();
		MyApplication.gpsmeterHandler = adapterhandler;
	}
	
	public void updateData(List<UsersInfo> tempdata){
		this.data.clear(); 
		this.data.addAll(tempdata);
		notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.gps_meterfragment_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.getItemNum().setText(data.get(position).getDOMETERID());
		holder.getUsName().setText("����:  "+data.get(position).getUSNAME());
		holder.getUsId().setText("���:  "+data.get(position).getUSID());
		holder.getMeterId().setText("���:  "+data.get(position).getMETERID());
		holder.getUsAddress().setText("��ַ:  "+data.get(position).getUSADRESS());
		
		if(data.get(position).getBASE_LATIUDE()!=null&&!"".equals(data.get(position).getBASE_LATIUDE())){
			holder.getLatitude().setText("γ��:  "+data.get(position).getBASE_LATIUDE());
		}else{
			holder.getLatitude().setText("γ��:  "+"����");
		}
		
		if(data.get(position).getBASE_LONGITUDE()!=null&&!"".equals("γ��:  "+data.get(position).getBASE_LONGITUDE())){
			holder.getLontitude().setText("����:  "+data.get(position).getBASE_LONGITUDE());
		}else{
			holder.getLontitude().setText("����:  "+"����");
		}
		if(data.get(position).getBASE_LATIUDE()==null||"".equals(data.get(position).getBASE_LATIUDE())||
				data.get(position).getBASE_LONGITUDE()==null||"".equals(data.get(position).getBASE_LONGITUDE())){
			holder.getBadgeView().setText("δ�ɼ�");
			holder.getBadgeView().setBackgroundResource(R.drawable.gps_background_red);
		}else{
			holder.getBadgeView().setText("�Ѳɼ�");
			holder.getBadgeView().setBackgroundResource(R.drawable.gps_background_blue);
		}
		holder.getBadgeView().show();
		holder.getTurn_to_detail().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context,
						MeterUserDetailActivity.class);
				intent.putExtra("action", "GPSCollectorActivity");
				intent.putExtra("usInfo", data.get(position));	
				context.startActivity(intent);
			}
		});
		holder.getGps_turntomap().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, GpsMapCollectorActivity.class);
				intent.setAction("GPSMeterFragment");
				intent.putExtra("usInfo", data.get(position));
				intent.putExtra("position", position);
				context.startActivity(intent);
				
			}
		});
		//mListener.callBackItem(position, data.get(position));
		return convertView;
	}
	
	class ViewHolder{
		private TextView itemNum;
		private TextView usName;
		private TextView usId;
		private TextView meterId;
		private TextView usAddress;
		private TextView latitude;
		private TextView lontitude;
		private LinearLayout turn_to_detail;
		private ImageView gps_turntomap;
		private BadgeView badgeView;
		public ViewHolder(View convertView){
			itemNum = (TextView) convertView.findViewById(R.id.gps_lv_item_userId);
			usName = (TextView) convertView.findViewById(R.id.gps_user_name);
			usId = (TextView) convertView.findViewById(R.id.gps_user_id);
			meterId = (TextView) convertView.findViewById(R.id.gps_user_meterId);
			usAddress = (TextView) convertView.findViewById(R.id.gps_user_addres);
			latitude = (TextView) convertView.findViewById(R.id.gps_latitude);
			lontitude = (TextView) convertView.findViewById(R.id.gps_lontitude);
			turn_to_detail = (LinearLayout) convertView.findViewById(R.id.turn_to_detail);
			gps_turntomap = (ImageView) convertView.findViewById(R.id.gps_turntomap);
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
		public TextView getLatitude() {
			return latitude;
		}
		public void setLatitude(TextView latitude) {
			this.latitude = latitude;
		}
		public TextView getLontitude() {
			return lontitude;
		}
		public void setLontitude(TextView lontitude) {
			this.lontitude = lontitude;
		}
		public ImageView getGps_turntomap() {
			return gps_turntomap;
		}
		public void setGps_turntomap(ImageView gps_turntomap) {
			this.gps_turntomap = gps_turntomap;
		}
		public BadgeView getBadgeView() {
			return badgeView;
		}
		public void setBadgeView(BadgeView badgeView) {
			this.badgeView = badgeView;
		}
		public LinearLayout getTurn_to_detail() {
			return turn_to_detail;
		}
		public void setTurn_to_detail(LinearLayout turn_to_detail) {
			this.turn_to_detail = turn_to_detail;
		}
	}
    public interface CallBackItemListener{
        void callBackItem(int position, UsersInfo info);
    }
    public class GPSMeterHandler extends Handler{
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			switch (Integer.parseInt(bundle.getString("key"))) {
			case 1:
				UsersInfo info = (UsersInfo) bundle.getSerializable("usInfo");
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
