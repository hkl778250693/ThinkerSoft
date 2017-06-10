package com.example.administrator.thinker_soft.myfirstpro.util;

import java.util.List;

import com.example.administrator.thinker_soft.myfirstpro.entity.UsersInfo;

/**
 * ��װ�ϴ���Ϣ
 * 
 * @author Administrator
 * 
 */
public class AssembleUpmes {
	// �ն�ʶ���&����������
	public static String RequestTempPortMes(String port, String reqName) {
		String mes = "";
		mes = mes + port;
		mes = mes + "&";
		mes = mes + reqName;
		return mes;
	}

	// ������&������&����������
	public static String RequestTypeBagMes(String operInstructions,
			String operName, String classname) {
		String msg = "";
		msg = msg + classname;
		msg = msg + "&";
		msg = msg + operInstructions;
		msg = msg + "&";
		msg = msg + operName;
		return msg;
	}

	/**
	 * �����û�SQL
	 * 
	 * @param areaID
	 *            �������
	 * @param bookID
	 *            �������
	 * @param floatrangedata
	 *            ������Χ
	 * @param mouthdata
	 *            �·�
	 * @param startNum
	 *            ��ʼ�ϱ��
	 * @param endNum
	 *            �����ϱ��
	 * @return
	 */
	public static String downLoadParameter(List<String> areaID,
			List<String> bookID, String startNum, String endNum) {
		System.out.println(areaID.isEmpty());
		System.out.println(bookID.isEmpty());
		String range = "and((u.c_user_id between '" + startNum + "' and '"
				+ endNum + "') or (u.c_old_user_id between '" + startNum
				+ "' and '" + endNum + "' ))";
		String area = "";
		String book = "";
		String tempbook = "";
		for (int i = 0; i < bookID.size(); i++) {
			tempbook = tempbook + bookID.get(i) + ",";
		}
		if (tempbook.length() > 0) {
			tempbook = tempbook.substring(0, tempbook.length() - 1);
			book = "and u.n_book_id in (" + tempbook + ")";
		}
		String temparea = "";
		for (int i = 0; i < areaID.size(); i++) {
			temparea = temparea + areaID.get(i) + ",";
		}
		if (temparea.length() > 0) {
			temparea = temparea.substring(0, temparea.length() - 1);
			area = "and u.n_area_id in (" + temparea + ")";
		}
		if ("".equals(startNum) && "".equals(endNum)) {
			range = "";
		}
		if (bookID == null || bookID.size() <= 0) {
			book = "";
		}
		if (areaID == null || areaID.size() <= 0) {
			area = "";
		}
		String parameter = "9��" + area + book + range;
		/*
		 * String sql =
		 * "select u.c_user_id �û����,case when length(u.c_old_user_id) > 8 then substr(u.c_old_user_id, 0, 8) || '-' || substr(u.c_old_user_id, 9) else u.c_old_user_id end as �ϱ��,u.c_user_name �û���,u.c_user_address �û���ַ,a.c_area_name ����Ƭ��,u.n_book_id ����ID,b.c_book_name ����,u.n_order_num1 �������,u.c_user_phone ��ϵ�绰,u.n_amount �û����,nvl(qfje.n_amount, '0') Ƿ�ѽ��,nvl(sum(qfys.cnt), '0') Ƿ������,u.c_meter_number ����,m.c_model_name ���ͺ�,s.c_user_name ����Ա,u.n_user_meter_reader_id ����ԱID,u.n_meter_degrees ���¶���,nvl(sum(syyl.n_dosage), '0') ��������,"
		 * + "decode(u.n_properties_id, "+floatrangedata+
		 * ",'30') ������Χ,u.n_change_meter ������,u.n_minimum ������,u.n_remission �Ӽ���,nvl(sum(ljf.n_detail_amount), '0') ������,u.n_properties_id from yx_user u,yx_area a,yx_book b,yx_model m,qx_systemuser s,(select t.c_user_id,decode(sum(t.n_amount), null, 0, sum(t.n_amount)) n_amount from yx_situation t where t.n_charge_state = 0 group by t.c_user_id) qfje,(select count(*) cnt, c_user_id from yx_situation "
		 * +
		 * "where n_calculation_way = 0 and n_charge_state = 0 and c_situation_use_month <= '"
		 * +mouthdata+
		 * "' group by c_situation_use_month, c_user_id) qfys,(select c_user_id, n_dosage from (select t.c_user_id,t.n_use_year,t.n_use_month,decode(sum(t.n_dosage), null, 0, sum(t.n_dosage)) n_dosage from yx_situation t group by t.n_use_year, t.n_use_month, t.c_user_id order by t.n_use_year desc, t.n_use_month desc) where rownum = 1) syyl,(select d.c_user_id,decode(sum(d.n_detail_amount),null,0,sum(d.n_detail_amount)) n_detail_amount from yx_situation t, yx_detail d where d.n_situation_id = t.n_situation_id and t.n_charge_state = 0 and d.n_cost_id = 3 group by d.c_user_id) ljf where u.n_area_id = a.n_area_id and u.n_book_id = b.n_book_id and u.n_model_id = m.n_model_id and u.n_user_meter_reader_id = s.n_systemuser_id and u.c_user_id = qfje.c_user_id(+) and u.c_user_id = qfys.c_user_id(+) and u.c_user_id = syyl.c_user_id(+) and u.c_user_id = ljf.c_user_id(+) and u.n_enter_state = 1 and u.n_user_state not in (0, 9) "
		 * + ""+range+""+ ""+book+""+ ""+area+""+
		 * "group by u.c_user_id,u.c_old_user_id,u.c_user_name,u.c_user_address,a.c_area_name,u.n_book_id,b.c_book_name,u.n_order_num1,u.c_user_phone,u.n_amount,u.n_properties_id,qfje.n_amount,u.c_meter_number,m.c_model_name,s.c_user_name,u.n_meter_degrees,u.n_properties_id,u.n_change_meter,u.n_minimum,u.n_remission,u.n_company_id,u.n_user_meter_reader_id"
		 * ;
		 */System.out.println("parameter:" + parameter);

		return parameter;
	}

	public static String selectverificationCode() {
		String sql = "select t.C_REG_CODE from yx_devinfo t";
		return sql;
	}

	public static String InsertUUIDSql(String UUID, String Code) {
		String sql = "update yx_devinfo set C_REG_NUM = " + UUID
				+ " where C_REG_CODE = " + Code + "";
		return sql;
	}

	public static String verifyRigisterCodeParameter(String UUID, String Code) {
		String parameter = "24��" + UUID + "��" + Code + "��";
		return parameter;
	}
         //�ϴ������û�
	public static String upLoadParameter(List<UsersInfo> users) {
		// %@��%@��%@��1��%@��%@��%@��%@��
		// user.strYHBH,user.strBYDS,user.strBYYL,user.strCBYID,user.strGPSX,user.strGPSY,user.strCBRQ
		String parameter = "8��";
		for (int i = 0; i < users.size(); i++) {
			UsersInfo user = users.get(i);
			String signal = "��1";
			String DOSAGE = user.getTHISMONTH_DOSAGE();
			if ("".equals(DOSAGE)) {
				DOSAGE = "0";
			}
			int thisDosage = Integer.parseInt(DOSAGE);
			if (thisDosage < 0) {
				signal = "��3";
			}
			String RANGE = user.getFLOATRANGE();
			if ("".equals(RANGE)) {
				RANGE = "0";
			}
			if (thisDosage > Integer.parseInt(RANGE)) {
				signal = "��2";
			}
			parameter = parameter + user.getUSID() + "��"
					+ user.getTHISMONTH_RECORD() + "��"
					+ user.getTHISMONTH_DOSAGE() + "��" + "1" + "��"
					+ user.getMETERREADERID() + "��" + user.getLONGITUDE() + "��"
					+ user.getLATIUDE() + "��" + user.getDOMETERDATE() + signal
					+ "��";
		}
		System.out.println(parameter);
		return parameter;
		
	}

	public static String initialParameter() {
		return "7��";
	}

	//30���豸���ơע���豸��ַ���豸���ȡ��豸γ�ȡ��ϴ��ˡ��豸��š��豸���ͱ�š��豸״̬��
	public static String AddDeviceParameter(String devName, String devRemark,
			String devAddr, String latitude, String longitude, String upPerson,
			String devId, String devTypeId, String devState) {
		String parameter = "30��" + devName + "��";
		if (devRemark != null && !"".equals(devRemark)) {
			parameter += devRemark + "��";
		} else {
			parameter += "��";
		}
		if (devAddr != null && !"".equals(devAddr)) {
			parameter += devAddr + "��";
		} else {
			parameter += "��";
		}
		if (latitude != null && !"".equals(latitude)) {
			parameter += latitude + "��";
		} else {
			parameter += "��";
		}
		if (longitude != null && !"".equals(longitude)) {
			parameter += longitude + "��";
		} else {
			parameter += "��";
		}
		if (upPerson != null && !"".equals(upPerson)) {
			parameter += upPerson + "��";
		} else {
			parameter += "��";
		}
		if (devId != null && !"".equals(devId)) {
			parameter += devId + "��";
		} else {
			parameter += "��";
		}
		if (devTypeId != null && !"".equals(devTypeId)) {
			parameter += devTypeId + "��";
		} else {
			parameter += "��";
		}
		if (devState != null && !"".equals(devState)) {
			parameter += devState + "��";
		} else {
			parameter += "��";
		}
		return parameter;
	}

	// 31 ��ȡ�豸����
	public static String getDevTypeParameter() {
		return "31��";
	}

	// 29���豸��š��豸���ȡ��豸γ�ȡ�
	public static String UpDevGPSParamter(String devId, String latitude,
			String longitude) {
		String parameter = "29��" + devId + "��" + latitude + "��" + longitude
				+ "��";
		return parameter;
	}

	public static String getDevDataParameter() {
		return "32��";
	}

	// 28���û���š�ȡ�γ�ȡ�
	public static String UpMeterGPSParamter(String usId, String latitude,
			String longitude) {
		String parameter = "28��" + usId + "��" + latitude + "��" + longitude
				+ "��";
		return parameter;
	}

	/**
	 * ��¼
	 * 
	 * @param var0
	 * @param var1
	 * @return
	 */
	public static String loginParameter(String var0, String var1) {
		String var3 = "";
		if (var0 != null && !"".equals(var0)) {
			String var6;
			try {
				String var5 = Gadget.EncoderByMd5(var1 + "www.thinkersoft.cc");
				var6 = "10��" + var0 + "��" + var5 + "��";
			} catch (Exception var7) {
				var7.printStackTrace();
				return var3;
			}
			var3 = var6;
		}
		return var3;
	}

	/**
	 * ����ǰ��������
	 * 
	 * @param userId
	 * @return
	 */
	public static String reqtTasksParameter(String userId) {
		return "15��" + userId + "��" + "and task_status in ('δ����', '�ѽ���')��";
	} 

	/**
	 * ��������������� һ������״̬������ δ���ա����ѽ��ܣ�δ��ɣ�������ע��/���ܳ��֣���������ɡ����ѹ鵵
	 * 
	 * @param userId
	 * @return
	 */
	public static String reqtOldTasksParameter(String userId) {
		return "15��" + userId + "��" + "and task_status in ('�����', '�ѹ鵵')��";
	}

	/**
	 * �ϴ��������� �ϴ����û�ID�������š���γ�� workID��Ϊ�������
	 * 
	 * @param
	 * @return
	 */
	public static String uploadCoordinate(String workID, String C_TASK_Y,
			String C_TASK_X) {
		return "34��" + workID + "��" + C_TASK_Y + "��" + C_TASK_X + "��";
	}

	/**
	 * �������� �ύ���� workID��Ϊ�������
	 * 
	 * @param userId
	 * @return
	 */
	public static String uploadWorkState(String workID, String userId,
			String state) {
		return "33��" + workID + "��" + userId + "��" + state + "��";
	}

	public static String upRequestNews(String usId, String x, String y) {// 12���û���š�ȡ�γ�ȡ�
		String parameter = "12��" + usId + "��" + x + "��" + y + "��";
		return parameter;
	}

}
