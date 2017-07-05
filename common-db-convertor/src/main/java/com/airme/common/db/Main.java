package com.airme.common.db;

import java.io.FileInputStream;
import java.util.Properties;

import com.airme.common.db.constants.Constants;

public class Main {
	public static void main(String[] args) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(Constants.CURRENT_DIR + Constants.DB_CONVERTOR_CONFIG_FILE_NAME));
			String packageName = prop.getProperty(Constants.PACKAGE);
			String username = prop.getProperty(Constants.USERNAME);
			String password = prop.getProperty(Constants.PASSWORD);
			String url = prop.getProperty(Constants.URL);
			String sqlType = prop.getProperty(Constants.SQLTYPE);
			String destination = Constants.CURRENT_DIR + "dest/";
			String templateFolder = Constants.CURRENT_DIR + "template/";
			Executor executor = new Executor();
			executor.setPackageName(packageName);
			executor.setUsername(username);
			executor.setUrl(url);
			executor.setPassword(password);
			executor.setSqlType(sqlType);
			executor.setDestination(destination);
			executor.setTemplateFolder(templateFolder);
			executor.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
