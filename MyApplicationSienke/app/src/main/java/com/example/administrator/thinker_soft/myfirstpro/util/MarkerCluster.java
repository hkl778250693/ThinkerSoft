package com.example.administrator.thinker_soft.myfirstpro.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerCluster {
	private int interrupt;
	private double[] bigRatio = new double[]{0.01,0.02,0.07,0.1,0.2,0.3,0.3};//�ֱ��Ӧ7��ͼ���ͼ����ʾ���� 100���ϸ���ע
	private double[] smallRatio = new double[]{0.1,0.0,0.0,0.1,0.2,0.3,0.3};//�ֱ��Ӧ7��ͼ���ͼ����ʾ���� 100���� 10���ϸ���ע
	private double[] smallerRatio = new double[]{1,0.0,0.0,0.0,0.0,0.0,0.0};//�ֱ��Ӧ7��ͼ���ͼ����ʾ���� 10���¸���ע
	public MarkerCluster() {
		
	}
	/**
	 * ������ĵ�ŵ�һ��
	 * @param allPoints
	 * @return
	 */
	public List<List<String[]>> markerClassify(List<String[]> allPoints) {
		List<List<String[]>> similarPoints_no_grade = new ArrayList<List<String[]>>();
		List<String[]> tempPoints = new ArrayList<String[]>();
		tempPoints.addAll(allPoints);
		interrupt = tempPoints.size();
		while (interrupt > 0) {
			String[] point = tempPoints.get(0);
			List<String[]> result = new ArrayList<String[]>();
			result.add(point);
			tempPoints.set(0, null);
			for (int i = 0; i < tempPoints.size(); i++) {
				if(tempPoints.get(i)==null)
					continue;
				if (getDistanceTwoPoints(
						new LatLng(Double.parseDouble(point[1]),
								Double.parseDouble(point[2])), new LatLng(
								Double.parseDouble(tempPoints.get(i)[1]),
								Double.parseDouble(tempPoints.get(i)[2]))) <= 10.0) {
					result.add(tempPoints.get(i));
					tempPoints.set(i, null);
				}
			}
			similarPoints_no_grade.add(result);
			List<String[]> ttempPoints = new ArrayList<String[]>();
			for (int i = 0; i < tempPoints.size(); i++) {
				if(tempPoints.get(i)!=null){
					ttempPoints.add(tempPoints.get(i));
				}
			}
			tempPoints.clear();
			tempPoints.addAll(ttempPoints);
			interrupt = tempPoints.size();
		}
		return similarPoints_no_grade;
	}
	/**
	 * ��������
	 * @param pointOne
	 * @param pointTwo
	 * @return
	 */
	public double getDistanceTwoPoints(LatLng pointOne, LatLng pointTwo) {
		return DistanceUtil.getDistance(pointOne, pointTwo);
	}
	
	/**
	 * �����������ע �������ͼ�����ݷָ�
	 * @param allPoints_grade
	 * @return
	 */
	public Map<String, List<List<String[]>>> markerClassify_Slice(List<List<String[]>> allPoints_grade){
		
		Map<String, List<List<String[]>>> map = new HashMap<String, List<List<String[]>>>();
		int count = allPoints_grade.size();	
		if(count<=0){
			return null;
		}
		if (count>=100) {
			int start = 0;
			int end = 0;
			for (int i = 0; i < bigRatio.length; i++) {
				if(count==end+1){
					map.put(String.valueOf(10+i), null);
					continue; 
				}
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				int number = (int) (bigRatio[i]*(count));
				end = end + number - 1;//�±�λ��Ҫ��һ
				allPoints_grade_order.addAll(allPoints_grade.subList(start, end +1));//sublist����ҿ� ����Ϊ��ȡ���λ+1
				map.put(String.valueOf(10+i), allPoints_grade_order);
				start = end + 1;
			}
			if(end<count-1){//�����������
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				allPoints_grade_order.addAll(allPoints_grade.subList(end, count-1));
				map.put(String.valueOf(10+bigRatio.length), allPoints_grade_order);
			}	
		}else if(count>=10&&count<100){
			int start = 0;
			int end = 0;
			for (int i = 0; i < smallRatio.length; i++) {
				if(count==end+1){
					map.put(String.valueOf(10+i), null);
					continue; 
				}
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				int number = (int) (smallRatio[i]*(count));
				end = end + number - 1;
				allPoints_grade_order.addAll(allPoints_grade.subList(start, end + 1));//sublist����ҿ�
				map.put(String.valueOf(10+i), allPoints_grade_order);
				start = end + 1;
			}
			if(end<count-1){//�����������
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				allPoints_grade_order.addAll(allPoints_grade.subList(end, count-1));
				map.put(String.valueOf(10+bigRatio.length), allPoints_grade_order);
			}
		}else if(count<10){
			int start = 0;//��ʼ�±�
			int end = 0;//�����±�
			for (int i = 0; i < smallerRatio.length; i++) {
				if(count==end+1){
					map.put(String.valueOf(10+i), null);
					continue; 
				}
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				int number = (int) (smallerRatio[i]*(count));
				end = end + number - 1;
				allPoints_grade_order.addAll(allPoints_grade.subList(start, end + 1));//sublist����ҿ�
				map.put(String.valueOf(10+i), allPoints_grade_order);
				start = end + 1;
			}
			if(end<count-1){//�����������
				List<List<String[]>> allPoints_grade_order = new ArrayList<List<String[]>>();
				allPoints_grade_order.addAll(allPoints_grade.subList(end, count-1));
				map.put(String.valueOf(10+bigRatio.length), allPoints_grade_order);
			}
		}
		return map;
	}  
}
