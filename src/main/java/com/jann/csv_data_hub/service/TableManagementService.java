package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.exception.error.TableValidationException;
import com.jann.csv_data_hub.message.tracker.domain.RequestStatus;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerMessage;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.model.table.ColumnInfo;
import com.jann.csv_data_hub.model.table.TableInfo;
import com.jann.csv_data_hub.repository.TableManagementRepository;
import org.springframework.stereotype.Service;

@Service
public class TableManagementService {

    private final TableManagementRepository repository;
    private final RequestTrackerService tracker;

    public TableManagementService(TableManagementRepository repository,
                                  RequestTrackerService tracker) {
        this.repository = repository;
        this.tracker = tracker;
    }

    public void createTable(RequestTrackerMessage<TableInfo> message) {
        String requestId = message.getRequestId();

        try {
            tracker.executeStep(requestId, RequestStatus.RUNNING);

            TableInfo tableInfo = message.getPayload();

            validate(tableInfo);

            repository.createTable(tableInfo);

            tracker.executeStep(requestId, RequestStatus.DONE);

        } catch (Exception ex) {
            tracker.executeStep(requestId, RequestStatus.FAILED);
            throw ex;
        }
    }

    public TableInfo getTable(String tableName) {

        System.out.println("Searching table: " + tableName);

        validateTableName(tableName);

        return repository.getTableInfo(tableName);
    }

    public void deleteTable(RequestTrackerMessage<String> message) {
        String requestId = message.getRequestId();

        try {
            tracker.executeStep(requestId, RequestStatus.RUNNING);

            String tableName = message.getPayload();

            validateTableName(tableName);

            tableExists(tableName);

            repository.dropTable(tableName);

            tracker.executeStep(requestId, RequestStatus.DONE);

        } catch (Exception ex) {
            tracker.executeStep(requestId, RequestStatus.FAILED);
            throw ex;
        }
    }

    private void validate(TableInfo tableInfo) {

        validateTableName(tableInfo.getTableName());

        if (tableInfo.getColumns() == null || tableInfo.getColumns().isEmpty()) {
            throw new TableValidationException("Table must have at least one column");
        }

        if (tableInfo.getColumns().size() > 200) {
            throw new TableValidationException(
                    "Table exceeds maximum allowed columns (200). Provided: "
                            + tableInfo.getColumns().size()
            );
        }

        for (ColumnInfo col : tableInfo.getColumns()) {

            if (col.getName() == null || !col.getName().matches("^[a-zA-Z0-9_]+$")) {
                throw new TableValidationException("Invalid column name: " + col.getName());
            }

            if (col.getType() == null || !col.getType().matches("^[a-zA-Z0-9() ]+$")) {
                throw new TableValidationException("Invalid column type: " + col.getType());
            }
        }
    }

    private void validateTableName(String tableName) {

        if (tableName == null || tableName.isBlank()) {
            throw new TableValidationException("Table name cannot be empty");
        }

        if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
            throw new TableValidationException("Invalid table name: " + tableName);
        }
    }

    public void tableExists(String tableName) {
        if (!repository.tableExists(tableName)) {
            throw new TableValidationException("Table does not exist: " + tableName);
        }
    }
}