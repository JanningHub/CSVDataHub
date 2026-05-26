package com.jann.csv_data_hub.component.builder;

import com.jann.csv_data_hub.model.analytics.DataAnalyticsRequest;
import com.jann.csv_data_hub.model.analytics.FilterCondition;
import com.jann.csv_data_hub.model.analytics.MetricRequest;
import com.jann.csv_data_hub.model.analytics.QueryType;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyticsQueryBuilder {

    public BuiltQuery build(DataAnalyticsRequest request) {
        List<Object> params = new ArrayList<>();

        String select = buildSelect(request);
        String where = buildWhere(request.getFilters(), params);
        String groupBy = buildGroupBy(request.getGroupBy());
        String orderBy = buildOrderBy(request);

        String sql = "SELECT " + select +
                " FROM " + request.getTableName() +
                where +
                groupBy +
                orderBy;

        return new BuiltQuery(sql, params);
    }

    private String buildSelect(DataAnalyticsRequest request) {
        List<String> parts = new ArrayList<>();

        if (request.getGroupBy() != null) {
            parts.addAll(request.getGroupBy());
        }

        if (request.getMetrics() != null) {

            for (MetricRequest m : request.getMetrics()) {

                String expr = switch (m.getType()) {
                    case COUNT -> "COUNT(*)";
                    case AVG -> "AVG(" + m.getField() + ")";
                    case SUM -> "SUM(" + m.getField() + ")";
                    case MIN -> "MIN(" + m.getField() + ")";
                    case MAX -> "MAX(" + m.getField() + ")";
                };

                if (m.getAlias() != null) {
                    expr += " AS " + m.getAlias();
                }

                parts.add(expr);
            }
        }

        return String.join(", ", parts);
    }

    private String buildWhere(List<FilterCondition> filters, List<Object> params) {
        if (filters == null || filters.isEmpty()) {
            return "";
        }

        List<String> parts = new ArrayList<>();

        for (FilterCondition f : filters) {

            Object value = normalizeValue(f.getField(), f.getValue());

            switch (f.getOperator()) {
                case EQ -> {
                    parts.add(f.getField() + " = ?");
                    params.add(value);
                }
                case NEQ -> {
                    parts.add(f.getField() + " != ?");
                    params.add(value);
                }
                case GT -> {
                    parts.add(f.getField() + " > ?");
                    params.add(value);
                }
                case GTE -> {
                    parts.add(f.getField() + " >= ?");
                    params.add(value);
                }
                case LT -> {
                    parts.add(f.getField() + " < ?");
                    params.add(value);
                }
                case LTE -> {
                    parts.add(f.getField() + " <= ?");
                    params.add(value);
                }
                case LIKE -> {
                    parts.add(f.getField() + " LIKE ?");
                    params.add("%" + f.getValue() + "%");
                }
            }
        }

        return " WHERE " + String.join(" AND ", parts);
    }

    private Object normalizeValue(String field, Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof String)) {
            return value;
        }

        String str = value.toString();

        if (isDateField(field)) {
            return Timestamp.from(Instant.parse(str));
        }

        return value;
    }

    private boolean isDateField(String field) {
        return field.toLowerCase().contains("created_at")
                || field.toLowerCase().contains("updated_at")
                || field.toLowerCase().contains("date")
                || field.toLowerCase().contains("time");
    }

    private String buildGroupBy(List<String> groupBy) {
        if (groupBy == null || groupBy.isEmpty()) {
            return "";
        }

        return " GROUP BY " + String.join(", ", groupBy);
    }

    private String buildOrderBy(DataAnalyticsRequest request) {
        if (request.getMetrics() == null || request.getMetrics().isEmpty()) {
            return "";
        }

        boolean hasCount = request.getMetrics().stream()
                .anyMatch(m -> m.getType() == QueryType.COUNT);

        if (!hasCount) {
            return "";
        }

        return " ORDER BY COUNT(*) DESC";
    }

    public static class BuiltQuery {
        public final String sql;
        public final List<Object> params;

        public BuiltQuery(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }
    }
}