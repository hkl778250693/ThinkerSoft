package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.myfirstpro.service.DBService;
import com.example.administrator.thinker_soft.myfirstpro.util.CloudLed;
import com.example.administrator.thinker_soft.zxing.android.CaptureActivityHandler;
import com.example.administrator.thinker_soft.zxing.android.InactivityTimer;
import com.example.administrator.thinker_soft.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class CaptureActivity extends Activity implements Callback,OnClickListener{
	private CaptureActivityHandler handler;
	private RelativeLayout perviw_parent;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private SharedPreferences sharedPreferences;
	private String DBName;
	private String filepath;
	TextView tv_show;
	private Boolean isOpen = true;
	private CheckBox control_lamp;
	private CloudLed led;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private Parameters parameter;
	private boolean isRun;
	private MyActivityManager mam ;
	private Handler myhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			switch (key) {
			case 0:
				viewfinderView.invalidate();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orshaomiao);
		/*mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		Log.v("onCreate:", "onCreate()");
        WindowManager windowManager = CaptureActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();  
        int screenWidth = display.getWidth();  
        int screenHeight = display.getHeight();
		sharedPreferences = getApplication().getSharedPreferences("IP_PORT_DBNAME", 0);
		DBName = sharedPreferences.getString("dbName", "");
		if(DBName!=null&&!"".equals(DBName)){
			filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases/";
		}else{
			Toast.makeText(getApplicationContext(), "�������ҳ�ļ�ѡ��", Toast.LENGTH_SHORT).show();
		}
		control_lamp = (CheckBox) findViewById(R.id.control_lamp);
		control_lamp.setOnClickListener(this);
		tv_show = (TextView) findViewById(R.id.tv_show);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		isRun = true;
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewfinderView.getLayoutParams();
		params.width = screenWidth-80;
		params.height = screenWidth-80;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		control_lamp.setEnabled(true);
		Log.v("onResume:", "onResume()");
		isRun = true;
		new Thread(){
			public void run() {
				try {
					while(isRun){
						sleep(50);
						myhandler.sendEmptyMessage(0);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v("onSaveInstanceState:", "onSaveInstanceState()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		/*control_lamp.setEnabled(false);
		Log.v("onPause:", "onPause()");
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
*//*		CameraManager.get().openDriver(holder);
		CameraManager.get().startPreview();*//*
		isRun = false;
		CameraManager.get().closeDriver();*/
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v("onStop:", "onStop()");

	}

	@Override
	protected void onDestroy() {
		Log.v("onDestroy:", "onDestroy()");
		inactivityTimer.shutdown();
		isOpen = false;
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 *
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "ɨ��ʧ��",
					Toast.LENGTH_SHORT).show();
		}else {
			// System.out.println("Result:"+resultString);
			String usId = resultString;
			tv_show.setText(resultString);
			if(DBName==null||"".equals(DBName)){
				Toast.makeText(CaptureActivity.this, "�Բ����޷�������ݱȶԣ�����ѡ�񳭱���", Toast.LENGTH_SHORT).show();
				return;
			}
			DBService service = new DBService(filepath+DBName);
			List<UsersInfo> usList = service.queryUsInfobyUsId(usId,1,0);
			if(usList==null||usList.size()<0){
				Toast.makeText(CaptureActivity.this, "�Բ���δ���ҵ�����������Ϣ�����ݣ�", Toast.LENGTH_SHORT).show();
				handler.postDelayed(new Runnable() {//��ʾ���� ����������ɨ�蹦��
					@Override
					public void run() {
						//if(handler==null)
							handler.restartPreviewAndDecode();
					}
				}, 2000);
			}else{
				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, MeterUserDetailActivity.class);
				intent.putExtra("action", "CaptureActivity");
				intent.putExtra("usInfo", (Serializable)usList.get(0));
				startActivity(intent);
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		/*try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}*/
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		/*CameraManager.stopPreview();*/

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	@Override
	public void onClick(View v) {
		/*int key = v.getId();
		switch (key) {
		case R.id.control_lamp:
			//led = new CloudLed();
			camera = CameraManager.getCamera();//��ȡcamera�������ڿ���LED��
			parameter = camera.getParameters(); //��ָ��1����
			if(isOpen){
				parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
			    camera.setParameters(parameter);
			    isOpen = false;
			}else{
				parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			    camera.setParameters(parameter);
			    isOpen = true;
			}
			break;
		default:
			break;
		}*/
	}
}
