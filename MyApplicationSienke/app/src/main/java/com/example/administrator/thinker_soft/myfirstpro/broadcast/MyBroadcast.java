package com.example.administrator.thinker_soft.myfirstpro.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcast extends BroadcastReceiver {
	private static String DBNAME;
	private static String IP;
	private static String PORT;
	private static String COUNT;
	@Override
	public void onReceive(Context context, Intent intent) {
		COUNT = intent.getStringExtra("count"); 
		DBNAME = intent.getStringExtra("DBNAME"); 
		IP = intent.getStringExtra("ip");
		PORT = intent.getStringExtra("port");
	}
	public static String getData(){
		return DBNAME;
	}
	public static String getIP() {
		return IP;
	}
	public static String getPORT() {
		return PORT;
	}
	public static String getCOUNT() {
		return COUNT;
	}	

}
