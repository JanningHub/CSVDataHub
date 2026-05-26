package com.jann.csv_data_hub.controller;

import com.jann.csv_data_hub.model.data_query.DataQueryResponse;
import com.jann.csv_data_hub.model.analytics.DataAnalyticsRequest;
import com.jann.csv_data_hub.model.analytics.DataAnalyticsResponse;
import com.jann.csv_data_hub.service.DataQueryService;
import com.jann.csv_data_hub.service.DataAnalyticsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data-query")
public class DataQueryController {

    private final DataQueryService dataQueryService;
    private final DataAnalyticsService dataAnalyticsService;

    public DataQueryController(DataQueryService dataQueryService,
                               DataAnalyticsService dataAnalyticsService) {
        this.dataQueryService = dataQueryService;
        this.dataAnalyticsService = dataAnalyticsService;
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<DataQueryResponse> getData(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "false") boolean includeTableInfo,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                dataQueryService.getData(tableName, includeTableInfo, pageable)
        );
    }

    @PostMapping("/analytics")
    public ResponseEntity<DataAnalyticsResponse> analytics(
            @RequestBody DataAnalyticsRequest request
    ) {
        return ResponseEntity.ok(
                new DataAnalyticsResponse(
                        dataAnalyticsService.execute(request)
                )
        );
    }
}