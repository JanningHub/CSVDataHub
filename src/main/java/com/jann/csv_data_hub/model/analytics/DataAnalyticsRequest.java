package com.jann.csv_data_hub.model.analytics;

import java.util.List;

public class DataAnalyticsRequest {

    private String tableName;
    private List<String> groupBy;
    private List<FilterCondition> filters;
    private List<MetricRequest> metrics;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }

    public List<FilterCondition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterCondition> filters) {
        this.filters = filters;
    }

    public List<MetricRequest> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricRequest> metrics) {
        this.metrics = metrics;
    }
}