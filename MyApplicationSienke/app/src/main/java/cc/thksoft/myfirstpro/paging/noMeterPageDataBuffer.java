package cc.thksoft.myfirstpro.paging;

import java.util.List;

import cc.thksoft.myfirstpro.entity.UsersInfo;

public class noMeterPageDataBuffer {
	private List<UsersInfo> usersInfos;
/*	private int currentdatanum;//������ʾ����
	private int currentdatacount;//����������
	private int pagenum=0;//��ǰҳ  Ĭ�ϵ�һҳ
	private int pagecount;//��ҳ��
*/	public noMeterPageDataBuffer(List<UsersInfo> users){
		usersInfos = users;
	}
	public List<UsersInfo> getPageUsersbycount(int currentdatanum, int pagenum){
		List<UsersInfo> backUsers = null;
		if(usersInfos!=null&&usersInfos.size()>0){
			int pagecount = usersInfos.size()/currentdatanum;
			if(usersInfos.size()%currentdatanum!=0){
				pagecount = pagecount + 1;
			}
			System.out.println("��ǰ��pagenum�Ĵ�С��"+pagenum);
			System.out.println("��ǰ��pagecount�Ĵ�С��"+pagecount);
			System.out.println("��ǰ����ʼλ�ã�"+currentdatanum*(pagenum));
			System.out.println("��ǰ�Ľ���λ�ã�"+currentdatanum*(pagenum+1));
			if(pagenum<(pagecount-1)){
				System.out.println("����1");
				backUsers = usersInfos.subList(currentdatanum*(pagenum), currentdatanum*(pagenum+1));
			}else{
				System.out.println("����2");
				backUsers = usersInfos.subList(currentdatanum*(pagenum), usersInfos.size());
			}
		}
		return backUsers;
	}	
	public void updateoneOfUsersBuffer(UsersInfo userinfo, int position){
		System.out.println("usersInfos.get(position).setTHISMONTH_DOSAGE(userinfo.getTHISMONTH_DOSAGE()):"+userinfo.getTHISMONTH_DOSAGE());
		System.out.println("usersInfos.get(position).setTHISMONTH_DOSAGE(userinfo.getTHISMONTH_DOSAGE()):"+userinfo.getTHISMONTH_RECORD());
		System.out.println("usersInfos.get(position).setTHISMONTH_DOSAGE(userinfo.getTHISMONTH_DOSAGE()):"+userinfo.getDOMETERSIGNAL());
		usersInfos.get(position).setTHISMONTH_DOSAGE(userinfo.getTHISMONTH_DOSAGE());
		usersInfos.get(position).setTHISMONTH_RECORD(userinfo.getTHISMONTH_RECORD());
		usersInfos.get(position).setDOMETERSIGNAL("1");
	}
	public int getPageUserAllcount(){
		int count = 0;
		if(usersInfos!=null&&usersInfos.size()>0)
			count = usersInfos.size();
		return count;
	}
	
}
