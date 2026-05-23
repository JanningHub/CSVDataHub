package com.jann.csv_data_hub.model.table;

import java.util.List;

public class TableInfo {

    private String tableName;
    private List<ColumnInfo> columns;

    public TableInfo() {}

    public TableInfo(String tableName, List<ColumnInfo> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }
}