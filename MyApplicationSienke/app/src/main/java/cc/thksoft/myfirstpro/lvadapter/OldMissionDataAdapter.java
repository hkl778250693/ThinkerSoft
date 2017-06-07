package cc.thksoft.myfirstpro.lvadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.thinker_soft.R;

import java.util.List;

public class OldMissionDataAdapter extends BaseAdapter {
	private List<String[]> oldWork_lists;
	private Context context;
	private LayoutInflater inflater;

	public OldMissionDataAdapter(Context context, List<String[]> oldWork_lists) {
		this.context = context;
		this.oldWork_lists = oldWork_lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return oldWork_lists.size();
	}

	@Override
	public Object getItem(int position) {

		return oldWork_lists.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

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
		viewholder.distance.setText(oldWork_lists.get(position)[18].trim());
		viewholder.task_name.setText(oldWork_lists.get(position)[2].trim());
		Log.e("xxq", "��������=" + oldWork_lists.get(position)[2].trim()
				+ "  ������=" + oldWork_lists.get(position)[1].trim());
		viewholder.work_number.setText(oldWork_lists.get(position)[1].trim());
		viewholder.work_miaosu.setText(oldWork_lists.get(position)[3].trim());
		viewholder.work_address.setText(oldWork_lists.get(position)[4].trim());
		viewholder.work_state.setText("�ܽ��ȣ�"
				+ oldWork_lists.get(position)[10].trim());
		viewholder.geren_jindu.setText("���˽��ȣ�"
				+ oldWork_lists.get(position)[16].trim());
		viewholder.work_time.setText(oldWork_lists.get(position)[8].trim());

		return convertView;
	}

	public List<String[]> getDataList() {
		return this.oldWork_lists;
	}

	public void setDataList(List<String[]> oldWork_lists) {
		this.oldWork_lists = oldWork_lists;
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