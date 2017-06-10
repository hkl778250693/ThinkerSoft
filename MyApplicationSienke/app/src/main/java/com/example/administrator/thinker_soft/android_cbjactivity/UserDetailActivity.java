package com.example.administrator.thinker_soft.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.util.Mytoast;
import com.example.administrator.thinker_soft.myprinter.WorkService;
import com.example.administrator.thinker_soft.niftydialogeffects.NiftyDialogBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.niftydialogeffects.Effectstype;
import com.example.administrator.thinker_soft.myprinter.Global;

public class UserDetailActivity extends Activity {
	private String TAG = "UserDetailActivity";
	private UsersInfo usInfo;
	private TextView bluetooth_printbtn;// ������ӡ��ť
	private TextView view1, view2, view3, view4, view5, view6, view7, view8,
			view9, view10, view11, view12, view13, view14, view15, view16,
			view17, view18, view19, view20, view21, view22, view23, view24,
			view25;
	private String lat, lon, temp1, temp2, temp3, temp4, temp5, temp6, temp7,
			temp8, temp9, temp10, state, temp12, temp13, temp14,
			temp15, temp16, temp17, temp18, temp19, temp20, temp21, temp22,
			temp23, temp24, temp25;
	private EditText editText;
	private TextView tv_latitude, tv_longitude;
	private LinearLayout detail_lon_lat_ll;
	private LinearLayout detail_dosage_ll;
	private LinearLayout detail_et_ll;
	private LinearLayout layout;
	private StringBuffer result;
	private int index;// ���λ��
	// .........................
	private DBService dbService;
	private String filepath = Environment.getDataDirectory().getPath()
			+ "/data/" + "com.example.android_cbjactivity" + "/databases/";
	private String dbName;

	private String LONGITUDE;
	private String LATIUDE;// γ��
	private String sb;
	private SharedPreferences sharedPreferences;
	private NiftyDialogBuilder dialogBuilder;
	private String trunPage;
	private int position;// ǰҳ����Item��λ�á�
	private boolean isModifyData = false;

	// ..................������ӡ����........................
	private String printText;// ��Ҫ��ӡ�Ķ���
	private String printNote = "����";// ��ӡ��ע
	private static int nFontSize = 0, nScaleTimesWidth = 0,
			nLineHeight = 32,
			nRightSpace = 0;
	private double UnitPrice = 3.7;// ���ۣ���ʱֻ��������õ�һ�����ۣ�������Ҫ��ʵ�ĵ���
	private double AllPrice;// ���еĽ��
	private double WaterConsumption;// ��ˮ��
	// ---------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdetail);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		result = new StringBuffer("");
		sharedPreferences = getApplication().getSharedPreferences(
				"IP_PORT_DBNAME", 0);
		dbName = sharedPreferences.getString("dbName", "");
		if (sharedPreferences.contains("printNote")) {
			printNote = sharedPreferences.getString("printNote", "");
			Log.e(TAG, "printNote=" + printNote);
		}
		/*
		 * sb = sharedPreferences.getString("sb", "");
		 * System.out.println("sb:"+sb); if(sb==null||"".equals(sb)){
		 * Toast.makeText(getApplicationContext(), "û�п��þ�γ��",
		 * Toast.LENGTH_SHORT).show(); return; } int loc = sb.indexOf("$");
		 * LATIUDE = sb.substring(0, loc-1); LONGITUDE = sb.substring(loc+1,
		 * sb.length());
		 */
		bluetooth_printbtn = (TextView) findViewById(R.id.bluetooth_printbtn);
		view1 = (TextView) findViewById(R.id.text1);
		view2 = (TextView) findViewById(R.id.text2);
		view3 = (TextView) findViewById(R.id.text3);
		view4 = (TextView) findViewById(R.id.text4);
		view5 = (TextView) findViewById(R.id.text5);
		view6 = (TextView) findViewById(R.id.text6);
		view7 = (TextView) findViewById(R.id.text7);
		view8 = (TextView) findViewById(R.id.text8);
		view9 = (TextView) findViewById(R.id.text9);
		view10 = (TextView) findViewById(R.id.text10);
		view11 = (TextView) findViewById(R.id.text11);
		view12 = (TextView) findViewById(R.id.text12);
		view13 = (TextView) findViewById(R.id.text13);
		view14 = (TextView) findViewById(R.id.text14);
		view15 = (TextView) findViewById(R.id.text15);
		view16 = (TextView) findViewById(R.id.text16);
		view17 = (TextView) findViewById(R.id.text17);
		view18 = (TextView) findViewById(R.id.text18);
		view19 = (TextView) findViewById(R.id.text19);
		view20 = (TextView) findViewById(R.id.text20);
		view21 = (TextView) findViewById(R.id.text21);
		view22 = (TextView) findViewById(R.id.text22);
		view23 = (TextView) findViewById(R.id.text23);
		view24 = (TextView) findViewById(R.id.text24);
		view25 = (TextView) findViewById(R.id.text25);
		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);
		detail_lon_lat_ll = (LinearLayout) findViewById(R.id.detail_lon_lat_ll);
		detail_dosage_ll = (LinearLayout) findViewById(R.id.detail_dosage_ll);
		detail_et_ll = (LinearLayout) findViewById(R.id.detail_et_ll);
		layout = (LinearLayout) findViewById(R.id.ll_detail_btn);
		editText = (EditText) findViewById(R.id.et_useage);
		editText.getBackground().setAlpha(40);
		Intent intent = getIntent();
		usInfo = (UsersInfo) intent.getSerializableExtra("usInfo");
		position = intent.getIntExtra("position", -1);
		trunPage = intent.getStringExtra("action");
		temp6 = "";
		if ("GPSCollectorActivity".equals(trunPage)) {
			bluetooth_printbtn.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			detail_et_ll.setVisibility(View.GONE);
			LayoutParams layoutParams = (LayoutParams) detail_dosage_ll
					.getLayoutParams();
			layoutParams.weight = 10;
			editText.setVisibility(View.GONE);
			temp6 = "".equals(usInfo.getTHISMONTH_RECORD()) ? "����" : usInfo
					.getTHISMONTH_RECORD();
		} else if ("ShowListviewActivity".equals(trunPage)
				|| "CaptureActivity".equals(trunPage)
				|| "MapMeterActivity".equals(trunPage)
				|| "CbbCXAction".equals(trunPage)) {
			detail_lon_lat_ll.setVisibility(View.GONE);
			editText.setVisibility(View.VISIBLE);
			detail_et_ll.setVisibility(View.VISIBLE);
		}
		lat = "".equals(usInfo.getBASE_LATIUDE()) ? "����" : usInfo
				.getBASE_LATIUDE();
		tv_latitude.setText("γ��:" + lat);
		lon = "".equals(usInfo.getBASE_LONGITUDE()) ? "����" : usInfo
				.getBASE_LONGITUDE();
		tv_longitude.setText("����:" + lon);
		view6.setText("  ���¶���:    " + temp6);
		System.out.println("��ת��" + usInfo.getBOOK());
		System.out.println("��ת��" + usInfo.getLASTMONTH_RECORD());
		temp1 = "".equals(usInfo.getUSNAME()) ? "����" : usInfo.getUSNAME();
		view1.setText("  �û�����:    " + temp1);
		temp2 = "".equals(usInfo.getUSID()) ? "����" : usInfo.getUSID();
		view2.setText("��  �±��:    " + temp2);
		temp3 = "".equals(usInfo.getOLDUSID()) ? "����" : usInfo.getOLDUSID();
		view3.setText("  �û����:    " + temp3);
		temp4 = "".equals(usInfo.getMETERID()) ? "����" : usInfo.getMETERID();
		view4.setText("         ���:    " + temp4);
		temp5 = "".equals(usInfo.getUSAREA()) ? "����" : usInfo.getUSAREA();
		view5.setText("  ����Ƭ��:    " + temp5);

		temp7 = "".equals(usInfo.getTHISMONTH_DOSAGE()) ? "����" : usInfo
				.getTHISMONTH_DOSAGE();
		view7.setText("  ��������:    " + temp7);
		temp8 = "".equals(usInfo.getLASTMONTH_RECORD()) ? "����" : usInfo
				.getLASTMONTH_RECORD();
		view8.setText("  ���¶���:    " + temp8);
		temp9 = "".equals(usInfo.getLASTMONTH_DOSAGE()) ? "����" : usInfo
				.getLASTMONTH_DOSAGE();
		view9.setText("  ��������:    " + temp9);
		temp10 = "".equals(usInfo.getDOMETERDATE()) ? "����" : usInfo
				.getDOMETERDATE();
		view10.setText("  ��������:    " + temp10);
		state = "0".equals(usInfo.getDOMETERSIGNAL()) ? "δ��" : "�ѳ�";
		view11.setText("  �����־:    " + state + "");
		temp12 = "".equals(usInfo.getUSADRESS()) ? "����" : usInfo.getUSADRESS();
		view12.setText("  �û���ַ:    " + temp12);
		temp13 = "".equals(usInfo.getUSPHONE()) ? "����" : usInfo.getUSPHONE();
		view13.setText("  �û��绰:    " + temp13);
		temp14 = "".equals(usInfo.getUSBALANCE()) ? "����" : usInfo
				.getUSBALANCE();
		view14.setText("  �û����:    " + temp14);
		temp15 = "".equals(usInfo.getUSDEBT()) ? "����" : usInfo.getUSDEBT();
		view15.setText("  Ƿ�ѽ��:    " + temp15);
		temp16 = "".equals(usInfo.getDEBTMONTHS()) ? "����" : usInfo
				.getDEBTMONTHS();
		view16.setText("  Ƿ������:    " + temp16);
		temp17 = "".equals(usInfo.getC_PROPERTIES_NAME()) ? "����" : usInfo
				.getC_PROPERTIES_NAME();
		view17.setText("  �û�����:    " + temp17);
		temp18 = "".equals(usInfo.getBOOK()) ? "����" : usInfo.getBOOK();
		view18.setText("��  ����:    " + temp18);
		temp19 = "".equals(usInfo.getDOMETERID()) ? "����" : usInfo
				.getDOMETERID();
		view19.setText("  �������:    " + temp19);
		temp20 = "".equals(usInfo.getMETERKINDS()) ? "����" : usInfo
				.getMETERKINDS();
		view20.setText("��  ���ͺ�:    " + temp20);
		temp21 = "".equals(usInfo.getMETERREADER()) ? "����" : usInfo
				.getMETERREADER();
		view21.setText("��  ����Ա:    " + temp21);
		temp22 = "".equals(usInfo.getCHANGEAMOUNT()) ? "����" : usInfo
				.getCHANGEAMOUNT();
		view22.setText("��  ������:    " + temp22);
		temp23 = "".equals(usInfo.getSTARTAMOUNT()) ? "����" : usInfo
				.getSTARTAMOUNT();
		view23.setText("��  ������:    " + temp23);
		temp24 = "".equals(usInfo.getADD_OR_REDUCE_AMOUNT()) ? "����" : usInfo
				.getADD_OR_REDUCE_AMOUNT();
		view24.setText("��  �Ӽ���:    " + temp24);
		temp25 = "".equals(usInfo.getGARBAGEMONEY()) ? "����" : usInfo
				.getGARBAGEMONEY();
		view25.setText("��  ������:    " + temp25);

		final String temprecord = usInfo.getTHISMONTH_RECORD();
		if (temprecord != null && !"".equals(temprecord)) {
			/*
			 * if("0".equals(temprecord)){ editText.setText("");
			 *
			 * }else{
			 */
			editText.setText(temprecord);
			editText.setSelection(temprecord.length());
			// }
		}
		/*
		 * editText.clearFocus(); editText.setCursorVisible(false);
		 */
		editText.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int temp = editText.getText().toString().length();
				if (temp != 0) {
					index = temp;
				}

				index = 0;
				layout.setVisibility(LinearLayout.VISIBLE);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

				int currentVersion = android.os.Build.VERSION.SDK_INT;
				String methodName = null;
				if (currentVersion >= 16) {
					// 4.2
					methodName = "setShowSoftInputOnFocus";
				} else if (currentVersion >= 14) {
					// 4.0
					methodName = "setSoftInputShownOnFocus";
				}

				if (methodName == null) {
					editText.setInputType(InputType.TYPE_NULL);
				} else {
					Class<EditText> cls = EditText.class;
					Method setShowSoftInputOnFocus;
					try {
						setShowSoftInputOnFocus = cls.getMethod(methodName,
								boolean.class);
						setShowSoftInputOnFocus.setAccessible(true);
						setShowSoftInputOnFocus.invoke(editText, false);
					} catch (NoSuchMethodException e) {
						editText.setInputType(InputType.TYPE_NULL);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				/* Gadget.closeKeybord(editText, getApplicationContext()); */
				/*
				 * editText.requestFocus(); editText.setCursorVisible(true);
				 */
				editText.setSelection(index);
				return false;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (layout.getVisibility() == View.VISIBLE) {
				layout.setVisibility(LinearLayout.GONE);
			} else {
				if ("ShowListviewActivity".equals(trunPage) && usInfo != null
						&& isModifyData) {
					Intent intent = new Intent(UserDetailActivity.this,
							ShowListviewActivity.class);
					intent.putExtra("userinfo", usInfo);
					setResult(10, intent);
					System.out.println("�ش�");
				} else if ("MapMeterActivity".equals(trunPage)
						&& usInfo != null && isModifyData) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("key", "1");
					bundle.putSerializable("usInfo", usInfo);
					if (position != -1)
						bundle.putInt("position", position);
					msg.setData(bundle);
					MyApplication.mapmhandler.sendMessage(msg);
				}
				UserDetailActivity.this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// TODO
	public void onAction(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.detail_backButton:
			if ("ShowListviewActivity".equals(trunPage) && usInfo != null
					&& isModifyData) {
				Intent intent = new Intent(UserDetailActivity.this,
						ShowListviewActivity.class);
				// intent.putExtra("userinfo", usInfo);
				setResult(10, intent);
			} else if ("MapMeterActivity".equals(trunPage) && usInfo != null
					&& isModifyData) {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("key", "1");
				bundle.putSerializable("usInfo", usInfo);
				if (position != -1)
					bundle.putInt("position", position);
				msg.setData(bundle);
				MyApplication.mapmhandler.sendMessage(msg);
			}
			UserDetailActivity.this.finish();
			break;


		 case R.id.bluetooth_printbtn:// ��ӡ�߽ɵ�
			WaterConsumption = Double.parseDouble(temp7.trim());
			//�۸��㷨
			AllPrice = WaterConsumption * UnitPrice;

			Log.e(TAG, "editText.getText()=" + editText.getText());
			if ("".equals(editText.getText().toString().trim())) {
				Mytoast.showToast(this, "���ȳ�¼���¶����ɹ����ٴ�ӡ", 2000);
				break;
			}
			printText = "���ţ�" + temp2 + "\r\n" + "������" + temp1 + "\r\n" + "��ַ��"
					+ temp12 + "\r\n" + "��ȣ�" + temp8 + "\r\n" + "ֹ�ȣ�"
					+ usInfo.getTHISMONTH_RECORD() + "\r\n" + "������"
					+ usInfo.getTHISMONTH_DOSAGE() + "\r\n\r\n" + "���ʣ�"
					+ temp17 + "\r\n" + "���ۣ�" + UnitPrice + "\r\n" + "����: "
					+ temp7 + "\r\n" + "��" + AllPrice + "\r\n\r\n" + "Ƿ�ѽ�"
					+ temp15 + "\r\n\r\n" + "����Ա��" + temp21 + "\r\n" + "�������ڣ�"
					+ temp10 + "\r\n\r\n" + "��ע��" + printNote + "\r\n"
					+ "����绰��5580888" + "\r\n\r\n\r\n";

			Log.e(TAG, "printText" + printText);

			// �ж������Ƿ��
			BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
			if (!mAdapter.isEnabled()) {
				// �����Ի�����ʾ�û��Ǻ��
				Mytoast.showToast(this, "�������򿪺����µ����������ӡ��", 2000);
				Intent enabler = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enabler, 2);
				// // ������ʾ��ǿ�д�
				// mAdapter.enable();
				break;
			}
			if (MyApplication.Bluetoo_isClick == false) {
				Toast.makeText(this, "����ϵͳ���������Ӵ�ӡ��", Toast.LENGTH_SHORT).show();
			} else {
				// ��Ҫֱ�Ӻ�Pos�򽻵���Ҫͨ��workThread������
				if (WorkService.workThread.isConnected()) {
					Mytoast.showToast(UserDetailActivity.this, "���ڴ�ӡ֪ͨ����", 1000);
					int charset = 0, codepage = 0;
					String encoding = "";
					byte[] addBytes = new byte[0];
					encoding = "GBK";
					charset = 15;
					codepage = 255;
					// ------------------------��ӡ����---------------------
					String text = "����������ˮ��˾\n����֪ͨ��\n";
					Bundle dataCP = new Bundle();
					Bundle dataAlign = new Bundle();
					Bundle dataRightSpace = new Bundle();
					Bundle dataLineHeight = new Bundle();
					Bundle dataTextOut = new Bundle();
					Bundle dataWrite = new Bundle();
					dataCP.putInt(Global.INTPARA1, charset);// 15
					dataCP.putInt(Global.INTPARA2, codepage);// 255
					dataAlign.putInt(Global.INTPARA1, 1);// ���뷽ʽ

					dataRightSpace.putInt(Global.INTPARA1, nRightSpace);// 0
					dataLineHeight.putInt(Global.INTPARA1, nLineHeight);// 32

					dataTextOut.putString(Global.STRPARA1, text);
					dataTextOut.putString(Global.STRPARA2, encoding);// GBK
					dataTextOut.putInt(Global.INTPARA1, 0);
					dataTextOut.putInt(Global.INTPARA2, nScaleTimesWidth);// 0
					dataTextOut.putInt(Global.INTPARA3, 1);// 0����߲��Ŵ� 1����߷Ŵ�һ��
					dataTextOut.putInt(Global.INTPARA4, nFontSize);// 0
					dataTextOut.putInt(Global.INTPARA5, 0);// ������

					dataWrite.putByteArray(Global.BYTESPARA1, addBytes);
					dataWrite.putInt(Global.INTPARA1, 0);
					dataWrite.putInt(Global.INTPARA2, addBytes.length);

					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETCHARSETANDCODEPAGE, dataCP);
					WorkService.workThread.handleCmd(Global.CMD_POS_SALIGN,
							dataAlign);
					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETRIGHTSPACE, dataRightSpace);
					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETLINEHEIGHT, dataLineHeight);
					WorkService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
							dataTextOut);
					WorkService.workThread.handleCmd(Global.CMD_POS_WRITE,
							dataWrite);

					// ---------------��ӡ����-----------------
					Bundle dataCP1 = new Bundle();
					Bundle dataAlign1 = new Bundle();
					Bundle dataRightSpace1 = new Bundle();
					Bundle dataLineHeight1 = new Bundle();
					Bundle dataTextOut1 = new Bundle();
					Bundle dataWrite1 = new Bundle();
					dataCP1.putInt(Global.INTPARA1, charset);// 15
					dataCP1.putInt(Global.INTPARA2, codepage);// 255
					dataAlign1.putInt(Global.INTPARA1, 0);// ���뷽ʽ

					dataRightSpace1.putInt(Global.INTPARA1, nRightSpace);// 0
					dataLineHeight1.putInt(Global.INTPARA1, nLineHeight);// 32
					dataTextOut1.putString(Global.STRPARA1, printText);
					dataTextOut1.putString(Global.STRPARA2, encoding);// GBK
					dataTextOut1.putInt(Global.INTPARA1, 0);
					dataTextOut1.putInt(Global.INTPARA2, nScaleTimesWidth);// 0
					dataTextOut1.putInt(Global.INTPARA3, 0);// 0����߲��Ŵ� 1����߷Ŵ�һ��
					dataTextOut1.putInt(Global.INTPARA4, nFontSize);// 0
					dataTextOut1.putInt(Global.INTPARA5, 0);// ������

					dataWrite1.putByteArray(Global.BYTESPARA1, addBytes);
					dataWrite1.putInt(Global.INTPARA1, 0);
					dataWrite1.putInt(Global.INTPARA2, addBytes.length);

					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETCHARSETANDCODEPAGE, dataCP1);
					WorkService.workThread.handleCmd(Global.CMD_POS_SALIGN,
							dataAlign1);
					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETRIGHTSPACE, dataRightSpace1);
					WorkService.workThread.handleCmd(
							Global.CMD_POS_SETLINEHEIGHT, dataLineHeight1);
					WorkService.workThread.handleCmd(Global.CMD_POS_STEXTOUT,
							dataTextOut1);
					WorkService.workThread.handleCmd(Global.CMD_POS_WRITE,
							dataWrite1);
				} else {
					Toast.makeText(this, "����ϵͳ���������Ӵ�ӡ��", Toast.LENGTH_SHORT)
							.show();
				}

			}
			break;
		default:
			break;
		}
	}

	// ...........����................
	@SuppressLint("NewApi")
	public void onButton(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.one:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(1);
				editText.setText(result);
				index++;
				System.out.println("index:" + index);
				editText.setSelection(index);
			}
			break;
		case R.id.two:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(2);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.three:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(3);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.four:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(4);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.five:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(5);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.six:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(6);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.seven:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(7);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.eight:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(8);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.night:
			if (result.length() < 9) {
				if ("0".equals(editText.getText().toString())) {
					result.deleteCharAt(0);
				}
				result.append(9);
				editText.setText(result);
				index++;
				editText.setSelection(index);
			}
			break;
		case R.id.zero:
			if (editText.getText().toString() == null
					|| "".equals(editText.getText().toString())) {
				result.append(0);
				editText.setText(result);
				break;
			} else {
				if ("0".equals(editText.getText().toString())) {
					break;
				}
				if (result.length() < 9) {
					result.append(0);
					editText.setText(result);
					index++;
					editText.setSelection(index);
				}
			}
			break;
		case R.id.done:
			// ..........��GPS...........
			LocationManager alm = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
			if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				dialogBuilder = new NiftyDialogBuilder(UserDetailActivity.this,
						R.style.dialog_untran);
				dialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
						.withDividerColor("#999999")
						.withMessage("���GPS�����������ڹ���������������ӣ�")
						.withMessageColor("#000000")
						.isCancelableOnTouchOutside(true).withDuration(700)
						.withEffect(Effectstype.Slidetop)
						.withButton1Text("ȡ������").withButton2Text("��������")
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent, 0); // ��Ϊ������ɺ󷵻ص���ȡ����
								dialogBuilder.dismiss();
							}
						}).show();
				return;
			}
			sb = sharedPreferences.getString("sb", "");
			if (sb == null || "".equals(sb)) {
				Toast.makeText(getApplicationContext(),
						"û�п��þ�γ��,�뿪�������GPS,���ѿ����ŵȴ�Ƭ�̣�", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int loc = sb.indexOf("$");
			LATIUDE = sb.substring(0, loc - 1);
			LONGITUDE = sb.substring(loc + 1, sb.length());
			final String thisRecord = editText.getText().toString();// ���¶���
			if (thisRecord != null && !"".equals(thisRecord)) {

				String lastRecord = usInfo.getLASTMONTH_RECORD();// ���¶���
				// ��������
				final String thisDosage = String.valueOf(Integer
						.parseInt(thisRecord) - Integer.parseInt(lastRecord));
				if (Integer.parseInt(thisDosage) < 0) {// �ų����������û�
					Toast.makeText(UserDetailActivity.this, "����ȷ��д��Ϣ",
							Toast.LENGTH_SHORT).show();
					break;
				}
				String LOATRANGE = usInfo.getFLOATRANGE();
				if ("".equals(LOATRANGE)) {
					LOATRANGE = "0";
				}
				if (Integer.parseInt(thisDosage) > Integer.parseInt(LOATRANGE)
						&& !"0".equals(LOATRANGE)) {
					dialogBuilder = new NiftyDialogBuilder(
							UserDetailActivity.this, R.style.dialog_untran);
					dialogBuilder
							.withTitle("��ʾ")
							.withTitleColor("#000000")
							.withDividerColor("#999999")
							.withMessage(
									"���µ���������" + usInfo.getFLOATRANGE() + "������Χ")
							.withMessageColor("#000000")
							.isCancelableOnTouchOutside(true).withDuration(700)
							.withEffect(Effectstype.RotateBottom)
							.withButton1Text("ȡ��¼��").withButton2Text("ȷ��¼��")
							.setButton1Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									result.delete(0, result.length());
									editText.setFocusable(false);
									if ("0".equals(usInfo.getTHISMONTH_RECORD())) {
										editText.setText("");
									} else {
										editText.setText(usInfo
												.getTHISMONTH_RECORD());
									}
									index = 0;
									layout.setVisibility(LinearLayout.GONE);
									dialogBuilder.dismiss();
								}
							}).setButton2Click(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									isModifyData = true; // �ж��Ƿ��޸�����
															// ���˳�ʱ�����ж��Ƿ񷵻����ݡ�
									result.delete(0, result.length());
									view7.setText("  ��������:    " + thisDosage);
									// ����ʱ��
									SimpleDateFormat dateformat = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									String date = dateformat.format(new Date());
									view10.setText("  ��������:    " + date);
									// �����־
									view11.setText("  �����־:    " + "�ѳ�");
									// �������ݿ�
									dbService = new DBService(filepath + dbName);
									dbService.modifyUserData(usInfo.getUSID(),
											thisRecord, thisDosage, date,
											LONGITUDE, LATIUDE, "1");
									dbService.connclose();
									editText.setFocusable(false);
									Toast.makeText(UserDetailActivity.this,
											"¼��ɹ�", Toast.LENGTH_SHORT).show();
									usInfo.setTHISMONTH_DOSAGE(thisDosage);
									usInfo.setTHISMONTH_RECORD(thisRecord);
									usInfo.setLONGITUDE(LONGITUDE);
									usInfo.setLATIUDE(LATIUDE);
									usInfo.setDOMETERSIGNAL("1");
									layout.setVisibility(LinearLayout.GONE);
									dialogBuilder.dismiss();
								}
							}).show();
				} else {
					isModifyData = true; // �ж��Ƿ��޸����� ���˳�ʱ�����ж��Ƿ񷵻����ݡ�
					view7.setText("  ��������:    " + thisDosage);
					// ����ʱ��
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String date = dateformat.format(new Date());

					view10.setText("  ��������:    " + date);
					// �����־
					view11.setText("  �����־:    " + "�ѳ�");
					// �������ݿ�
					dbService = new DBService(filepath + dbName);
					dbService.modifyUserData(usInfo.getUSID(), thisRecord,
							thisDosage, date, LONGITUDE, LATIUDE, "1");
					dbService.connclose();
					editText.setFocusable(false);
					Toast.makeText(UserDetailActivity.this, "¼��ɹ�",
							Toast.LENGTH_SHORT).show();
					usInfo.setTHISMONTH_DOSAGE(thisDosage);
					usInfo.setTHISMONTH_RECORD(thisRecord);
					usInfo.setLONGITUDE(LONGITUDE);
					usInfo.setLATIUDE(LATIUDE);
					usInfo.setDOMETERSIGNAL("1");
					layout.setVisibility(LinearLayout.GONE);
				}
			} else {
				Toast.makeText(UserDetailActivity.this, "����ȷ��д��Ϣ",
						Toast.LENGTH_SHORT).show();
				layout.setVisibility(LinearLayout.GONE);
			}
			break;
		case R.id.delete:
			String tempstr = editText.getText().toString();
			if ("".equals(tempstr)) {
				if ("0".equals(usInfo.getTHISMONTH_RECORD())) {
					editText.setText("");
				} else {
					editText.setText(usInfo.getTHISMONTH_RECORD());
					editText.setSelection(usInfo.getTHISMONTH_RECORD().length());
				}
				layout.setVisibility(LinearLayout.GONE);
			}
			if (tempstr != null && tempstr.length() > 0) {
				tempstr = tempstr.substring(0, tempstr.length() - 1);
				result.replace(0, result.length(), tempstr);
				editText.setText(tempstr);
				if (index > 0)
					index--;
				editText.setSelection(index);
			}
			break;

		default:
			break;
		}
	}
}
