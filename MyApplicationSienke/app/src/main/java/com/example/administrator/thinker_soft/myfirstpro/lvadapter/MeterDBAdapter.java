package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.thinker_soft.R;

import java.util.List;

public class MeterDBAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	public MeterDBAdapter(Context context,List<String> list){
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.meter_db_lv_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}	
		holder.meter_db_lv_tv.setText(list.get(position));
		return convertView;
	}
	class ViewHolder{
		private ImageView meter_db_lv_iv;
		private TextView meter_db_lv_tv;
		ViewHolder(View convertView){
			meter_db_lv_iv = (ImageView) convertView.findViewById(R.id.meter_db_lv_iv);
			meter_db_lv_tv = (TextView) convertView.findViewById(R.id.meter_db_lv_tv);
		}
	}
}
