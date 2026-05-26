package com.jann.csv_data_hub.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataAnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> execute(String sql, List<Object> params) {
        return jdbcTemplate.queryForList(sql, params.toArray());
    }
}