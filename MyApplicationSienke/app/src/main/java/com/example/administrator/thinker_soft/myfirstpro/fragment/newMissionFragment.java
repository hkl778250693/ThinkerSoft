package com.example.administrator.thinker_soft.myfirstpro.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshBase;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.lvadapter.NewMissionDataAdapter;
import com.example.administrator.thinker_soft.myfirstpro.refreshListView.RefreshPullToRefreshListView;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.android_cbjactivity.ActualMissionNEW;

public class newMissionFragment extends Fragment implements OnClickListener {
	private String TAG = "newMissionFragment";
	private LayoutInflater inflater;
	private View newView;
	private LinearLayout notask;
	private RefreshPullToRefreshListView new_mission_listview;
	public LinearLayout new_mission_control_choose;
	private TextView mission_choice_cancel;
	private TextView new_mission_choice_all;
	public LinearLayout new_mission_control_delete;
	private NewMissionDataAdapter adapter;
	private Context context;
	public boolean isDelete_newMissionFragment = false;// �����ж϶�ѡbutton�Ƿ���ʾ
	public List<String[]> newWork_lists;

	/***
	 * ��������ˢ��
	 */
	private Dialog downDialog;
	private String SystemUserId;
	private String ip = "";
	private String port = "";
	private int clickCount;
	private Map<Integer, Boolean> dialogControl = new HashMap<Integer, Boolean>();
	private String operName = "NANBU";
	private String requestWorkJson;
	private List<Map<String, String>> ALLworkList_before = new ArrayList<Map<String, String>>();
	public List<String[]> allWork_lists = new ArrayList<String[]>();
	private Double mLat1;
	private Double mLat2;
	private Double mLon1;
	private Double mLon2;
	private MyApplication myapp;

	// ------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			newWork_lists = (List<String[]>) savedInstanceState
					.getSerializable("newWork_lists");
		} else {
			myapp = (MyApplication) getActivity().getApplication();
			newWork_lists = ((ActualMissionNEW) getActivity())
					.getAllWork_lists();
			Log.e(TAG, "��õ�newWork_lists=" + newWork_lists);
		}
		context = getActivity().getApplicationContext();
		inflater = getActivity().getLayoutInflater();
		newView = inflater.inflate(R.layout.fragment_newmission,
				(ViewGroup) getActivity().findViewById(R.id.mission_viewpager),
				false);
		guiwdaget();
		init();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("onSaveInstanceState", "onSaveInstanceState");
		outState.putSerializable("newWork_lists", (Serializable) newWork_lists);
	}

	private void guiwdaget() {
		notask = (LinearLayout) newView.findViewById(R.id.notask);
		new_mission_listview = (RefreshPullToRefreshListView) newView
				.findViewById(R.id.new_mission_listview);
		new_mission_listview.setMode(RefreshPullToRefreshBase.Mode.PULL_FROM_START);// ����ˢ��ģʽ ���� ���� Ĭ������
		new_mission_listview.setOnRefreshListener(onRefreshListener2);
		new_mission_control_choose = (LinearLayout) newView
				.findViewById(R.id.new_mission_control_choose);
		mission_choice_cancel = (TextView) newView
				.findViewById(R.id.mission_choice_cancel);
		// new_mission_choice_condition = (EditText) newView
		// .findViewById(R.id.new_mission_choice_condition);
		new_mission_choice_all = (TextView) newView
				.findViewById(R.id.new_mission_choice_all);
		// new_mission_control_delete_textview = (TextView) newView
		// .findViewById(R.id.new_mission_control_delete_textview);
		new_mission_control_delete = (LinearLayout) newView
				.findViewById(R.id.new_mission_control_delete);

		mission_choice_cancel.setOnClickListener(this);
		new_mission_choice_all.setOnClickListener(this);
		new_mission_control_delete.setOnClickListener(this);
		new_mission_listview.setOnItemClickListener(new OnItemClickListener() {// Item����¼�
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dialogControl.put(clickCount, false);
						new_mission_listview.onRefreshComplete();
						if (newWork_lists != null && newWork_lists.size() != 0) {
							Log.e(TAG, "position=" + position + "  ������="
									+ newWork_lists.get(position - 1)[2]);
							Intent intent = new Intent(getActivity(),
									newWorkXiangQing.class);
							intent.putExtra("newWork",
									newWork_lists.get(position - 1));
							getActivity().startActivityForResult(intent, 0);
						}

					}
				});

	}

	public void init() {
		if (newWork_lists == null || newWork_lists.size() == 0) {
			notask.setVisibility(View.VISIBLE);
		} else {
			notask.setVisibility(View.GONE);
			adapter = new NewMissionDataAdapter(context, newWork_lists);
			new_mission_listview.setAdapter(adapter);
			// adapter.notifyDataSetChanged();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup group = (ViewGroup) newView.getParent();
		if (group != null) {
			group.removeAllViewsInLayout();
		}
		return newView;
	}

	@Override
	public void onResume() {
		Log.e(TAG, "onResume--------------------");
		Log.w(TAG, "onResume--------------------");
		super.onResume();

	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy--------------------");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int key = v.getId();
		switch (key) {

		default:
			break;
		}
	}

	// ˢ�µ�ʱ��
	private RefreshPullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new RefreshPullToRefreshBase.OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(
				RefreshPullToRefreshBase<View> refreshView) {
			Log.e(TAG, "����ˢ��");
			notask.setVisibility(View.GONE);
			requestWork_fresh();
			// new_mission_listview.onRefreshComplete();
		}

		@Override
		public void onPullUpToRefresh(RefreshPullToRefreshBase<View> refreshView) {
			Log.e(TAG, "��������");
			/**
			 * ��ʱû������ҳ
			 */
			new_mission_listview.onRefreshComplete();
		}
	};

	/**
	 * ������������ ���ж��Ƿ�������
	 */
	public void requestWork_fresh() {
		if (!JaugeInternetState.isNetworkAvailable(getActivity()
				.getApplicationContext())) {
			Toast.makeText(getActivity(), "�����������WIFI������������",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			SharedPreferences sharedPreferences = getActivity()
					.getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
			if (sharedPreferences.contains("ip")) {
				ip = sharedPreferences.getString("ip", "");
			}
			if (sharedPreferences.contains("port")) {
				port = sharedPreferences.getString("port", "");
			}
			clickCount++;
			dialogControl.put(clickCount, true);
			Log.e("xxq", "MyDialog--------------");
			final MyHandler dataHandler = new MyHandler();
			new Thread() {
				@Override
				public void run() {
					super.run();
					int loc = clickCount;
					SharedPreferences preferences = getActivity()
							.getApplication().getSharedPreferences("config", 0);
					SystemUserId = preferences.getString("SystemUserId", "");
					boolean singal = false;// �ж���;�Ƿ����
					String parameter = AssembleUpmes
							.reqtTasksParameter(SystemUserId);
					Log.e("xxq", "parameter=" + parameter);
					SocketInteraction areaSocket = new SocketInteraction(
							getActivity().getApplicationContext(),
							Integer.parseInt(port), ip, operName, parameter,
							dataHandler);
					singal = areaSocket.DataDownLoadConn();// ����
					areaSocket.closeConn();// �Ͽ�����
					if (dialogControl.size() > 0) {
						if (dialogControl.get(loc) == true) {
							if (singal == true) {// �ɹ�
								dataHandler.post(new Runnable() {
									@Override
									public void run() {

									}
								});
							} else {// ʧ��
								dataHandler.post(new Runnable() {
									@Override
									public void run() {
										if (downDialog != null) {
											downDialog.dismiss();
										}
										new_mission_listview
												.onRefreshComplete();
										Mytoast.showToast(getActivity(),
												"������δ��Ӧ������֤IP�Ͷ˿��Ƿ�����", 2000);
									}
								});
							}
						}
					}
				}
			}.start();
		}
	}

	class MyHandler extends Handler {
		/**
		 * ��ȡsocket�߳�����
		 */
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int loc = clickCount;// ��ǰ�̵߳����
			Bundle bundle = msg.getData();
			String data = bundle.getString("data");
			Log.e(TAG, "data=" + data);
			if (data != null && "û������".equals(data)) {
				new_mission_listview.onRefreshComplete();
				if (adapter != null) {
					Log.e(TAG, "׼��ˢ��");
					ALLworkList_before.clear();
					myapp.setNewWorkList(ALLworkList_before);
					newWork_lists.clear();
					adapter.notifyDataSetChanged();
				} else {
					Log.e(TAG, "adapter == null");
				}
				if (downDialog != null) {
					downDialog.dismiss();
				}
				((ActualMissionNEW) getActivity()).getBadgeView().hide();
				notask.setVisibility(View.VISIBLE);
				return;
			}
			if (dialogControl.size() > 0) {
				Log.v("ZHOUTAO", "��ǰ�߳���ţ�" + loc);

				if (dialogControl.get(loc) == true) {
					requestWorkJson = JsonAnalyze.analyzeData(data);// ����������Ľ��
					Log.i(TAG, "requestWorkJson=" + requestWorkJson);
					// Log.v("xxq", "login_json=" + login_json);
					// TODO
					try {
						ALLworkList_before = JsonAnalyze
								.jsonTaskData(requestWorkJson);
						myapp.setNewWorkList(ALLworkList_before);
						ALLworkList_before_fenlei();
						newWork_lists.clear();
						newWork_lists.addAll(allWork_lists);
						if (newWork_lists == null || newWork_lists.size() == 0) {
							notask.setVisibility(View.VISIBLE);
						} else {
							notask.setVisibility(View.GONE);
							if (adapter == null) {
								adapter = new NewMissionDataAdapter(context,
										newWork_lists);
								new_mission_listview.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
							Mytoast.showToast(getActivity(), "ˢ�³ɹ�", 1000);
							((ActualMissionNEW) getActivity()).getBadgeView()
									.hide();
						}
						new_mission_listview.onRefreshComplete();
						if (downDialog != null) {
							downDialog.dismiss();
						}
					} catch (JSONException e) {
						e.printStackTrace();

					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void ALLworkList_before_fenlei() {
		allWork_lists.clear();
		for (int i = 0; i < ALLworkList_before.size(); i++) {
			String[] result = new String[19];
			Map<String, String> localMap = ALLworkList_before.get(i);
			result[0] = localMap.get("N_TASK_ID");// �������
			result[1] = localMap.get("C_TASK_NUMBER");// ������
			result[2] = localMap.get("C_TASK_NAME");// ��������
			result[3] = localMap.get("C_TASK_CONTENT");// ��������
			result[4] = localMap.get("C_TASK_ADDRESS");// �����ַ
			result[5] = localMap.get("C_TASK_TYPE_NAME");//
			result[6] = localMap.get("C_TASK_X");// ���񾭶�
			result[7] = localMap.get("C_TASK_Y");// ����γ��

			// 2��ľ�γ�ȣ�������2��֮��ľ��루���ص������ľ��룩
			SharedPreferences sharedPreferences = getActivity()
					.getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
			String sb = sharedPreferences.getString("sb", "");
			if (sb == null || "".equals(sb)) {
				Toast.makeText(context, "û�п��þ�γ��", Toast.LENGTH_SHORT).show();
				return;
			}
			int loc = sb.indexOf("$");
			String Latitude = sb.substring(0, loc - 1);
			String Longitude = sb.substring(loc + 1, sb.length());
			if ((result[6] == null || "".equals(result[6]))
					&& (result[7] == null || "".equals(result[7]))) {
				result[18] = "";
			} else {
				mLat1 = Double.valueOf(Double.parseDouble(Latitude));
				mLon1 = Double.valueOf(Double.parseDouble(Longitude));
				mLat2 = Double.valueOf(Double.parseDouble(localMap
						.get("C_TASK_Y")));
				mLon2 = Double.valueOf(Double.parseDouble(localMap
						.get("C_TASK_X")));
				double d = DistanceUtil.getDistance(
						new LatLng(mLat1.doubleValue(), mLon1.doubleValue()),
						new LatLng(mLat2.doubleValue(), mLon2.doubleValue()));
				int j = (int) (d / 1000.0D);
				int k = (int) d % 1000;
				result[18] = (String.valueOf(j) + "." + String.valueOf(k) + "km");
			}

			result[8] = localMap.get("D_TASK_TIME");// �����·�ʱ��
			result[9] = localMap.get("D_TASK_MAN");// �����·���
			result[10] = localMap.get("TASK_STATUS");// ����״̬
														// 0��δ����:1��δ��ɡ�:2����ע����3������ɡ���4���ѹ鵵

			result[11] = localMap.get("C_TASK_EQUIPMENT_NAME");// �����豸����
																// �û���д�û�����������д������
			result[12] = localMap.get("D_PIGEONHOLE_TIME");// �鵵ʱ��
			result[13] = localMap.get("N_PERSONNEL_ID");// ��Ա���
			result[14] = localMap.get("C_USER_NAME");// ��Ա����
			result[15] = localMap.get("C_FEEDBACK");// ��Ա������Ϣ
			result[16] = localMap.get("RECEIVE_TASKS");// ��Ա״̬
			result[17] = localMap.get("C_REMARK");// ��ע

			allWork_lists.add(result);
		}
	}

	/**
	 * ��̬����listview��margin��
	 */
	public void setMargin() {
		if (isDelete_newMissionFragment == true) {
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					new_mission_listview.getLayoutParams());
			fl.setMargins(0, 50, 0, 50);
			new_mission_listview.setLayoutParams(fl);
		} else if (isDelete_newMissionFragment == false) {
			FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
					new_mission_listview.getLayoutParams());
			fl.setMargins(0, 0, 0, 0);
			new_mission_listview.setLayoutParams(fl);

		}
	}

	/**
	 * ����������ı��ʱ�򷵻ظ���
	 */
	public void Fresh() {
		downDialog = MyDialog.createLoadingDialog(getActivity(), "����������......");
		downDialog.setCancelable(true);
		downDialog.setCanceledOnTouchOutside(false);
		downDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				dialogControl.put(clickCount, false);
			}
		});
		if (downDialog != null)
			downDialog.show();
		requestWork_fresh();
		// downDialog.dismiss();

	}

	/**
	 * �ڷ��ص���һ�������ʱ��ȡ������
	 */
	public void NWcancelFresh() {
		dialogControl.put(clickCount, false);
		if (new_mission_listview != null)
			new_mission_listview.onRefreshComplete();
	}
}
