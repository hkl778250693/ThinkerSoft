package example.android_cbjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import cc.thksoft.myfirstpro.entity.DeviceInfo;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;

public class DeviceDetailActivity extends Activity {
	private TextView view1,view2,view3,view4,view5,view12,view13,view14;
	private TextView devicetv_latitude,devicetv_longitude; 
	//.........................
	private DeviceInfo devInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devicedetail);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		view1 = (TextView) findViewById(R.id.devicetext1);
		view2=(TextView) findViewById(R.id.devicetext2);
		view3 = (TextView) findViewById(R.id.devicetext3);
		view4=(TextView) findViewById(R.id.devicetext4);
		view5 = (TextView) findViewById(R.id.devicetext5);
		view12=(TextView) findViewById(R.id.devicetext12);
		view13 = (TextView) findViewById(R.id.devicetext13);
		view14=(TextView) findViewById(R.id.devicetext14);
		devicetv_latitude = (TextView) findViewById(R.id.devicetv_latitude); 
		devicetv_longitude = (TextView) findViewById(R.id.devicetv_longitude); 
		Intent intent = getIntent();
		devInfo = (DeviceInfo)intent.getSerializableExtra("devInfo");
		String lat = "".equals(devInfo.getC_EQUIPMENT_Y())?"����":devInfo.getC_EQUIPMENT_Y();
		devicetv_latitude.setText("γ��:"+lat);
		String lon = "".equals(devInfo.getC_EQUIPMENT_X())?"����":devInfo.getC_EQUIPMENT_X();
		devicetv_longitude.setText("����:"+lon);
		String temp1 = "".equals(devInfo.getN_EQUIPMENT_ID())?"����":devInfo.getN_EQUIPMENT_ID();
		view1.setText("  �豸���:    "+temp1);
		String temp2 = "".equals(devInfo.getC_EQUIPMENT_NAME())?"����":devInfo.getC_EQUIPMENT_NAME();
		view2.setText("  �豸����:    "+temp2);
		String temp3 = "".equals(devInfo.getC_EQUIPMENT_TYPE_NAME())?"����":devInfo.getC_EQUIPMENT_TYPE_NAME();
		view3.setText("  �豸����:    "+temp3);
		String temp4 = "".equals(devInfo.getN_EQUIPMENT_STATUS())?"����":("1".equals(devInfo.getN_EQUIPMENT_STATUS())?"����":"ͣ��");
		view4.setText("  �豸״̬:    "+temp4);
		String temp5 = "".equals(devInfo.getC_USER_NAME())?"����":devInfo.getC_USER_NAME();
		view5.setText("  ��  �� ��:    "+temp5);
		
		
		String temp12 = "".equals(devInfo.getD_OPERATING_TIME())?"����":devInfo.getD_OPERATING_TIME();
		view12.setText("  ����ʱ��:    "+temp12);
		String temp13 = "".equals(devInfo.getC_EQUIPMENT_ADDRESS())?"����":devInfo.getC_EQUIPMENT_ADDRESS();	
		view13.setText("  �豸��ַ:    "+temp13);
		String temp14 = "".equals(devInfo.getC_EQUIPMENT_REMARK())?"����":devInfo.getC_EQUIPMENT_REMARK();	
	    view14.setText("  �豸��ע:    "+temp14);
	}
	public void onAction(View view){
		int key = view.getId();
		switch (key) {
		case R.id.devicedetail_backButton:
			DeviceDetailActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
