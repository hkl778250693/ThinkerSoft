package com.example.administrator.thinker_soft.meter_code.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.zxing.android.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class ScanCodeMeterFragment extends Fragment {
    private View view;
    private Button scanBtn;
    private TextView resultTv;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; //权限数组
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private List<String> permissionList = new ArrayList<>();  //权限集合

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_scan_code, null);
        bindView();
        setViewClickListener();
        return view;
    }

    public void bindView(){
        scanBtn = (Button) view.findViewById(R.id.scanBtn);
        resultTv = (TextView) view.findViewById(R.id.resultTv);
    }

    //点击事件
    private void setViewClickListener() {
        scanBtn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.scanBtn:
                    requestPermissions();
                    break;
            }
        }
    };

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(getActivity(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(getActivity(), permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(getActivity(), permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        } else {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
                Toast.makeText(getActivity(), "请求权限失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                resultTv.setText("扫码结果为：" + content);
            }
        }
    }
}
