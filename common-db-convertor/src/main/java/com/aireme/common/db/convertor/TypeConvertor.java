package com.aireme.common.db.convertor;

public interface TypeConvertor {
	Class<?> convertType(String columnType);
}