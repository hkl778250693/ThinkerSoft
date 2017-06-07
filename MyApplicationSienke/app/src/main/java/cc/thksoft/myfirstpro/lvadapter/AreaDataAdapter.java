package cc.thksoft.myfirstpro.lvadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import cc.thksoft.myfirstpro.entity.AreaInfo;

public class AreaDataAdapter extends BaseAdapter {
	private Context context;
	private List<AreaInfo> list;
	private LayoutInflater inflater;
	public AreaDataAdapter(Context context,List<AreaInfo> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.down_lv_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.textViewchaobiao);
			tv.setText(list.get(position).getArea());
		}else{
			TextView tv = (TextView) convertView.findViewById(R.id.textViewchaobiao);
			tv.setText(list.get(position).getArea());
		}
		return convertView;
	}

}
