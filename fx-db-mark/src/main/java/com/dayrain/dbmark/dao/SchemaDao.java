package com.dayrain.dbmark.dao;

import com.dayrain.dbmark.entity.ColumnInfo;

import java.util.List;

public interface SchemaDao {
    List<ColumnInfo> listColumns(String dbName);

    void alterColumn(ColumnInfo columnInfo);

    List<String> showTables();

    void alterTableName(String dbName, String originTableName, String targetTableName);
}
