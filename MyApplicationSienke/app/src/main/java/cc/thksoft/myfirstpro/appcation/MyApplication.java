package cc.thksoft.myfirstpro.appcation;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.thksoft.myfirstpro.entity.Db_Name;
import cc.thksoft.myfirstpro.lvadapter.GPSMeterAdapter.GPSMeterHandler;
import cc.thksoft.myfirstpro.lvadapter.GPSValveAdapter.GPSValveHandler;
import cc.thksoft.myfirstpro.lvadapter.MapMeterAdapter.MapMHandler;
import cc.thksoft.myfirstpro.service.LocationService;
import cc.thksoft.myfirstpro.service.LocationService.LocationBinder;
import cc.thksoft.myfirstpro.util.BDLocationModel;

public class MyApplication extends Application {
    public static GPSMeterHandler gpsmeterHandler;
    public static GPSValveHandler gpsvalveHandler;
    public static MapMHandler mapmhandler;
    public LocationService locationService;
    public static boolean Bluetoo_isClick = false;
    public static List<Db_Name> dbname = new ArrayList<Db_Name>();
    public static List<Marker> clickMarkers = new ArrayList<Marker>(1);
    public static List<BitmapDescriptor> clickMarkers_icon = new ArrayList<BitmapDescriptor>(
            1);
    //public static List<DeviceInfo> deviceInfos;
    public static int MARKERTYPEFARNEAR = 0;//0����� 1����Զ
    public boolean mBound = false;
    public List<Map<String, String>> newWorkList = new ArrayList<Map<String, String>>(); // ������ļ���
    public List<Map<String, String>> oldWorkList = new ArrayList<Map<String, String>>(); // ��ʷ����ļ���
    public static BDLocationModel bdLocationModel;
    public static Lock lock = new ReentrantLock();
    public ServiceConnection mConnection = new ServiceConnection() {//�����������service�Ķ��󣬵���δ����

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // �����Ѿ��󶨵���LocalService����IBinder����ǿ������ת�����һ�ȡLocalServiceʵ����
            LocationBinder binder = (LocationBinder) service;
            locationService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        bdLocationModel = new BDLocationModel(getApplicationContext(), null);

    }

    public void ShutDownService(){
        if (mBound) {
            unbindService(this.mConnection);
            mBound = false;
        }
    }
    public List<Map<String, String>> getOldWorkList() {
        return oldWorkList;
    }

    public void setOldWorkList(List<Map<String, String>> oldWorkList) {
        if(oldWorkList!=null){
            this.oldWorkList.addAll(oldWorkList);
        }
    }

    public List<Map<String, String>> getNewWorkList() {
        return newWorkList;
    }

    public void setNewWorkList(List<Map<String, String>> newWorkList) {
        this.newWorkList.clear();
        if(newWorkList!=null){
            this.newWorkList.addAll(newWorkList);
        }
    }
}
