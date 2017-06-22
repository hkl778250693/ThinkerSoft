package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.android_cbjactivity.modle.MeterBookViewHolder;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;

import java.util.HashMap;
import java.util.List;

public class BookDataAdapter extends BaseAdapter {
	private Context context;
	private List<BookInfo> list;
	private LayoutInflater inflater;
	private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

	public BookDataAdapter(Context context,List<BookInfo> list){
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
		BookDataAdapter.isCheck = isCheck;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MeterBookViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MeterBookViewHolder();
			convertView = inflater.inflate(R.layout.meter_area_listview_item, null);
			viewHolder.bookName = (TextView) convertView.findViewById(R.id.textViewchaobiao);
			viewHolder.checkedState = (CheckBox) convertView.findViewById(R.id.check_state);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (MeterBookViewHolder) convertView.getTag();
		}
		BookInfo item = list.get(position);
		viewHolder.bookName.setText(item.getBOOK());
		viewHolder.checkedState.setChecked(getIsCheck().get(position));
		return convertView;
	}
}
