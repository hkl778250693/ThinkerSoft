package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.MapMeterAdapter;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.util.BDLocationModel;
import com.example.administrator.thinker_soft.myfirstpro.util.MarkerCluster;
import com.example.administrator.thinker_soft.myfirstpro.util.MyOrientationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;

public class MapMeterActivity extends Activity {
    // ......�ؼ�.............
    private MapView bmapView;
    private TextView BMap_compassMode;
    private LinearLayout BMap_Info_ll;
    private ListView BMap_Info_lv;
    // ......��Ա����.............
    private BaiduMap baiduMap;
    private double CURRENTLATIUDE;
    private double CURRENTLONGITUDE;
    private double PURPOSELATIUDE;
    private double PURPOSELONGITUDE;
    private float Direction;
    private float Radius;
    private LocationMode mCurrentMode = LocationMode.NORMAL;
    private MyOrientationListener myOrientationListener;
    private SharedPreferences preferences;
    private String dbName;
    private String filepath;
    private boolean isOpen_Compass;
    private Overlay circleOverlay;
    private DBService dbService;
    private int screenWidth;
    private int screenHeigth;
    private MapMeterAdapter mapMeterAdapter;
    private int nearPosition;// ������marker���б��е�λ��
    private int farPosition;// Զ����marker���б��е�λ��
    // .............�ۺ����ݱ���............
    private MarkerCluster mcluster;
    private List<List<String[]>> similarPoints_no_grade;// �������ŵĵ�ŵ�һ��
    private List<String>[] allDevpoint;// δ�ۺϵ����ݼ���
    private Map<String, List<List<String[]>>> mapPoint;// �Ѿ��ۺϵ����ݼ���
    private List<List<String[]>> farmarkers = new ArrayList<List<String[]>>();// �������
    // �����ݶ�Ӧ��Marker
    private List<List<String[]>> nearmarkers = new ArrayList<List<String[]>>();// �������
    // �����ݶ�Ӧ��Marker
    private List<InfoWindow> infoWindows = new ArrayList<InfoWindow>();
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            int key = Integer.parseInt(bundle.getString("key"));
            switch (key) {
                case 0:
                    // �жϵص��Ƿ�ı� ���ı�������ˢ�µ�ͼ
                    Direction = bundle.getFloat("Direction");
                    Radius = bundle.getFloat("Radius");
                    CURRENTLATIUDE = bundle.getDouble("Latitude");
                    CURRENTLONGITUDE = bundle.getDouble("Longitude");
/*				if (CURRENTLATIUDE != bundle.getDouble("Latitude")
                        || CURRENTLONGITUDE != bundle.getDouble("Longitude")) {
				} else {

				}*/
                    //changeLocation(CURRENTLATIUDE, CURRENTLONGITUDE);
				/*
				 * 
				 * updateDirction(Radius, CURRENTLATIUDE, CURRENTLONGITUDE,
				 * Direction);
				 */
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapmeter);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        if (MyApplication.bdLocationModel.isstartLoaction()) {
            MyApplication.bdLocationModel.stopLocation();
        }
        MyApplication.bdLocationModel = new BDLocationModel(getApplicationContext(), handler);
        MyApplication.bdLocationModel.startLoaction();
        guiwadget();
        saveZoomControl();// ���ر�����
        initBaseData();
        firstLocation();
        initOritationListener();

        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.v("MapMeterActivity��", "�����");
            if (handler != null)
                handler = null;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initBaseData() {
        preferences = getSharedPreferences("IP_PORT_DBNAME", 0);
        dbName = preferences.getString("dbName", "");
        filepath = Environment.getDataDirectory().getPath() + "/data/"
                + "com.example.android_cbjactivity" + "/databases/";
        mcluster = new MarkerCluster();

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeigth = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) BMap_Info_ll
                .getLayoutParams();
        params.height = screenHeigth / 2 - 20;
        BMap_Info_ll.setVisibility(View.GONE);
    }

    private void guiwadget() {
        bmapView = (MapView) findViewById(R.id.bmapView);
        BMap_Info_ll = (LinearLayout) findViewById(R.id.BMap_Info_ll);

        BMap_Info_lv = (ListView) findViewById(R.id.BMap_Info_lv);
        BMap_compassMode = (TextView) findViewById(R.id.BMap_compassMode);
        baiduMap = bmapView.getMap();

        baiduMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

            @Override
            public void onMapClick(LatLng latLng) {
                // �����û���Ϣ��ʾ�б�
                BMap_Info_ll.setVisibility(View.GONE);
            }
        });
        baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getPosition() == baiduMap.getMapStatus().target) {
                    Log.i("�����ظ����", "");
                    return false;
                }

                // �Ա��������� ��ȡ����
                List<String[]> infos = null;
                for (int i = 0; i < nearmarkers.size(); i++) {
                    if (marker.getPosition().latitude == Double
                            .parseDouble(nearmarkers.get(i).get(0)[2])
                            && marker.getPosition().latitude == Double
                            .parseDouble(nearmarkers.get(i).get(0)[2])) {
                        infos = nearmarkers.get(i);
                        MyApplication.MARKERTYPEFARNEAR = 0;
                    }
                }

                if (infos == null) {
                    for (int i = 0; i < farmarkers.size(); i++) {
                        if (marker.getPosition().latitude == Double
                                .parseDouble(farmarkers.get(i).get(0)[2])
                                && marker.getPosition().latitude == Double
                                .parseDouble(farmarkers.get(i).get(0)[2])) {
                            infos = farmarkers.get(i);
                            MyApplication.MARKERTYPEFARNEAR = 1;
                        }
                    }
                }
                // �滻֮ǰ�����Marker
                if (MyApplication.clickMarkers.size() == 0) {
                    MyApplication.clickMarkers.add(marker);
                    MyApplication.clickMarkers_icon.add(marker.getIcon());

                } else if (MyApplication.clickMarkers.size() == 1) {
                    Marker lastmarker = MyApplication.clickMarkers.get(0);
                    BitmapDescriptor lastmarker_bg = MyApplication.clickMarkers_icon.get(0);
                    lastmarker.setIcon(lastmarker_bg);
                    MyApplication.clickMarkers.set(0, marker);
                    MyApplication.clickMarkers_icon.set(0, marker.getIcon());
                }
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding);
                marker.setIcon(bitmap);

                // ����������ѯ���ݿ�
                DBService dbService = new DBService(filepath + dbName);
                List<UsersInfo> usersInfos = dbService
                        .queryInfoByMoreUsId(infos);
                BMap_Info_ll.setVisibility(View.VISIBLE);
                if (usersInfos != null && usersInfos.size() > 0)
                    if (mapMeterAdapter == null) {
                        mapMeterAdapter = new MapMeterAdapter(
                                MapMeterActivity.this, MapMeterActivity.this,
                                usersInfos);
                        BMap_Info_lv.setAdapter(mapMeterAdapter);
                    } else {
                        mapMeterAdapter.updateAllData(usersInfos);
                    }
                return false;
            }
        });

        // ��������ģʽ
        BMap_compassMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOpen_Compass == false) {
                    isOpen_Compass = true;
                    mCurrentMode = LocationMode.COMPASS;
                    BMap_compassMode.setBackgroundResource(R.mipmap.ft_loc_normal);
                } else {
                    isOpen_Compass = false;
                    mCurrentMode = LocationMode.NORMAL;
                    BMap_compassMode.setBackgroundResource(R.mipmap.main_icon_compass);
                }
            }
        });
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
        // ...............�������ĵ�................
        int zoom = 10;
        LatLng cenpt = new LatLng(CURRENTLATIUDE, CURRENTLONGITUDE);
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom)
                .build();// (�ٶȵ�ͼ�㼶��Χ 3~17)
        // ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);
        dbService = new DBService(filepath + dbName);
        List<String[]> allDevpoint = dbService.getNumLatLngInfo();
        AddAllMarker(cenpt, allDevpoint);
        //addAllInfoWindow(allDevpoint); ����ֻ����ʾһ��  ͬһʱ��

    }

    private void AddAllMarker(LatLng cenpt, List<String[]> allDevpoint) {
        // ...............����豸��ע................

        similarPoints_no_grade = mcluster.markerClassify(allDevpoint);
        for (int i = 0; i < similarPoints_no_grade.size(); i++) {
            List<String[]> infos = similarPoints_no_grade.get(i);
            LatLng latlng = new LatLng(
                    Double.parseDouble(similarPoints_no_grade.get(i).get(0)[2]),
                    Double.parseDouble(similarPoints_no_grade.get(i).get(0)[1]));
            boolean choice = true;
            // ����MarkerOption�������ڵ�ͼ�����Marker
            BitmapDescriptor bitmap = null;
            if (DistanceUtil.getDistance(cenpt, latlng) > 1000) {
                boolean isDone = false;
                for (int j = 0; j < infos.size(); j++) {
                    if (infos.get(j)[3] == null || "".equals(infos.get(j)[3])) {
                        isDone = true;
                    }
                }
                if (isDone == false) {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_mark_pt_blue);
                } else {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_mark_pt_red);
                }
                choice = true;
            } else {
                boolean isDone = false;
                for (int j = 0; j < infos.size(); j++) {
                    if (infos.get(j)[3] == null || "".equals(infos.get(j)[3])) {
                        isDone = true;
                    }
                }
                if (isDone == false) {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_gcoding_blue);
                } else {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_gcoding_red);
                }
                choice = false;
            }

            OverlayOptions option = new MarkerOptions().position(latlng)
                    .icon(bitmap).zIndex((int) baiduMap.getMapStatus().zoom);
            baiduMap.addOverlay(option);
            // �ڵ�ͼ�����Marker������ʾ
            if (choice) {
                farmarkers.add(similarPoints_no_grade.get(i));
            } else {
                nearmarkers.add(similarPoints_no_grade.get(i));
            }
        }
    }

    private void changeLocation(double LATIUDE, double LONGITUDE) {
        if (baiduMap.isMyLocationEnabled()) {
            MapStatus mMapStatus = baiduMap.getMapStatus();
            int zoom = 0;
            LatLng cenpt = null;
            if (mMapStatus != null) {
                zoom = (int) mMapStatus.zoom;
                cenpt = mMapStatus.target;
            } else {
                zoom = 10;
                cenpt = new LatLng(LATIUDE, LONGITUDE);
            }

            mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom).build();// (�ٶȵ�ͼ�㼶��Χ
            // 3~17)
            // ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            baiduMap.setMapStatus(mMapStatusUpdate);
        }
    }

    private void updateDirction(float Radius, double Latitude,
                                double Longitude, float Direction) {
        // ���춨λ����
        MyLocationData locData = new MyLocationData.Builder().accuracy(Radius)
                // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
                .direction(Direction).latitude(Latitude).longitude(Longitude)
                .build();
        // mCurrentAccracy = Radius;
        // ���ö�λ����
        baiduMap.setMyLocationData(locData);
        // mCurrentLantitude = location.getLatitude();
        // mCurrentLongitude = location.getLongitude();
        // �����Զ���ͼ��
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.main_my_follow);
        MyLocationConfiguration config = new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker);
        baiduMap.setMyLocationConfigeration(config);
    }

    /**
     * ����豸������
     *
     * @param icon      ������ͼ��
     * @param Latitude  γ��
     * @param Longitude ����
     * @return
     */
    private Marker addMarker(int icon, double Latitude, double Longitude) {
        LatLng point = new LatLng(Latitude, Longitude);
        // ����Markerͼ��
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        // ����MarkerOption�������ڵ�ͼ�����Marker
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap).zIndex(9);
        // �ڵ�ͼ�����Marker������ʾ

        return (Marker) baiduMap.addOverlay(option);
    }


    private void addAllInfoWindow(List<String[]> allDevpoint) {
        // ����InfoWindowչʾ��view
        for (int i = 0; i < allDevpoint.size(); i++) {
            String[] infos = allDevpoint.get(i);
            LatLng latlng = new LatLng(
                    Double.parseDouble(infos[2]),
                    Double.parseDouble(infos[1]));

            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.mipmap.tips);
            button.setText(infos[4]);
            button.setTextSize(12);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(getResources().getColor(R.color.base_text_color));
            button.setPadding(2, 2, 2, 10);
            InfoWindow infoWindow = new InfoWindow(button, latlng, -47);
            infoWindows.add(infoWindow);
            baiduMap.showInfoWindow(infoWindow);
            // button.setBackgroundResource(R.drawable.popup);
            // ����InfoWindow�ĵ���¼�������
			/*		OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				System.out.println("��㵽���ˣ���");
				
			}
		};*/
            // ����InfoWindow
        }
    }

    /**
     * ��ʼ�����򴫸���
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
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
                        baiduMap.setMyLocationData(locData);
                        // �����Զ���ͼ��
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.mipmap.main_my_follow);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker);
                        baiduMap.setMyLocationConfigeration(config);
                    }
                });
    }

    public void saveZoomControl() {
        // ���ر����߿ؼ�
        int count = bmapView.getChildCount();

        View scale = null;

        for (int i = 0; i < count; i++) {

            View child = bmapView.getChildAt(i);

            if (child instanceof ZoomControls) {
                scale = child;
                break;
            }
        }
        scale.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bmapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // ����ͼ�㶨λ
        baiduMap.setMyLocationEnabled(true);
        // �������򴫸���
        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // �ر�ͼ�㶨λ
        baiduMap.setMyLocationEnabled(false);
        // �رշ��򴫸���
        myOrientationListener.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //bmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.clickMarkers.clear();
        MyApplication.clickMarkers_icon.clear();
        bmapView.onDestroy();
    }
}
