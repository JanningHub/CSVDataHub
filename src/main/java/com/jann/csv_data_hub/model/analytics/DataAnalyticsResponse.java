package com.jann.csv_data_hub.model.analytics;

import java.util.List;
import java.util.Map;

public class DataAnalyticsResponse {

    private List<Map<String, Object>> data;

    public DataAnalyticsResponse(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}