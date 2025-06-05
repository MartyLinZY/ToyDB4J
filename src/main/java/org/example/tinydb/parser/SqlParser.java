package org.example.tinydb.parser;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {

    public SqlStatement parse(String sql) {
        sql = sql.trim();

        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            return parseCreateTable(sql);
        } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
            return parseInsert(sql);
        }else if (sql.toUpperCase().startsWith("SELECT")) {
            return parseSelect(sql);
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

    private InsertStatement parseInsert(String sql) {
        // INSERT INTO users VALUES (1, 'Alice')
        String upper = sql.toUpperCase();
        int intoIndex = upper.indexOf("INTO") + "INTO".length();
        int valuesIndex = upper.indexOf("VALUES");

        String tableName = sql.substring(intoIndex, valuesIndex).trim();
        int open = sql.indexOf("(", valuesIndex);
        int close = sql.lastIndexOf(")");

        String valuesStr = sql.substring(open + 1, close).trim();
        String[] rawValues = valuesStr.split(",");

        List<String> values = new ArrayList<>();
        for (String v : rawValues) {
            values.add(v.trim().replaceAll("^'(.*)'$", "$1")); // 去除引号
        }

        return new InsertStatement(tableName, values);
    }
    private SelectStatement parseSelect(String sql) {
        // 仅支持：SELECT * FROM users [WHERE col = value]
        String upper = sql.toUpperCase();
        if (!upper.startsWith("SELECT * FROM")) {
            throw new IllegalArgumentException("Only SELECT * FROM <table> supported for now");
        }

        String[] parts = sql.split("(?i)WHERE");

        String fromPart = parts[0].substring("SELECT * FROM".length()).trim();
        if (parts.length == 1) {
            return new SelectStatement(fromPart); // 无 WHERE
        }

        // 解析 WHERE col = val
        String wherePart = parts[1].trim(); // e.g., "id = 1"
        String[] condition = wherePart.split("=");
        if (condition.length != 2) {
            throw new IllegalArgumentException("Malformed WHERE clause: " + wherePart);
        }

        String column = condition[0].trim();
        String value = condition[1].trim().replaceAll("^'(.*)'$", "$1");

        return new SelectStatement(fromPart, column, value);
    }


}
