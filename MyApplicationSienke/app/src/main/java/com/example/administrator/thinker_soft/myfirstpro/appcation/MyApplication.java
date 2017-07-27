package com.example.administrator.thinker_soft.myfirstpro.appcation;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.thinker_soft.myfirstpro.entity.Db_Name;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.GPSMeterAdapter;
import com.example.administrator.thinker_soft.myfirstpro.lvadapter.GPSValveAdapter;
import com.example.administrator.thinker_soft.myfirstpro.service.LocationService;
import com.example.administrator.thinker_soft.myfirstpro.service.LocationService.LocationBinder;
import com.example.administrator.thinker_soft.myfirstpro.util.BDLocationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyApplication extends Application {
    public static GPSMeterAdapter.GPSMeterHandler gpsmeterHandler;
    public static GPSValveAdapter.GPSValveHandler gpsvalveHandler;
    public LocationService locationService;
    public static boolean Bluetoo_isClick = false;
    public static List<Db_Name> dbname = new ArrayList<Db_Name>();
    //public static List<DeviceInfo> deviceInfos;
    public static int MARKERTYPEFARNEAR = 0;//0����� 1����Զ
    public boolean mBound = false;
    public List<Map<String, String>> newWorkList = new ArrayList<Map<String, String>>(); // ������ļ���
    public List<Map<String, String>> oldWorkList = new ArrayList<Map<String, String>>(); // ��ʷ����ļ���
    public static BDLocationModel bdLocationModel;
    public static Lock lock = new ReentrantLock();
    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
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
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        /**
         * 下拉刷新
         */
        /*//设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });*/
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
