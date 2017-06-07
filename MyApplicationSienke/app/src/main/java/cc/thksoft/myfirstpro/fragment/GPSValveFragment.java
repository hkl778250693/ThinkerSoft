package cc.thksoft.myfirstpro.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.thksoft.myfirstpro.entity.DeviceInfo;
import cc.thksoft.myfirstpro.entity.DeviceTypeInfo;
import cc.thksoft.myfirstpro.lvadapter.GPSValveAdapter;
import cc.thksoft.myfirstpro.lvadapter.WheelViewAdapter;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase.Mode;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshBase.OnRefreshListener2;
import cc.thksoft.myfirstpro.refreshListView.RefreshPullToRefreshListView;
import cc.thksoft.myfirstpro.threadsocket.SocketInteraction;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.JsonAnalyze;
import cc.thksoft.myfirstpro.util.MyDialog;
import cc.thksoft.myfirstpro.util.Mytoast;
import cc.thksoft.myfirstpro.wheel.WheelView;
import gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

public class GPSValveFragment extends Fragment implements OnClickListener {
	private Activity activity;
	private Context context;
	private View view;
	private static GPSValveFragment valveFragment;
	
	private LinearLayout gps_valve_listview_ll;
	private TextView gps_valve_listview_image;
	private TextView gps_valve_listview_word;
	private ImageView gps_valve_listview_progressg;
	private RefreshPullToRefreshListView gps_valve_fragment_lv;
	private ScrollView gps_valve_ll;
	private ImageView gps_page_show_bg_control;
	private boolean controlbl;

	private RelativeLayout gps_valve_devName;
	private TextView gps_valve_devName_updown;
	private EditText gps_valve_devName_edit;
	private boolean namebl;

	private RelativeLayout gps_valve_devAddress;
	private TextView gps_valve_devAddress_updown;
	private EditText gps_valve_devAddress_edit;
	private boolean addrbl;

	private RelativeLayout gps_valve_devRemark;
	private TextView gps_valve_devRemark_updown;
	private EditText gps_valve_devRemark_edit;
	private boolean remkbl;

	private RelativeLayout gps_valve_devType;
	private TextView gps_valve_devType_updown;
	private LinearLayout gps_valve_wheelview_ll;
	private TextView gps_valve_wheelview_image;
	private TextView gps_valve_wheelview_word;
	private ImageView gps_valve_wheelview_progress;
	private WheelView gps_valve_wheelview;
	private boolean typebl;

	private TextView gps_valve_btn_Done;

	
	private NiftyDialogBuilder dialogBuilder;
	private Display display;
	private Map<Integer, Boolean> dialogControl;
	private int clickCount;
	private Dialog downDialog;
	
	private SharedPreferences preferences;
	private String userId;
	private String userName;
	private String Ip;
	private String port;
	private String dbName;
	private final String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
	private MyHandler dataHandler  = new MyHandler();
	
	private GPSValveAdapter gpsValveAdapter;
	private List<DeviceTypeInfo> deviceTypeInfos;//�豸��������
	private List<DeviceInfo> deviceInfos;

	public static GPSValveFragment getInstance() {
		if (valveFragment == null) {
			valveFragment = new GPSValveFragment();
		}
		return valveFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		context = getActivity();
		guiwadget();
		init();
	}

	private void guiwadget() {
		LayoutInflater inflater = LayoutInflater.from(context);

		view = inflater.inflate(R.layout.fragment_valvecollector,
				(ViewGroup) activity.findViewById(R.id.gps_viewpager), false);
		//����
		gps_valve_listview_ll = (LinearLayout) view.findViewById(R.id.gps_valve_listview_ll);
		gps_valve_listview_image = (TextView) view.findViewById(R.id.gps_valve_listview_image);
		gps_valve_listview_word = (TextView) view.findViewById(R.id.gps_valve_listview_word);
		gps_valve_listview_progressg = (ImageView) view.findViewById(R.id.gps_valve_listview_progress);
		gps_valve_fragment_lv = (RefreshPullToRefreshListView) view
				.findViewById(R.id.gps_valve_fragment_lv);
		gps_valve_fragment_lv.setMode(Mode.PULL_FROM_START);// ����ˢ��ģʽ ���� ���� Ĭ������
		gps_valve_fragment_lv.setOnRefreshListener(onRefreshListener2);
		gps_valve_ll = (ScrollView) view.findViewById(R.id.gps_valve_ll);
		
		gps_page_show_bg_control = (ImageView) view
				.findViewById(R.id.gps_page_show_bg_control);
		//����
		gps_valve_devName = (RelativeLayout) view
				.findViewById(R.id.gps_valve_devName);
		gps_valve_devName_updown = (TextView) view
				.findViewById(R.id.gps_valve_devName_updown);
		gps_valve_devName_edit = (EditText) view
				.findViewById(R.id.gps_valve_devName_edit);
		//��ַ
		gps_valve_devAddress = (RelativeLayout) view
				.findViewById(R.id.gps_valve_devAddress);
		gps_valve_devAddress_updown = (TextView) view
				.findViewById(R.id.gps_valve_devAddress_updown);
		gps_valve_devAddress_edit = (EditText) view
				.findViewById(R.id.gps_valve_devAddress_edit);
		//��ע
		gps_valve_devRemark = (RelativeLayout) view
				.findViewById(R.id.gps_valve_devRemark);
		gps_valve_devRemark_updown = (TextView) view
				.findViewById(R.id.gps_valve_devRemark_updown);
		gps_valve_devRemark_edit = (EditText) view
				.findViewById(R.id.gps_valve_devRemark_edit);
		//TODO
		//����
		gps_valve_devType = (RelativeLayout) view
				.findViewById(R.id.gps_valve_devType);
		gps_valve_devType_updown = (TextView) view
				.findViewById(R.id.gps_valve_devType_updown);
		gps_valve_wheelview_ll = (LinearLayout) view
				.findViewById(R.id.gps_valve_wheelview_ll);
		gps_valve_wheelview_image = (TextView) view
				.findViewById(R.id.gps_valve_wheelview_image);
		gps_valve_wheelview_word = (TextView) view
				.findViewById(R.id.gps_valve_wheelview_word);
		gps_valve_wheelview_progress = (ImageView) view
				.findViewById(R.id.gps_valve_wheelview_progress);
		gps_valve_wheelview = (WheelView) view
				.findViewById(R.id.gps_valve_wheelview);
		//¼��
		gps_valve_btn_Done = (TextView) view
				.findViewById(R.id.gps_valve_btn_Done);
		gps_valve_listview_image.setOnClickListener(this);
		gps_page_show_bg_control.setOnClickListener(this);
		gps_valve_devName.setOnClickListener(this);
		gps_valve_devAddress.setOnClickListener(this);
		gps_valve_devRemark.setOnClickListener(this);
		gps_valve_devType.setOnClickListener(this);
		gps_valve_wheelview_image.setOnClickListener(this);
		gps_valve_btn_Done.setOnClickListener(this);
		gps_valve_wheelview.setCyclic(false);
		gps_valve_wheelview.setVisibleItems(3);
		if(deviceInfos==null||deviceInfos.size()==0){
			gps_valve_listview_ll.setVisibility(View.VISIBLE);
			gps_valve_listview_image.setVisibility(View.VISIBLE);
			gps_valve_listview_word.setVisibility(View.VISIBLE);
			gps_valve_listview_progressg.setVisibility(View.GONE);
			gps_valve_fragment_lv.setVisibility(View.GONE);
		}else{
			gps_valve_listview_ll.setVisibility(View.GONE);
			gps_valve_listview_image.setVisibility(View.GONE);
			gps_valve_listview_word.setVisibility(View.GONE);
			gps_valve_listview_progressg.setVisibility(View.GONE);
			gps_valve_fragment_lv.setVisibility(View.VISIBLE);
			gpsValveAdapter = new GPSValveAdapter(context,deviceInfos);
			gps_valve_fragment_lv.setAdapter(gpsValveAdapter);
		}

		
		
		// �����豸��Ϣ����,
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gps_valve_ll
				.getLayoutParams();
		params.height = 70;
		Matrix matrix = new Matrix();
		matrix.setRotate(45);
		Bitmap bp = ((BitmapDrawable) getResources().getDrawable(
				R.mipmap.valve_top_bz)).getBitmap();
		bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(),
				matrix, true);
		gps_page_show_bg_control.setImageBitmap(bp);
		// ���ü���
		gps_valve_devName_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0 != null && !"".equals(arg0.toString())) {
					gps_valve_btn_Done.setVisibility(View.VISIBLE);
				} else {
					gps_valve_btn_Done.setVisibility(View.GONE);
				}
			}
		});
	}

	private void init() {
		dialogControl = new HashMap<Integer, Boolean>();
		preferences = activity.getSharedPreferences("config", 0);
		userId = preferences.getString("SystemUserId", "");
		userName = preferences.getString("USER_NAME", "");
		preferences = activity.getSharedPreferences("IP_PORT_DBNAME", 0);
		dbName = preferences.getString("dbName", "");
		if (preferences.contains("ip")) {
			Ip = preferences.getString("ip", "");
		}
		if (preferences.contains("port")) {
			port = preferences.getString("port", "");
		}
		//�ж�IP��PORT�Ƿ�Ϊ��
		// ��ȡ��Ļ�߶�
		display = activity.getWindowManager().getDefaultDisplay();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.gps_valve_listview_image:
			gps_valve_listview_ll.setVisibility(View.VISIBLE);
			gps_valve_listview_image.setVisibility(View.GONE);
			gps_valve_listview_word.setVisibility(View.GONE);
			gps_valve_listview_progressg.setVisibility(View.VISIBLE);
			gps_valve_fragment_lv.setVisibility(View.GONE);
			Animation animation1 = AnimationUtils.loadAnimation(
					context, R.anim.loading_animation);
			gps_valve_listview_progressg.startAnimation(animation1);
			new Thread(){public void run() {
				boolean signal = true;
				String parameter = AssembleUpmes.getDevDataParameter();
				SocketInteraction areaSocket= new SocketInteraction(context,Integer.parseInt(port),Ip,userName,parameter,dataHandler);
				signal = areaSocket.DataDownLoadConn();
				if(signal==true){

				}else{
					dataHandler.post(new Runnable() {

						@Override
						public void run() {
							gps_valve_listview_ll.setVisibility(View.VISIBLE);
							gps_valve_listview_image.setVisibility(View.VISIBLE);
							gps_valve_listview_word.setVisibility(View.VISIBLE);
							gps_valve_listview_progressg.setVisibility(View.GONE);
							gps_valve_fragment_lv.setVisibility(View.GONE);
						}
					});
				}
			};}.start();
			break;
		case R.id.gps_page_show_bg_control:
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gps_valve_ll
					.getLayoutParams();
			if (controlbl == false) {
				controlbl = true;
				params.height = LayoutParams.WRAP_CONTENT;
				gps_page_show_bg_control
						.setImageResource(R.mipmap.valve_top_bz);

			} else {
				controlbl = false;
				if (gps_valve_devName_edit.getVisibility() == View.VISIBLE) {
					gps_valve_devName_edit.setVisibility(View.GONE);
					gps_valve_devName_updown
					.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				}
				if (gps_valve_devAddress_edit.getVisibility() == View.VISIBLE) {
					gps_valve_devAddress_edit.setVisibility(View.GONE);
					gps_valve_devAddress_updown
					.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				}
				if (gps_valve_devRemark_edit.getVisibility() == View.VISIBLE) {
					gps_valve_devRemark_edit.setVisibility(View.GONE);
					gps_valve_devRemark_updown
					.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				}
				params.height = 70;
				Matrix matrix = new Matrix();
				matrix.setRotate(45);
				Bitmap bp = ((BitmapDrawable) getResources().getDrawable(
						R.mipmap.valve_top_bz)).getBitmap();
				bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(),
						bp.getHeight(), matrix, true);
				gps_page_show_bg_control.setImageBitmap(bp);
			}
			break;
		case R.id.gps_valve_devName:
			if (namebl == false) {
				namebl = true;
				addrbl = false;
				remkbl = false;
				typebl = false;
				if (gps_valve_wheelview.getVisibility() == View.VISIBLE) {
					gps_valve_wheelview.setVisibility(View.GONE);
				}
				gps_valve_devName_edit.setFocusable(true);
				gps_valve_devName_edit.setCursorVisible(true);
				gps_valve_devName_edit.setVisibility(View.VISIBLE);
				gps_valve_devAddress_edit.setVisibility(View.GONE);
				gps_valve_devRemark_edit.setVisibility(View.GONE);
				gps_valve_wheelview.setVisibility(View.GONE);
				gps_valve_wheelview_ll.setVisibility(View.GONE);
				gps_valve_devName_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_down_bg);
				gps_valve_devAddress_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devRemark_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devType_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			} else {
				namebl = false;
				gps_valve_devName_edit.setVisibility(View.GONE);
				gps_valve_devName_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			}
			break;
		case R.id.gps_valve_devAddress:
			if (addrbl == false) {
				addrbl = true;
				namebl = false;
				remkbl = false;
				typebl = false;
				if (gps_valve_wheelview.getVisibility() == View.VISIBLE) {
					gps_valve_wheelview.setVisibility(View.GONE);
				}
				gps_valve_devAddress_edit.setVisibility(View.VISIBLE);
				gps_valve_devAddress_edit.setFocusable(true);
				gps_valve_devAddress_edit.setCursorVisible(true);
				gps_valve_devAddress_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_down_bg);

				gps_valve_devName_edit.setVisibility(View.GONE);
				gps_valve_devRemark_edit.setVisibility(View.GONE);
				gps_valve_wheelview.setVisibility(View.GONE);
				gps_valve_wheelview_ll.setVisibility(View.GONE);
				gps_valve_devName_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devRemark_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devType_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			} else {
				addrbl = false;
				gps_valve_devAddress_edit.setVisibility(View.GONE);
				gps_valve_devAddress_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);

			}
			break;
		case R.id.gps_valve_devRemark:
			if (remkbl == false) {
				remkbl = true;
				namebl = false;
				addrbl = false;
				typebl = false;
				if (gps_valve_wheelview.getVisibility() == View.VISIBLE) {
					gps_valve_wheelview.setVisibility(View.GONE);
				}
				gps_valve_devRemark_edit.setVisibility(View.VISIBLE);
				gps_valve_devRemark_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_down_bg);
				gps_valve_devRemark_edit.setFocusable(true);
				gps_valve_devRemark_edit.setCursorVisible(true);

				gps_valve_devName_edit.setVisibility(View.GONE);
				gps_valve_devAddress_edit.setVisibility(View.GONE);
				gps_valve_wheelview.setVisibility(View.GONE);
				gps_valve_wheelview_ll.setVisibility(View.GONE);
				gps_valve_devName_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devAddress_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devType_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			} else {
				remkbl = false;
				gps_valve_devRemark_edit.setVisibility(View.GONE);
				gps_valve_devRemark_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			}
			break;
		case R.id.gps_valve_devType:
			if (typebl == false) {
				typebl = true;
				namebl = false;
				addrbl = false;
				remkbl = false;
				gps_valve_devType_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_down_bg);
				if(deviceTypeInfos==null||deviceTypeInfos.size()==0){
					gps_valve_wheelview.setVisibility(View.GONE);
					gps_valve_wheelview_ll.setVisibility(View.VISIBLE);
					gps_valve_wheelview_image.setVisibility(View.VISIBLE);
					gps_valve_wheelview_word.setVisibility(View.VISIBLE);
					gps_valve_wheelview_progress.setVisibility(View.GONE);
				}else{
					//����wheelView����
					List<String> wheelData = new ArrayList<String>();
					for (int i = 0; i < deviceTypeInfos.size(); i++) {
						wheelData.add(deviceTypeInfos.get(i).getC_EQUIPMENT_TYPE_NAME());
					}
					gps_valve_wheelview.setAdapter(new WheelViewAdapter(deviceTypeInfos));
					gps_valve_wheelview.setVisibility(View.VISIBLE);
					gps_valve_wheelview_ll.setVisibility(View.GONE);
					gps_valve_wheelview_image.setVisibility(View.GONE);
					gps_valve_wheelview_word.setVisibility(View.GONE);
					gps_valve_wheelview_progress.setVisibility(View.GONE);
				}
				gps_valve_devName_edit.setVisibility(View.GONE);
				gps_valve_devAddress_edit.setVisibility(View.GONE);
				gps_valve_devRemark_edit.setVisibility(View.GONE);
				gps_valve_devName_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devAddress_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_devRemark_updown
				.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
			} else {
				typebl = false;
				gps_valve_devType_updown
						.setBackgroundResource(R.mipmap.valve_middle_btn_up_bg);
				gps_valve_wheelview.setVisibility(View.GONE);
				gps_valve_wheelview_ll.setVisibility(View.GONE);
			}
			break;
		case R.id.gps_valve_btn_Done:
			String prompt = "�Ƿ���Ӹ��豸��";
			if (gps_valve_devRemark_edit.getText() == null
					|| "".equals(gps_valve_devRemark_edit.getText())) {
				prompt = "ȱ�ٱ�ע˵��," + prompt;
			}
			if (gps_valve_devAddress_edit.getText() == null
					|| "".equals(gps_valve_devAddress_edit.getText())) {
				prompt = "ȱ�ٵ�ַ," + prompt;
			}
			dialogBuilder = new NiftyDialogBuilder(activity,
					R.style.dialog_untran);
			dialogBuilder.withTitle("��ܰ��ʾ:").withTitleColor("#000000")
					.withDividerColor("#999999").withMessage(prompt)
					.withMessageColor("#000000")
					.isCancelableOnTouchOutside(true).withDuration(700)
					.withEffect(Effectstype.Slidetop).withButton1Text("����¼��")
					.withButton2Text("ȷ��¼��")
					.setButton1Click(new OnClickListener() {
						@Override
						public void onClick(View v) {
							gps_valve_devName_edit.setText("");
							gps_valve_devAddress_edit.setText("");
							gps_valve_devRemark_edit.setText("");
							dialogBuilder.dismiss();

						}
					}).setButton2Click(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// �豸����������
							// �豸��Ӻ� �������� 1�������Ϣ���б��� 2�������Ϣ����������
							dialogBuilder.dismiss();
							clickCount++;
							dialogControl.put(clickCount, true);
							downDialog = MyDialog.createLoadingDialog(activity,
									"�ύ�豸��Ϣ��...");
							downDialog.setCancelable(true);
							downDialog.setCanceledOnTouchOutside(false);
							downDialog
									.setOnDismissListener(new OnDismissListener() {
										@Override
										public void onDismiss(
												DialogInterface dialog) {
											dialogControl
													.put(clickCount, false);
										}
									});
							if(downDialog!=null){
								downDialog.show();
							}
							new Thread(){
								public void run() {
								int loc = clickCount;
								String devname = gps_valve_devName_edit
										.getEditableText().toString();
								String devaddr = gps_valve_devAddress_edit
										.getEditableText().toString();
								String devmark = gps_valve_devRemark_edit
										.getEditableText().toString();
								String devtype = deviceTypeInfos.get(gps_valve_wheelview.getCurrentItem()).getN_EQUIPMENT_TYPE_ID()
										;
								String parameter = AssembleUpmes
										.AddDeviceParameter(devname, devmark,
												devaddr, "", "", userId, "",
												devtype, "1");
								
								SocketInteraction areaSocket= new SocketInteraction(context,Integer.parseInt(port),Ip,userName,parameter,null);
								String result = areaSocket.DataUpLoadConn();					
								Log.v("�����豸������Ϣ��", result);
								if(dialogControl.size()>0){
									Log.v("ZHOUTAO","��ǰ�߳���ţ�"+loc);
									if(dialogControl.get(loc)==true){
										if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
												&&!"������δ��Ӧ".equals(result)
												&&!"����Ϊ�գ���˶���������!".equals(result)
												&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){
											Message msg = new Message();
											Bundle bundle = new Bundle();
											bundle.putInt("key", 4);
											bundle.putString("data", result);
											msg.setData(bundle);
											dataHandler.sendMessage(msg);
											downDialog.dismiss();
										}else{	
											dataHandler.post(new Runnable() {
												
												@Override
												public void run() {
													Mytoast.showToast(getActivity(), "���ʧ��!", Toast.LENGTH_LONG);
													downDialog.dismiss();
												}
											});
										}
									}
								}
								
							};}.start();
						}
					}).show();
			break;
		case R.id.gps_valve_wheelview_image://������������豸����
			//��ʾprogressBar--ʧ��-->����wheelview ;--�ɹ�-->����wheelview
			//TODO
			if(deviceTypeInfos==null||deviceTypeInfos.size()==0){
				gps_valve_wheelview_ll.setVisibility(View.VISIBLE);
				gps_valve_wheelview_image.setVisibility(View.GONE);
				gps_valve_wheelview_word.setVisibility(View.GONE);
				gps_valve_wheelview_progress.setVisibility(View.VISIBLE);
				Animation animation = AnimationUtils.loadAnimation(
						context, R.anim.loading_animation);
				gps_valve_wheelview_progress.startAnimation(animation);
				new Thread(){@Override
					public void run() {
						super.run();
						boolean signal = true;
						String parameter = AssembleUpmes.getDevTypeParameter();
						SocketInteraction areaSocket= new SocketInteraction(context,Integer.parseInt(port),Ip,userName,parameter,dataHandler);
						signal = areaSocket.DataDownLoadConn();
						areaSocket.closeConn();
						if(signal==true){
							dataHandler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(activity, "�豸���ͳ�ʼ�ɹ�", Toast.LENGTH_SHORT).show();
									gps_valve_wheelview_progress.setVisibility(View.GONE);	
								}
							});
						}else{
							dataHandler.post(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(activity, "�豸���ͳ�ʼʧ��", Toast.LENGTH_SHORT).show();
									gps_valve_wheelview_ll.setVisibility(View.VISIBLE);
									gps_valve_wheelview_image.setVisibility(View.VISIBLE);
									gps_valve_wheelview_word.setVisibility(View.VISIBLE);
									gps_valve_wheelview_progress.setVisibility(View.GONE);
								}
							});
						}
					}}.start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) view.getParent();
		if (vg != null) {
			vg.removeAllViewsInLayout();
		}
		return view;
	}

	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			int key = bundle.getInt("key",0);
			String data = bundle.getString("data");
			switch (key) {
			case 0://�ɼ�GPS����
				
				break;
			case 1://�����豸
				
				break;
			case 2://��ȡ�豸����
				Log.v("��ȡ�豸���ͣ�", data);
				if(data!=null&&!"û������".equals(data)){
					deviceTypeInfos = JsonAnalyze.analyszeDevType(JsonAnalyze.analyzeData(data));
					//����wheelView����
					List<String> wheelData = new ArrayList<String>();
					for (int i = 0; i < deviceTypeInfos.size(); i++) {
						wheelData.add(deviceTypeInfos.get(i).getC_EQUIPMENT_TYPE_NAME());
					}
					gps_valve_wheelview.setAdapter(new WheelViewAdapter(deviceTypeInfos));
					gps_valve_wheelview.setVisibility(View.VISIBLE);
					gps_valve_wheelview_ll.setVisibility(View.GONE);
					gps_valve_wheelview_image.setVisibility(View.GONE);
					gps_valve_wheelview_word.setVisibility(View.GONE);
					gps_valve_wheelview_progress.setVisibility(View.GONE);
				}else{
					gps_valve_wheelview_ll.setVisibility(View.VISIBLE);
					gps_valve_wheelview_progress.setVisibility(View.GONE);
					gps_valve_wheelview_image.setVisibility(View.VISIBLE);
					gps_valve_wheelview_word.setVisibility(View.VISIBLE);
				}
				break;
			case 3:// ��ȡ�豸����
				Log.v("��ȡ�豸���ݣ�", data);
				if(data!=null&&!"û������".equals(data)){
					if(deviceInfos==null||deviceInfos.size()==0){
						deviceInfos = JsonAnalyze.analyszeDevData(JsonAnalyze.analyzeData(data));
					}else{
						deviceInfos.clear();
						deviceInfos.addAll(JsonAnalyze.analyszeDevData(JsonAnalyze.analyzeData(data)));
					}
					if (gpsValveAdapter==null){
						gpsValveAdapter = new GPSValveAdapter(context,deviceInfos);
						gps_valve_fragment_lv.setAdapter(gpsValveAdapter);
					}else{
						gpsValveAdapter.notifyDataSetChanged();
						gps_valve_fragment_lv.onRefreshComplete();
					}
					gps_valve_listview_ll.setVisibility(View.GONE);
					gps_valve_listview_image.setVisibility(View.GONE);
					gps_valve_listview_word.setVisibility(View.GONE);
					gps_valve_listview_progressg.setVisibility(View.GONE);
					if(gps_valve_fragment_lv.getVisibility()==View.GONE)
						gps_valve_fragment_lv.setVisibility(View.VISIBLE);
				}else{
					gps_valve_listview_ll.setVisibility(View.VISIBLE);
					gps_valve_listview_image.setVisibility(View.VISIBLE);
					gps_valve_listview_word.setVisibility(View.VISIBLE);
					gps_valve_listview_progressg.setVisibility(View.GONE);
					gps_valve_fragment_lv.setVisibility(View.GONE);
				}
				break;
			case 4://����豸����ֵ
				if(data!=null&&!"".equals(data)){
					String[] res = data.split("��");
					if("�����豸�ɹ�".equals(res[1])){
						DeviceInfo deviceInfo = new DeviceInfo();
						deviceInfo.setN_EQUIPMENT_ID(res[0]);
						deviceInfo.setC_EQUIPMENT_NAME(gps_valve_devName_edit
								.getEditableText().toString());
						deviceInfo.setC_EQUIPMENT_TYPE_NAME(deviceTypeInfos.get(gps_valve_wheelview.getCurrentItem()).getC_EQUIPMENT_TYPE_NAME());
						deviceInfo.setN_EQUIPMENT_STATUS("1");
						deviceInfo.setC_USER_NAME(userName);
						Date time =new Date(System.currentTimeMillis());
						SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
						deviceInfo.setD_OPERATING_TIME(format.format(time));
						deviceInfo.setC_EQUIPMENT_ADDRESS(gps_valve_devAddress_edit
								.getEditableText().toString());
						deviceInfo.setC_EQUIPMENT_REMARK(gps_valve_devRemark_edit
								.getEditableText().toString());
						deviceInfo.setC_EQUIPMENT_X("");
						deviceInfo.setC_EQUIPMENT_Y("");
						if(deviceInfos==null||deviceInfos.size()==0){
							deviceInfos = new ArrayList<DeviceInfo>();
						}
						deviceInfos.add(0,deviceInfo);
						if(gpsValveAdapter==null){
							gpsValveAdapter = new GPSValveAdapter(context, deviceInfos);
							gps_valve_fragment_lv.setAdapter(gpsValveAdapter);
						}else{
							gpsValveAdapter.notifyDataSetChanged();
						}
						gps_valve_listview_ll.setVisibility(View.GONE);
						gps_valve_listview_image.setVisibility(View.GONE);
						gps_valve_listview_word.setVisibility(View.GONE);
						gps_valve_listview_progressg.setVisibility(View.GONE);
						if(gps_valve_fragment_lv.getVisibility()==View.GONE)
							gps_valve_fragment_lv.setVisibility(View.VISIBLE);
					}else{
						Mytoast.showToast(activity, "�����������ʧ�ܣ�", Toast.LENGTH_LONG);
					}
					Mytoast.showToast(activity, "��ӳɹ���", Toast.LENGTH_LONG);
				}else{
					Mytoast.showToast(activity, "���ʧ�ܣ��������磡", Toast.LENGTH_LONG);
				}
				dialogBuilder.dismiss();
				break;
			case 6:
				gps_valve_listview_ll.setVisibility(View.VISIBLE);
				gps_valve_listview_image.setVisibility(View.GONE);
				gps_valve_listview_word.setVisibility(View.GONE);
				gps_valve_listview_progressg.setVisibility(View.VISIBLE);
				gps_valve_fragment_lv.setVisibility(View.GONE);
				Animation animation1 = AnimationUtils.loadAnimation(
						context, R.anim.loading_animation);
				gps_valve_listview_progressg.startAnimation(animation1);
				break;
			default:
				break;
			}
		}
	}
	// ˢ�µ�ʱ��
	private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(
				RefreshPullToRefreshBase<View> refreshView) {
			// mylistView.setRefreshingLabel("pullingDown");
			new Thread() {
				@Override
				public void run() {
					super.run();
						boolean signal = true;
						String parameter = AssembleUpmes.getDevDataParameter();
						SocketInteraction areaSocket= new SocketInteraction(context,Integer.parseInt(port),Ip,userName,parameter,dataHandler);
						signal = areaSocket.DataDownLoadConn();
				}
			}.start();
		}

		@Override
		public void onPullUpToRefresh(RefreshPullToRefreshBase<View> refreshView) {
			// mylistView.setRefreshingLabel("pullingUP");
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						Thread.sleep(2000);
						// handler.sendEmptyMessage(DOWN_LOAD_SUCCESS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	};
}
