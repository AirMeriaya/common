package com.airme.common.db.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConvertorUtil {
	private static final Map<String, String> SQL_DRIVER = new HashMap<String, String>();
	static{
		SQL_DRIVER.put("mysql", "com.mysql.jdbc.Driver");
	}
	
	public static String getSQLDriver(String sqlType) {
		return SQL_DRIVER.get(sqlType);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}
	
	public static String upperFirst(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	public static String lowerFirst(String str){
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}
	
	public static String package2Path(String str){
		return str.replaceAll("\\.", "/") + "/";
	}
	
	public static boolean isEmpty(List<?> list){
		return list == null || list.size() == 0;
	}
	
	public static boolean isEmpty(Object[] arr) {
		return arr == null || arr.length == 0;
	}
}
