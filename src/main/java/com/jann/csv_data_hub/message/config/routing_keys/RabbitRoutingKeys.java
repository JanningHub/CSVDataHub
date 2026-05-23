package com.jann.csv_data_hub.message.config.routing_keys;

public final class RabbitRoutingKeys {

    private RabbitRoutingKeys() {}

    public static final String CSV_INGESTION = "csv.ingestion";
    public static final String TABLE_CREATE = "csv.table.create";
    public static final String TABLE_DELETE = "csv.table.delete";
    public static final String TABLE_QUERY  = "csv.table.query";

    public static String dlq(String key) {
        return key + ".dlq";
    }
}