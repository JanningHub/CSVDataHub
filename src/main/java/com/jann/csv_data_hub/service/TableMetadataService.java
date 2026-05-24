package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.mapper.SqlTypeMapper;
import com.jann.csv_data_hub.model.metadata.ColumnMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableMetadataService {

    private final JdbcTemplate jdbcTemplate;

    public TableMetadataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ColumnMetadata> getColumns(String tableName) {

        return jdbcTemplate.query("""
            SELECT column_name, data_type
            FROM information_schema.columns
            WHERE table_name = ?
            ORDER BY ordinal_position
        """, (rs, rowNum) -> new ColumnMetadata(
                rs.getString("column_name"),
                mapToSqlType(rs.getString("data_type"))
        ), tableName);
    }

    private int mapToSqlType(String type) {
        return SqlTypeMapper.toSqlType(type);
    }
}