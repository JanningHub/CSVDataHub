package com.jann.csv_data_hub.model.file_storage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileStorageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String filePath;

    private String tableName;

    public FileStorageInfo() {
    }

    public FileStorageInfo(String filePath, String tableName) {
        this.filePath = filePath;
        this.tableName = tableName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


