package com.jann.csv_data_hub.mapper;

import com.jann.csv_data_hub.exception.error.SqlTypeMappingException;

public class SqlTypeMapper {

    public static int toSqlType(String type) {

        String normalized = type.toLowerCase().trim();

        return switch (normalized) {
            case "int", "integer" -> java.sql.Types.INTEGER;
            case "bigint" -> java.sql.Types.BIGINT;
            case "double", "float", "double precision" -> java.sql.Types.DOUBLE;
            case "boolean", "bool" -> java.sql.Types.BOOLEAN;
            case "date" -> java.sql.Types.DATE;
            case "timestamp", "timestamp without time zone" -> java.sql.Types.TIMESTAMP;
            case "text", "varchar", "varchar(255)", "char" -> java.sql.Types.VARCHAR;

            default -> throw new SqlTypeMappingException("Unsupported column type: " + type);
        };
    }

    public static Object convertSqlType(String value, int sqlType) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return switch (sqlType) {
                case java.sql.Types.INTEGER -> Integer.parseInt(value);
                case java.sql.Types.BIGINT -> Long.parseLong(value);
                case java.sql.Types.DOUBLE -> Double.parseDouble(value);
                case java.sql.Types.BOOLEAN -> Boolean.parseBoolean(value);
                case java.sql.Types.DATE -> java.sql.Date.valueOf(value);
                case java.sql.Types.TIMESTAMP -> java.sql.Timestamp.valueOf(value);
                default -> value;
            };
        } catch (Exception e) {
            throw new SqlTypeMappingException("Invalid value: " + value, e);
        }
    }
}