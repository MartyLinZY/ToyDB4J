package org.example.tinydb.parser;

public class SelectStatement implements SqlStatement {
    private final String tableName;
    private final String whereColumn;  // 可为 null
    private final String whereValue;   // 可为 null

    public SelectStatement(String tableName) {
        this(tableName, null, null);
    }

    public SelectStatement(String tableName, String whereColumn, String whereValue) {
        this.tableName = tableName;
        this.whereColumn = whereColumn;
        this.whereValue = whereValue;
    }

    public String getTableName() {
        return tableName;
    }

    public String getWhereColumn() {
        return whereColumn;
    }

    public String getWhereValue() {
        return whereValue;
    }

    @Override
    public StatementType getType() {
        return StatementType.SELECT;
    }

    public boolean hasWhereClause() {
        return whereColumn != null && whereValue != null;
    }
}
