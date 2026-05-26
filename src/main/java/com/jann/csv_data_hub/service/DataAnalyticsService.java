package com.jann.csv_data_hub.service;

import com.jann.csv_data_hub.component.builder.AnalyticsQueryBuilder;
import com.jann.csv_data_hub.component.cache.TableSchemaCache;
import com.jann.csv_data_hub.exception.error.DataQueryAnalyticsException;
import com.jann.csv_data_hub.model.analytics.DataAnalyticsRequest;
import com.jann.csv_data_hub.model.analytics.MetricRequest;
import com.jann.csv_data_hub.model.analytics.QueryType;
import com.jann.csv_data_hub.repository.DataAnalyticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DataAnalyticsService {

    private final AnalyticsQueryBuilder builder;
    private final DataAnalyticsRepository repository;
    private final TableSchemaCache schemaCache;

    public DataAnalyticsService(AnalyticsQueryBuilder builder,
                                DataAnalyticsRepository repository,
                                TableSchemaCache schemaCache) {
        this.builder = builder;
        this.repository = repository;
        this.schemaCache = schemaCache;
    }

    public List<Map<String, Object>> execute(DataAnalyticsRequest request) {

        try {
            DataAnalyticsRequest internal = map(request);

            validate(internal);

            var built = builder.build(internal);

            return repository.execute(built.sql, built.params);

        } catch (Exception e) {
            throw new DataQueryAnalyticsException(
                    "Error executing analytics query for table: " + request.getTableName(),
                    e
            );
        }
    }

    private DataAnalyticsRequest map(DataAnalyticsRequest r) {

        try {
            DataAnalyticsRequest a = new DataAnalyticsRequest();
            a.setTableName(r.getTableName());
            a.setGroupBy(r.getGroupBy());
            a.setFilters(r.getFilters());
            a.setMetrics(r.getMetrics());

            return a;

        } catch (Exception e) {
            throw new DataQueryAnalyticsException(
                    "Error mapping analytics request for table: " + r.getTableName(),
                    e
            );
        }
    }

    private void validate(DataAnalyticsRequest request) {

        try {
            if (request.getTableName() == null || request.getTableName().isBlank()) {
                throw new IllegalArgumentException("Table name is required");
            }

            Set<String> numericCols = schemaCache.getNumericColumns(request.getTableName());

            if (request.getMetrics() != null) {
                for (MetricRequest metric : request.getMetrics()) {
                    if (metric.getType() == QueryType.AVG
                            || metric.getType() == QueryType.SUM
                            || metric.getType() == QueryType.MIN
                            || metric.getType() == QueryType.MAX) {

                        if (!numericCols.contains(metric.getField())) {
                            throw new IllegalArgumentException(
                                    "Field " + metric.getField() + " is not numeric"
                            );
                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new DataQueryAnalyticsException(
                    "Error validating analytics request for table: " + request.getTableName(),
                    e
            );
        }
    }
}