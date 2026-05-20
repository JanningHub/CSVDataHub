package com.jann.csv_data_hub.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataIngestionRepository {

    public void batchInsert(List<String> batch) {

        // AQUI ENTRA O ACESSO AO BANCO:
        // - JdbcTemplate.batchUpdate(...)
        // - PreparedStatement batch
        // - COPY (Postgres) se quiser performance máxima

    }
}