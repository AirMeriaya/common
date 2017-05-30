package com.aireme.common.db;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.aireme.common.db.constants.Constants;
import com.aireme.common.db.convertor.BaseConvertor;
import com.aireme.common.db.convertor.impl.MySQLConvertor;
import com.aireme.common.db.data.Column;
import com.aireme.common.db.data.Table;
import com.aireme.common.db.util.DBConvertorUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class Executor {
	private String packageName;
	private String packagePath;
	private String destination;
	private String username;
	private String password;
	private String url;
	private String templateFolder;
	private String sqlType;
	private BaseConvertor convertor;
	private Configuration templateConfig;
	private Connection connection;
	
	public void execute() {
		System.out.println("DB convertor start...");
		try {
			initialize();
			process();
			System.out.println("DB convertor complete.");
		} catch (Exception e) {
			String msg = e.getMessage();
			Throwable cause = e.getCause();
			if(cause != null){
				msg = cause.getMessage();
			}
			System.out.println("DB convertor fail due to [" + msg + "]");
			onFail();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Failed to close database connection due to " + e.getMessage());
				}
			}
		}
	}
	
	private void initialize() {
		if(!Pattern.matches("^[a-z_A-Z][0-9_a-zA-Z\\.]*", packageName)){
			throw new RuntimeException("Invalid package value: " + packageName);
		}
		packagePath = DBConvertorUtil.package2Path(packageName);
		cleanup(new File(destination));
		new File(destination + packagePath).mkdirs();
		try {
			Class.forName(DBConvertorUtil.getSQLDriver(sqlType));
			connection = DriverManager.getConnection(url, username, password);
			templateConfig = new Configuration(Configuration.VERSION_2_3_23);
			templateConfig.setDirectoryForTemplateLoading(new File(templateFolder));
			templateConfig.setDefaultEncoding("UTF-8");
		} catch (Exception e) {
			System.out.println("Failed to initialize");
			throw new RuntimeException(e);
		}
		switch(sqlType){
		case "mysql":
			convertor = new MySQLConvertor(connection);
			break;
		default:
			throw new RuntimeException("DO NOT support the current sql type: " + sqlType);
		}
	}
	
	private void cleanup(File file) {
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(!DBConvertorUtil.isEmpty(files)){
				for(File sub : files){
					cleanup(sub);
				}
			}
			file.delete();
		}else{
			file.delete();
		}
	}
	
	private void process() {
		List<Table> tables = convertor.getTables();
		if(DBConvertorUtil.isEmpty(tables)){
			throw new RuntimeException("There is no any tables in database.");
		}
		new File(destination + packagePath + DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MODEL)).mkdirs();
		new File(destination + packagePath + DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MAP)).mkdirs();
		new File(destination + packagePath + DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MAPPER)).mkdirs();
		for(Table table : tables){
			parseToEntity(table);
			parseToDAO(table);
			parseToMapper(table);
		}
	}
	
	private void parseToEntity(Table table) {
		System.out.println("Generating entity for table ["+ table.getTableName() +"]...");
		try {
			Template template = templateConfig.getTemplate(Constants.TEMPLATE_MODEL);
			String fileName = new StringBuilder(destination).append(packagePath)
					.append(DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MODEL))
					.append(table.getClassName()).append(".java").toString();
			FileWriter fw = new FileWriter(fileName);
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("table", table);
			objMap.put("package", packageName + Constants.PKG_SUFFIX_MODEL);
			objMap.put("className", table.getClassName());
			objMap.put("importPackage", getImportPackage(table));
			template.process(objMap, fw);
			System.out.println("Entity for table [" + table.getTableName() + "] complete.");
		} catch (Exception e) {
			System.out.println("Failed to generate entity for table [" 
					+ table.getTableName() + "]");
			throw new RuntimeException(e);
		}
	}
	
	private void parseToDAO(Table table) {
		System.out.println("Generating DAO for table ["+ table.getTableName() +"]...");
		try {
			Template template = templateConfig.getTemplate(Constants.TEMPLATE_MAP);
			String fileName = new StringBuilder(destination).append(packagePath)
					.append(DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MAP))
					.append(table.getClassName()).append("DAO.java").toString();
			FileWriter fw = new FileWriter(fileName);
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("table", table);
			objMap.put("package", packageName + Constants.PKG_SUFFIX_MAP);
			objMap.put("className", table.getClassName() + "DAO.java");
			objMap.put("importPackage", packageName + table.getClassName());
			template.process(objMap, fw);
			System.out.println("DAO for table [" + table.getTableName() + "] complete.");
		} catch (Exception e) {
			System.out.println("Failed to generate DAO for table [" 
					+ table.getTableName() + "]");
			throw new RuntimeException(e);
		}
	}
	
	private void parseToMapper(Table table) {
		System.out.println("Generating mapper for table ["+ table.getTableName() +"]...");
		try {
			Template template = templateConfig.getTemplate(Constants.TEMPLATE_MAPPER);
			String fileName = new StringBuilder(destination).append(packagePath)
					.append(DBConvertorUtil.package2Path(Constants.PKG_SUFFIX_MAPPER))
					.append(table.getTableName()).append(".xml").toString();
			FileWriter fw = new FileWriter(fileName);
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("table", table);
			objMap.put("namespace", packageName + Constants.PKG_SUFFIX_MAP + "." + table.getClassName() + "DAO");
			template.process(objMap, fw);
			System.out.println("Mapper for table [" + table.getTableName() + "] complete.");
		} catch (Exception e) {
			System.out.println("Failed to generate mapper for table [" 
					+ table.getTableName() + "]");
			throw new RuntimeException(e);
		}
	}
	
	private Set<String> getImportPackage(Table table) {
		Set<String> importPackages = new HashSet<String>();
		for(Column column : table.getColumns()){
			if(column.getFieldType().equals("BigDecimal")){
				importPackages.add(Constants.PKG_DECIMAL);
			} else if(column.getFieldType().equals("Date")){
				importPackages.add(Constants.PKG_DATE);
			}
		}
		return importPackages;
	}
	
	private void onFail() {
		File dest = new File(destination);
		if(dest.exists()){
			File[] sub = dest.listFiles();
			if(sub != null){
				for(File f : sub){
					cleanup(f);
				}
			}
		}
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTemplateFolder() {
		return templateFolder;
	}

	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
}
