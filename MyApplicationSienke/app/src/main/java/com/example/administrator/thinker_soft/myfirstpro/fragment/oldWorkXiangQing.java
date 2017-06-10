package com.example.administrator.thinker_soft.myfirstpro.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;

import java.util.HashMap;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.navigation.BNavigatorActivity;
import com.example.administrator.thinker_soft.myfirstpro.util.Gadget;

/**
 * �����������
 * 
 * @author Administrator
 * 
 */
public class oldWorkXiangQing extends Activity implements OnClickListener {
	private String TAG = "oldWorkXiangQing";
	// NW����new work
	private TextView NW_name;// ��������
	private LinearLayout NW_jieshou_lin;// ��������
	private LinearLayout NW_daohang_lin;// ��������
	private TextView NW_miaosu;// ��������
	private TextView NW_address;// �����ַ
	private TextView shuxian;//
	private TextView NW_shebei;// �����豸
	private TextView NW_time_start;// ����ʼʱ��
	private TextView NW_time_end;// ����鵵ʱ��
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
	private String startLat;
	private String startLon;
	private String ip, port;
	private int clickCount;
	private Map<Integer, Boolean> dialogControl = new HashMap<Integer, Boolean>();
	private String WorkID;
	private String SystemUserId;
	private String operName = "NANBU";
	private String state;
	private Dialog downDialog;

	// private LinearLayout OW_guidang_lin;

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
		xiangQing = intent.getStringArrayExtra("oldWork");
		// OW_guidang_lin = (LinearLayout) findViewById(R.id.OW_guidang_lin);
		NW_jieshou_lin = (LinearLayout) findViewById(R.id.NW_jieshou_lin);
		NW_daohang_lin = (LinearLayout) findViewById(R.id.NW_daohang_lin);
		NW_daohang_lin.setOnClickListener(this);
		// OW_guidang_lin.setVisibility(View.VISIBLE);
		NW_name = (TextView) findViewById(R.id.NW_name);
		shuxian = (TextView) findViewById(R.id.shuxian);
		shuxian.setVisibility(View.GONE);
		NW_workNumber = (TextView) findViewById(R.id.NW_workNumber);
		NW_workNumber = (TextView) findViewById(R.id.NW_workNumber);
		NW_time_end = (TextView) findViewById(R.id.NW_time_end);
		NW_jieshou = (TextView) findViewById(R.id.NW_jieshou);
		NW_jieshou_lin.setVisibility(View.GONE);
		NW_zuobiao = (TextView) findViewById(R.id.NW_zuobiao);
		NW_daohang = (TextView) findViewById(R.id.NW_daohang);
		NW_miaosu = (TextView) findViewById(R.id.NW_miaosu);
		NW_address = (TextView) findViewById(R.id.NW_address);
		NW_zong_state = (TextView) findViewById(R.id.NW_zong_state);
		NW_shebei = (TextView) findViewById(R.id.NW_shebei);
		NW_time_start = (TextView) findViewById(R.id.NW_time_start);
		NW_geren_state = (TextView) findViewById(R.id.NW_geren_state);
		NW_juli = (TextView) findViewById(R.id.NW_juli);
		NW_beizhu = (TextView) findViewById(R.id.NW_beizhu);
		NW_backButton = (LinearLayout) findViewById(R.id.NW_backButton);
		// NW_daohang.setOnClickListener(this);
		// NW_jieshou.setOnClickListener(this);
		NW_backButton.setOnClickListener(this);
		NW_name.setText(xiangQing[2]);
		// �������Ϊ�գ���ѵ�����ť��Ϊ���ɼ����ꡱ��ť
		if ((xiangQing[6] == null && "".equals(xiangQing[6]))
				&& (xiangQing[7] == null && "".equals(xiangQing[7]))) {
			NW_daohang.setText("�ϴ���������");
		} else {
			NW_daohang.setText("����");
		}
		NW_time_end.setText(xiangQing[12]);
		NW_zong_state.setText(xiangQing[10]);
		daohang_zuobaio = NW_daohang.getText().toString().trim();
		NW_zuobiao.setText(xiangQing[6] + "    " + xiangQing[7]);
		NW_miaosu.setText(xiangQing[3]);
		NW_address.setText(xiangQing[4]);
		NW_shebei.setText(xiangQing[11]);
		NW_time_start.setText(xiangQing[8]);
		NW_geren_state.setText(xiangQing[16]);
		NW_beizhu.setText(xiangQing[17]);
		NW_juli.setText(xiangQing[18]);
		NW_workNumber.setText(xiangQing[1]);
		state = xiangQing[16];
		if (state == "δ����") {
			NW_jieshou.setText("��������");
		} else if (state == "δ���") {
			NW_jieshou.setText("�ύ����");
		} else if (state == "�����" || state == "�ѹ鵵") {
			NW_jieshou_lin.setVisibility(View.GONE);
		}

		// ��������
		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		String sb = sharedPreferences.getString("sb", "");
		if (sb == null || "".equals(sb)) {
			Toast.makeText(getApplicationContext(), "û�п��þ�γ��",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int loc = sb.indexOf("$");
		startLat = sb.substring(0, loc - 1);
		startLon = sb.substring(loc + 1, sb.length());

		WorkID = xiangQing[1];

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.NW_backButton:
			this.finish();
			break;
		case R.id.NW_daohang_lin:// ����
			String endLat = xiangQing[7];
			String endLon = xiangQing[6];
			Log.e(TAG, "�������");
			Log.e(TAG, "γ��  startLat=" + startLat + "     ����startLon="
					+ startLon + "  xiangQing[7]=" + xiangQing[7]
					+ "   xiangQing[6]= " + xiangQing[6]);
			if (("".equals(startLat) || startLat == null)
					|| ("".equals(startLon) || startLon == null)) {
				Mytoast.showToast(this, "���û�п������ض�λ���޷��������뿪����λ", 1000);
				break;
			} 
			if (("".equals(xiangQing[7]) || xiangQing[7] == null)
					|| ("".equals(xiangQing[6]) || xiangQing[6] == null)) {
				Mytoast.showToast(this, "û��Ŀ�ĵ����꣬���ܵ���", 1000);
				break;
			} 
			// ���ж��Ƿ��б������꣬û�б���������ʾû������
				Log.v("oldWorkXiangQing.this:", "endLon-"+endLon);
				Log.v("oldWorkXiangQing.this:", "endLat-"+endLat);
				if (Gadget.isSDCardExist()) {
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
					launchNavigator(Double.parseDouble(startLon),
							Double.parseDouble(startLat),
							Double.parseDouble(endLon),
							Double.parseDouble(endLat));
				} else {
					Mytoast.showToast(oldWorkXiangQing.this,
							"��⵽����ֻ�ȱ��SD��������ʹ�õ������ܣ�", Toast.LENGTH_SHORT);
				}
			
			break;

		default:
			break;
		}

	}

	private void launchNavigator(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		BNaviPoint startPoint = new BNaviPoint(latitude1, longitude1, "���",
				BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(latitude2, longitude2, "�յ�",
				BNaviPoint.CoordinateType.BD09_MC);
		Log.e(TAG, "latitude1=" + latitude1 + "  longitude1=" + longitude1
				+ "   latitude2=" + latitude2 + "   longitude2=" + longitude2);
		BaiduNaviManager.getInstance().launchNavigator(this,
				startPoint, // ��㣨��ָ������ϵ��
				endPoint, // �յ㣨��ָ������ϵ��
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,// ��·��ʽ
				true, // ��ʵ����
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
				new OnStartNavigationListener() {

					@Override
					public void onJumpToDownloader() {
					}

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(oldWorkXiangQing.this,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
					}
				} // ��ת����
				);
	}
}
