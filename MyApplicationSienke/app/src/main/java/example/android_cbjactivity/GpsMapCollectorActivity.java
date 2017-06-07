package example.android_cbjactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;

import cc.thksoft.myfirstpro.appcation.MyApplication;
import cc.thksoft.myfirstpro.entity.DeviceInfo;
import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import cc.thksoft.myfirstpro.threadsocket.QucklySocketInteraction;
import cc.thksoft.myfirstpro.util.AssembleUpmes;
import cc.thksoft.myfirstpro.util.BDLocationModel;
import cc.thksoft.myfirstpro.util.MyDialog;
import cc.thksoft.myfirstpro.util.MyOrientationListener;
import cc.thksoft.myfirstpro.util.MyOrientationListener.OnOrientationListener;
import cc.thksoft.myfirstpro.util.Mytoast;
import gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

public class GpsMapCollectorActivity extends Activity implements
		OnClickListener {
	private MapView gps_bmapView;
	private BaiduMap gps_baiduMap;
	private TextView gps_BMap_compassMode;
	private LinearLayout gps_BMap_control;
	private TextView gps_BMap_current_name;
	private TextView gps_BMap_current__address;
	private Button gps_BMap_current_collbtn;
	private TextView gps_BMap_LONGITUDE;
	private TextView gps_BMap_LATIUDE;
	private TextView gps_BMap_Radius;
	private LinearLayout gps_BMap_Radius_ll;
	private TextView gps_BMap_SatelliteNumber;
	private TextView gps_BMap_AddrStr;
	private TextView gps_BMap_DeviceLoc;
	private SharedPreferences preferences;
	private String dbName;
	private String filepath;
	private double CURRENTLATIUDE;
	private double CURRENTLONGITUDE;
	private double PURPOSELATIUDE;
	private double PURPOSELONGITUDE;
	private float Direction;
	private float Radius;
	private int satellite;
	private String currAdr;
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	private MyOrientationListener myOrientationListener;
	private boolean isOpen_Compass;
	private NiftyDialogBuilder dialogBuilder;
	private UsersInfo usInfo;
	private DeviceInfo devInfo;
	private DBService dbService;
	private Marker devmarker;
	private int icon = R.mipmap.water_meter;
	private int position;
	private String action;
	private String userName;
	private String userId;
	private String Ip;
	private String port;
	private Dialog myDialog;
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int key = Integer.parseInt(bundle.getString("key"));
			switch (key) {
			case 0:
				// �жϵص��Ƿ�ı� ���ı�������ˢ�µ�ͼ
				Direction = bundle.getFloat("Direction");
				Radius = bundle.getFloat("Radius");// satellite Address
				currAdr = bundle.getString("Address");
				CURRENTLATIUDE = bundle.getDouble("Latitude");
				CURRENTLONGITUDE = bundle.getDouble("Longitude");
				//changeLocation(CURRENTLATIUDE, CURRENTLONGITUDE);
				//updateDirction(Radius, CURRENTLATIUDE, CURRENTLONGITUDE,
				//Direction);
				break;
			case 1:// �Ե�ǰλ����Ϊ�豸��
					// ��ȡ�ҵ�λ������
				// ����豸��ʶ
				devmarker = addMarker(icon, CURRENTLATIUDE,
						CURRENTLONGITUDE);
				// �洢�����ݿ�
		 		if("GPSMeterFragment".equals(action)){//�޸ı�������
					dbService = new DBService(filepath + dbName);
					dbService.modifyLat_lngbyUsId(String.valueOf(CURRENTLATIUDE), String.valueOf(CURRENTLONGITUDE),
							usInfo.getUSID());
					usInfo.setBASE_LATIUDE(String.valueOf(CURRENTLATIUDE));
					usInfo.setBASE_LONGITUDE(String.valueOf(CURRENTLONGITUDE));
				}else if("GPSValveFragment".equals(action)){//�޸Ļ�������
					//MyApplication.deviceInfos.get(position)
					devInfo.setC_EQUIPMENT_Y(String.valueOf(CURRENTLATIUDE));
					devInfo.setC_EQUIPMENT_X(String.valueOf(CURRENTLONGITUDE));

				}

				// �޸İ�������
				gps_BMap_current_collbtn.setText("���²ɼ�");
				//��ʾ���ĵ�
				if(gps_BMap_DeviceLoc.getVisibility()==View.GONE)
					gps_BMap_DeviceLoc.setVisibility(View.VISIBLE);
				myDialog.dismiss();
				break;
			case 2:// �Գ���λ����Ϊ�豸��
					// ��ȡ�ҵ�λ������
				double latitude = bundle.getDouble("latitude");
				double longitude = bundle.getDouble("longitude");
				// ����豸��ʶ
				devmarker = addMarker(icon, latitude,
						longitude);
				// �洢�����ݿ�
		 		if("GPSMeterFragment".equals(action)){
					dbService = new DBService(filepath + dbName);
					dbService.modifyLat_lngbyUsId(String.valueOf(latitude), String.valueOf(longitude),
							usInfo.getUSID());
					usInfo.setBASE_LATIUDE(String.valueOf(latitude));
					usInfo.setBASE_LONGITUDE(String.valueOf(longitude));
				}else if("GPSValveFragment".equals(action)){
					devInfo.setC_EQUIPMENT_Y(String.valueOf(latitude));
					devInfo.setC_EQUIPMENT_X(String.valueOf(longitude));
				}

				// �޸İ�������
				gps_BMap_current_collbtn.setText("���²ɼ�");
				//��ʾ���ĵ�
				if(gps_BMap_DeviceLoc.getVisibility()==View.GONE)
					gps_BMap_DeviceLoc.setVisibility(View.VISIBLE);
				myDialog.dismiss();
				break;
			case 3:
				// ɾ��marker
				if(devmarker!=null){
					devmarker.remove();
					devmarker=null;
				}
		 		if("GPSMeterFragment".equals(action)){
					dbService = new DBService(filepath + dbName);
					dbService.modifyLat_lngbyUsId("", "",
							usInfo.getUSID());
					usInfo.setBASE_LATIUDE("");
					usInfo.setBASE_LONGITUDE("");
				}else if("GPSValveFragment".equals(action)){
					devInfo.setC_EQUIPMENT_Y("");
					devInfo.setC_EQUIPMENT_X("");
				}

				// �޸İ�������
				gps_BMap_current_collbtn.setText("��ʼ�ɼ�");
				gps_BMap_control.setVisibility(View.GONE);
				//�������ĵ�
				if(gps_BMap_DeviceLoc.getVisibility()==View.VISIBLE)
					gps_BMap_DeviceLoc.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gc_map);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		if(MyApplication.bdLocationModel.isstartLoaction()){
			MyApplication.bdLocationModel.stopLocation();
		}
		MyApplication.bdLocationModel = new BDLocationModel(getApplicationContext(), handler);
		MyApplication.bdLocationModel.startLoaction();
		preferences = getSharedPreferences("config", 0);
		userName = preferences.getString("USER_NAME", "");
		preferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		if (preferences.contains("ip")) {
			Ip = preferences.getString("ip", "");
		}
		if (preferences.contains("port")) {
			port = preferences.getString("port", "");
		}
		action = getIntent().getAction();
 		if("GPSMeterFragment".equals(action)){
 			usInfo = (UsersInfo)getIntent().getSerializableExtra("usInfo");
		}else if("GPSValveFragment".equals(action)){
			devInfo = (DeviceInfo)getIntent().getSerializableExtra("devInfo");
		}

		position = getIntent().getIntExtra("position", 0);
		guiwadget();//��ʼ�ؼ�
		saveZoomControl();//���ر�����
		initBaseData();//��ʼ��������
		firstLocation();
		initOritationListener();
		gps_baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

	}


	private void guiwadget() {
		gps_BMap_control = (LinearLayout) findViewById(R.id.gps_BMap_control);
		gps_BMap_current_name = (TextView) findViewById(R.id.gps_BMap_current_name);
		gps_BMap_current__address = (TextView) findViewById(R.id.gps_BMap_current__address);
		gps_BMap_current_collbtn = (Button) findViewById(R.id.gps_BMap_current_collbtn);
		gps_BMap_LONGITUDE = (TextView) findViewById(R.id.gps_BMap_LONGITUDE);
		gps_BMap_LATIUDE = (TextView) findViewById(R.id.gps_BMap_LATIUDE);
		gps_BMap_Radius = (TextView) findViewById(R.id.gps_BMap_Radius);
		gps_BMap_Radius_ll = (LinearLayout) findViewById(R.id.gps_BMap_Radius_ll);
		gps_BMap_control.setVisibility(View.GONE);
		gps_BMap_current_collbtn.setOnClickListener(this);
		gps_bmapView = (MapView) findViewById(R.id.gps_bmapView);
		gps_baiduMap = gps_bmapView.getMap();
		gps_baiduMap
				.setOnMyLocationClickListener(new OnMyLocationClickListener() {

					@Override
					public boolean onMyLocationClick() {
						gps_BMap_control.setVisibility(View.VISIBLE);
						gps_BMap_Radius_ll.setVisibility(View.VISIBLE);
						modifyPointFaceData("��ǰλ��", Radius, currAdr, CURRENTLATIUDE,
								CURRENTLONGITUDE);
						return false;
					}
				});
		gps_baiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(final LatLng latLng) {
				if(devmarker==null){
				dialogBuilder = new NiftyDialogBuilder(
						GpsMapCollectorActivity.this, R.style.dialog_untran);
				dialogBuilder.withTitle("��ܰ��ʾ��").withTitleColor("#000000")
						.withDividerColor("#999999")
						.withMessage("�Ƿ񽫴˵�������Ϊ�豸����?")
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
								dialogBuilder.dismiss();
						 		if("GPSMeterFragment".equals(action)){
									myDialog = MyDialog.createLoadingDialog(GpsMapCollectorActivity.this, "�����ϴ����꣬���Ժ�");
									myDialog.setCancelable(false);
									myDialog.setCanceledOnTouchOutside(false);
									myDialog.show();
									new Thread(){
										@Override
										public void run() {
											super.run();
											//TODO
											String one = usInfo.getUSID();
											String parameter = AssembleUpmes.UpMeterGPSParamter(one,  String.valueOf(latLng.longitude),
													String.valueOf(latLng.latitude));
											QucklySocketInteraction areaSocket= new QucklySocketInteraction(getApplicationContext(),Integer.parseInt(port),Ip,userName,parameter,null);
											String result = areaSocket.DataUpLoadConn();
											Log.v("GpsMapCollectorActivity", "�ϴ���γ�ȣ����ؽ����"+result);
											if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
													&&!"������δ��Ӧ".equals(result)
													&&!"����Ϊ�գ���˶���������!".equals(result)
													&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){
												if(!"�ɼ��û�����ʧ��".equals(result)){
													Message msg = new Message();
													Bundle data = new Bundle();
													data.putString("key", "2");
													data.putDouble("latitude", latLng.latitude);
													data.putDouble("longitude", latLng.longitude);
													msg.setData(data);
													handler.sendMessage(msg);
												}else{
													handler.post(new Runnable() {

														@Override
														public void run() {
															myDialog.dismiss();
															Mytoast.showToast(GpsMapCollectorActivity.this, "����������ݴ���", Toast.LENGTH_SHORT);
														}
													});
												}
											}else{
												handler.post(new Runnable() {

													@Override
													public void run() {
														myDialog.dismiss();
														Mytoast.showToast(GpsMapCollectorActivity.this, "��������״̬��", Toast.LENGTH_SHORT);
													}
												});
											}
										}
									}.start();
								}else if("GPSValveFragment".equals(action)){
									myDialog = MyDialog.createLoadingDialog(GpsMapCollectorActivity.this, "�����ϴ����꣬���Ժ�");
									myDialog.setCancelable(false);
									myDialog.setCanceledOnTouchOutside(false);
									myDialog.show();
									new Thread(){
										@Override
										public void run() {
											super.run();
											//TODO

											String one = devInfo.getN_EQUIPMENT_ID();
											String parameter = AssembleUpmes.UpDevGPSParamter(one, String.valueOf(latLng.longitude),
													String.valueOf(latLng.latitude));
											QucklySocketInteraction areaSocket= new QucklySocketInteraction(getApplicationContext(),Integer.parseInt(port),Ip,userName,parameter,null);
											String result = areaSocket.DataUpLoadConn();
											Log.v("GpsMapCollectorActivity", "�ϴ���γ�ȣ����ؽ����"+result);
											if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
													&&!"������δ��Ӧ".equals(result)
													&&!"����Ϊ�գ���˶���������!".equals(result)
													&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){
												if(!"�ɼ��û�����ʧ��".equals(result)){
													Message msg = new Message();
													Bundle data = new Bundle();
													data.putString("key", "2");
													data.putDouble("latitude", latLng.latitude);
													data.putDouble("longitude", latLng.longitude);
													msg.setData(data);
													handler.sendMessage(msg);
												}else{
													handler.post(new Runnable() {

														@Override
														public void run() {
															myDialog.dismiss();
															Mytoast.showToast(GpsMapCollectorActivity.this, "����������ݴ���", Toast.LENGTH_SHORT);
														}
													});
												}
											}else{
												handler.post(new Runnable() {

													@Override
													public void run() {
														myDialog.dismiss();
														Mytoast.showToast(GpsMapCollectorActivity.this, "��������״̬��", Toast.LENGTH_SHORT);
													}
												});
											}
											}
									}.start();
								}
							}
						}).show();
				}
			}
		});
		gps_baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				gps_BMap_control.setVisibility(View.VISIBLE);
				LatLng latLng = marker.getPosition();
		 		if("GPSMeterFragment".equals(action)){
					gps_BMap_current_name.setText(usInfo.getUSNAME());
					gps_BMap_current__address.setText(usInfo.getUSADRESS());
					gps_BMap_LONGITUDE.setText(String.valueOf(usInfo.getBASE_LONGITUDE()));
					gps_BMap_LATIUDE.setText(String.valueOf(usInfo.getBASE_LATIUDE()));
				}else if("GPSValveFragment".equals(action)){
					gps_BMap_current_name.setText(devInfo.getC_EQUIPMENT_NAME());
					gps_BMap_current__address.setText(devInfo.getC_EQUIPMENT_ADDRESS());
					gps_BMap_LONGITUDE.setText(String.valueOf(devInfo.getC_EQUIPMENT_X()));
					gps_BMap_LATIUDE.setText(String.valueOf(devInfo.getC_EQUIPMENT_Y()));
				}
				gps_BMap_Radius_ll.setVisibility(View.GONE);
				return false;
			}
		});
		gps_baiduMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent event) {
				gps_BMap_control.setVisibility(View.GONE);
			}
		});
		gps_BMap_compassMode = (TextView) findViewById(R.id.gps_BMap_Mode);
		// ��������ģʽ
		gps_BMap_compassMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpen_Compass == false) {
					isOpen_Compass = true;
					mCurrentMode = LocationMode.COMPASS;
					gps_BMap_compassMode.setBackgroundResource(R.mipmap.ft_loc_normal);

				} else {
					isOpen_Compass = false;
					mCurrentMode = LocationMode.NORMAL;
					gps_BMap_compassMode.setBackgroundResource(R.mipmap.main_icon_compass);
				}
			}
		});
		gps_BMap_DeviceLoc = (TextView) findViewById(R.id.gps_BMap_Device);

		gps_BMap_DeviceLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(devmarker!=null){
					LatLng latLng = devmarker.getPosition();
					changeLocation(latLng.latitude, latLng.longitude);
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
	 		if("GPSMeterFragment".equals(action)){
				if(MyApplication.gpsmeterHandler!=null){
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("key", "1");
					bundle.putSerializable("usInfo", usInfo);
					bundle.putInt("position", position);
					msg.setData(bundle);
					MyApplication.gpsmeterHandler.sendMessage(msg);
				}
			}else if("GPSValveFragment".equals(action)){
				if(MyApplication.gpsvalveHandler!=null){
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("key", "1");
					bundle.putSerializable("devInfo", devInfo);
					bundle.putInt("position", position);
					msg.setData(bundle);
					MyApplication.gpsvalveHandler.sendMessage(msg);
				}
			}
	 		if(handler!=null)
	 			handler = null;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initBaseData() {
		preferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		dbName = preferences.getString("dbName", "");
		filepath = Environment.getDataDirectory().getPath() + "/data/"
				+ "com.example.android_cbjactivity" + "/databases/";
 		if("GPSMeterFragment".equals(action)){
 			if (usInfo.getBASE_LATIUDE() != null
 					&& usInfo.getBASE_LONGITUDE() != null
 					&& !"".equals(usInfo.getBASE_LATIUDE())
 					&& !"".equals(usInfo.getBASE_LONGITUDE())) {
 				devmarker = addMarker(icon, Double.parseDouble(usInfo.getBASE_LATIUDE()), Double.parseDouble(usInfo.getBASE_LONGITUDE()));
 				gps_BMap_current_collbtn.setText("���²ɼ�");
 			}else{//û���豸����
 				if(gps_BMap_DeviceLoc.getVisibility()==View.VISIBLE)
 					gps_BMap_DeviceLoc.setVisibility(View.GONE);
 			}
		}else if("GPSValveFragment".equals(action)){
			if (devInfo.getC_EQUIPMENT_Y() != null
					&& devInfo.getC_EQUIPMENT_X() != null
					&& !"".equals(devInfo.getC_EQUIPMENT_Y())
					&& !"".equals(devInfo.getC_EQUIPMENT_X())) {
				devmarker = addMarker(icon, Double.parseDouble(devInfo.getC_EQUIPMENT_X()) ,Double.parseDouble(devInfo.getC_EQUIPMENT_Y()));
				gps_BMap_current_collbtn.setText("���²ɼ�");
			}else{//û���豸����
				if(gps_BMap_DeviceLoc.getVisibility()==View.VISIBLE)
					gps_BMap_DeviceLoc.setVisibility(View.GONE);
			}
		}
	}

	private void modifyPointFaceData(String Name, float Radius, String currAdr,
			Double CURRENTLATIUDE, Double CURRENTLONGITUDE) {
		gps_BMap_current_name.setText(Name);
		gps_BMap_current__address.setText(currAdr);
		gps_BMap_LONGITUDE.setText(String.valueOf(CURRENTLONGITUDE));
		gps_BMap_LATIUDE.setText(String.valueOf(CURRENTLATIUDE));
		gps_BMap_Radius.setText(String.valueOf(Radius));
	}

	private void firstLocation() {
		String sb = preferences.getString("sb", "");
		if (sb == null || "".equals(sb)) {
			Toast.makeText(getApplicationContext(), "û�п��þ�γ��",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int loc = sb.indexOf("$");
		CURRENTLATIUDE = Double.parseDouble(sb.substring(0, loc - 1));
		CURRENTLONGITUDE = Double
				.parseDouble(sb.substring(loc + 1, sb.length()));
		LatLng cenpt = new LatLng(CURRENTLATIUDE, CURRENTLONGITUDE);
		// �����ͼ״̬
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(17)
				.build();// (�ٶȵ�ͼ�㼶��Χ 3~17)
		// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		gps_baiduMap.setMapStatus(mMapStatusUpdate);
	}

	private void changeLocation(double LATIUDE, double LONGITUDE) {
		if(gps_baiduMap.isMyLocationEnabled()){
			LatLng cenpt = null;
			MapStatus mMapStatus = gps_baiduMap.getMapStatus();
			if (mMapStatus == null) {
				// �����ͼ״̬
				cenpt = new LatLng(LATIUDE, LONGITUDE);
				mMapStatus = new MapStatus.Builder().target(cenpt).zoom(17).build();// (�ٶȵ�ͼ�㼶��Χ
			}else{
				int zoom = (int) mMapStatus.zoom;
				cenpt = new LatLng(LATIUDE, LONGITUDE);
				mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom).build();
			}

			// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			gps_baiduMap.setMapStatus(mMapStatusUpdate);
		}
	}

	private void updateDirction(float Radius, double Latitude,
			double Longitude, float Direction) {
		// ���춨λ����
		MyLocationData locData = new MyLocationData.Builder().accuracy(Radius)
		// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
				.direction(Direction)// .latitude(Latitude).longitude(Longitude)
				.build();
		// ���ö�λ����
		gps_baiduMap.setMyLocationData(locData);
		// mCurrentLantitude = location.getLatitude();
		// mCurrentLongitude = location.getLongitude();
		// �����Զ���ͼ��
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.mipmap.main_my_follow);

		MyLocationConfiguration config = new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker);
		gps_baiduMap.setMyLocationConfigeration(config);
	}

	/**
	 * ����豸������
	 *
	 * @param icon
	 *            ������ͼ��
	 * @param Latitude
	 *            γ��
	 * @param Longitude
	 *            ����
	 * @return
	 */
	private Marker addMarker(int icon, double Latitude, double Longitude) {
		LatLng point = new LatLng(Latitude, Longitude);
		// ����Markerͼ��
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);// R.drawable.icon_gcoding
		// ����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).zIndex(9);
		// �ڵ�ͼ�����Marker������ʾ
		return (Marker) gps_baiduMap.addOverlay(option);
	}

	private InfoWindow addInfoWindow(LatLng marPoint) {
		Button button = new Button(getApplicationContext());
		button.setBackgroundResource(R.mipmap.location_tips);
		button.setText(marPoint.latitude + "    " + marPoint.longitude);
		button.setTextSize(12);
		button.setGravity(Gravity.CENTER | Gravity.TOP);
		button.setTextColor(getResources().getColor(R.color.dialog_bg));
		button.setPadding(2, 2, 2, 10);
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		return new InfoWindow(button, marPoint, -47);
	}

	/**
	 * ��ʼ�����򴫸���
	 */
	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						Direction = (int) x;
						// ���춨λ����
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(Radius)
								// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
								.direction(Direction).latitude(CURRENTLATIUDE)
								.longitude(CURRENTLONGITUDE).build();
						// ���ö�λ����
						gps_baiduMap.setMyLocationData(locData);
						// �����Զ���ͼ��
						BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
								.fromResource(R.mipmap.main_my_follow);
						Log.v("Icon", ":" + mCurrentMarker);
						MyLocationConfiguration config = new MyLocationConfiguration(
								mCurrentMode, true, mCurrentMarker);
						gps_baiduMap.setMyLocationConfigeration(config);
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		gps_bmapView.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// ����ͼ�㶨λ
		gps_baiduMap.setMyLocationEnabled(true);
		// �������򴫸���
		myOrientationListener.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// �ر�ͼ�㶨λ
		gps_baiduMap.setMyLocationEnabled(false);
		// �رշ��򴫸���
		myOrientationListener.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		gps_bmapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gps_bmapView.onDestroy();
	}

	public void saveZoomControl(){
        // ���ر����߿ؼ�

        int count = gps_bmapView.getChildCount();

        View scale = null;

        for (int i = 0; i < count; i++) {

                View child = gps_bmapView.getChildAt(i);

                if (child instanceof ZoomControls) {
                        scale = child;
                        break;
                }
        }
        scale.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		int key = v.getId();
		switch (key) {
		case R.id.gps_BMap_current_collbtn:
			String curFunction = gps_BMap_current_collbtn.getText().toString();
			if ("��ʼ�ɼ�".equals(curFunction)) {
				dialogBuilder = new NiftyDialogBuilder(
						GpsMapCollectorActivity.this, R.style.dialog_untran);
				dialogBuilder.withTitle("��ܰ��ʾ��").withTitleColor("#000000")
						.withDividerColor("#999999")
						.withMessage("�Ƿ�ʹ�õ�ǰλ��������Ϊ���豸����?")
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
								dialogBuilder.dismiss();
								if("GPSMeterFragment".equals(action)){
									myDialog = MyDialog.createLoadingDialog(GpsMapCollectorActivity.this, "�����ϴ����꣬���Ժ�");
									myDialog.setCancelable(false);
									myDialog.setCanceledOnTouchOutside(false);
									myDialog.show();
									new Thread(){
										@Override
										public void run() {
											super.run();
											String parameter = AssembleUpmes.UpMeterGPSParamter(usInfo.getUSID(), String.valueOf(CURRENTLONGITUDE),
													String.valueOf(CURRENTLATIUDE));
											QucklySocketInteraction areaSocket= new QucklySocketInteraction(getApplicationContext(),Integer.parseInt(port),Ip,userName,parameter,null);
											String result = areaSocket.DataUpLoadConn();
											Log.v("GpsMapCollectorActivity", "�ϴ���γ�ȣ����ؽ����"+result);
											if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
													&&!"������δ��Ӧ".equals(result)
													&&!"����Ϊ�գ���˶���������!".equals(result)
													&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){

												Message msg = new Message();
												Bundle data = new Bundle();
												data.putString("key", "1");
												msg.setData(data);
												handler.sendMessage(msg);
											}else{
												handler.post(new Runnable() {

													@Override
													public void run() {
														myDialog.dismiss();
														Mytoast.showToast(GpsMapCollectorActivity.this, "��������״̬��", Toast.LENGTH_SHORT);

													}
												});
											}
										}
									}.start();
								}else if("GPSValveFragment".equals(action)){
									myDialog = MyDialog.createLoadingDialog(GpsMapCollectorActivity.this, "�����ϴ����꣬���Ժ�");
									myDialog.setCancelable(false);
									myDialog.setCanceledOnTouchOutside(false);
									myDialog.show();
									new Thread(){
										@Override
										public void run() {
											super.run();
											String parameter = AssembleUpmes.UpDevGPSParamter(devInfo.getN_EQUIPMENT_ID(), String.valueOf(CURRENTLONGITUDE),
													String.valueOf(CURRENTLATIUDE));
											QucklySocketInteraction areaSocket= new QucklySocketInteraction(getApplicationContext(),Integer.parseInt(port),Ip,userName,parameter,null);
											String result = areaSocket.DataUpLoadConn();
											Log.v("GpsMapCollectorActivity", "�ϴ���γ�ȣ����ؽ����"+result);
											if(!"���粻�ȶ������ݻ�ȡʧ��!".equals(result)
													&&!"������δ��Ӧ".equals(result)
													&&!"����Ϊ�գ���˶���������!".equals(result)
													&&!"���Ĺؼ�������".equals(result)&&!"".equals(result)){

												Message msg = new Message();
												Bundle data = new Bundle();
												data.putString("key", "1");
												msg.setData(data);
												handler.sendMessage(msg);
											}else{
												handler.post(new Runnable() {

													@Override
													public void run() {
														myDialog.dismiss();
														Mytoast.showToast(GpsMapCollectorActivity.this, "��������״̬��", Toast.LENGTH_SHORT);

													}
												});
											}
											}
									}.start();
								}
							}
						}).show();
			} else if ("���²ɼ�".equals(curFunction)) {
				dialogBuilder = new NiftyDialogBuilder(
						GpsMapCollectorActivity.this, R.style.dialog_untran);
				dialogBuilder.withTitle("��ܰ��ʾ��").withTitleColor("#000000")
						.withDividerColor("#999999")
						.withMessage("ȷ��ɾ��ԭ���豸����,����¼�룿")
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
								Message msg = new Message();
								Bundle data = new Bundle();
								data.putString("key", "3");
								msg.setData(data);
								handler.sendMessage(msg);
								dialogBuilder.dismiss();
							}
						}).show();
			}
			break;
		default:
			break;
		}
	}

}
