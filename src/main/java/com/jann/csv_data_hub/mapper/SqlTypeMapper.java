package com.jann.csv_data_hub.mapper;

import com.jann.csv_data_hub.exception.error.SqlTypeMappingException;

import java.sql.Types;

public class SqlTypeMapper {

    public static int toSqlType(String type) {

        try {
            if (type == null || type.isBlank()) {
                throw new SqlTypeMappingException("Column type is null or blank");
            }

            String normalized = type.toLowerCase().trim();

            return switch (normalized) {
                case "smallint", "int2" -> Types.SMALLINT;
                case "integer", "int", "int4" -> Types.INTEGER;
                case "bigint", "int8", "bigserial", "serial8" -> Types.BIGINT;
                case "serial", "serial4" -> Types.INTEGER;

                case "numeric", "decimal", "money" -> Types.DECIMAL;
                case "real", "float4" -> Types.REAL;
                case "double precision", "float8", "float" -> Types.DOUBLE;

                case "text" -> Types.LONGVARCHAR;
                case "varchar", "character varying", "char", "character" -> Types.VARCHAR;

                case "boolean", "bool" -> Types.BOOLEAN;

                case "date" -> Types.DATE;
                case "time", "time without time zone" -> Types.TIME;
                case "timetz", "time with time zone" -> Types.TIME_WITH_TIMEZONE;
                case "timestamp", "timestamp without time zone" -> Types.TIMESTAMP;
                case "timestamptz", "timestamp with time zone" -> Types.TIMESTAMP_WITH_TIMEZONE;

                case "uuid", "json", "jsonb", "bytea",
                     "point", "line", "lseg", "box", "path", "polygon", "circle"
                        -> Types.OTHER;

                case "int[]", "integer[]", "bigint[]", "text[]", "varchar[]"
                        -> Types.ARRAY;

                default -> throw new SqlTypeMappingException("Unsupported PostgreSQL column type: " + type);
            };

        } catch (Exception e) {
            throw (e instanceof SqlTypeMappingException)
                    ? (SqlTypeMappingException) e
                    : new SqlTypeMappingException("Unexpected error while mapping type: " + type, e);
        }
    }

    public static Object convertSqlType(String value, int sqlType) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return switch (sqlType) {
                case Types.SMALLINT -> Short.parseShort(value);
                case Types.INTEGER -> Integer.parseInt(value);
                case Types.BIGINT -> Long.parseLong(value);
                case Types.REAL -> Float.parseFloat(value);
                case Types.DOUBLE, Types.DECIMAL -> Double.parseDouble(value);

                case Types.BOOLEAN -> Boolean.parseBoolean(value);

                case Types.VARCHAR, Types.LONGVARCHAR -> value;

                case Types.DATE -> java.sql.Date.valueOf(value);
                case Types.TIME -> java.sql.Time.valueOf(value);
                case Types.TIMESTAMP -> java.sql.Timestamp.valueOf(value);

                case Types.OTHER, Types.ARRAY -> value;

                default -> value;
            };
        } catch (Exception e) {
            throw new SqlTypeMappingException("Invalid value: " + value + " for type: " + sqlType, e);
        }
    }
}