package com.dayrain.dbmark;

import com.dayrain.dbmark.dao.SchemaDao;
import com.dayrain.dbmark.dao.impl.SchemaDaoImpl;
import com.dayrain.dbmark.entity.ColumnInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DbMarkServiceImpl implements DbMarkService{

    private SchemaDao schemaDao = new SchemaDaoImpl();

    private static Set<String>defaultSet = new HashSet<>();

    static {
        defaultSet.add("information_schema");
        defaultSet.add("mysql");
        defaultSet.add("performance_schema");
        defaultSet.add("sys");
    }
    @Override
    public List<String> listTables() {
        List<String> tables= schemaDao.showTables();
        return filterTable(tables);
    }

    @Override
    public void alterColumn(ColumnInfo columnInfo) {
        schemaDao.alterColumn(columnInfo);
    }

    @Override
    public void alterTableName(String dbName, String tableName, String targetName) {
        schemaDao.alterTableName(dbName, tableName, tableName);
    }

    @Override
    public List<ColumnInfo> listColumns(String curTable) {
        return schemaDao.listColumns(curTable);
    }

    private List<String> filterTable(List<String> tables) {
        return tables.stream().filter(table -> !defaultSet.contains(table)).collect(Collectors.toList());
    }
}
