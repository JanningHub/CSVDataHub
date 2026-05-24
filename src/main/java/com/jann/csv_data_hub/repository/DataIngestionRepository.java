package com.jann.csv_data_hub.repository;

import com.jann.csv_data_hub.exception.error.DataIngestionException;
import com.jann.csv_data_hub.model.metadata.ColumnMetadata;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataIngestionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DataIngestionRepository(JdbcTemplate jdbcTemplate,
                                   DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public void copyInsert(String tableName, InputStream inputStream) {

        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            BaseConnection pgConnection = connection.unwrap(BaseConnection.class);

            CopyManager copyManager = new CopyManager(pgConnection);

            String sql = "COPY " + tableName + " FROM STDIN WITH (FORMAT csv, HEADER true)";

            copyManager.copyIn(sql, inputStream);

        } catch (Exception e) {
            throw new DataIngestionException("Error during COPY operation", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    public void batchInsert(String tableName,
                            List<ColumnMetadata> columns,
                            List<Object[]> batch) {

        String columnNames = columns.stream()
                .map(ColumnMetadata::name)
                .collect(Collectors.joining(","));

        String placeholders = columns.stream()
                .map(c -> "?")
                .collect(Collectors.joining(","));

        String sql = "INSERT INTO " + tableName +
                " (" + columnNames + ") VALUES (" + placeholders + ")";

        jdbcTemplate.batchUpdate(sql, batch);
    }
}