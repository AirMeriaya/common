package com.airme.common.db.convertor.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.airme.common.db.convertor.BaseConvertor;

public class MySQLConvertor extends BaseConvertor {

	public MySQLConvertor(Connection connection) {
		super(connection);
	}

	@Override
	protected String getTableNameLabel() {
		return "TABLE_NAME";
	}

	@Override
	protected String getColumnNameLabel() {
		return "COLUMN_NAME";
	}

	@Override
	protected String getColumnTypeLabel() {
		return "TYPE_NAME";
	}

	@Override
	protected ResultSet getTableResultSet(DatabaseMetaData metadata) throws SQLException {
		return metadata.getTables(null, null, null, new String[]{"TABLE"});
	}

	@Override
	protected ResultSet getColumnResultSet(DatabaseMetaData metadata, String tableName) throws SQLException {
		return metadata.getColumns(null, null, tableName, null);
	}

	@Override
	protected String getPrimaryKeyName(DatabaseMetaData metadata, String tableName) throws SQLException {
		ResultSet pkResultSet = metadata.getPrimaryKeys(null, null, tableName);
		if(pkResultSet.next()){
			return pkResultSet.getString(4);
		}
		return "";
	}

}
