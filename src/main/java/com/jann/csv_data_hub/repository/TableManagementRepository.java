package com.jann.csv_data_hub.repository;

import org.springframework.stereotype.Repository;

@Repository
public class TableManagementRepository {

    public void createTable(String schemaJson) {

        // parse JSON + montar SQL CREATE TABLE
        // jdbcTemplate.execute(sql);

    }

    public void dropTable(String tableName) {

        // jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName);

    }
}