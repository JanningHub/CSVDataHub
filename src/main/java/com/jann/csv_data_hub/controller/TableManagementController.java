package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.message.producer.TableProducer;
import com.jann.csv_data_hub.message.tracker.dto.RequestTrackerResponse;
import com.jann.csv_data_hub.message.tracker.service.RequestTrackerService;
import com.jann.csv_data_hub.model.table.TableInfo;
import com.jann.csv_data_hub.service.TableManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table-management")
public class TableManagementController {

    private final TableManagementService tableManagementService;
    private final TableProducer producer;
    private final RequestTrackerService requestTrackerService;

    public TableManagementController(TableManagementService tableManagementService,
                                     TableProducer producer,
                                     RequestTrackerService requestTrackerService) {
        this.tableManagementService = tableManagementService;
        this.producer = producer;
        this.requestTrackerService = requestTrackerService;
    }

    @PostMapping
    public ResponseEntity<String> createTable(@RequestBody TableInfo tableInfo) {

        String requestId = producer.create(tableInfo);

        String response = String.format(
                "Create request for table '%s' has been queued for processing. Request ID: %s",
                tableInfo.getTableName(),
                requestId
        );

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<TableInfo> getTable(@PathVariable String tableName) {

        TableInfo tableInfo = tableManagementService.getTable(tableName);

        return ResponseEntity.ok(tableInfo);
    }

    @DeleteMapping("/{tableName}")
    public ResponseEntity<String> dropTable(@PathVariable String tableName) {

        String requestId = producer.delete(tableName);

        String response = String.format(
                "Delete request for table '%s' has been queued for processing. Request ID: %s",
                tableName,
                requestId
        );

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/request-status/{id}")
    public ResponseEntity<RequestTrackerResponse> requestStatus(@PathVariable String id) {
        return ResponseEntity.ok(requestTrackerService.getRequestTracker(id));
    }
}