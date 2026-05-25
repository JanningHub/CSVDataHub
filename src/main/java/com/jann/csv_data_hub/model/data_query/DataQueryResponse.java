package com.jann.csv_data_hub.model.data_query;

import com.jann.csv_data_hub.model.table.TableInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public class DataQueryResponse {

    private TableInfo tableInfo;
    private Page<Map<String, Object>> data;

    public DataQueryResponse() {
    }

    public DataQueryResponse(TableInfo tableInfo, Page<Map<String, Object>> data) {
        this.tableInfo = tableInfo;
        this.data = data;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public Page<Map<String, Object>> getData() {
        return data;
    }

    public void setData(Page<Map<String, Object>> data) {
        this.data = data;
    }
}