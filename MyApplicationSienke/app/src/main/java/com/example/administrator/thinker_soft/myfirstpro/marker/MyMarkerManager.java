package com.example.administrator.thinker_soft.myfirstpro.marker;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.util.MarkerCluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMarkerManager {
	private Activity act;
	private BaiduMap bMap;
	private Map<List<String[]>,Marker> markers = new HashMap<List<String[]>,Marker>();//������� �����ݶ�Ӧ��Marker
	private MarkerCluster mcluster = new MarkerCluster();
	
	public MyMarkerManager(BaiduMap bMap , Activity act) {
		this.bMap = bMap;
		this.act = act;
	}
	
	public Map<String, List<List<String[]>>> initMarkerInfo(List<String[]> allPoints){
		List<List<String[]>> similarPoints_no_grade = mcluster.markerClassify(allPoints);
		Map<String, List<List<String[]>>> map = mcluster.markerClassify_Slice(similarPoints_no_grade);  
		for (String key : map.keySet()) {
			Log.v("����", ""+key);
			Log.v("ֵ��", ""+map.get(key));
		}
		return map;
	}
	/**
	 * �������б�ע��Ϣ
	 * @return
	 */
	public Map<List<String[]>,Marker> getAllMarkerAndInfo(){
		return markers;
	}
	/**
	 * ��ѯ��ע��Ϣ
	 * @param latLng marker�ľ�γ��
	 * @return
	 */
	public List<String[]> getInfoByMarkerLatLng(LatLng latLng){
		for (List<String[]> Infolist : markers.keySet()) {
			if(latLng.latitude == Double.parseDouble(Infolist.get(0)[1])&&latLng.longitude == Double.parseDouble(Infolist.get(0)[2])){
				return Infolist;
			}
		}
		return null;
	}
	
	
	public Map<List<String[]>,Marker> addMarker(int icon,Map<String, List<List<String[]>>> map,int zoom){
		if(map==null||map.size()<=0){
			return null;
		}
		List<List<String[]>> lists = null;
		switch (zoom) {
		case 10:
			lists = map.get("10");
			break;
		case 11:
			lists = map.get("11");
			break;
		case 12:
			lists = map.get("12");
			break;
		case 13:
			lists = map.get("13");
			break;
		case 14:
			lists = map.get("14");
			break;
		case 15:
			lists = map.get("15");
			break;
		case 16:
			lists = map.get("16");
			break;	
		case 17:
			lists = map.get("16");
			break;
			
		default:
			break;
		}
		if(zoom<10){
			lists = map.get("10");
		}
		if(lists==null||lists.size()<=0){
				return null;
		}
		//bMap.clear();
		//106.531025,29.514536
		//106.43099,29.53465
		//106.58018,29.534901
		//106.534762,29.500706
		//106.448237,29.506238
		//106.520389,29.570341
		for (int i = 0; i < lists.size(); i++) {	
			if(lists.get(i).size()!=0&&lists.get(i).size()==1){//���Ƶ�ֻ�����Լ�
				String[] usId = new String[]{lists.get(i).get(0)[0]};
				double lat = Double.parseDouble(lists.get(i).get(0)[1]);
				double lng = Double.parseDouble(lists.get(i).get(0)[2]);
				if(getScreemRangePoints(lat,lng)==true){
					LatLng point = new LatLng(lng, lat);  
					//����Markerͼ��  
					BitmapDescriptor bitmap = BitmapDescriptorFactory  
					    .fromResource(R.mipmap.icon_gcoding);
					//����MarkerOption�������ڵ�ͼ�����Marker  
					OverlayOptions option = new MarkerOptions()  
					    .position(point)  
					    .icon(bitmap)
					    ;
					bMap.addOverlay(option);
					markers.put(lists.get(i), (Marker) bMap.addOverlay(option));
				}
			}else if(lists.get(i).size()!=0&&lists.get(i).size()>1){//���Ƶ��ж��
				String[] usId = new String[lists.get(i).size()];
				double lat = Double.parseDouble(lists.get(i).get(0)[1]);
				double lng = Double.parseDouble(lists.get(i).get(0)[2]);
				if(getScreemRangePoints(lat,lng)==true){
					
					LatLng point = new LatLng(lng, lat);  
					//����Markerͼ��  
					BitmapDescriptor bitmap = BitmapDescriptorFactory  
					    .fromResource(R.mipmap.icon_gcoding);
					//����MarkerOption�������ڵ�ͼ�����Marker  
					OverlayOptions option = new MarkerOptions()  
					    .position(point)  
					    .icon(bitmap)
					    ;
					markers.put(lists.get(i), (Marker) bMap.addOverlay(option));
				}
			}
		}
		return markers;
	}
	
    private boolean getScreemRangePoints(double latitude,double longitude){
    	WindowManager manager = act.getWindowManager();
    	Display disPlay = manager.getDefaultDisplay();
    	int screenWidth = disPlay.getWidth();
    	int screenHeight =disPlay.getHeight();
    	//���������
    	LatLng mCenter = bMap.getMapStatus().target;
    	double center_lat = mCenter.latitude;
    	double center_lng = mCenter.longitude;
    	//bmapView
    	Point topPoint = new Point(screenWidth/2,30);//��Ļ���Ϸ� x,y
    	Point bottomPoint = new Point(screenWidth/2,screenHeight-30);//��Ļ���·� x,y
    	Point leftPoint = new Point(0+30,screenHeight/2);//��Ļ���� x,y
    	Point rightPoint = new Point(screenWidth-30,screenHeight/2);//��Ļ���ҷ� x,y
    	Projection pj = bMap.getProjection();
    	LatLng topLatLng = pj.fromScreenLocation(topPoint);//��ͼ���Ϸ� ��γ��
    	LatLng bottomLatLng = bMap.getProjection().fromScreenLocation(bottomPoint);//��ͼ���·� ��γ��
    	LatLng leftLatLng = bMap.getProjection().fromScreenLocation(leftPoint);//��ͼ���� ��γ��
    	LatLng rightLatLng = bMap.getProjection().fromScreenLocation(rightPoint);//��ͼ���ҷ� ��γ��
    	
        double topLat = topLatLng.latitude;
        double bottomLat = bottomLatLng.latitude;
        double leftLng = leftLatLng.longitude;
        double rightLng = rightLatLng.longitude;
        
        if(longitude>bottomLat&&longitude<topLat&&latitude>leftLng&&latitude<rightLng){
        	return true;
        }
        return false;
    } 
}
