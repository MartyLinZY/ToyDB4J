package org.example.tinydb.exec;

import org.example.tinydb.TableStorage;
import org.example.tinydb.parser.SelectStatement;
import org.example.tinydb.parser.SqlStatement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class SelectExecutor implements StatementExecutor {

    private static final String TABLE_DIR = "tables";

    @Override
    public void execute(SqlStatement stmt) {
        if (!(stmt instanceof SelectStatement selectStmt)) return;

        String table = selectStmt.getTableName();
        File metaFile = new File(TABLE_DIR, table + ".meta");
        File dataFile = new File(TABLE_DIR, table + ".data");

        if (!metaFile.exists()) {
            System.out.println("Table does not exist: " + table);
            return;
        }

        try {
            // 读取字段名
            List<String> columns = new java.util.ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(metaFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    columns.add(parts[0]);
                }
            }

            // 打印标题
            System.out.println(String.join("\t", columns));
            System.out.println("-".repeat(columns.size() * 8));

            // 读取数据
            // 显示数据行
            if (dataFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = line.split("\t");

                        if (selectStmt.hasWhereClause()) {
                            String filterCol = selectStmt.getWhereColumn();
                            String filterVal = selectStmt.getWhereValue();
                            int colIndex = columns.indexOf(filterCol);

                            if (colIndex == -1) {
                                System.err.println("Column not found in WHERE: " + filterCol);
                                return;
                            }

                            if (!fields[colIndex].equals(filterVal)) {
                                continue; // 不满足条件，跳过
                            }
                        }

                        System.out.println(line);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("SELECT failed: " + e.getMessage());
        }
    }
}
