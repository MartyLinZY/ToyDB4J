package org.example.tinydb.parser;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {

    public SqlStatement parse(String sql) {
        sql = sql.trim();

        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            return parseCreateTable(sql);
        }

        String finalSql = sql;
        return new SqlStatement() {
            @Override
            public StatementType getType() {
                return StatementType.UNKNOWN;
            }

            @Override
            public String toString() {
                return "Unknown statement: " + finalSql;
            }
        };
    }

    private CreateTableStatement parseCreateTable(String sql) {
        // 例：CREATE TABLE users (id INT, name TEXT)
        String upper = sql.toUpperCase();
        int start = upper.indexOf("TABLE") + "TABLE".length();
        int openParen = sql.indexOf("(", start);
        String tableName = sql.substring(start, openParen).trim();

        int closeParen = sql.lastIndexOf(")");
        String colDefStr = sql.substring(openParen + 1, closeParen).trim();

        String[] colDefs = colDefStr.split(",");
        List<CreateTableStatement.ColumnDef> columns = new ArrayList<>();

        for (String col : colDefs) {
            String[] parts = col.trim().split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid column definition: " + col);
            }
            columns.add(new CreateTableStatement.ColumnDef(parts[0], parts[1]));
        }

        return new CreateTableStatement(tableName, columns);
    }
}
