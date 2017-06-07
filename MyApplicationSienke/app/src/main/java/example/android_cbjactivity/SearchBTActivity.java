package example.android_cbjactivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.thksoft.myfirstpro.appcation.MyApplication;
import lvrenyang.myprinter.Global;
import lvrenyang.myprinter.WorkService;

/**
 * 蓝牙配对
 *
 * @author Administrator
 */
public class SearchBTActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    private List<String> BluetooName_list = new ArrayList<String>();// 搜到的蓝牙设备
    public String ICON = "ICON";
    public String PRINTERNAME = "PRINTERNAME";
    public String PRINTERMAC = "PRINTERMAC";
    private List<Map<String, Object>> boundedPrinters;
    private LinearLayout linearlayoutdevices;
    private ProgressBar progressBarSearchStatus;
    private ProgressDialog dialog;
    private ListView listViewSettingConnect;// 已配对设备
    private LinearLayout work_back_Btn_Bluetoo;
    private Button buttonSearch;
    private SharedPreferences preferences;
    private Editor editor;

    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;

    private static Handler mHandler = null;
    private static String TAG = "SearchBTActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_searchbt);
        // 启动服务，注意Service需要注册
        if (null == WorkService.workThread) {
            Intent intent = new Intent(this, WorkService.class);
            startService(intent);
        }
        init();
        initBroadcast();

        mHandler = new MHandler(this);
        WorkService.addHandler(mHandler);
    }

    private void init() {
        preferences = getApplication()
                .getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = preferences.edit();
        work_back_Btn_Bluetoo = (LinearLayout) findViewById(R.id.work_back_Btn_Bluetoo);
        work_back_Btn_Bluetoo.setOnClickListener(this);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
        listViewSettingConnect = (ListView) findViewById(R.id.listViewSettingConnect);
        progressBarSearchStatus = (ProgressBar) findViewById(R.id.progressBarSearchStatus);
        dialog = new ProgressDialog(this);
        boundedPrinters = getBoundedPrinters();
        listViewSettingConnect.setAdapter(new SimpleAdapter(this,
                boundedPrinters, R.layout.list_item_printernameandmac,
                new String[]{ICON, PRINTERNAME, PRINTERMAC}, new int[]{
                R.id.btListItemPrinterIcon, R.id.tvListItemPrinterName,
                R.id.tvListItemPrinterMac}));
        listViewSettingConnect.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {
        String address = (String) boundedPrinters.get(position).get(PRINTERMAC);
        String name = (String) boundedPrinters.get(position).get(PRINTERNAME);
        dialog.setMessage(Global.toast_connecting + " " + name);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        WorkService.workThread.connectBt(address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WorkService.delHandler(mHandler);
        mHandler = null;
        uninitBroadcast();
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.buttonSearch: {
                BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mAdapter.isEnabled()) {
                    // 弹出对话框提示用户是后打开
                    Intent enabler = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enabler, 2);
                    // 不做提示，强行打开
                    // mAdapter.enable();
                }

                BluetooName_list.clear();// 在多次点击搜索的时候先吧原来集合的名字清空；

                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    break;
                }
                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        break;
                    }
                }

                adapter.cancelDiscovery();
                linearlayoutdevices.removeAllViews();
                adapter.startDiscovery();
                break;
            }
            case R.id.work_back_Btn_Bluetoo:
                finish();
                break;
        }
    }

    /**
     * 在linearlayoutdevices添加搜寻到的蓝牙设备Button
     */
    private boolean isChongfu;
    private String name;

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    final String address = device.getAddress();
                    name = device.getName();
                    isChongfu = false;
                    // 先判断BluetooName_list是否为空
                    Log.e(TAG, "name=" + name);
                    if (BluetooName_list != null
                            && BluetooName_list.size() != 0) {
                        for (int i = 0; i < BluetooName_list.size(); i++) {
                            Log.e(TAG, i + "=" + BluetooName_list.get(i));
                            if (BluetooName_list.get(i).equals(name)) {
                                isChongfu = true;
                                Log.e(TAG, "名字重复");
                            }
                        }
                    }
                    if (isChongfu == false) {
                        Log.e(TAG, "isChongfu=" + isChongfu + "    name ="
                                + name);
                        BluetooName_list.add(name);
                        if (name == null)
                            name = "BT";
                        else if (name.equals(address))
                            name = "BT";
                        Button button = new Button(context);
                        button.setText(name);
                        button.setGravity(Gravity.CENTER_VERTICAL
                                | Gravity.LEFT);
                        button.setOnClickListener(new OnClickListener() {
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                // 只有没有连接且没有在用，这个才能改变状态
                                dialog.setMessage(Global.toast_connecting + ""
                                        + name);
                                dialog.setIndeterminate(true);
                                dialog.setCancelable(false);
                                dialog.show();
                                WorkService.workThread.connectBt(address);
                            }
                        });
                        button.getBackground().setAlpha(50);
                        linearlayoutdevices.addView(button);
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(false);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    static class MHandler extends Handler {

        WeakReference<SearchBTActivity> mActivity;

        MHandler(SearchBTActivity activity) {
            mActivity = new WeakReference<SearchBTActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchBTActivity theActivity = mActivity.get();
            switch (msg.what) {
                /**
                 * DrawerService 的 onStartCommand会发送这个消息
                 */

                case Global.MSG_WORKTHREAD_SEND_CONNECTBTRESULT: {
                    int result = msg.arg1;
                    Toast.makeText(
                            theActivity,
                            (result == 1) ? Global.toast_success
                                    : Global.toast_fail, Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Connect Result: " + result);
                    theActivity.dialog.cancel();
                    if (1 == result) {
                        MyApplication.Bluetoo_isClick = true;
                        PrintTest();
                        // Mytoast.showToast(mActivity.get(), "连接成功", 1000);
                    }
                    break;
                }

            }
        }

        void PrintTest() {
            // String str = "A0";
            // byte[] tmp1 = { 0x1b, 0x40, (byte) 0xB2, (byte) 0xE2, (byte)
            // 0xCA,
            // (byte) 0xD4, (byte) 0xD2, (byte) 0xB3, 0x0A };
            // byte[] tmp2 = { 0x1b, 0x21, 0x01 };
            // byte[] tmp3 = { 0x0A, 0x0A, 0x0A, 0x0A };
            // byte[] buf = DataUtils.byteArraysToBytes(new byte[][] { tmp1,
            // str.getBytes(), tmp2, str.getBytes(), tmp3 });
            if (WorkService.workThread.isConnected()) {
                // Bundle data = new Bundle();
                // data.putByteArray(Global.BYTESPARA1, buf);
                // data.putInt(Global.INTPARA1, 0);
                // data.putInt(Global.INTPARA2, buf.length);
                // WorkService.workThread.handleCmd(Global.CMD_WRITE, data);
            } else {
                Toast.makeText(mActivity.get(), Global.toast_notconnect,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 获得已配对设备集合
    private List<Map<String, Object>> getBoundedPrinters() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return list;
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                .getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a
                // ListView
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(ICON, android.R.drawable.stat_sys_data_bluetooth);
                // Toast.makeText(this,
                // ""+device.getBluetoothClass().getMajorDeviceClass(),
                // Toast.LENGTH_LONG).show();
                map.put(PRINTERNAME, device.getName());
                map.put(PRINTERMAC, device.getAddress());
                list.add(map);
            }
        }
        return list;
    }
}
