package org.example.tinydb.exec;

import org.example.tinydb.TableStorage;
import org.example.tinydb.parser.CreateTableStatement;
import org.example.tinydb.parser.InsertStatement;
import org.example.tinydb.parser.SqlStatement;

import java.io.IOException;
import java.util.List;

public class InsertExecutor implements StatementExecutor {
    @Override
    public void execute(SqlStatement stmt) {
        if (!(stmt instanceof InsertStatement insertStmt)) return;

        String table = insertStmt.getTableName();
        List<String> values = insertStmt.getValues();

        try {
            List<CreateTableStatement.ColumnDef> schema = TableStorage.loadTableSchema(table);

            // 校验字段数量
            if (schema.size() != values.size()) {
                System.err.printf("Column count mismatch: expected %d, got %d%n",
                        schema.size(), values.size());
                return;
            }

            // 校验字段类型
            for (int i = 0; i < schema.size(); i++) {
                String expectedType = schema.get(i).getType();
                String raw = values.get(i);

                if (expectedType.equals("INT")) {
                    try {
                        Integer.parseInt(raw);
                    } catch (NumberFormatException e) {
                        System.err.printf("Invalid INT value: %s for column %s%n",
                                raw, schema.get(i).getName());
                        return;
                    }
                } else if (expectedType.equals("TEXT")) {
                    // 目前 TEXT 默认接受所有字符串
                    // 可扩展：限制长度或格式
                } else {
                    System.err.printf("Unknown type %s%n", expectedType);
                    return;
                }
            }

            TableStorage.appendRow(table, values);
        } catch (IOException e) {
            System.err.println("INSERT failed: " + e.getMessage());
        }
    }
}
