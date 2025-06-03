package org.example.tinydb.parser;

import java.util.List;

public class CreateTableStatement implements SqlStatement {
    private final String tableName;
    private final List<ColumnDef> columns;

    public CreateTableStatement(String tableName, List<ColumnDef> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    @Override
    public StatementType getType() {
        return StatementType.CREATE;
    }

    public static class ColumnDef {
        private final String name;
        private final String type;

        public ColumnDef(String name, String type) {
            this.name = name;
            this.type = type.toUpperCase(); // e.g. INT, TEXT
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return name + " " + type;
        }
    }
}
