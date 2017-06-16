package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.niftydialogeffects.Effectstype;
import com.example.administrator.thinker_soft.niftydialogeffects.NiftyDialogBuilder;

public class SheZiActivity extends Activity {
    private String TAG = "SheZiActivity";
    private TextView exit;// �˳�����
    private TextView tv_ip_port;
    private LinearLayout replace_use;// �л��˺�
    private TextView tv_setCount;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private int number;
    private String MAP_isDownload;// ��ͼ�Ƿ�����
    private String ip;
    private String port;
    private String printNote;
    ;
    private TextView map_xiazhai;
    private TextView print_note_text;
    private TextView tv_set_bluetooth_state;
    private NiftyDialogBuilder dialogBuilder;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1; // ��ȡ�豸��Ϣ
    private int conn_flag = 0;
    private MyActivityManager mam = MyActivityManager.getInstance();

    // ----------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mam.pushOneActivity(this);
        setContentView(R.layout.activity_sehzi);
        tv_ip_port = (TextView) findViewById(R.id.tv_set_ip_port);
        tv_setCount = (TextView) findViewById(R.id.tv_set_count);
        map_xiazhai = (TextView) findViewById(R.id.map_xiazhai);
        tv_set_bluetooth_state = (TextView) findViewById(R.id.tv_set_bluetooth_state);
        print_note_text = (TextView) findViewById(R.id.print_note_text);
        exit = (TextView) findViewById(R.id.exit);// �˳�����
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new NiftyDialogBuilder(SheZiActivity.this,
                        R.style.dialog_untran);
                dialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
                        .withDividerColor("#999999").withMessage("ȷ���˳�����")
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
                        mam.finishAllActivity();
                    }
                }).show();
                Log.e("xxq", "�˳�");

            }
        });
        replace_use = (LinearLayout) findViewById(R.id.replace_use);// �����˺�
        replace_use.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new NiftyDialogBuilder(SheZiActivity.this,
                        R.style.dialog_untran);
                dialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
                        .withDividerColor("#999999").withMessage("ȷ�������˺�")
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
                        intent = new Intent();
                        intent.setClass(SheZiActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        mam.finishAllActivity();
                    }
                }).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        if (sharedPreferences.contains("number")) {
            number = sharedPreferences.getInt("number", 0);
            System.out.println(number);
            tv_setCount.setText("��ǰÿҳ��ʾ������ " + String.valueOf(number));
        }
        if (sharedPreferences.contains("MAP")) {
            MAP_isDownload = sharedPreferences.getString("MAP", "");
            System.out.println(MAP_isDownload);
            map_xiazhai.setText(MAP_isDownload);
        }
        if (sharedPreferences.contains("ip")) {
            ip = sharedPreferences.getString("ip", "");
            System.out.println(ip);
        }
        if (sharedPreferences.contains("printNote")) {
            printNote = sharedPreferences.getString("printNote", "");
            print_note_text.setText("��ӡ��ע��" + printNote);
        }
        if (sharedPreferences.contains("port")) {
            port = sharedPreferences.getString("port", "");
            System.out.println(port);
        }
        if (ip != null && port != null)
            tv_ip_port.setText("IP��" + ip + "  " + "PORT��" + port);
    }

    public void onAction(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_set_bluetooth:
                Intent serverIntent = new Intent(SheZiActivity.this,
                        SearchBTActivity.class); // ��������һ����Ļ
                startActivity(serverIntent);
                break;
            case R.id.set_back_Btn:
                finish();
                break;
            case R.id.ll_set_ip_port:
                intent = new Intent();
                intent.setClass(this, Ip_Port_activity.class);
                startActivity(intent);
                break;
            case R.id.ll_set_delete:
                intent = new Intent();
                intent.setClass(this, DelDataBasesActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_set_count:
                intent = new Intent();
                intent.setClass(this, ChooseCountActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_map_xiazhai:
                intent = new Intent();
                intent.setClass(this, MapXiazhaiActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_print_note:// ��ӡ��ע
                intent = new Intent();
                intent.setClass(this, printNoteActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
