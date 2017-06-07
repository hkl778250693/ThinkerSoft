package cc.thksoft.myfirstpro.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cc.thksoft.myfirstpro.entity.PropertyInfo;
import cc.thksoft.myfirstpro.entity.ProportionInfo;
import cc.thksoft.myfirstpro.entity.UsersInfo;

public class DBService {
	private SQLiteDatabase db;

	public DBService(String datapath) {
		db = SQLiteDatabase.openDatabase(datapath, null,
				SQLiteDatabase.OPEN_READWRITE);// CREATE_IF_NECESSARY
	}

	public void insertUserData(List<UsersInfo> usList) {
		db.beginTransaction();
		for (int i = 0; i < usList.size(); i++) {
			UsersInfo usInfo = usList.get(i);
			if ("".equals(usInfo.getTHISMONTH_RECORD())) {// ���¼�¼
				usInfo.setDOMETERSIGNAL("0");
				usInfo.setTHISMONTH_RECORD("");
			} else {
				usInfo.setDOMETERSIGNAL("1");
				usInfo.setSTATE("1");
			}
			if (usInfo.getTHISMONTH_DOSAGE() == null
					|| "".equals(usInfo.getTHISMONTH_DOSAGE()))// ��������
				usInfo.setTHISMONTH_DOSAGE("");
			if (usInfo.getDOMETERDATE() == null
					|| "".equals(usInfo.getDOMETERDATE()))
				usInfo.setDOMETERDATE("");
			usInfo.setLONGITUDE("");
			usInfo.setLATIUDE("");
			if (usInfo.getBASE_LATIUDE() == null
					|| "".equals(usInfo.getBASE_LATIUDE())) {
				usInfo.setBASE_LATIUDE("");
			}
			if (usInfo.getBASE_LONGITUDE() == null
					|| "".equals(usInfo.getBASE_LONGITUDE())) {
				usInfo.setBASE_LONGITUDE("");
			}
			db.execSQL(
					"insert into 'THK_USER'('�û����','�ϱ��','�û���','�û���ַ',"
							+ "'����Ƭ��','����ID','����','�������','��ϵ�绰','�û����',"
							+ "'Ƿ�ѽ��','Ƿ������','����','���ͺ�','����Ա','����ԱID','���¶���',"
							+ "'��������','���¶���','��������','������Χ','������','������',"
							+ "'�Ӽ���','������','N_PROPERTIES_ID','�����־','��������','����',"
							+ "'γ��','C_JW_X','C_JW_Y','��־')values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
					new Object[] { usInfo.getUSID(), usInfo.getOLDUSID(),
							usInfo.getUSNAME(), usInfo.getUSADRESS(),
							usInfo.getUSAREA(), usInfo.getBOOKID(),
							usInfo.getBOOK(), usInfo.getDOMETERID(),
							usInfo.getUSPHONE(), usInfo.getUSBALANCE(),
							usInfo.getUSDEBT(), usInfo.getDEBTMONTHS(),
							usInfo.getMETERID(), usInfo.getMETERKINDS(),
							usInfo.getMETERREADER(), usInfo.getMETERREADERID(),
							usInfo.getLASTMONTH_RECORD(),
							usInfo.getLASTMONTH_DOSAGE(),
							usInfo.getTHISMONTH_RECORD(),
							usInfo.getTHISMONTH_DOSAGE(),
							usInfo.getFLOATRANGE(), usInfo.getCHANGEAMOUNT(),
							usInfo.getSTARTAMOUNT(),
							usInfo.getADD_OR_REDUCE_AMOUNT(),
							usInfo.getGARBAGEMONEY(),
							usInfo.getN_PROPERTIES_ID(),
							usInfo.getDOMETERSIGNAL(), usInfo.getDOMETERDATE(),
							usInfo.getLONGITUDE(), usInfo.getLATIUDE(),
							usInfo.getBASE_LONGITUDE(),
							usInfo.getBASE_LATIUDE(), usInfo.getSTATE() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void insertProportionData(List<ProportionInfo> proporList) {
		db.beginTransaction();
		for (int i = 0; i < proporList.size(); i++) {
			ProportionInfo proporInfo = proporList.get(i);
			db.execSQL(
					"insert into 'THK_PROPORTION'('C_USER_ID','N_FIXED','N_PROPERTIES_ID','N_PROPORTION')values(?,?,?,?)",
					new Object[] { proporInfo.getC_USER_ID(),
							proporInfo.getN_FIXED(),
							proporInfo.getN_PROPERTIES_ID(),
							proporInfo.getN_PROPORTION() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void insertPropertyData(List<PropertyInfo> properList) {
		db.beginTransaction();
		for (int i = 0; i < properList.size(); i++) {
			PropertyInfo properInfo = properList.get(i);
			db.execSQL(
					"insert into 'THK_PROPERTIES'('C_PROPERTIES_NAME','N_LADDER_END','N_LADDER_NUMBER','N_LADDER_START',"
							+ "'N_PRICE','N_PROPERTIES_ID')values(?,?,?,?,?,?)",
					new Object[] { properInfo.getC_PROPERTIES_NAME(),
							properInfo.getN_LADDER_END(),
							properInfo.getN_LADDER_NUMBER(),
							properInfo.getN_LADDER_START(),
							properInfo.getN_PRICE(),
							properInfo.getN_PROPERTIES_ID() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	/*
	 * private String N_EQUIPMENT_TYPE_ID; private String C_EQUIPMENT_TYPE_NAME;
	 * private String N_EQUIPMENT_TYPE_STATUS; private String
	 * C_EQUIPMENT_TYPE_REMARK; private String D_OPERATION_TIEM; private String
	 * N_OPERATION_MAN;
	 */
/*	public void InsertDeviceData(List<DeviceInfo> deviceInfos) {
		db.beginTransaction();
		for (int i = 0; i < deviceInfos.size(); i++) {
			DeviceInfo deviceInfo = deviceInfos.get(i);
			db.execSQL(
					"insert into 'DEVICETYPE' ('N_EQUIPMENT_TYPE_ID'," +
					"'C_EQUIPMENT_TYPE_NAME'," +
					"'N_EQUIPMENT_TYPE_STATUS'," +
					"'C_EQUIPMENT_TYPE_REMARK'," +
					"'D_OPERATION_TIEM','N_OPERATION_MAN')" +
					" values(?,?,?,?,?,?)",
					new String[]{deviceInfo.getN_EQUIPMENT_TYPE_ID(),
							deviceInfo.getC_EQUIPMENT_TYPE_NAME(),
							deviceInfo.getN_EQUIPMENT_TYPE_STATUS(),
							deviceInfo.getC_EQUIPMENT_TYPE_REMARK(),
							deviceInfo.getD_OPERATION_TIEM(),
							deviceInfo.getN_OPERATION_MAN()});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}*/

	public void modifyUserData(String usId, String MouthRecord,
			String MouthDosage, String meterdate, String longitude,
			String latiude, String metersignal) {
		db.beginTransaction();
		db.execSQL(
				"update THK_USER  set '���¶���' = ?,'��������' = ?,'��������' = ?,'����' = ?,'γ��' = ?,'�����־' = ? where '�û����' = ?",
				new Object[] { MouthRecord, MouthDosage, meterdate, longitude,
						latiude, metersignal, usId });
		ContentValues values = new ContentValues();
		values.put("���¶���", MouthRecord);
		values.put("��������", MouthDosage);
		values.put("��������", meterdate);
		values.put("����", longitude);
		values.put("γ��", latiude);
		values.put("�����־", metersignal);
		db.update("THK_USER", values, "�û���� = ?", new String[] { usId });
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	/**
	 * �������г�������
	 * 
	 * @return
	 */
	public List<String> queryBookofUser() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = db
				.rawQuery(
						"select distinct us.'����ID',us.'����' from THK_USER us order by us.'����ID' asc",
						null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("����")));
		}
		cursor.close();
		db.close();
		db.close();
		return list;
	}

	/**
	 * ���س��걾��δ������û�����
	 * 
	 * @return
	 */
	public int queryNoMeterInfosbyBookNameCount(String bookname) {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'�����־' = '0' and thk.'����' = ?",
						new String[] { bookname });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();
		db.close();
		return count;
	}

	/**
	 * �������г������ݲ���ҳ
	 * 
	 * @param bookname
	 * @param currentdatanum
	 * @param pagenum
	 * @return
	 */
	public List<UsersInfo> queryNoMeterInfosbyBookNamenoPaging(String bookname) {
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
	Cursor cursor = db
				.rawQuery(
						"select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and us.'����' = ? and us.'�����־' = '0' and pro.'N_LADDER_NUMBER'='0' order by us.'�������'asc",
						new String[] { bookname });
		
		String tt=Integer.toString(cursor.getCount());
	    Log.v("cursor��¼������",tt);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
		}
		cursor.close();
		db.close();
		db.close();
		return infos;
	}

	/**
	 * ����û�г���ļ�¼
	 * 
	 * @param bookname
	 *            ��������
	 * @return
	 */
	public List<UsersInfo> queryNoMeterInfosbyBookName(String bookname,
													   int currentdatanum, int pagenum) {
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and us.'����' = ? and us.'�����־' = '0'  and pro.'N_LADDER_NUMBER'='0' order by us.'�������'asc",
						new String[] { bookname });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
			if (cursor.moveToNext() == false) {
				return infos;
			}
		}
		cursor.close();
		db.close();
		db.close();
		return infos;
	}

	/**
	 * ��������δ������û�����
	 * 
	 * @return
	 */
	public int queryAllNoMeterInfoCount() {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'�����־' = '0'",
						null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();
		db.close();
		return count;
	}

	/**
	 * �������е�δ������ ����ҳ
	 * 
	 * @param currentdatanum
	 * @param pagenum
	 * @return
	 */
	public List<UsersInfo> queryAllOfNoMeterInfosnoPaging() {
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from "
								+ "(select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',"
								+ "us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',us.'�û����',"
								+ "us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',"
								+ "us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',"
								+ "us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' "
								+ "and us.'�����־' = '0'  and pro.'N_LADDER_NUMBER'='0' order by us.'�������' asc) tt "
								+ "order by tt.'����ID' asc", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
		}
		cursor.close();
		db.close();
		db.close();
		return infos;
	}

	/**
	 * ��������δ����ļ�¼
	 * 
	 * @return
	 */
	public List<UsersInfo> queryAllOfNoMeterInfos(int currentdatanum,
												  int pagenum) {
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from "
								+ "(select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',"
								+ "us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',us.'�û����',"
								+ "us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',"
								+ "us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',"
								+ "us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' "
								+ "and us.'�����־' = '0' and pro.'N_LADDER_NUMBER'='0' order by us.'�������' asc) tt "
								+ "order by tt.'����ID' asc", null);
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
			if (cursor.moveToNext() == false) {
				return infos;
			}
		}
		cursor.close();
		db.close();
		return infos;
	}

	/**
	 * ���������û�����
	 * 
	 * @return
	 */
	public int queryAllUsersInfoCount() {
		int count = 0;
		Cursor cursor = db.rawQuery(
				"select count(thk.'�û����') from THK_USER thk", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * ��ҳ���������û���Ϣ
	 * 
	 * @return
	 */
	public List<UsersInfo> queryAllUsersInfoPage(int currentdatanum, int pagenum) {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from (select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' order by us.'�������' asc) tt order by tt.'����ID' asc",
						null);
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
			if (cursor.moveToNext() == false) {
				return usList;
			}
		}
		cursor.close();
		db.close();
		return usList;
	}

	/**
	 * ���������û���Ϣ
	 * 
	 * @return
	 */
	public List<UsersInfo> queryAllUsersInfo() {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from THK_USER us where us.'�����־' = '1' and  us.'��־' = '0'",
						null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setN_PROPERTIES_ID(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
		}
		cursor.close();
		db.close();
		return usList;
	}

	/**
	 * �޸������û�ID
	 * 
	 * @param usId
	 * @param state
	 * @return
	 */
	public boolean modifytheStateofUsersyes(List<UsersInfo> userInfos) {
		db.beginTransaction();
		for (int i = 0; i < userInfos.size(); i++) {
			UsersInfo info = userInfos.get(i);
			ContentValues values = new ContentValues();
			values.put("��־", 1);
			db.update("THK_USER", values, "�û���� = ?",
					new String[] { info.getUSID() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}

	/**
	 * �޸������û�ID
	 * 
	 * @param usId
	 * @param state
	 * @return
	 */
	public boolean modifytheStateofUsersno(List<UsersInfo> userInfos) {
		db.beginTransaction();
		for (int i = 0; i < userInfos.size(); i++) {
			UsersInfo info = userInfos.get(i);
			ContentValues values = new ContentValues();
			values.put("��־", "0");
			db.update("THK_USER", values, "�û���� = ?",
					new String[] { info.getUSID() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}

	public boolean modifytheStateoneofUser(String usId, String state) {
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("��־", state);
		db.update("THK_USER", values, "�û���� = ?", new String[] { usId });
		db.setTransactionSuccessful();
		db.endTransaction();

		return true;
	}

	public boolean modifytheStateArrayofUseryes(String[] usIds) {
		db.beginTransaction();
		for (int i = 0; i < usIds.length; i++) {
			String id = usIds[i].substring(1, usIds[i].length() - 1);
			ContentValues values = new ContentValues();
			values.put("��־", "1");
			db.update("THK_USER", values, "�û���� = ?", new String[] { id });
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return true;
	}
	
	public boolean modifytheStateArrayofUserno(String[] usIds) {
		db.beginTransaction();
		for (int i = 0; i < usIds.length; i++) {
			String id = usIds[i].substring(1, usIds[i].length() - 1);
			ContentValues values = new ContentValues();
			values.put("��־", "0");
			db.update("THK_USER", values, "�û���� = ?", new String[] { id });
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return true;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param book
	 *            ��������
	 * @param DOMETERID
	 *            ��������
	 * @return
	 */
	public UsersInfo queryUserInfobybook_meterid(String book, String DOMETERID) {
		UsersInfo info = new UsersInfo();
		Cursor cursor = db
				.rawQuery(
						"select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' and us.'����' = ? and us.'�������' = ?",
						new String[] { book, DOMETERID });

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
		}
		cursor.close();
		db.close();

		return info;
	}

	/**
	 * ���س������Ƶ��û�����
	 * 
	 * @return
	 */
	public int queryUserInfobybookNameCount(String bookname) {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'����' = ?",
						new String[] { bookname });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		return count;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param book
	 *            ��������
	 * @return
	 */
	public List<UsersInfo> queryUserInfobybookname(String bookname,
												   int currentdatanum, int pagenum) {
		System.out.println("��ѯ��bookname" + bookname);
		System.out.println("��ѯ��currentdatanum" + currentdatanum);
		System.out.println("��ѯ��pagenum" + pagenum);
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' and us.'����' = ? order by us.'�������' asc",
						new String[] { bookname });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					System.out.println("λ�ã�" + i);
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
			if (cursor.moveToNext() == false) {
				return infos;
			}
		}
		cursor.close();
		db.close();

		return infos;
	}

	/**
	 * ���ظ��û����������û�����
	 * 
	 * @param usName
	 * @return
	 */
	public int queryUserInfobyUsNameCount(String usName) {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'�û���' like ?",
						new String[] { "%" + usName + "%" });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();

		return count;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param usName�û�������
	 * @return
	 */
	public List<UsersInfo> queryUsInfobyUsName(String usName,
											   int currentdatanum, int pagenum) {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from (select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' and us.'�û���' like ? order by us.'�������' asc) tt order by tt.'����ID' asc",
						new String[] { "%" + usName + "%" });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
			if (cursor.moveToNext() == false) {
				return usList;
			}
		}
		cursor.close();
		db.close();

		return usList;
	}

	/**
	 * ���ظ�Id�������û�����
	 * 
	 * @param usId
	 * @return
	 */
	public int queryUserInfobyUsIdCount(String usId) {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'�û����' like ?",
						new String[] { "%" + usId + "%" });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();

		return count;
	}

	/**
	 * ���������û���Ϣ
	 * 
	 * @param usId
	 *            �û����
	 * @return
	 */
	public List<UsersInfo> queryUsInfobyUsId(String usId, int currentdatanum,
											 int pagenum) {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		/*
		 * Cursor cursor = db.rawQuery("select * from " +
		 * "(select  us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��'," +
		 * "us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',us.'�û����',us.'Ƿ�ѽ��'," +
		 * "us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���'," +
		 * "us.'��������',us.'���¶���',us.'��������',us.'������Χ',us.'������',us.'������'," +
		 * "us.'�Ӽ���',us.'������',us.'C_PROPERTIES_NAME',us.'�����־',us.'��������'," +
		 * "us.'����',us.'γ��',us.'��־' from (select * from THK_USER us,THK_PROPERTIES pro "
		 * + "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and " +
		 * "(us.'�û����' like ?)) " +/// or us.'�ϱ��' like ? ,"%"+usId+"%"
		 * "as us group by us.'�û����',us.'�ϱ��' order by us.'�������' asc) " +
		 * "tt order by tt.'����ID' asc", new String[]{"%"+usId+"%"});
		 */
		Cursor cursor = db
				.rawQuery(
						"select * from "
								+ "(select  us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',"
								+ "us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',us.'�û����',us.'Ƿ�ѽ��',"
								+ "us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',"
								+ "us.'��������',us.'���¶���',us.'��������',us.'������Χ',us.'������',us.'������',"
								+ "us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',"
								+ "us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־' from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID'  and pro.'N_LADDER_NUMBER'='0' and "
								+ "(us.'�û����' like ? or us.'�ϱ��' like ?) "
								+ "order by us.'�������' asc) "
								+ "tt order by tt.'����ID' asc", new String[] {
								"%" + usId + "%", "%" + usId + "%" });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			System.out.println("info.setUSNAME(cursor.getString(2)):"
					+ cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
			if (cursor.moveToNext() == false) {
				return usList;
			}
		}
		cursor.close();
		db.close();

		return usList;
	}

	public UsersInfo queryOneofUsInfobyUsId(String usId) {
		UsersInfo info = new UsersInfo();
		Cursor cursor = db
				.rawQuery(
						"select  us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',"
								+ "us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',us.'�û����',us.'Ƿ�ѽ��',"
								+ "us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',"
								+ "us.'��������',us.'���¶���',us.'��������',us.'������Χ',us.'������',us.'������',"
								+ "us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',"
								+ "us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־' from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' and "
								+ "(us.'�û����' = ? or us.'�ϱ��' = ?) "
								+ "order by us.'�������' asc", new String[] {
								usId, usId });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
		}
		cursor.close();
		db.close();

		return info;
	}

	/**
	 * ���ظó���Id�������û�����
	 * 
	 * @param meterId
	 * @return
	 */
	public int queryUserInfobyMeterIdCount(String meterId) {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'����' like ?",
						new String[] { "%" + meterId + "%" });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();

		return count;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param meterId
	 *            �����
	 * @return
	 */
	public List<UsersInfo> queryUsInfobyMeterId(String meterId,
												int currentdatanum, int pagenum) {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from (select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' and us.'����' like ? order by us.'�������' asc) tt order by tt.'����ID' asc",
						new String[] { "%" + meterId + "%" });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {

			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
			if (cursor.moveToNext() == false) {
				return usList;
			}
		}
		cursor.close();
		db.close();

		return usList;
	}

	/**
	 * �������и����û�����
	 * 
	 * @param currentdatanum
	 * @param pagenum
	 * @return
	 */
	public List<UsersInfo> queryUsInforofminus(int currentdatanum, int pagenum) {
		List<UsersInfo> usList = new ArrayList<UsersInfo>();
		Cursor cursor = db
				.rawQuery(
						"select * from (select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰'"
								+ ",us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',us.'��������',"
								+ "us.'������Χ',us.'������',us.'������',us.'�Ӽ���',us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
								+ "from THK_USER us,THK_PROPERTIES pro "
								+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID'  and pro.'N_LADDER_NUMBER'='0' and us.'��������' like ? order by us.'�������' asc) tt order by tt.'����ID' asc",
						new String[] { "-" + "%" });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usList.add(info);
			if (cursor.moveToNext() == false) {
				return usList;
			}

		}
		cursor.close();
		db.close();

		return usList;
	}

	/**
	 * ���ظ��������û�����
	 * 
	 * @param meterId
	 * @return
	 */
	public int queryUsInforofminusCount() {
		int count = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����') from THK_USER thk where thk.'��������' like ?",
						new String[] { "-" + "%" });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			count = Integer.parseInt((cursor.getString(0)));
		}
		cursor.close();
		db.close();

		return count;
	}

	/**
	 * �����û����������ѳ�������������
	 * 
	 * @return
	 */
	public List<String> querystatisticsinfos() {
		List<String> usList = new ArrayList<String>();
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����'),"
								+ "(select count(us.'�û����')from THK_USER us where us.'�����־'='1'),"
								+ "(select sum(us.'��������')from THK_USER us where us.'�����־'='1')"
								+ "from THK_USER thk ", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			usList.add(cursor.getString(0));
			usList.add(cursor.getString(1));
			usList.add(cursor.getString(2));
		}
		cursor.close();
		db.close();

		return usList;
	}

	/**
	 * �����û����������ѳ�������������
	 * 
	 * @param bookname
	 *            ��������
	 * @return
	 */
	public List<String> querystatisticsinfosbybook(String bookname) {
		List<String> usList = new ArrayList<String>();
		Cursor cursor = db
				.rawQuery(
						"select count(thk.'�û����'),"
								+ "(select count(us.'�û����')from THK_USER us where us.'�����־'='1'and us.'����' = ?),"
								+ "(select sum(us.'��������')from THK_USER us where us.'�����־'='1'and us.'����' = ?)"
								+ "from THK_USER thk where  thk.'����' = ?;",
						new String[] { bookname, bookname, bookname });
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			usList.add(cursor.getString(0));
			usList.add(cursor.getString(1));
			usList.add(cursor.getString(2));
		}
		cursor.close();
		db.close();
		return usList;
	}

	/**
	 * ��ҳ��ʾ��ͨ�����ơ���š���ַ��ѯ�û���Ϣ
	 * 
	 * @param condition
	 * @param currentdatanum
	 * @param pagenum
	 * @return
	 */
	public List<UsersInfo> queryDataByCondition(String condition,
												int currentdatanum, int pagenum) {
		List<UsersInfo> usersInfos = new ArrayList<UsersInfo>();
		Cursor cursor = db.rawQuery("select * from "
				+ "(select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',"
				+ "us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',"
				+ "us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',"
				+ "us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',"
				+ "us.'��������',us.'������Χ',us.'������',us.'������',us.'�Ӽ���',"
				+ "us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',"
				+ "us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
				+ "from THK_USER us,THK_PROPERTIES pro "
				+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' ) tt "
				+ "where  tt.'�ϱ��' like ? or tt.'�û����' like ? or "
				+ "tt.'�û���' like ? or tt.'�û���ַ' like ? order by tt.'�������' asc",
				new String[] { "%" + condition + "%", "%" + condition + "%",
						"%" + condition + "%", "%" + condition + "%" });
		if (cursor.moveToFirst() == false) {
			return null;
		}
		if (pagenum > 0) {
			for (int i = 0; i < pagenum * currentdatanum; i++) {
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		for (int i = pagenum * currentdatanum; i < currentdatanum + pagenum
				* currentdatanum; i++) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			usersInfos.add(info);
			if (cursor.moveToNext() == false) {
				return usersInfos;
			}
		}
		cursor.close();
		db.close();

		return usersInfos;
	}

	public List<String[]> getNumLatLngInfo() {
		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = db
				.rawQuery(
						"select �û����,C_JW_X,C_JW_Y,���¶���,�û��� from THK_USER order by ������� asc",
						null);
		if (cursor.moveToFirst() == false) {
			return null;
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String[] object = new String[5];
			object[0] = cursor.getString(0);
			object[1] = cursor.getString(1);
			object[2] = cursor.getString(2);
			object[3] = cursor.getString(3);
			object[4] = cursor.getString(4);
			if (object[1] == null || object[2] == null || "".equals(object[1])
					|| "".equals(object[2])) {
				continue;
			}
			list.add(object);
		}
		cursor.close();
		db.close();
		return list;
	}

	public int getDataByConditionCount(String condition) {
		Cursor cursor = db.rawQuery("select * from "
				+ "(select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',"
				+ "us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',"
				+ "us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',"
				+ "us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',"
				+ "us.'��������',us.'������Χ',us.'������',us.'������',us.'�Ӽ���',"
				+ "us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',"
				+ "us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
				+ "from THK_USER us,THK_PROPERTIES pro "
				+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' ) tt "
				+ "where  tt.'�ϱ��' like ? or tt.'�û����' like ? or "
				+ "tt.'�û���' like ? or tt.'�û���ַ' like ? order by tt.'�������' asc",
				new String[] { "%" + condition + "%", "%" + condition + "%",
						"%" + condition + "%", "%" + condition + "%" });
		int count = cursor.getCount();
		cursor.close();
		db.close();

		return count;
	}

	public void modifyLat_lngbyUsId(String lat, String lng, String usId) {
		db.beginTransaction();
		db.execSQL("update THK_USER set C_JW_X = ?,C_JW_Y = ? where �û���� = ?",
				new String[] { lng, lat, usId });
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();

	}

	public List<UsersInfo> queryInfoByMoreUsId(List<String[]> UsIds) {
		List<UsersInfo> infos = new ArrayList<UsersInfo>();
		String sql = "select * from "
				+ "(select us.'�û����',us.'�ϱ��',us.'�û���',us.'�û���ַ',"
				+ "us.'����Ƭ��',us.'����ID',us.'����',us.'�������',us.'��ϵ�绰',"
				+ "us.'�û����',us.'Ƿ�ѽ��',us.'Ƿ������',us.'����',us.'���ͺ�',"
				+ "us.'����Ա',us.'����ԱID',us.'���¶���',us.'��������',us.'���¶���',"
				+ "us.'��������',us.'������Χ',us.'������',us.'������',us.'�Ӽ���',"
				+ "us.'������',pro.'C_PROPERTIES_NAME',us.'�����־',us.'��������',"
				+ "us.'����',us.'γ��',us.'C_JW_X',us.'C_JW_Y',us.'��־'"
				+ "from THK_USER us,THK_PROPERTIES pro "
				+ "where us.'N_PROPERTIES_ID'= pro.'N_PROPERTIES_ID' and pro.'N_LADDER_NUMBER'='0' ) tt "
				+ "where ";
		String[] args = new String[UsIds.size()];
		for (int i = 0; i < UsIds.size(); i++) {
			sql = sql + "tt.�û���� = ? or ";
			args[i] = UsIds.get(i)[0];
			Log.v("��" + i + "��ѭ��SQLΪ��", "" + sql);
		}
		sql = sql.substring(0, sql.lastIndexOf("or"));
		Log.v("��װ��SQLΪ��", "" + sql);
		Cursor cursor = db.rawQuery(sql, args);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UsersInfo info = new UsersInfo();
			info.setUSID(cursor.getString(0));
			info.setOLDUSID(cursor.getString(1));
			info.setUSNAME(cursor.getString(2));
			info.setUSADRESS(cursor.getString(3));
			info.setUSAREA(cursor.getString(4));
			info.setBOOKID(cursor.getString(5));
			info.setBOOK(cursor.getString(6));
			info.setDOMETERID(cursor.getString(7));
			info.setUSPHONE(cursor.getString(8));
			info.setUSBALANCE(cursor.getString(9));
			info.setUSDEBT(cursor.getString(10));
			info.setDEBTMONTHS(cursor.getString(11));
			info.setMETERID(cursor.getString(12));
			info.setMETERKINDS(cursor.getString(13));
			info.setMETERREADER(cursor.getString(14));
			info.setMETERREADERID(cursor.getString(15));
			info.setLASTMONTH_RECORD(cursor.getString(16));
			info.setLASTMONTH_DOSAGE(cursor.getString(17));
			info.setTHISMONTH_RECORD(cursor.getString(18));
			info.setTHISMONTH_DOSAGE(cursor.getString(19));
			info.setFLOATRANGE(cursor.getString(20));
			info.setCHANGEAMOUNT(cursor.getString(21));
			info.setSTARTAMOUNT(cursor.getString(22));
			info.setADD_OR_REDUCE_AMOUNT(cursor.getString(23));
			info.setGARBAGEMONEY(cursor.getString(24));
			info.setC_PROPERTIES_NAME(cursor.getString(25));
			info.setDOMETERSIGNAL(cursor.getString(26));
			info.setDOMETERDATE(cursor.getString(27));
			info.setLONGITUDE(cursor.getString(28));
			info.setLATIUDE(cursor.getString(29));
			info.setBASE_LONGITUDE(cursor.getString(30));
			info.setBASE_LATIUDE(cursor.getString(31));
			info.setSTATE(cursor.getString(32));
			infos.add(info);
		}
		return infos;

	}

	
	public void connclose() {
		if (db != null) {
			db.close();
		}
	}
}
