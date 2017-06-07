package cc.thksoft.myfirstpro.util;

import android.content.Context;

import cc.thksoft.myfirstpro.appcation.MyApplication;

public class SocketGetData
{
	private SocketConnection socketConn;

	private String downresult;//ÿ��ȡ�õĽ��
	private String lastResult;//�������
	private String errorResult;//����������
	private Boolean signel;//�Ƿ����ӳɹ���ʶ
	private int bufferSize = 2048;//buff��С
	private int tempPort;//��ʱ�˿ں�
	
	public String getData(Context context,int port,String ip,String operName,String parameter)
	{
		MyApplication.lock.lock();
		try{
			lastResult="";
			errorResult="��ȡ����ʧ��";
			//��ȡ��ʱ�˿�
			socketConn = new SocketConnection(port,ip);
			signel = socketConn.connect();
			if(signel == true)
			{
				String getProtMsg = AssembleUpmes.RequestTempPortMes(new UniqueID(context).getUniqueID(),operName);
				socketConn.UpLoadData(getProtMsg);
				downresult = socketConn.DownLoadData(bufferSize).trim();
				if("".equals(downresult))
				{
					return errorResult;
				}
				socketConn.UpLoadData("A");
				socketConn.closeconn();//�رճ�ʼ�˿�
				tempPort = Integer.parseInt(downresult);
				System.out.println("��ȡ������ʱ�˿ڣ�"+tempPort);
				socketConn = new SocketConnection(tempPort,ip);
				signel = socketConn.connect();
				if(signel==false){
					return errorResult;
				}
					//TODO �жϷ���ֵ�Ƿ���Ч
					//����SQL����������
					socketConn.UpLoadData(parameter);
					downresult = socketConn.DownLoadData(bufferSize).trim(); 
					if("".equals(downresult))
						return errorResult;
					System.out.println("����parameter���������ݽ����"+downresult);
					if(downresult==null||"".equals(downresult)){
						return errorResult;
					}
					if(downresult.contains("B"))
					{
						socketConn.UpLoadData("A");
						System.out.println("û�л�ȡ��ֵ����������");
					}else{
						socketConn.UpLoadData("A");
						System.out.println("����SQL���������ݰ��У�"+downresult);
						
						int bagCount = Integer.parseInt(downresult);
						for(int i=0;i<bagCount;i++)
						{
							downresult = socketConn.DownLoadData(bufferSize).trim();
							System.out.println("���ص����ݰ���С��"+downresult);
							int tempsize = Integer.parseInt(downresult);
							socketConn.UpLoadData("A");
							downresult = socketConn.DownLoadData(tempsize).trim();
							System.out.println("���ص����ݣ�"+downresult);
							lastResult = lastResult + downresult;
							lastResult = JsonAnalyze.analyzeData(lastResult);
							socketConn.UpLoadData("A");
						}
						downresult = socketConn.DownLoadData(bufferSize).trim();//C
						System.out.println("SocketGetData:"+downresult);
						socketConn.UpLoadData("A");	
						lastResult +=lastResult;
						return lastResult;
					}	
					socketConn.closeconn();
					return errorResult;
			}else{
				return errorResult;
			}
		}finally{
			MyApplication.lock.unlock();
		}
	}
}
