package cc.thksoft.myfirstpro.lvadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.thinker_soft.R;

import java.util.List;

public class NewMissionDataAdapter extends BaseAdapter {
	private List<String[]> newWork_lists;
	private Context context;
	private LayoutInflater inflater;

	public NewMissionDataAdapter(Context context, List<String[]> newWork_lists) {
		this.context = context;
		this.newWork_lists = newWork_lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return newWork_lists.size();
	}

	@Override
	public Object getItem(int position) {

		return newWork_lists.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Viewholder viewholder = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.mission_listview_item, null);
			viewholder = new Viewholder(convertView);
			convertView.setTag(viewholder);
		} else {
			viewholder = (Viewholder) convertView.getTag();
		}
		viewholder.distance.setText(newWork_lists.get(position)[18].trim());
		viewholder.task_name.setText(newWork_lists.get(position)[2].trim());
		Log.e("xxq", "��������=" + newWork_lists.get(position)[2].trim()
				+ "  ������=" + newWork_lists.get(position)[1].trim());
		viewholder.work_number.setText(newWork_lists.get(position)[1].trim());
		viewholder.work_miaosu.setText(newWork_lists.get(position)[3].trim());
		viewholder.work_address.setText(newWork_lists.get(position)[4].trim());
		viewholder.work_time.setText(newWork_lists.get(position)[8].trim());
		viewholder.work_state.setText("�ܽ��ȣ�"
				+ newWork_lists.get(position)[10].trim());
		if("δ����".equals(newWork_lists.get(position)[16].trim())){
			viewholder.geren_jindu.setTextColor(context.getResources().getColor(R.color.red));
		}else{
			viewholder.geren_jindu.setTextColor(context.getResources().getColor(R.color.daohang_color));
		}
		viewholder.geren_jindu.setText("���˽��ȣ�"
				+ newWork_lists.get(position)[16].trim());
        //android:textColor="#2291fa"
		// viewholder.mission_lv_cb.setVisibility(View.GONE);
		return convertView;
	}

	public List<String[]> getDataList() {
		return this.newWork_lists;
	}

	public void setDataList(List<String[]> newWork_lists) {
		this.newWork_lists = newWork_lists;
	}

	void setAllCheckboxVisible() {
		for (int i = 0; i < this.getCount(); i++) {
			this.getItem(i);
		}
	}

	public class Viewholder {
		private TextView mission_lv_name;
		private TextView distance;// ����
		private TextView task_name;// ��������
		private TextView work_number;// ������
		private TextView work_miaosu;// ��������
		private TextView work_address;// ����ص�
		private TextView work_time;// ����ʼʱ��
		private TextView work_state;// ����״̬
		private TextView geren_jindu;// ����״̬

		public Viewholder(View convertView) {

			task_name = (TextView) convertView.findViewById(R.id.task_name);
			work_state = (TextView) convertView.findViewById(R.id.work_state);
			distance = (TextView) convertView.findViewById(R.id.distance);
			work_number = (TextView) convertView.findViewById(R.id.work_number);
			work_miaosu = (TextView) convertView.findViewById(R.id.work_miaosu);
			work_address = (TextView) convertView
					.findViewById(R.id.work_address);
			work_time = (TextView) convertView.findViewById(R.id.work_time);
			geren_jindu = (TextView) convertView.findViewById(R.id.geren_jindu);
		}

		// public TextView getMission_lv_name() {
		// return mission_lv_name;
		// }
		public void setMission_lv_name(TextView mission_lv_name) {
			this.mission_lv_name = mission_lv_name;
		}

	}
}
