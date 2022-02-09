package com.dayrain.dbmark;

import com.dayrain.dbmark.entity.ColumnInfo;

import java.util.List;

public interface DbMarkService {
    List<String> listTables();

    void alterColumn(ColumnInfo columnInfo);

    void alterTableName(String dbName, String tableName, String targetName);

    List<ColumnInfo> listColumns(String curTable);
}
