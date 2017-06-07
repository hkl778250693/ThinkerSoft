package cc.thksoft.myfirstpro.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 * socket�����Լ������ϴ�������
 * @author Administrator
 *
 */
public class SocketConnection {
	private   static SocketConnection sconn;
	private   static Socket socket; 
	private   BufferedOutputStream writer;
	private   BufferedInputStream reader; 
	private   String ip;
	private   int  port;

	public  SocketConnection(int port,String ip){
		this.port = port;
		this.ip = ip;		
		}
	/**
	 * ���Ӻ���
	 * @return
	 */
	public boolean connect() {
		try {
			System.out.println("����Ĭ��");
			System.out.println("ip:"+ip);
			System.out.println("port:"+port);
			socket = new  Socket();
			SocketAddress socAddress = new InetSocketAddress(ip, port);
			socket.connect(socAddress, 1000*5);
			System.out.println("�������Ƿ�رգ�"+socket.isClosed());
			System.out.println("��������ַ��"+socket.getRemoteSocketAddress());
			System.out.println("���ص�ַ��"+socket.getLocalAddress());
			System.out.println("�����Ƿ�ɹ���"+socket.isConnected());
			
		} catch (Exception e) {
			System.out.println("����ʧ��");
			//connect(port);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * �ϴ����ݺ���
	 * @param upMes
	 */
	public void UpLoadData(String upMes) {
		try {
			byte[] buffer = upMes.getBytes("GBK");
			System.out.println("�ϴ���Ϣ��"+upMes);
			writer = new BufferedOutputStream(socket.getOutputStream());
			writer.write(buffer);
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * �������ݺ���
	 * @return
	 */
	public  String  DownLoadData(int buffersize) {
		System.out.println("�������غ�����");	
		String result = "";
		int count = 0;
		try {
			reader = new BufferedInputStream(socket.getInputStream());
			byte[] resultbuf = new byte[buffersize];
			int contentLength = reader.available();
			while(contentLength==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
				contentLength = reader.available();
				if(count==35){
					return result;
				}
			}
			System.out.println("��ǰ���е����ɶ�����"+contentLength);
			if(buffersize>2048){
			    // ��ǰ���е����ɶ���   
			    // ƫ����   
			     int offset = 0;  
			    // ÿ�ζ�ȡ���ֽ���   
			     int len = 0;  
		  
			    while(contentLength > 0  && len != -1&&offset<buffersize)  
			    {  
			    	byte[] buffer = new byte[2048];
			        try  
			        {  
			            len = reader.read(buffer, 0, 2048);  
			            System.out.println("ÿ�ζ�ȡ�ĳ��ȣ�"+len);
			        }  
			        catch(SocketTimeoutException e)  
			        {  
			            break;  
			        } 
			        if(len != -1)  
			        {  
			            System.arraycopy(buffer, 0, resultbuf, offset, len);
			            offset = offset + len;
			            System.out.println("ƫ������"+offset);
			        }      
			    }
			}else{
				if(contentLength>0)
				reader.read(resultbuf,0,buffersize);
			}
		    result = new String(resultbuf, "GBK");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	
	
	public  String  QucklyDownLoadData(int buffersize) {
		System.out.println("�������غ�����");	
		String result = "";
		int count = 0;
		try {
			reader = new BufferedInputStream(socket.getInputStream());
			byte[] resultbuf = new byte[buffersize];
			int contentLength = reader.available();
			while(contentLength==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
				contentLength = reader.available();
				if(count==5){
					return result;
				}
			}
			System.out.println("��ǰ���е����ɶ�����"+contentLength);
			if(buffersize>2048){
			    // ��ǰ���е����ɶ���   
			    // ƫ����   
			     int offset = 0;  
			    // ÿ�ζ�ȡ���ֽ���   
			     int len = 0;  
		  
			    while(contentLength > 0  && len != -1&&offset<buffersize)  
			    {  
			    	byte[] buffer = new byte[2048];
			        try  
			        {  
			            len = reader.read(buffer, 0, 2048);  
			            System.out.println("ÿ�ζ�ȡ�ĳ��ȣ�"+len);
			        }  
			        catch(SocketTimeoutException e)  
			        {  
			            break;  
			        } 
			        if(len != -1)  
			        {  
			            System.arraycopy(buffer, 0, resultbuf, offset, len);
			            offset = offset + len;
			            System.out.println("ƫ������"+offset);
			        }      
			    }
			}else{
				if(contentLength>0)
				reader.read(resultbuf,0,buffersize);
			}
		    result = new String(resultbuf, "GBK");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	/**
	 * �ر����Ӻ���
	 */
	public void closeconn(){
	try {
		if(reader!=null){
			reader.close();
		}else if(writer!=null){
			writer.close();
		}else if(socket.isClosed()==true){
			socket.close();
		}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}