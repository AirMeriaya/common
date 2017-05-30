package com.aireme.common.db.convertor;

public interface NameConvertor {
	String convertTableName(String tableName);
	String convertColumnName(String columnName);
}