package com.example.administrator.thinker_soft.myfirstpro.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.ActualMissionNEW;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.threadsocket.SocketInteraction;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;
import com.example.administrator.thinker_soft.myfirstpro.util.JaugeInternetState;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.MyDialog;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;
import com.example.administrator.thinker_soft.niftydialogeffects.Effectstype;
import com.example.administrator.thinker_soft.niftydialogeffects.NiftyDialogBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * ���������� �޸ģ�û�н�������ǰ�ǲ��ܵ����� ����������жϸ����Ƿ��������û�еĻ���ֻ��ʾ���������񡱣������������˽��Ⱥ��ܽ��ȶ���ɡ��ѽ��ա���
 * ��ʱ���������񡱱�ɡ��ύ���񡱣��ж��Ƿ����������꣬û�еĻ�����������Ϊ���ϴ��������ꡱ
 * 
 * 
 * @author Administrator
 * 
 */
public class newWorkXiangQing extends Activity implements OnClickListener {
	private String TAG = "newWorkXiangQing";
	// NW����new work
	private TextView shuxian;
	private LinearLayout NW_daohang_lin;
	private LinearLayout NW_jieshou_lin;
	private TextView NW_name;// ��������
	private TextView NW_miaosu;// ��������
	private TextView NW_address;// �����ַ
	private TextView NW_shebei;// �����豸
	private TextView NW_time_start;// ����ʼʱ��
	private TextView NW_geren_state;// ��������״̬
	private TextView NW_zong_state;// ������״̬
	private TextView NW_beizhu;// ����ע
	private TextView NW_juli;// ����
	private TextView NW_daohang;// ����
	private TextView NW_jieshou;// ��������
	private TextView NW_zuobiao;// ����
	private TextView NW_workNumber;// ������
	private LinearLayout NW_backButton;
	private Intent intent;
	private String[] xiangQing = new String[19];
	private String daohang_zuobaio;
	private SharedPreferences sharedPreferences;
	public String startLat = "";
	public String startLon = "";
	private String ip, port;
	private int clickCount;
	private Map<Integer, Boolean> dialogControl = new HashMap<Integer, Boolean>();
	private String WorkID;
	private String SystemUserId;
	private String operName = "NANBU";
	private String state;
	private boolean NW_jieshou_click = false;
	private boolean NW_daohang_click = false;
	private Dialog downDialog;
	private String JSRW_state = null;// �ж��ǽ����������ύ����
	private boolean isFresh = false;// ���ص���һ������ʱ���Ƿ����
	private NiftyDialogBuilder dialogBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.work_xiangqing_layout);
		init();
	}

	private void init() {
		intent = getIntent();
		xiangQing = intent.getStringArrayExtra("newWork");
		NW_name = (TextView) findViewById(R.id.NW_name);
		NW_daohang_lin = (LinearLayout) findViewById(R.id.NW_daohang_lin);
		NW_daohang_lin.setOnClickListener(this);
		NW_jieshou_lin = (LinearLayout) findViewById(R.id.NW_jieshou_lin);
		NW_jieshou_lin.setOnClickListener(this);
		NW_workNumber = (TextView) findViewById(R.id.NW_workNumber);
		NW_jieshou = (TextView) findViewById(R.id.NW_jieshou);
		NW_zuobiao = (TextView) findViewById(R.id.NW_zuobiao);
		NW_zong_state = (TextView) findViewById(R.id.NW_zong_state);
		NW_daohang = (TextView) findViewById(R.id.NW_daohang);
		NW_miaosu = (TextView) findViewById(R.id.NW_miaosu);
		NW_address = (TextView) findViewById(R.id.NW_address);
		NW_shebei = (TextView) findViewById(R.id.NW_shebei);
		shuxian = (TextView) findViewById(R.id.shuxian);
		NW_time_start = (TextView) findViewById(R.id.NW_time_start);
		NW_geren_state = (TextView) findViewById(R.id.NW_geren_state);
		NW_juli = (TextView) findViewById(R.id.NW_juli);
		NW_beizhu = (TextView) findViewById(R.id.NW_beizhu);
		NW_backButton = (LinearLayout) findViewById(R.id.NW_backButton);
		NW_backButton.setOnClickListener(this);
		NW_name.setText(xiangQing[2]);
		state = xiangQing[16].trim();
		Log.e(TAG, "state=" + state);
		if ("δ����".equals(state)) {
			NW_jieshou.setText("��������");
			NW_daohang_lin.setVisibility(View.GONE);
			shuxian.setVisibility(View.GONE);
		} else if ("�ѽ���".equals(state)) {
			NW_jieshou.setText("�ύ����");
		} else if ("�����".equals(state) || "�ѹ鵵".equals(state)) {
			NW_jieshou_lin.setVisibility(View.GONE);
			shuxian.setVisibility(View.GONE);
		}

		// �������Ϊ�գ���ѵ�����ť��Ϊ���ɼ����ꡱ��ť
		if ((xiangQing[6] == null || "".equals(xiangQing[6]))
				&& (xiangQing[7] == null || "".equals(xiangQing[7]))) {
			NW_daohang.setText("�ɼ���������");
		} else {
			NW_daohang.setText("����");
		}
		daohang_zuobaio = NW_daohang.getText().toString().trim();
		if ((xiangQing[6] != null || !"".equals(xiangQing[6]))
				&& (xiangQing[7] != null || !"".equals(xiangQing[7]))) {
			NW_zuobiao.setText(xiangQing[6] + "    " + xiangQing[7]);
		}
		NW_zong_state.setText(xiangQing[10]);
		NW_miaosu.setText(xiangQing[3]);
		NW_address.setText(xiangQing[4]);
		NW_shebei.setText(xiangQing[11]);
		NW_time_start.setText(xiangQing[8]);
		NW_geren_state.setText(xiangQing[16]);
		NW_beizhu.setText(xiangQing[17]);
		NW_juli.setText(xiangQing[18]);
		NW_workNumber.setText(xiangQing[1]);
		WorkID = xiangQing[1];
		// ��������
		
		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		String sb = sharedPreferences.getString("sb", "");
		if(sb==null||"".equals(sb)){
			Toast.makeText(getApplicationContext(), "û�п��þ�γ��", Toast.LENGTH_SHORT).show();
			return;
		}
		int loc = sb.indexOf("$");
		startLat = sb.substring(0, loc-1);
		startLon = sb.substring(loc+1, sb.length());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.NW_backButton:
			Intent intent = new Intent(this, ActualMissionNEW.class);
			intent.putExtra("isFresh", isFresh);
			setResult(1, intent);
			this.finish();

			break;

		case R.id.NW_jieshou_lin:
			NW_jieshou_click = true;
			NW_daohang_click = false;
			Log.e(TAG, "�������");
			if ("δ����".equals(state)) {
				Log.e(TAG, "δ����");
				JSRW_state = "1";
				uploadCoordinate();
			} else if ("�ѽ���".equals(state)) {
				Log.e(TAG, "δ���");
				JSRW_state = "3";
				uploadCoordinate();
			}
			break;
		case R.id.NW_daohang_lin:// ���� ���� �Ѽ�����
			NW_daohang_click = false;
			NW_jieshou_click = false;
			Log.e(TAG, "�������");
			if (("".equals(startLat) || startLat == null)
					|| ("".equals(startLon) || startLon == null)) {
				Mytoast.showToast(this, "���û�п������ض�λ���޷��������뿪����λ", 2000);
				break;
			}
			if ((xiangQing[6] == null || "".equals(xiangQing[6]))// û�����������ʱ���ܵ�������Ҫ�ϴ�����
					&& (xiangQing[7] == null || "".equals(xiangQing[7]))) {
				Log.v(TAG, "daohang_zuobaio=" + daohang_zuobaio);
				dialogBuilder = new NiftyDialogBuilder(newWorkXiangQing.this,
						R.style.dialog_untran);
				dialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
						.withDividerColor("#999999")
						.withMessage("��Ҫ���ռ��������꣬��ȷ���ϴ�����������")
						.withMessageColor("#000000")
						.isCancelableOnTouchOutside(true).withDuration(700)
						.withEffect(Effectstype.Slidetop).withButton1Text("ȡ��")
						.withButton2Text("ȷ��")
						.setButton1Click(new OnClickListener() {
							@Override
							public void onClick(View v) {

								dialogBuilder.dismiss();
							}
						}).setButton2Click(new OnClickListener() {
							@Override
							public void onClick(View v) {
								uploadCoordinate();// �ϴ�����
								dialogBuilder.dismiss();
							}
						}).show();

			} else {// ���ж��Ƿ��б������꣬û�б���������ʾû������

				if (Gadget.isSDCardExist()) {
					String endLat = xiangQing[7];
					String endLon = xiangQing[6];
					if(startLat==null||"".equals(startLat)){
						startLat = "0";
					} if(startLon==null||"".equals(startLon)){
						startLon = "0";
					}
					if(endLon==null||"".equals(endLon)){
						endLon = "0";
					} if(endLat==null||"".equals(endLat)){
						endLat = "0";
					}
				} else {
					Mytoast.showToast(newWorkXiangQing.this,
							"��⵽����ֻ�ȱ��SD��������ʹ�õ������ܣ�", Toast.LENGTH_SHORT);
				}
			}
			NW_daohang_click = false;
			break;

		default:
			break;
		}
	}

	public void uploadCoordinate() {
		clickCount++;
		dialogControl.put(clickCount, true);
		Log.e("xxq", "MyDialog--------------");
		downDialog = MyDialog.createLoadingDialog(newWorkXiangQing.this,
				"�ϴ�������......");
		downDialog.setCancelable(false);// ���ؼ�ȡ��
		downDialog.setCanceledOnTouchOutside(false);// ����
		downDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				dialogControl.put(clickCount, false);
			}
		});
		if (downDialog != null)
			downDialog.show();
		if (!JaugeInternetState.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(this, "�����������WIFI������������", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {

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
					SharedPreferences preferences = getApplication()
							.getSharedPreferences("config", 0);
					SystemUserId = preferences.getString("SystemUserId", "");
					Log.e(TAG, "SystemUserId=" + SystemUserId);
					boolean singal = false;// �ж���;�Ƿ����
					String parameter = null;
					/**
					 * �ж����ϴ����껹�ǽ�����������ύ����
					 */
					Log.e(TAG, "NW_daohang_click=" + NW_daohang_click
							+ "  NW_jieshou_click=" + NW_jieshou_click);
					if (NW_daohang_click == false && NW_jieshou_click == false) {// �������
						// �������� startLon =���� startLat=γ��
						parameter = AssembleUpmes.uploadCoordinate(
								xiangQing[0], startLon, startLat);
						Log.e(TAG, "�ϴ�����" + "  parameter=" + parameter);
					} else if (NW_jieshou_click == true
							&& NW_daohang_click == false) {// �����������
						Log.e(TAG, "��������");
						parameter = AssembleUpmes.uploadWorkState(xiangQing[0],
								SystemUserId, JSRW_state);
					}
					Log.e("xxq", "parameter=" + parameter);
					SocketInteraction areaSocket = new SocketInteraction(
							getApplicationContext(), Integer.parseInt(port),
							ip, operName, parameter, dataHandler);
					singal = areaSocket.DataDownLoadConn();// ����
					areaSocket.closeConn();// �Ͽ�����
					if (dialogControl.size() > 0) {
						if (dialogControl.get(loc) == true) {
							if (singal == true) {// �ɹ�
								dataHandler.post(new Runnable() {
									@Override
									public void run() {
										downDialog.dismiss();
										NW_daohang_click = false;
										NW_jieshou_click = false;
									}
								});
							} else {// ʧ��
								dataHandler.post(new Runnable() {
									@Override
									public void run() {
										downDialog.dismiss();
										NW_daohang_click = false;
										NW_jieshou_click = false;
										Mytoast.showToast(
												newWorkXiangQing.this,
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
			if (dialogControl.size() > 0) {
				Log.v("ZHOUTAO", "��ǰ�߳���ţ�" + loc);
				if (dialogControl.get(loc) == true) {
					String requestZuoBiaoJson = JsonAnalyze.analyzeData(data);// ����������Ľ��
					Log.i(TAG, "requestZuoBiaoJson=" + requestZuoBiaoJson);
					if (NW_daohang_click == false && NW_jieshou_click == false) {// �������
						// ��������
						if ("����״̬ʧ��".equals(requestZuoBiaoJson)) {
							Mytoast.showToast(newWorkXiangQing.this,
									"�ϴ�����ʧ�ܣ�������", 2000);
						} else {
							Mytoast.showToast(newWorkXiangQing.this, "�ϴ�����ɹ�",
									2000);
							xiangQing[6] = startLon;
							xiangQing[7] = startLat;
							NW_daohang.setText("����");
							NW_zuobiao.setText(startLon + "    " + startLat);
							isFresh = true;
						}
					} else if (NW_jieshou_click == true
							&& NW_daohang_click == false) {// �����������
						if ("1".equals(JSRW_state)) {
							Mytoast.showToast(newWorkXiangQing.this,
									"��������ɹ����������ز���", 2000);
							NW_jieshou.setText("�ύ����");
							state = "�ѽ���";
							NW_geren_state.setText("�ѽ���");
							NW_zong_state.setText("�ѽ���");
							NW_daohang_lin.setVisibility(View.VISIBLE);
							shuxian.setVisibility(View.VISIBLE);
							isFresh = true;
						} else if ("3".equals(JSRW_state)) {
							Mytoast.showToast(newWorkXiangQing.this, "�ύ����ɹ�",
									2000);
							NW_geren_state.setText("�����");
							NW_jieshou_lin.setVisibility(View.GONE);
							shuxian.setVisibility(View.GONE);
							isFresh = true;

						}
					}

				}
			}
		}
	}

	/**
	 * 
	 * �˵������ؼ���Ӧ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, ActualMissionNEW.class);
			intent.putExtra("isFresh", isFresh);
			setResult(1, intent);
			this.finish();
		}
		return false;
	}


}
