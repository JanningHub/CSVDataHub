package com.jann.csv_data_hub.repository;

import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class FileStorageRepository implements JpaRepository<FileStorageInfo, String> {
}
