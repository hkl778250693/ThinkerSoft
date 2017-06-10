package com.example.administrator.thinker_soft.myfirstpro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper{

	public MyDBOpenHelper(Context context, String name, CursorFactory factory,
						  int version) {
		super(context, name, factory, version);
	}
/*15508096890*/

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�����û���
		db.execSQL("CREATE TABLE 'THK_USER' " +
				"('�û����' VARCHAR2(40) PRIMARY KEY NOT NULL, " +//'ID' INTEGER PRIMARY KEY NOT NULL,
				"'�ϱ��' VARCHAR2(20)," +
				"'�û���' VARCHAR2(20), " +
				"'�û���ַ' VARCHAR2(200), " +
				"'����Ƭ��' VARCHAR2(20), " +
				"'����ID' NUMBER(10), " +
				"'����' VARCHAR2(20), " +
				"'�������' NUMBER(10), " +
				"'��ϵ�绰' VARCHAR2(15), " +
				"'�û����' VARCHAR2(10), " +
				"'Ƿ�ѽ��' VARCHAR2(10)," +
				"'Ƿ������' VARCHAR2(4), " +
				"'����' VARCHAR2(10), " +
				"'���ͺ�' VARCHAR2(10), " +
				"'����Ա' VARCHAR2(10)," +
				"'����ԱID' NUMBER(20), " +
				"'���¶���' VARCHAR2(10), " +
				"'��������' VARCHAR2(10), " +
				"'���¶���' VARCHAR2(10) DEFAULT ' ', " +
				"'��������' VARCHAR2(10) DEFAULT ' ', " +
				"'������Χ' VARCHAR2(10), " +
				"'������' VARCHAR2(10), " +
				"'������' VARCHAR2(10), " +
				"'�Ӽ���' VARCHAR2(10), " +
				"'������' VARCHAR2(10), " +
				"'N_PROPERTIES_ID' VARCHAR2(10), " +
				"'�����־' VARCHAR2(1) DEFAULT '0', " +
				"'��������' VARCHAR2(20), " +
				"'����' VARCHAR2(10), " +
				"'γ��' VARCHAR2(10)," +
				"'C_JW_X' VARCHAR2(10), " +//����
				"'C_JW_Y' VARCHAR2(10)," +//γ��
				"'��־' VARCHAR2(1) DEFAULT '0' )");
		//�����Ȳ��
		db.execSQL("CREATE TABLE 'THK_PROPORTION' ('C_USER_ID' VARCHAR2(40)," +
				" 'N_FIXED' VARCHAR2(10)," +
				" 'N_PROPERTIES_ID' VARCHAR2(10)," +
				" 'N_PROPORTION' VARCHAR2(10))");
		
/*		"UserProperties":[{"N_PROPERTIES_ID":"1","C_PROPERTIES_NAME":"��ͨ������ˮ",
			"N_LADDER_NUMBER":"0","N_LADDER_START":"0","N_LADDER_END":"0","N_PRICE":"3.70"},*/
		//�������Ա�
		db.execSQL("CREATE TABLE 'THK_PROPERTIES' ('C_PROPERTIES_NAME' VARCHAR2(20) NOT NULL, " +
				"'N_LADDER_END' VARCHAR2(10)," +
				"'N_LADDER_NUMBER' VARCHAR2(10), " +
				"'N_LADDER_START' VARCHAR2(10), " +
				"'N_PRICE' VARCHAR2(10), " +
				"'N_PROPERTIES_ID' VARCHAR2(10))");
/*		db.execSQL("CREATE TABLE 'DEVICETYPE' ('N_EQUIPMENT_TYPE_ID' VARCHAR2(20) NOT NULL," +
				"'C_EQUIPMENT_TYPE_NAME' VARCHAR2(40)," +
				"'N_EQUIPMENT_TYPE_STATUS' VARCHAR2(1)," +
				"'C_EQUIPMENT_TYPE_REMARK' VARCHAR2(5000)," +
				"'D_OPERATION_TIEM' VARCHAR2(20)," +
				"'N_OPERATION_MAN' VARCHAR2(1)," +
				"'C_USER_NAME' VARCHAR2(20))");*/


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}
