package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.exception.error.DataQueryAnalyticsException;
import com.jann.csv_data_hub.model.data_query.DataQueryResponse;
import com.jann.csv_data_hub.model.table.TableInfo;
import com.jann.csv_data_hub.repository.DataQueryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataQueryService {

    private final DataQueryRepository dataQueryRepository;
    private final TableManagementService tableManagementService;

    public DataQueryService(DataQueryRepository dataQueryRepository,
                            TableManagementService tableManagementService) {
        this.dataQueryRepository = dataQueryRepository;
        this.tableManagementService = tableManagementService;
    }

    public DataQueryResponse getData(String tableName, boolean includeTableInfo, Pageable pageable) {
        try {
            TableInfo tableInfo = null;

            if (includeTableInfo) {
                tableInfo = tableManagementService.getTable(tableName);
            }

            return new DataQueryResponse(
                    tableInfo,
                    dataQueryRepository.getData(tableName, pageable)
            );

        } catch (Exception e) {
            throw new DataQueryAnalyticsException(
                    "Error while fetching data for table: " + tableName,
                    e
            );
        }
    }
}