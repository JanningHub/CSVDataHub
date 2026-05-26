package com.jann.csv_data_hub.component.cache;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TableSchemaCache {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, Set<String>> cache = new ConcurrentHashMap<>();

    public TableSchemaCache(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<String> getNumericColumns(String table) {
        return cache.computeIfAbsent(table, this::load);
    }

    private Set<String> load(String table) {

        String sql = """
            SELECT column_name, data_type
            FROM information_schema.columns
            WHERE table_name = ?
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, table);

        Set<String> result = new HashSet<>();

        for (Map<String, Object> row : rows) {
            String type = (String) row.get("data_type");

            if (type.equals("integer")
                    || type.equals("bigint")
                    || type.equals("numeric")
                    || type.equals("decimal")
                    || type.equals("double precision")) {

                result.add((String) row.get("column_name"));
            }
        }

        return result;
    }
}
