package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MapMeterActivity extends Activity {
    private MapView mMapView;
    private RadioButton openRb;
    private RadioButton closeRb;
    private boolean mEnableCustomStyle = true;
    //用于设置个性化地图的样式文件
    // 提供三种样式模板："custom_config_blue.txt"，"custom_config_dark.txt"，"custom_config_midnightblue.txt"
    private static String PATH = "custom_config_dark.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_meter);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        openRb = (RadioButton) findViewById(R.id.open_rb);
        closeRb = (RadioButton) findViewById(R.id.close_rb);
    }

    //初始化设置
    private void defaultSetting() {
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(29.595559,106.579227);  //默认重庆星耀天地29.595559,106.579227
        float zoom = 11.0f; // 默认 11级
        Intent intent = getIntent();
        if (null != intent) {
            mEnableCustomStyle = intent.getBooleanExtra("customStyle", true);
            center = new LatLng(intent.getDoubleExtra("y", 29.595559), intent.getDoubleExtra("x", 106.579227));
            zoom = intent.getFloatExtra("level", 11.0f);
        }
        builder.target(center).zoom(zoom);
        setMapCustomFile(MapMeterActivity.this,PATH);
        mMapView = new MapView(MapMeterActivity.this,new BaiduMapOptions());
        if (mEnableCustomStyle) {
            openRb.setChecked(true);
        } else {
            closeRb.setChecked(true);
        }
        MapView.setMapCustomEnable(true);
    }

    //点击事件
    public void setViewClickListener() {
        mMapView = (MapView) findViewById(R.id.map_meter);
        openRb.setOnClickListener(clickListener);
        closeRb.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.open_rb:
                    MapView.setMapCustomEnable(true);
                    break;
                case R.id.close_rb:
                    MapView.setMapCustomEnable(false);
                    break;
            }
        }
    };

    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context, String PATH) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open("customConfigdir/" + PATH);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + PATH);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(moduleName + "/" + PATH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        MapView.setMapCustomEnable(false);
        mMapView.onDestroy();
    }

}
