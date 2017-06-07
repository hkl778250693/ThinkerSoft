package cc.thksoft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.thinker_soft.R;

import java.util.List;

public class BookNumberAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	public BookNumberAdapter(Context context,List<String> list){
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.book_num_lv_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.book_num_lv_tv);
			tv.setText(list.get(position));
		}else{
			TextView tv = (TextView) convertView.findViewById(R.id.book_num_lv_tv);
			tv.setText(list.get(position));
		}		
		return convertView;
	}

}
