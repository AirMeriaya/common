package com.airme.common.db.convertor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.airme.common.db.constants.Constants;
import com.airme.common.db.data.Column;
import com.airme.common.db.data.Table;
import com.airme.common.db.util.DBConvertorUtil;

public abstract class BaseConvertor implements Convertor {
	private DatabaseMetaData metadata;
	private NameConvertor nameConvertor;
	private TypeConvertor typeConvertor;
	
	public BaseConvertor(Connection connection) {
		try {
			this.metadata = connection.getMetaData();
			this.nameConvertor = new DefaultNameConvertor();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setNameConvertor(NameConvertor nameConvertor) {
		this.nameConvertor = nameConvertor;
	}

	public void setTypeConvertor(TypeConvertor typeConvertor) {
		this.typeConvertor = typeConvertor;
	}
	
	protected abstract String getTableNameLabel();
	protected abstract String getColumnNameLabel();
	protected abstract String getColumnTypeLabel();
	protected abstract ResultSet getTableResultSet(DatabaseMetaData metadata) throws SQLException ;
	protected abstract ResultSet getColumnResultSet(DatabaseMetaData metadata, String tableName) throws SQLException ;
	protected abstract String getPrimaryKeyName(DatabaseMetaData metadata, String tableName) throws SQLException ;
	
	@Override
	public List<Table> getTables() {
		List<Table> tables = new ArrayList<Table>();
		ResultSet tableResultSet = null;
		ResultSet columnResultSet = null;
		try {
			tableResultSet = getTableResultSet(metadata);
			while(tableResultSet.next()){
				String tableName = tableResultSet.getString(getTableNameLabel());
				String className = this.nameConvertor.convertTableName(tableName);
				Table table = new Table();
				table.setTableName(tableName);
				table.setClassName(className);
				List<Column> columns = new ArrayList<Column>();
				boolean pkHasFound = false;
				columnResultSet = getColumnResultSet(metadata, tableName);
				String primaryKeyName = getPrimaryKeyName(metadata, tableName);
				if(DBConvertorUtil.isEmpty(primaryKeyName)){
					System.out.println("Table [" + tableName + "] doesn't have primary key.");
				}
				while(columnResultSet.next()){
					String columnName = columnResultSet.getString(getColumnNameLabel());
					String fieldName = this.nameConvertor.convertColumnName(columnName);
					String columnType = columnResultSet.getString(getColumnTypeLabel());
					Class<?> fieldType = convertType(columnType);
					if(boolean.class == fieldType && fieldName.startsWith("is")){
						fieldName = DBConvertorUtil.lowerFirst(fieldName.substring(2));
					}
					Column column = new Column();
					column.setColumnName(columnName);
					column.setColumnType(columnType);
					column.setFieldName(fieldName);
					column.setFieldType(fieldType.getSimpleName());
					columns.add(column);
					if(!pkHasFound && columnName.equals(primaryKeyName)){
						table.setPrimaryKey(column);
						pkHasFound = true;
					}
				}
				table.setColumns(columns);
				tables.add(table);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(columnResultSet != null)
					columnResultSet.close();
				if(tableResultSet != null)
					tableResultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return tables;
	}
	
	private Class<?> convertType(String columnType) {
		if(DBConvertorUtil.isEmpty(columnType))
			throw new RuntimeException("Column type must not be empty.");
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_CHAR)
				|| columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_VARCHAR)
				|| columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_LONGTEXT))
			return String.class;
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_INT)
				|| columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_INTEGER))
			return Integer.class;
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_BIT))
			return boolean.class;
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_DECIMAL))
			return BigDecimal.class;
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_DATE)
				|| columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_DATETIME))
			return Date.class;
		else if(columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_BLOB)
				|| columnType.equalsIgnoreCase(Constants.COLUMN_TYPE_LONGBLOB))
			return byte[].class;
		else{
			if(this.typeConvertor != null){
				return this.typeConvertor.convertType(columnType);
			}else{
				throw new RuntimeException("Can not convert column type: " + columnType);
			}
		}
	}
	
	private static class DefaultNameConvertor implements NameConvertor {

		@Override
		public String convertTableName(String tableName) {
			if(DBConvertorUtil.isEmpty(tableName))
				throw new RuntimeException("Table name must not be empty.");
			String[] words = tableName.split("_");
			StringBuilder strBuilder = new StringBuilder();
			for(String word : words)
				strBuilder.append(DBConvertorUtil.upperFirst(word));
			return strBuilder.toString();
		}

		@Override
		public String convertColumnName(String columnName) {
			if(DBConvertorUtil.isEmpty(columnName))
				throw new RuntimeException("Column name must not be empty.");
			DBConvertorUtil.lowerFirst(columnName);
			String[] words = columnName.split("_");
			StringBuilder strBuilder = new StringBuilder(words[0]);
			for(int i=1; i<words.length; i++)
				strBuilder.append(DBConvertorUtil.upperFirst(words[i]));
			return strBuilder.toString();
		}
		
	}

}