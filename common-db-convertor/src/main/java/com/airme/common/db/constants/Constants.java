package com.airme.common.db.constants;

public class Constants {
	// Configuration Key
	public static final String PACKAGE = "package";
	public static final String SQLTYPE = "sql_type";
	public static final String URL = "db_url";
	public static final String USERNAME = "db_username";
	public static final String PASSWORD = "db_password";

	// Package
	public static final String PKG_DATE = "java.util.Date";
	public static final String PKG_DECIMAL = "java.math.BigDecimal";
	public static final String PKG_SUFFIX_MODEL = ".dao.model";
	public static final String PKG_SUFFIX_MAP = ".dao.map";
	public static final String PKG_SUFFIX_MAPPER = ".dao.mapper";

	// Database Field Type
	public static final String COLUMN_TYPE_VARCHAR = "VARCHAR";
	public static final String COLUMN_TYPE_CHAR = "CHAR";
	public static final String COLUMN_TYPE_INT = "INT";
	public static final String COLUMN_TYPE_INTEGER = "INTEGER";
	public static final String COLUMN_TYPE_DECIMAL = "DECIMAL";
	public static final String COLUMN_TYPE_DATE = "DATE";
	public static final String COLUMN_TYPE_DATETIME = "DATETIME";
	public static final String COLUMN_TYPE_BLOB = "BLOB";
	public static final String COLUMN_TYPE_LONGBLOB = "LONGBLOB";
	public static final String COLUMN_TYPE_LONGTEXT = "LONGTEXT";
	public static final String COLUMN_TYPE_BIT = "BIT";

	// File Name
	public static final String TEMPLATE_MODEL = "model.ftl";
	public static final String TEMPLATE_MAP = "map.ftl";
	public static final String TEMPLATE_MAPPER = "mapper.ftl";
	public static final String DB_CONVERTOR_CONFIG_FILE_NAME = "db-convertor.conf";

	// Path
	public static final String CURRENT_DIR = System.getProperty("user.dir") + "/";
}
