package com.example.administrator.thinker_soft.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;

import java.util.List;

public class ChaoBiaoBenTongjiActivity extends Activity {
    private ImageView back;
    private TextView allUserNumber, doneNumber, undone_number, meterNumber, finishRate;
    private RadioButton allUserStatistics, bookStatistics;
    private String filepath;
    private String DBName;
    private String temp;
    private DBService service;
    private List<String> usList;
    private List<String> bookName;
    private int totaluser, meterdone, meterno, meterCount;
    private double completrate;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaobiaotongji);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        allUserNumber = (TextView) findViewById(R.id.all_user_number);
        doneNumber = (TextView) findViewById(R.id.done_number);
        undone_number = (TextView) findViewById(R.id.undone_number);
        meterNumber = (TextView) findViewById(R.id.meter_number);
        finishRate = (TextView) findViewById(R.id.finish_rate);
        allUserStatistics = (RadioButton) findViewById(R.id.all_user_statistics);
        bookStatistics = (RadioButton) findViewById(R.id.book_statistics);
    }

    //初始化设置
    private void defaultSetting() {
        allUserStatistics.setChecked(true);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
        DBName = sharedPreferences.getString("dbName", "");
        if (DBName != null && !"".equals(DBName)) {
            filepath = Environment.getDataDirectory().getPath() + "/data/" + "com.example.android_cbjactivity" + "/databases/";
        } else {
            Toast.makeText(ChaoBiaoBenTongjiActivity.this, "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
        }
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        handler = new Handler();
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        allUserStatistics.setOnClickListener(clickListener);
        bookStatistics.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.all_user_statistics:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            service = new DBService(filepath + DBName);
                            usList = service.querystatisticsinfos();
                            service.connclose();
                            System.out.println(usList.get(0));
                            if ("0".equals(usList.get(0)) && "0".equals(usList.get(1)) && "0".equals(usList.get(2))) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Adddata();
                            }
                        }
                    }.start();
                    break;
                case R.id.book_statistics:
                    SharedPreferences sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("signal", 10);
                    editor.commit();
                    Intent intent = new Intent(ChaoBiaoBenTongjiActivity.this, ChaoBiaoXuanZeActivity.class);
                    intent.putExtra("DBName", DBName);
                    int requestCode = 1;
                    startActivityForResult(intent, requestCode);
            /*Thread thread = new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					System.out.println("���볭��ͳ���߳�");
					service = new DBService(filepath+DBName);
					bookName = service.queryBookofUser();
					service.connclose();
				}
			};
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String[] array = (String[])bookName.toArray(new String[bookName.size()]);
			if(array.length<=0){
				array = new String[]{"û���ļ�"};
			}
			if(array!=null){
			new AlertDialog.Builder(ChaoBiaoBenTongjiActivity.this)
			.setTitle("�����ļ�ѡ��")
			.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String name = bookName.get(which);
					Thread thread = new Thread(){
						@Override
						public void run() {
							super.run();
							service = new DBService(filepath+DBName);
							usList = service.querystatisticsinfosbybook(name);
							service.connclose();
							if("0".equals(usList.get(0))&&"0".equals(usList.get(1))&&"0".equals(usList.get(2))){
								handler.post(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
									}
								});
							}else{
								Adddata();
							}
						}
					};
					thread.start();
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				}
			})
			.setNegativeButton("ȡ��", null)
			.show();
			} */
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            String tempname = data.getAction();
            service = new DBService(filepath + DBName);
            usList = service.querystatisticsinfosbybook(tempname);
            if ("0".equals(usList.get(0)) && "0".equals(usList.get(1)) && "0".equals(usList.get(2))) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChaoBiaoBenTongjiActivity.this, "û�п�������", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Adddata();
            }
        }
    }

    public void Adddata() {
        totaluser = Integer.parseInt(usList.get(0));
        meterdone = Integer.parseInt(usList.get(1));
        if (usList.get(2) == null || "".equals(usList.get(2))) {
            meterCount = Integer.parseInt("0");
        } else
            meterCount = Integer.parseInt(usList.get(2));

        meterno = totaluser - meterdone;
        if (totaluser != 0) {
            completrate = (double) meterdone / (double) totaluser;
            completrate = Double.parseDouble(String.format("%.4f", completrate));
            System.out.println("completrate:" + completrate);
            temp = String.valueOf(completrate * 100);
            int loc = temp.indexOf(".");
            if (temp.length() > loc + 3) {
                temp = temp.substring(0, loc + 3);
            } else if (temp.length() == loc + 2) {
                temp = temp.substring(0, loc + 2);
                temp = temp + "0";
            } else if (temp.length() == loc + 1) {
                temp = temp.substring(0, loc + 1);
                temp = temp + "00";
            } else if (temp.length() == loc) {
                temp = temp.substring(0, loc);
                temp = temp + ".00";
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                allUserNumber.setText(String.valueOf(totaluser));
                doneNumber.setText(String.valueOf(meterdone));
                undone_number.setText(String.valueOf(meterno));
                meterNumber.setText(String.valueOf(meterCount));
                finishRate.setText(temp);
            }
        });

    }
}
