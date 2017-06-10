package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.thinker_soft.R;

import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;

public class BookDataAdapter extends BaseAdapter {
	private Context context;
	private List<BookInfo> list;
	private LayoutInflater inflater;
	public BookDataAdapter(Context context,List<BookInfo> list){
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
		 
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.down_lv_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.textViewchaobiao);
			tv.setText(list.get(position).getBOOK());
		}else{
			TextView tv = (TextView) convertView.findViewById(R.id.textViewchaobiao);
			tv.setText(list.get(position).getBOOK());
		}
		return convertView;
	}

}
