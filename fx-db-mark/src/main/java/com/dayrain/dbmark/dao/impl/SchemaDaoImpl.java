package com.dayrain.dbmark.dao.impl;

import com.dayrain.common.StringUtils;
import com.dayrain.dbmark.dao.DatasourceHolder;
import com.dayrain.dbmark.dao.SchemaDao;
import com.dayrain.dbmark.entity.ColumnInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SchemaDaoImpl implements SchemaDao {
    @Override
    public List<ColumnInfo> listColumns(String dbName) {
        Statement statement = null;
        try {
            Connection connection = DatasourceHolder.getConnection();
            statement = connection.createStatement();
            statement.execute("select * from information_schema.COLUMNS where TABLE_SCHEMA = '" + dbName + "'\n");
            ResultSet resultSet = statement.getResultSet();
            List<ColumnInfo> columnInfos = new ArrayList<>();
            while (resultSet.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                String columnName = resultSet.getString("COLUMN_NAME");
                String tableName = resultSet.getString("TABLE_NAME");
                columnInfo.setIdentify(tableName + "." + columnName);
                columnInfo.setColumnName(columnName);
                columnInfo.setTableName(tableName);
                columnInfo.setDbName(resultSet.getString("TABLE_SCHEMA"));
                columnInfo.setPosition(resultSet.getInt("ORDINAL_POSITION"));
                columnInfo.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
                columnInfo.setNullable("YES".equals(resultSet.getString("IS_NULLABLE")));

                String column_default = resultSet.getString("COLUMN_DEFAULT");
                columnInfo.setDefaultValue("null".equals(column_default) ? null : column_default);

                columnInfo.setColumnType(resultSet.getString("COLUMN_TYPE"));
                columnInfo.setCharacterSet(resultSet.getString("CHARACTER_SET_NAME"));
                columnInfo.setCollationName(resultSet.getString("COLLATION_NAME"));
                columnInfo.setIndexType(resultSet.getString("COLUMN_KEY"));
                columnInfo.setExtra(resultSet.getString("EXTRA"));
                columnInfo.setComment(resultSet.getString("COLUMN_COMMENT"));
                columnInfos.add(columnInfo);
            }

            return columnInfos;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void alterColumn(ColumnInfo columnInfo) {
        Statement statement = null;
        try {
            Connection connection = DatasourceHolder.getConnection();
            statement = connection.createStatement();
            String sql = buildColumnSql(columnInfo);
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<String> showTables() {
        Statement statement = null;
        try {
            Connection connection = DatasourceHolder.getConnection();
            statement = connection.createStatement();
            statement.execute("show databases;");
            ResultSet resultSet = statement.getResultSet();
            List<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("Database"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void alterTableName(String dbName, String originTableName, String targetTableName) {
        Statement statement = null;
        try {
            Connection connection = DatasourceHolder.getConnection();
            statement = connection.createStatement();
            statement.execute("alter table " + dbName + "." + originTableName + " rename to " + dbName + "." + targetTableName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String buildColumnSql(ColumnInfo columnInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("alter table ").append(columnInfo.getDbName()).append(".").append(columnInfo.getTableName())
                .append(" modify column ").append(columnInfo.getColumnName()).append(" ").append(columnInfo.getColumnType());

        if (!columnInfo.isNullable()) {
            sqlBuilder.append(" not null ");
        }

        if (!StringUtils.isBlank(columnInfo.getDefaultValue())) {
            sqlBuilder.append(" default ").append(columnInfo.getDefaultValue());
        }

        if (!StringUtils.isBlank(columnInfo.getExtra())) {
            sqlBuilder.append(" ").append(columnInfo.getExtra()).append(" ");
        }

        if (!StringUtils.isBlank(columnInfo.getComment())) {
            sqlBuilder.append(" comment ").append("\"").append(columnInfo.getComment()).append("\"");
        }
        return sqlBuilder.toString();
    }
}
