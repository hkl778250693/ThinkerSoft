package com.example.administrator.thinker_soft.myfirstpro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regularexpression {
	

	 public static boolean checkUserName(String userName) {
		  String regex = "([a-z]|[A-Z]|[0-9]|[\\u4e00-\\u9fa5])+";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(userName);
		  return m.matches();
	 }
	 public static boolean checkCount(String checkStr){
		  String regex = "([0-9])+";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(checkStr);
		 return m.matches();
	 }
	 public static boolean checkIp(String ip){
		  String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(ip);
		 return m.matches();
	 }	
	 public static boolean checkPort(String port){
		  String regex = "([0-9])+";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(port);
		  return m.matches();
	 }		 
	 public static boolean checkfileName(String filename){
		  String regex = "([^-/+])+";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(filename);
		  return m.matches();
	 }
}


