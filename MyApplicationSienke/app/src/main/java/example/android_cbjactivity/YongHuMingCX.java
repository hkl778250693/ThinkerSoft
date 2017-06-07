package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import cc.thksoft.myfirstpro.entity.UsersInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.service.DBService;
import cc.thksoft.myfirstpro.util.Gadget;

public class YongHuMingCX extends Activity {
    private EditText et;
    private Button btn;
    private String filepath;
    private String DBName;
    private DBService service;
    private List<UsersInfo> usList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        setContentView(R.layout.activity_user_name);
        et = (EditText) findViewById(R.id.edit_usname);
        btn = (Button) findViewById(R.id.btn_quedin);
        sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        editor = sharedPreferences.edit();
        DBName = sharedPreferences.getString("dbName", "");
        if (DBName != null && !"".equals(DBName)) {
            btn.setEnabled(true);
            filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
        } else {
            btn.setEnabled(false);
            Toast.makeText(getApplicationContext(), "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_quedin:
                String usID = et.getText().toString();
                if ("".equals(usID) || usID == null) {
                    Toast.makeText(getApplicationContext(), "����д��ѯ��Ϣ", Toast.LENGTH_LONG).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            int signal = 7;
                            int con_signal = 7;
                            String usName = et.getText().toString();
                            editor.putInt("signal", signal);
                            editor.putInt("con_signal", con_signal);
                            editor.putString("usName", usName);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.setClass(YongHuMingCX.this, ShowListviewActivity.class);
                            startActivity(intent);
                        }
                    }.start();
                }
                break;
            case R.id.btn_quxiao:
                if (et != null && et.getText() != null) {
                    et.setText("");
                }
                break;
            case R.id.uhm_back_btn:
                Gadget.closeKeybord(et, YongHuMingCX.this);
                finish();
                break;
            default:
                break;
        }
    }
}
