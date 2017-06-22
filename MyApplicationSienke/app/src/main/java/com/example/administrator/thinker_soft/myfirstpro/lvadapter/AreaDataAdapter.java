package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterAreaViewHolder;
import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;

import java.util.HashMap;
import java.util.List;

public class AreaDataAdapter extends BaseAdapter {
	private Context context;
	private List<AreaInfo> list;
	private LayoutInflater inflater;
	private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

	public AreaDataAdapter(Context context,List<AreaInfo> list){
		this.context = context;
		this.list = list;
		if (context != null) {
			inflater = LayoutInflater.from(context);
		}
		// 默认为不选中
		initCheck(false);
	}

	// 初始化map集合
	public void initCheck(boolean flag) {
		for (int i = 0; i < list.size(); i++) {  // map集合的数量和list的数量是一致的
			getIsCheck().put(i, flag);  // 设置默认的显示
		}
	}

	public static HashMap<Integer, Boolean> getIsCheck() {
		return isCheck;
	}

	public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
		AreaDataAdapter.isCheck = isCheck;
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
		MeterAreaViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MeterAreaViewHolder();
			convertView = inflater.inflate(R.layout.meter_area_listview_item, null);
			viewHolder.meterAreaName = (TextView) convertView.findViewById(R.id.meter_name);
			viewHolder.checkedState = (CheckBox) convertView.findViewById(R.id.check_state);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (MeterAreaViewHolder) convertView.getTag();
		}
		AreaInfo item = list.get(position);
		viewHolder.meterAreaName.setText(item.getArea());
		viewHolder.checkedState.setChecked(getIsCheck().get(position));
		return convertView;
	}

}
