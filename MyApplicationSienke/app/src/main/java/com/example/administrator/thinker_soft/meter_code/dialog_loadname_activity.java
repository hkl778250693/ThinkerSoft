package com.example.administrator.thinker_soft.meter_code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;

public class dialog_loadname_activity extends Activity {
	private EditText editText;
	private int resultCode = 1;
	private List<String> areaName;
	private List<String> bookName;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loadname);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		areaName = (List<String>) getIntent().getSerializableExtra("areaName");
		bookName = (List<String>) getIntent().getSerializableExtra("bookName");
		String fileName = getDownloadFileName(areaName, bookName);
		editText = (EditText) findViewById(R.id.downloadfilename);
		if(!"".equals(fileName)){
			editText.setText(fileName);
			editText.setSelection(fileName.length());
		}
	}
	
	public void action(View v){
		
		switch (v.getId()) {
		case R.id.downloadfiledone:		
			String filename = editText.getText().toString();
			System.out.println("filename:"+filename);
			//Regularexpression.checkfileName(filename);
			Intent intent = new Intent();
			intent.setAction(editText.getText().toString());
			setResult(resultCode, intent);
			finish();
			break;
		case R.id.downloadfileno:
			finish();
			break;
		default:
			break;
		}
	}
	public String getDownloadFileName(List<String> areaName,List<String> bookName){
		String filename = "";
/*		if((areaName==null||areaName.size()<0)&&(bookName==null||bookName.size()<0)){	
			return filename;
		}
		else*/
		if((areaName!=null&&areaName.size()>20)||(bookName!=null&&bookName.size()>20)){
			Toast.makeText(this, "�ļ������� ���ֶ�����", Toast.LENGTH_LONG).show();
			return filename;				
		}
		if(areaName!=null&&areaName.size()>0){
			for (int i = 0; i < areaName.size(); i++) {
				if(areaName.get(i).contains("��")){
					filename = filename +areaName.get(i).replace("��", "")+"��";
				}else{
					filename = filename +areaName.get(i)+"��";
				}
			}
			filename = filename.substring(0,filename.length()-1) + "��";
		}
		if(bookName!=null&&bookName.size()>0){
			if(!"".equals(filename))
				filename = filename + "(";
			for (int j = 0; j < bookName.size(); j++) {
				if(bookName.get(j).contains("��")){
					filename = filename +bookName.get(j).replace("��", "")+"��";
				}else{
					filename = filename +bookName.get(j)+"��";
				}
			}
			filename = filename.substring(0,filename.length()-1) +"��";
			if(filename.contains("("))
				filename = filename +")";
		}
		return filename;
	}
}
