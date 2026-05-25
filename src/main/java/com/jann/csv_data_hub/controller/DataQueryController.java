package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.model.data_query.DataQueryResponse;
import com.jann.csv_data_hub.service.DataQueryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data-query")
public class DataQueryController {

    DataQueryService dataQueryService;

    public DataQueryController(DataQueryService dataQueryService) {
        this.dataQueryService = dataQueryService;
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<DataQueryResponse> getData(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "false") boolean includeTableInfo,
            Pageable pageable
    ) {
        DataQueryResponse response = dataQueryService.getData(tableName, includeTableInfo, pageable);
        return ResponseEntity.ok(response);
    }
}
