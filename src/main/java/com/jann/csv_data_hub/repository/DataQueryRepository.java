package com.jann.csv_data_hub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<Map<String, Object>> getData(String tableName, Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();

        String sql = "SELECT * FROM " + tableName +
                " LIMIT " + limit +
                " OFFSET " + offset;

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);

        String countSql = "SELECT COUNT(*) FROM " + tableName;
        Long total = jdbcTemplate.queryForObject(countSql, Long.class);

        return new PageImpl<>(data, pageable, total);
    }
}