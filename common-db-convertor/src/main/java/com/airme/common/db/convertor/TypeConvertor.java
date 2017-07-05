package com.airme.common.db.convertor;

public interface TypeConvertor {
	Class<?> convertType(String columnType);
}