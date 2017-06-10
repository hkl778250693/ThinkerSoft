package com.example.administrator.thinker_soft.myfirstpro.threadsocket;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.util.AssembleUpmes;
import com.example.administrator.thinker_soft.myfirstpro.util.JsonAnalyze;
import com.example.administrator.thinker_soft.myfirstpro.util.UniqueID;

import java.util.Map;

import com.example.administrator.thinker_soft.myfirstpro.util.SocketConnection;

/**
 * Socketͨ���߳�
 * @author Administrator
 *
 */
public class SocketInteraction{
	private SocketConnection socketConn;
	private int bufferSize = 2048;
	private String downresult;//ÿ��ȡ�õĽ��
	private Context context;//������
	private String ip;//ip��ַ
	private int defaultPort;//�˿ں�
	private String operName;//����������
	private String parameter;
	private Handler  handler;
	public SocketInteraction(Context context,int port,String ip,String operName,String parameter,Handler dataHandler){
		this.ip = ip;
		this.defaultPort = port;
		this.context = context;
		this.operName = operName;
		this.handler = dataHandler;
		this.parameter = parameter;
		System.out.println("ip:++"+ip+"defaultPort:++"+defaultPort);
	}
	public boolean DataDownLoadConn(){
		MyApplication.lock.lock();
		try{
			String lastResult = "";
			//��ȡ��ʱ�˿�
			Boolean signel = false;
			socketConn = new SocketConnection(defaultPort,ip);
			signel = socketConn.connect();
			if(signel == true){
				String getProtMsg = AssembleUpmes.RequestTempPortMes(new UniqueID(context).getUniqueID(),operName);
				socketConn.UpLoadData(getProtMsg);
				downresult = socketConn.DownLoadData(bufferSize).trim();
				if("".equals(downresult))
					return false;
				socketConn.UpLoadData("A");
				socketConn.closeconn();//�رճ�ʼ�˿�
				int tempPort = Integer.parseInt(downresult);//��ʱ�˿ں�
				System.out.println("��ȡ������ʱ�˿ڣ�"+tempPort);
				socketConn = new SocketConnection(tempPort,ip);
				signel = socketConn.connect();
				if(signel==false){
					return signel;
				}
				//TODO �жϷ���ֵ�Ƿ���Ч
				//����parameter����������
				socketConn.UpLoadData(parameter);
				downresult = socketConn.DownLoadData(bufferSize).trim();
				System.out.println("parameter��Ӧֵ��"+downresult);
				if("".equals(downresult)||(downresult.contains("2")&&downresult.contains("error"))||(downresult.contains("3")&&downresult.contains("error")))
					return false;
				if(downresult.contains("B")||"".equals(downresult)){
					socketConn.UpLoadData("B");
					System.out.println("û�л�ȡ��ֵ����������");
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("data", "û������");
					if(parameter.contains("31��")){////31 ��ȡ�豸����
						bundle.putInt("key", 2);
					}else if(parameter.contains("32��")){////32 ��ȡ�豸����
						bundle.putInt("key", 3);
					}
					msg.setData(bundle);
					handler.sendMessage(msg);
					return true;
				}else{
					socketConn.UpLoadData("A");
					int bagCount = Integer.parseInt(downresult);
					for(int i=0;i<bagCount;i++){
						downresult = socketConn.DownLoadData(bufferSize).trim();
						if("".equals(downresult))
							return false;
						System.out.println("���ص����ݰ���С��"+downresult);
						int tempsize = Integer.parseInt(downresult);
						socketConn.UpLoadData("A");
						downresult = socketConn.DownLoadData(tempsize).trim();
						if("".equals(downresult))
							return false;
						System.out.println("���ص����ݣ�"+downresult);
						lastResult = lastResult+downresult+"��";
						socketConn.UpLoadData("A");
					}
					downresult = socketConn.DownLoadData(bufferSize).trim();
					System.out.println(downresult);
					socketConn.UpLoadData("A");			
				}	
				lastResult = lastResult.substring(0, lastResult.length()-1);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				if("".equals(lastResult)){
					bundle.putString("data", "û������");
				}else{
					bundle.putString("data", lastResult);
				}
				if(parameter.contains("31��")){////31 ��ȡ�豸����
					bundle.putInt("key", 2);
				}else if(parameter.contains("32��")){////32 ��ȡ�豸����
					bundle.putInt("key", 3);
				}
				msg.setData(bundle);
				handler.sendMessage(msg);
				return true;
			}else{
				return false;
			}
		}finally{
			MyApplication.lock.unlock();
		}
	}
	public String DataUpLoadConn(){
		MyApplication.lock.lock();
		try{
			String lastResult = "";
			//��ȡ��ʱ�˿�
			Boolean signel = false;
			
			socketConn = new SocketConnection(defaultPort,ip);
			signel = socketConn.connect();
			if(signel == true){
				String getProtMsg = AssembleUpmes.RequestTempPortMes(new UniqueID(context).getUniqueID(),operName);
				socketConn.UpLoadData(getProtMsg);
				downresult = socketConn.DownLoadData(bufferSize).trim();
				socketConn.UpLoadData("A");
				socketConn.closeconn();//�رճ�ʼ�˿�
				if("".equals(downresult))
					return "���粻�ȶ������ݻ�ȡʧ��!";
				int tempPort = Integer.parseInt(downresult);//��ʱ�˿ں�
				System.out.println("��ȡ������ʱ�˿ڣ�"+tempPort);
				socketConn = new SocketConnection(tempPort,ip);
				if(socketConn.connect()!=true){			
					return "������δ��Ӧ";
				}
				socketConn.UpLoadData(parameter);				
				downresult = socketConn.DownLoadData(bufferSize).trim();
				if(downresult==null||"".equals(downresult)){
					return "���粻�ȶ������ݻ�ȡʧ��!";
				}
				if(downresult.contains("B"))
				{
					socketConn.UpLoadData("A");
					System.out.println("û�л�ȡ��ֵ����������");
					return "����Ϊ�գ���˶���������!";
				}else{
					socketConn.UpLoadData("A");
					System.out.println("����SQL���������ݰ��У�"+downresult);
					int bagCount = Integer.parseInt(downresult);
					for(int i=0;i<bagCount;i++)
					{
						downresult = socketConn.DownLoadData(bufferSize).trim();
						System.out.println("���ص����ݰ���С��"+downresult);
						if("".equals(downresult))
						{
							return "���粻�ȶ������ݻ�ȡʧ��!";
						}
						int tempsize = Integer.parseInt(downresult);
						socketConn.UpLoadData("A");
						downresult = socketConn.DownLoadData(tempsize).trim();
						System.out.println("���ص����ݣ�"+downresult);
						if("".equals(downresult))
						{
							return "���粻�ȶ������ݻ�ȡʧ��!";
						}
						if(parameter.contains("29��")||parameter.contains("30��")){////29 �ɼ�GPS���ꡢ30 �����豸
							downresult = analyzeDeviceData(downresult);
						}else {
							downresult = analyzeMeterData(downresult);
						}
						lastResult = lastResult + downresult;
						socketConn.UpLoadData("A");
					}
	/*				if("".equals(downresult)||(downresult.contains("2")&&downresult.contains("error"))){
						return false;
					}*/
					downresult = socketConn.DownLoadData(bufferSize).trim();//C
					System.out.println("SocketGetData:"+downresult);
					socketConn.UpLoadData("A");	
					return lastResult;
				}			
			}else{
				return "������δ��Ӧ";
			}	
		}finally{
			MyApplication.lock.unlock();
		}
	}
	public void closeConn(){
		socketConn.closeconn();
	}
	
	
	
	private String analyzeDeviceData(String Result){
		//05-07 18:21:09.986: I/System.out(24599): ���ص����ݣ�{"status":1,"DevId":"92","error":"�����豸�ɹ�"}
		Map<String, String> map  = JsonAnalyze.jsonServiceBackMsg(Result);
		if(map!=null&&!map.isEmpty()){
			if("1".equals(map.get("status"))){
				return map.get("DevId")+"��"+map.get("error");
			}
			if("2".equals(map.get("status"))){
					return "δ֪���";
			}
			if("3".equals(map.get("status"))){
				return "���Ĺؼ�������";
			}
		}
		return "";
	}
	
	private String analyzeMeterData(String Result) {
		Map<String, String> map = JsonAnalyze.jsonServiceBackMsg(Result);
		if (map != null && !map.isEmpty()) {
			if ("1".equals(map.get("State"))) {
				if (map.get("error") != null) {
					return "�ɹ�";
				}
			}
			if ("2".equals(map.get("State"))) {
				if (map.get("error") != null) {
					return map.get("error");
				} else {
					return "�ɹ�";
				}
			}
			if ("3".equals(map.get("State"))) {
				return "���Ĺؼ�������";
			}
		}
		return "";
	}
}
