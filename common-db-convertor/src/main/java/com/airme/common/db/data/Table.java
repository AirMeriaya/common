package com.airme.common.db.data;

import java.util.List;

public class Table {
	private String tableName;
	private String className;
	private Column primaryKey;
	private List<Column> columns;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Column getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey;
	}
}
