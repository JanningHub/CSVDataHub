package com.jann.csv_data_hub.repository;

import com.jann.csv_data_hub.exception.error.TableValidationException;
import com.jann.csv_data_hub.model.table.ColumnInfo;
import com.jann.csv_data_hub.model.table.TableInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TableManagementRepository {

    private final JdbcClient jdbcClient;

    public TableManagementRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void createTable(TableInfo tableInfo) {

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ")
                .append(tableInfo.getTableName())
                .append(" (");

        for (int i = 0; i < tableInfo.getColumns().size(); i++) {
            ColumnInfo col = tableInfo.getColumns().get(i);

            sql.append(col.getName())
                    .append(" ")
                    .append(col.getType());

            if (i < tableInfo.getColumns().size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");

        jdbcClient.sql(sql.toString())
                .update();
    }

    public TableInfo getTableInfo(String tableName) {

        List<ColumnInfo> columns = jdbcClient.sql("""
                SELECT column_name, data_type
                FROM information_schema.columns
                WHERE table_name = :tableName
                ORDER BY ordinal_position
            """)
                .param("tableName", tableName)
                .query((rs, rowNum) ->
                        new ColumnInfo(
                                rs.getString("column_name"),
                                rs.getString("data_type")
                        )
                )
                .list();

        if (columns.isEmpty()) {
            throw new TableValidationException("Table not found: " + tableName);
        }

        return new TableInfo(tableName, columns);
    }

    public void dropTable(String tableName) {

        jdbcClient.sql("DROP TABLE IF EXISTS " + tableName)
                .update();
    }

    public boolean tableExists(String tableName) {
        Integer count = jdbcClient.sql("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = 'public'
              AND table_name = :tableName
        """)
                .param("tableName", tableName)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }
}