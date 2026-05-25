package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.exception.error.CsvValidationException;
import com.jann.csv_data_hub.exception.error.DataIngestionException;
import com.jann.csv_data_hub.mapper.SqlTypeMapper;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.model.file_storage.FileStorageInfo;
import com.jann.csv_data_hub.model.metadata.ColumnMetadata;
import com.jann.csv_data_hub.repository.DataIngestionRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataIngestionService {

    private static final int BATCH_SIZE = 5000;

    private final DataIngestionRepository repository;
    private final TableMetadataService metadataService;
    private final RequestTrackerService tracker;

    public DataIngestionService(DataIngestionRepository repository,
                                TableMetadataService metadataService,
                                RequestTrackerService tracker) {
        this.repository = repository;
        this.metadataService = metadataService;
        this.tracker = tracker;
    }

    public void process(RequestTrackerMessage<FileStorageInfo> message) {
        String requestId = message.getRequestId();
        FileStorageInfo fileStorageInfo = message.getPayload();

        String tableName = fileStorageInfo.getTableName();
        Path path = Paths.get(fileStorageInfo.getFilePath());
        var columns = metadataService.getColumns(tableName);

        try {
            tracker.executeStep(requestId, RequestStatus.RUNNING);

            try (InputStream is = Files.newInputStream(path)) {

                repository.copyInsert(tableName, is);

                tracker.executeStep(requestId, RequestStatus.DONE);
                return;

            } catch (Exception copyError) {
                System.err.println("COPY failed, fallback to batch");
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {

                String headerLine = reader.readLine();
                validateHeader(headerLine, columns);

                List<Object[]> batch = new ArrayList<>(BATCH_SIZE);

                String line;
                long lineNumber = 1;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    try {
                        Object[] parsed = parseLine(line, columns);
                        batch.add(parsed);

                        if (batch.size() >= BATCH_SIZE) {
                            repository.batchInsert(tableName, columns, batch);
                            batch.clear();
                        }

                    } catch (Exception e) {
                        System.err.println("Error line " + lineNumber + ": " + e.getMessage());
                    }
                }

                if (!batch.isEmpty()) {
                    repository.batchInsert(tableName, columns, batch);
                }

                tracker.executeStep(requestId, RequestStatus.DONE);

            } catch (Exception e) {
                tracker.executeStep(requestId, RequestStatus.FAILED);
                throw new DataIngestionException("Error processing CSV file", e);
            }

        } catch (Exception ex) {
            tracker.executeStep(requestId, RequestStatus.FAILED);
            throw ex;
        }
    }

    private void validateHeader(String header, List<ColumnMetadata> columns) {

        if (header == null) {
            throw new CsvValidationException("CSV is empty");
        }

        if (header.split(",").length != columns.size()) {
            throw new CsvValidationException("CSV columns do not match table structure");
        }
    }

    private Object[] parseLine(String line, List<ColumnMetadata> columns) {

        String[] values = line.split(",", -1);

        if (values.length != columns.size()) {
            throw new CsvValidationException("Invalid column count");
        }

        Object[] parsed = new Object[values.length];

        for (int i = 0; i < values.length; i++) {
            parsed[i] = SqlTypeMapper.convertSqlType(values[i], columns.get(i).sqlType());
        }

        return parsed;
    }
}