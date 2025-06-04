package org.example.tinydb.parser;

import java.util.List;

public class InsertStatement implements SqlStatement {
    private final String tableName;
    private final List<String> values;

    public InsertStatement(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public StatementType getType() {
        return StatementType.INSERT;
    }
}
