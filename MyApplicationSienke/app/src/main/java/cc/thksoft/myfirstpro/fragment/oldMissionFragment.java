package cc.thksoft.myfirstpro.fragment;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.thinker_soft.R;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.thksoft.myfirstpro.appcation.MyApplication;
import cc.thksoft.myfirstpro.lvadapter.OldMissionDataAdapter;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase.Mode;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase.OnRefreshListener2;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshListView;
import cc.thksoft.myfirstpro.threadsocket.SocketInteraction;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.JaugeInternetState;
import cc.thksoft.myfirstpro.util.JsonAnalyze;
import cc.thksoft.myfirstpro.util.MyDialog;
import cc.thksoft.myfirstpro.util.Mytoast;

/**
 * �������ֻ��Ϊ��ʾ���ã��������ػ��棬��ʼ������Ҫ���ز鿴���˳����½���ͬ����Ҫ��������
 * 
 * @author Administrator
 * 
 */
public class oldMissionFragment extends Fragment implements OnClickListener {
	private String TAG = "oldMissionFragment";
	private LayoutInflater inflater;
	private LinearLayout old_notask;;
	private View oldView;
	private RefreshPullToRefreshListView old_mission_listview;
	private OldMissionDataAdapter adapter;
	private Context context;
	private LinearLayout old_request_work_lin;
	private String ip = "";
	private String port = "";
	private int clickCount;
	private Map<Integer, Boolean> dialogControl = new HashMap<Integer, Boolean>();
	private String SystemUserId;
	private String operName = "NANBU";
	private String requestWorkJson;
	private List<Map<String, String>> ALLworkList_before = new ArrayList<Map<String,String>>();
	public List<String[]> oldWork_lists = new ArrayList<String[]>();
	public List<String[]> allWork_lists = new ArrayList<String[]>();
	private Double mLat1;
	private Double mLat2;
	private Double mLon1;
	private Double mLon2;
	private Dialog downDialog;
	private MyApplication myapp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		inflater = getActivity().getLayoutInflater();
		oldView = inflater.inflate(R.layout.fragment_oldmission, null);
		guiwdaget(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("onSaveInstanceState", "onSaveInstanceState");//
		outState.putSerializable("ALLworkList_before",
				(Serializable) ALLworkList_before);
	}

	@SuppressLint("ResourceAsColor")
	private void guiwdaget(Bundle savedInstanceState) {
		old_notask = (LinearLayout) oldView.findViewById(R.id.old_notask);
		downDialog = MyDialog.createLoadingDialog(getActivity(), "����������......");
		old_request_work_lin = (LinearLayout) oldView
				.findViewById(R.id.old_request_work_lin);
		old_request_work_lin.setOnClickListener(this);
		old_mission_listview = (RefreshPullToRefreshListView) oldView
				.findViewById(R.id.old_mission_listview);

		// Ĭ������
		old_mission_listview.setOnRefreshListener(onRefreshListener2);
		myapp = (MyApplication) getActivity().getApplication();
		if (myapp.getOldWorkList() == null
				|| myapp.getOldWorkList().size() == 0) {
			old_mission_listview.setMode(Mode.DISABLED);// ���ò���ˢ��
			old_mission_listview.setVisibility(View.GONE);

		} else if (myapp.getOldWorkList() != null
				&& myapp.getOldWorkList().size() != 0) {
			old_mission_listview.setMode(Mode.PULL_FROM_START);// ����ˢ��ģʽ ���� ����
			if (savedInstanceState != null) {
				ALLworkList_before = (List<Map<String, String>>) savedInstanceState
						.getSerializable("ALLworkList_before");
			} else {
				ALLworkList_before = myapp.getOldWorkList();
			}
			ALLworkList_before_fenlei();
			oldWork_lists.clear();
			oldWork_lists.addAll(allWork_lists);
			old_request_work_lin.setVisibility(View.GONE);
			if (oldWork_lists == null || oldWork_lists.size() == 0) {
				old_notask.setVisibility(View.VISIBLE);
			} else {
				old_notask.setVisibility(View.GONE);
				adapter = new OldMissionDataAdapter(context, oldWork_lists);
				old_mission_listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		}
		old_mission_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialogControl.put(clickCount, false);
				old_mission_listview.onRefreshComplete();
				if (oldWork_lists != null && oldWork_lists.size() != 0) {
					Log.e(TAG, "position=" + position + "  ������="
							+ oldWork_lists.get(position - 1)[2]);
					Intent intent = new Intent(getActivity(),
							oldWorkXiangQing.class);
					intent.putExtra("oldWork", oldWork_lists.get(position - 1));
					startActivity(intent);

				}

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup group = (ViewGroup) oldView.getParent();
		if (group != null) {
			group.removeAllViewsInLayout();
		}
		return oldView;

	}

	@Override
	public void onResume() {
		Log.e(TAG, "onResume--------------------");
		super.onResume();

	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy--------------------");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		switch (key) {
		case R.id.old_request_work_lin:// ��������
			old_mission_listview.setVisibility(View.VISIBLE);
			old_mission_listview.setMode(Mode.PULL_FROM_START);// ����ˢ��ģʽ ���� ����
			old_request_work_lin.setVisibility(View.GONE);
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
			requesOldWork();

			break;

		default:
			break;
		}
	}

	private void requesOldWork() {

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
							.reqtOldTasksParameter(SystemUserId);
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
										if (downDialog.isShowing()) {
											downDialog.dismiss();
										}
									}
								});
							} else {// ʧ��
								dataHandler.post(new Runnable() {
									@Override
									public void run() {
										if (downDialog.isShowing()) {
											downDialog.dismiss();
										}
										old_mission_listview
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
				old_mission_listview.onRefreshComplete();
				if (adapter != null) {
					ALLworkList_before.clear();
					myapp.setOldWorkList(ALLworkList_before);
					oldWork_lists.clear();
					adapter.notifyDataSetChanged();
				}
				if (downDialog.isShowing()) {
					downDialog.dismiss();
				}
				old_notask.setVisibility(View.VISIBLE);
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
						myapp.setOldWorkList(ALLworkList_before);
						ALLworkList_before_fenlei();
						oldWork_lists.clear();
						oldWork_lists.addAll(allWork_lists);
						if (oldWork_lists == null || oldWork_lists.size() == 0) {
							old_notask.setVisibility(View.VISIBLE);
						} else {
							old_notask.setVisibility(View.GONE);
							if (adapter == null) {
								adapter = new OldMissionDataAdapter(context,
										oldWork_lists);
								old_mission_listview.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
							Mytoast.showToast(getActivity(), "�������ݳɹ�", 1000);
						}
						old_mission_listview.onRefreshComplete();
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
			result[10] = localMap.get("TASK_STATUS");// ����״̬ 0��δ��� 1������� 2���ѹ鵵
														// 3��ע��
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
	 * �ڷ��ص���һ�������ʱ��ȡ������
	 */
	public void OWcancelFresh() {
		dialogControl.put(clickCount, false);
		if (old_mission_listview != null)
			old_mission_listview.onRefreshComplete();
	}

	// ˢ�µ�ʱ��
	@SuppressWarnings("rawtypes")
	private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(
				RefreshPullToRefreshBase<View> refreshView) {
			old_notask.setVisibility(View.GONE);
			requesOldWork();
		}

		@Override
		public void onPullUpToRefresh(RefreshPullToRefreshBase<View> refreshView) {
			old_mission_listview.onRefreshComplete();
		}
	};

	/**
	 * ��̬����listview��margin��
	 */
	// public void setMargin() {
	// if (isDelete_oldMissionFragment == true) {
	// FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
	// old_mission_listview.getLayoutParams());
	// fl.setMargins(0, 50, 0, 50);
	// old_mission_listview.setLayoutParams(fl);
	// } else if (isDelete_oldMissionFragment == false) {
	// FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
	// old_mission_listview.getLayoutParams());
	// fl.setMargins(0, 0, 0, 0);
	// old_mission_listview.setLayoutParams(fl);
	//
	// }
	// }

}
