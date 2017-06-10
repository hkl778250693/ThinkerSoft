package com.example.administrator.thinker_soft.myfirstpro.util;

import com.example.administrator.thinker_soft.myfirstpro.entity.DeviceTypeInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.ProportionInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.entity.AreaInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.BookInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.DeviceInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.PropertyInfo;
import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;

public class JsonAnalyze {
	/**
	 * 解析单条抄表本数据
	 * @param strResult
	 */
	public static List<BookInfo> analyszeBook(String strResult) {
		List<BookInfo> dataList = null;
		try {
			dataList = new ArrayList<BookInfo>();
			JSONArray jsonObArray = new JSONObject(strResult)
					.getJSONArray("BookData");
			for(int i=0;i<jsonObArray.length();i++){
				JSONObject jsonObj = jsonObArray.getJSONObject(i);
			String ID = jsonObj.getString("抄表本ID");
			String NUMBER = jsonObj.getString("抄表本编号");
			String BOOK = jsonObj.getString("抄表本");
			String BOOKMEN = jsonObj.getString("抄表员");
			String BOOKREMARK = jsonObj.getString("抄表本备注");
			BookInfo info = new BookInfo();
			info.setID(ID);
			info.setNUMBER(NUMBER);
			info.setBOOK(BOOK);
			info.setBOOKMEN(BOOKMEN);
			info.setBOOKREMARK(BOOKREMARK);
			dataList.add(info);
			}
	
			
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 解析单条分区数据
	 * @param strResult
	 * @return
	 */
	public static List<AreaInfo> analyszeArea(String strResult) {
		List<AreaInfo> dataList = null;
		try {
			dataList = new ArrayList<AreaInfo>();
			JSONArray jsonObArray = new JSONObject(strResult)
					.getJSONArray("AreaData");
			for(int i=0;i<jsonObArray.length();i++){
				JSONObject jsonObj = jsonObArray.getJSONObject(i);
				String ID = jsonObj.getString("分区ID");
				String AREA = jsonObj.getString("分区");
				String AREAREMARK = jsonObj.getString("分区备注");
				AreaInfo Info = new AreaInfo();
				Info.setID(ID);
				Info.setArea(AREA);
				Info.setAreaRemark(AREAREMARK);
				dataList.add(Info);
			}
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 解析月份数据
	 * @param strResult
	 * @return
	 */
	public static String analyszeMonth(String strResult){
		
		String month = "";
		try {
/*			JSONObject jsonObject = new JSONObject(strResult).getJSONObject("SelectData");
			month = jsonObject.getString("C_ACCOUNTING_MONTH");*/
			JSONArray jsonArray = new JSONObject(strResult).getJSONArray("SelectData");
			for(int n=0;n<jsonArray.length();n++){
				JSONObject jsonObj = jsonArray.getJSONObject(n);
				month = jsonObj.getString("C_ACCOUNTING_MONTH");
			}			
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		return month;
	}
	/**
	 * 解析和封装表浮动区间数据
	 * @param strResult
	 * @return
	 */
	public static String analyszeFloatRange(String strResult){
		String allRange = "";
		try {
			JSONArray jsonArray = new JSONObject(strResult).getJSONArray("SelectData");
			for(int n=0;n<jsonArray.length();n++){
				JSONObject jsonObj = jsonArray.getJSONObject(n);
				allRange = jsonObj.getString("SET_VALUE");
			}
			String[] tempstrone = allRange.split(";");
			allRange = "";
			for(int i=0;i<tempstrone.length;i++){
				String[] tempstrtwo = tempstrone[i].split(",");
				allRange = allRange + tempstrtwo[0]+","+"'"+tempstrtwo[1]+"'"+",";
			}
			allRange = allRange.substring(0, allRange.length()-1);
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}	
		return allRange;
	}
	/**
	 * 解析用户数据
	 * @param strResult
	 * @return
	 * @throws JSONException 
	 */
	public static List<UsersInfo> analyszeUsersInfo(String strResult) throws JSONException{
		List<UsersInfo> proList = new ArrayList<UsersInfo>();
			JSONArray jsonObjects = new JSONObject(strResult).getJSONArray("UserData");
			for(int i=0 ;i<jsonObjects.length();i++){
				JSONObject jsonObject = jsonObjects.optJSONObject(i);
				String USID = jsonObject.getString("用户编号");
				String OLDUSID = jsonObject.getString("老编号");
				String USNAME = jsonObject.getString("用户名");
				String USADRESS = jsonObject.getString("用户地址");
				String USAREA = jsonObject.getString("所属片区");
				String BOOKID = jsonObject.getString("抄表本ID");
				String BOOK = jsonObject.getString("抄表本");
				String DOMETERID = jsonObject.getString("抄表序号");
				String USPHONE = jsonObject.getString("联系电话");
				String USBALANCE = jsonObject.getString("用户余额");
				String USDEBT = jsonObject.getString("欠费金额");
				String DEBTMONTHS = jsonObject.getString("欠费月数");
				String METERID = jsonObject.getString("表编号");
				String METERKINDS = jsonObject.getString("表型号");
				String METERREADER = jsonObject.getString("抄表员");
				String METERREADERID = jsonObject.getString("抄表员ID");
				String LASTMONTH_RECORD = jsonObject.getString("上月读数");
				String LASTMONTH_DOSAGE = jsonObject.getString("上月用量");
				String THISMONTH_RECORD = jsonObject.getString("本月止度");//本月用量
				String THISMONTH_DOSAGE = jsonObject.getString("本月用量");//本月读数
				String FLOATRANGE = jsonObject.getString("浮动范围");
				String CHANGEAMOUNT = jsonObject.getString("更换量");
				String STARTAMOUNT = jsonObject.getString("启用量");
				String ADD_OR_REDUCE_AMOUNT = jsonObject.getString("加减量");
				String GARBAGEMONEY = jsonObject.getString("垃圾费");
				String N_PROPERTIES_ID = jsonObject.getString("N_PROPERTIES_ID");
			    //String DOMETERSIGNAL = jsonObject.getString("抄表标志");//抄表标志
				String DOMETERDATE = jsonObject.getString("D_JW_TIME");//抄表日期
				String LONGITUDE = jsonObject.getString("N_JW_X")==null?"":jsonObject.getString("N_JW_X");//经度
				String LATIUDE = jsonObject.getString("N_JW_Y")==null?"":jsonObject.getString("N_JW_Y");//纬度
				String BASE_LONGITUDE = jsonObject.getString("C_JW_X")==null?"":jsonObject.getString("C_JW_X");//设备经度
				String BASE_LATIUDE = jsonObject.getString("C_JW_Y")==null?"":jsonObject.getString("C_JW_Y");//设备纬度
				
/*				"N_JW_X":"","N_JW_Y":"","D_JW_TIME":"","C_JW_X":"","C_JW_Y"
*/				UsersInfo users = new UsersInfo();
				users.setUSID(USID);
				users.setOLDUSID(OLDUSID);
				users.setUSNAME(USNAME);
				users.setUSADRESS(USADRESS);
				users.setUSAREA(USAREA);
				users.setBOOKID(BOOKID);
				users.setBOOK(BOOK);
				users.setDOMETERID(DOMETERID);
				users.setUSPHONE(USPHONE);
				users.setUSBALANCE(USBALANCE);
				users.setUSDEBT(USDEBT);
				users.setDEBTMONTHS(DEBTMONTHS);
				users.setMETERID(METERID);
				users.setMETERKINDS(METERKINDS);
				users.setMETERREADER(METERREADER);
				users.setMETERREADERID(METERREADERID);
				users.setLASTMONTH_RECORD(LASTMONTH_RECORD);
				users.setLASTMONTH_DOSAGE(LASTMONTH_DOSAGE);
				users.setTHISMONTH_RECORD(THISMONTH_RECORD);
				users.setTHISMONTH_DOSAGE(THISMONTH_DOSAGE);
				users.setFLOATRANGE(FLOATRANGE);
				users.setCHANGEAMOUNT(CHANGEAMOUNT);
				users.setSTARTAMOUNT(STARTAMOUNT);
				users.setADD_OR_REDUCE_AMOUNT(ADD_OR_REDUCE_AMOUNT);
				users.setGARBAGEMONEY(GARBAGEMONEY);
				users.setN_PROPERTIES_ID(N_PROPERTIES_ID);
				//users.setDOMETERSIGNAL(DOMETERSIGNAL);
				users.setDOMETERDATE(DOMETERDATE);
				users.setLONGITUDE(LONGITUDE);
				users.setLATIUDE(LATIUDE);
				users.setBASE_LONGITUDE(BASE_LONGITUDE);
				users.setBASE_LATIUDE(BASE_LATIUDE);
				users.setSTATE("0");
				proList.add(users);
/*				System.out.println("----------------------垃圾费-------------------:"+GARBAGEMONEY);
				System.out.println("----------------------上月记录-------------------:"+LASTMONTH_RECORD);
				System.out.println("----------------------上月用量-------------------:"+LASTMONTH_DOSAGE);
				System.out.println("----------------------本月记录-------------------:"+THISMONTH_RECORD);
				System.out.println("----------------------本月用量-------------------:"+THISMONTH_DOSAGE);*/
			}
		return proList;
	}
	/**
	 * 属性数据解析
	 * @param strResult
	 * @return
	 */
	public  static List<PropertyInfo> analyszeProperties(String strResult){
		List<PropertyInfo> propertyInfos = null;
		try {
			JSONArray jsonArray = new JSONObject(strResult).getJSONArray("UserProperties");
			propertyInfos = new ArrayList<PropertyInfo>();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String C_PROPERTIES_NAME = jsonObject.getString("C_PROPERTIES_NAME");
				String N_LADDER_END = jsonObject.getString("N_LADDER_END");
				String N_LADDER_NUMBER = jsonObject.getString("N_LADDER_NUMBER");
				String N_LADDER_START = jsonObject.getString("N_LADDER_START");
				String N_PRICE = jsonObject.getString("N_PRICE");
				String N_PROPERTIES_ID = jsonObject.getString("N_PROPERTIES_ID");
				PropertyInfo info = new PropertyInfo();
				info.setC_PROPERTIES_NAME(C_PROPERTIES_NAME);
				info.setN_LADDER_END(N_LADDER_END);
				info.setN_LADDER_NUMBER(N_LADDER_NUMBER);
				info.setN_LADDER_START(N_LADDER_START);
				info.setN_PRICE(N_PRICE);
				info.setN_PROPERTIES_ID(N_PROPERTIES_ID);
				propertyInfos.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return propertyInfos;
	}
	//0170101800194
	/**
	 * 解析比差数据
	 * @param strResult
	 * @return
	 */
	public static List<ProportionInfo> analyszeProportion(String strResult){
		List<ProportionInfo> propertyInfos = null;
		try {
			JSONArray jsonArray = new JSONObject(strResult).getJSONArray("BXData");
			propertyInfos = new ArrayList<ProportionInfo>();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String C_USER_ID = jsonObject.getString("C_USER_ID");
				String N_FIXED = jsonObject.getString("N_FIXED");
				String N_PROPERTIES_ID = jsonObject.getString("N_PROPERTIES_ID");
				String N_PROPORTION = jsonObject.getString("N_PROPORTION");
				ProportionInfo info = new ProportionInfo();
				info.setC_USER_ID(C_USER_ID);
				info.setN_FIXED(N_FIXED);
				info.setN_PROPERTIES_ID(N_PROPERTIES_ID);
				info.setN_PROPORTION(N_PROPORTION);
				propertyInfos.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return propertyInfos;
	}
	public static String assembleJson(List<UsersInfo> allInfos){
		String jsonString = "";
		List<UsersInfo> infos = allInfos;
		try {
		JSONArray arrays = new JSONArray();
		JSONObject object = new JSONObject();
		for(int i=0;i<infos.size();i++){
			object.put("用户编号", infos.get(i).getUSID());
			object.put("老编号", infos.get(i).getOLDUSID());
			object.put("用户名", infos.get(i).getUSNAME());
			object.put("用户地址", infos.get(i).getUSADRESS());
			object.put("所属片区", infos.get(i).getUSAREA());
			object.put("抄表本ID", infos.get(i).getBOOKID());
			object.put("抄表本", infos.get(i).getBOOK());
			object.put("抄表序号", infos.get(i).getDOMETERID());
			object.put("联系电话", infos.get(i).getUSPHONE());
			object.put("用户余额", infos.get(i).getUSBALANCE());
			object.put("欠费金额", infos.get(i).getUSDEBT());
			object.put("欠费月数", infos.get(i).getDEBTMONTHS());
			object.put("表编号", infos.get(i).getMETERID());
			object.put("表型号", infos.get(i).getMETERKINDS());
			object.put("抄表员", infos.get(i).getMETERREADER());
			object.put("抄表员ID", infos.get(i).getMETERREADERID());
			object.put("上月读数", infos.get(i).getLASTMONTH_RECORD());
			object.put("上月用量", infos.get(i).getLASTMONTH_DOSAGE());
			object.put("本月用量", infos.get(i).getTHISMONTH_RECORD());
			object.put("本月读数", infos.get(i).getTHISMONTH_DOSAGE());
			object.put("浮动范围", infos.get(i).getFLOATRANGE());
			object.put("更换量", infos.get(i).getCHANGEAMOUNT());
			object.put("启用量", infos.get(i).getSTARTAMOUNT());
			object.put("加减量", infos.get(i).getADD_OR_REDUCE_AMOUNT());
			object.put("垃圾费", infos.get(i).getGARBAGEMONEY());
			object.put("抄表标志", infos.get(i).getDOMETERSIGNAL());
			object.put("抄表日期", infos.get(i).getDOMETERDATE());
			object.put("经度", infos.get(i).getLONGITUDE());
			object.put("纬度", infos.get(i).getLATIUDE());
			arrays.put(object);
		}
		jsonString = arrays.toString();
		jsonString = "{["+"SelectData:"+jsonString+"]}";
		System.out.println("封装结果："+jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public static List<DeviceTypeInfo> analyszeDevType(String json){
		List<DeviceTypeInfo> deviceInfos = new ArrayList<DeviceTypeInfo>();
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("EquipmentTypeData");
			for (int i = 0; i < jsonArray.length(); i++) {
				 JSONObject object = jsonArray.getJSONObject(i);
				 DeviceTypeInfo info = new DeviceTypeInfo();
				 info.setN_EQUIPMENT_TYPE_ID(object.getString("N_EQUIPMENT_TYPE_ID"));
				 info.setC_EQUIPMENT_TYPE_NAME(object.getString("C_EQUIPMENT_TYPE_NAME"));
				 info.setN_EQUIPMENT_TYPE_STATUS(object.getString("N_EQUIPMENT_TYPE_STATUS"));
				 info.setC_EQUIPMENT_TYPE_REMARK(object.getString("C_EQUIPMENT_TYPE_REMARK"));
				 info.setD_OPERATION_TIEM(object.getString("D_OPERATION_TIEM"));
				 info.setN_OPERATION_MAN(object.getString("N_OPERATION_MAN"));
				 deviceInfos.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return deviceInfos;
	}
	
	public static List<DeviceInfo> analyszeDevData(String json){
		List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("EquipmentData");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setN_EQUIPMENT_ID(object.getString("N_EQUIPMENT_ID"));
				deviceInfo.setC_EQUIPMENT_NAME(object.getString("C_EQUIPMENT_NAME"));
				deviceInfo.setC_EQUIPMENT_REMARK(object.getString("C_EQUIPMENT_REMARK"));
				deviceInfo.setC_EQUIPMENT_ADDRESS(object.getString("C_EQUIPMENT_ADDRESS"));
				deviceInfo.setC_EQUIPMENT_X(object.getString("C_EQUIPMENT_X"));
				deviceInfo.setC_EQUIPMENT_Y(object.getString("C_EQUIPMENT_Y"));
				deviceInfo.setD_OPERATING_TIME(object.getString("D_OPERATING_TIME"));
				deviceInfo.setN_OPERATING_MAN(object.getString("N_OPERATING_MAN"));
				deviceInfo.setC_EQUIPMENT_NUMBER(object.getString("C_EQUIPMENT_NUMBER"));
				deviceInfo.setC_EQUIPMENT_TYPE_NAME(object.getString("C_EQUIPMENT_TYPE_NAME"));
				deviceInfo.setN_EQUIPMENT_TYPE_ID(object.getString("N_EQUIPMENT_TYPE_ID"));
				deviceInfo.setN_EQUIPMENT_STATUS(object.getString("N_EQUIPMENT_STATUS"));
				deviceInfo.setC_USER_NAME(object.getString("C_USER_NAME"));
				deviceInfos.add(deviceInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return deviceInfos;
	}
	
	public static  Map<String,String> jsonServiceBackMsg(String json){
	    if ((json != null) && (json.startsWith("﻿\ufeff")))
	    {
	      System.out.println("进入");
	      json = json.substring(1);
	    }
		 Map<String,String> map = new HashMap<String,String>();
		 try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<String> iterator = jsonObject.keys();
			String key;
			String value;
			while(iterator.hasNext())
			{
				key=iterator.next();

				value=jsonObject.getString(key);

				map.put(key, value);
			}
/*			String result = jsonObject.getString("status");
			map.put("status", jsonObject.getString("status"));
			
			map.put("error",jsonObject.getString("error"));*/	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return map;
	}
	
	/**
	 * 解析登录成功之后的
	 * 
	 * @param Result
	 * @return
	 */
	public static String analyzeData(String Result) {
		String lastResult="";
		Map<String, String> map = jsonServiceBackMsg(Result);
		if (map != null && !map.isEmpty()) {
			if ("1".equals(map.get("status"))) {
				if (map.get("userinfo") != null) {
					return map.get("userinfo");
				} else if (map.get("EquipmentTypeData") != null) {
					return "{EquipmentTypeData:" + map.get("EquipmentTypeData") + "}";
				} else if (map.get("EquipmentData") != null) {
					return "{EquipmentData:" + map.get("EquipmentData") + "}";
				}else if (map.get("TaskData") != null) {
					return "{TaskData:" + map.get("TaskData") + "}☆";
				}else if (map.get("MsgData") != null) {
					return "{MsgData:" + map.get("MsgData") + "}☆";
				}else {
					return "成功";
				}
			}
			if ("2".equals(map.get("status"))) {
				if (map.get("error") != null) {
					// return "网络不稳定，数据获取失败!"; 返回服务器提示的错误信息 由于我没做判断所以直接返回 下面部分
					return map.get("error");
				} else {
					return "网络不稳定，数据获取失败!";
				}
			}
			if ("3".equals(map.get("status"))) {
				if (map.get("error") != null) {
					return map.get("error");
				} else {
					return "网络不稳定，数据获取失败!";
				}
			}
			if ("4".equals(map.get("status"))) {
				if (map.get("typeNumber") != null) {
					String msgData = map.get("MsgData");
					if (msgData != null) {
						return map.get("typeNumber") + "☆" + msgData;
					} else {
						return map.get("typeNumber");
					}
				} else {
					return "网络不稳定，数据获取失败!";
				}
			}

		}
		return lastResult;
	}
	
	/**
	 * 任务JSON解析
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Map<String, String>> jsonTaskData(String json)
			throws JSONException {
		if ((json != null) && (json.startsWith("﻿\ufeff"))) {
			System.out.println("进入");
			json = json.substring(1);
		}
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

		JSONArray jsonArray = new JSONObject(json).getJSONArray("TaskData");

		for (int i = 0; i < jsonArray.length(); i++) {
			Map<String, String> map = new HashMap<String, String>();

			JSONObject jsonObject = jsonArray.getJSONObject(i);

			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keys();

			String key;

			String value;
			// 由于表字段和值是从服务器获取.使用迭代器获取KEY.再根据KEY获取值
			while (iterator.hasNext()) {
				key = iterator.next();

				value = jsonObject.getString(key);

				map.put(key, value);
			}
			maps.add(map);
		}
		return maps;
	}

}
