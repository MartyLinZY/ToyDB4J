package org.example.tinydb;

import org.example.tinydb.parser.CreateTableStatement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TableStorage {

    private static final String TABLE_DIR = "./tables";

    public static void saveTableDefinition(CreateTableStatement stmt) {
        try {
            File dir = new File(TABLE_DIR);
            if (!dir.exists()) {
                dir.mkdirs(); // 自动创建 tables 文件夹
            }

            File file = new File(dir, stmt.getTableName() + ".meta");
            FileWriter writer = new FileWriter(file);

            for (CreateTableStatement.ColumnDef col : stmt.getColumns()) {
                writer.write(col.getName() + " " + col.getType() + "\n");
            }

            writer.close();
            System.out.println("Table '" + stmt.getTableName() + "' saved to file.");
        } catch (IOException e) {
            System.err.println("Failed to save table definition: " + e.getMessage());
        }
    }

    public static void appendRow(String tableName, List<String> values) {
        try {
            File file = new File(TABLE_DIR, tableName + ".data");
            FileWriter writer = new FileWriter(file, true); // append 模式

            writer.write(String.join("\t", values) + "\n");
            writer.close();

            System.out.println("1 row inserted into " + tableName);
        } catch (IOException e) {
            System.err.println("Failed to insert data: " + e.getMessage());
        }
    }
    public static List<CreateTableStatement.ColumnDef> loadTableSchema(String tableName) throws IOException {
        File metaFile = new File(TABLE_DIR, tableName + ".meta");
        List<CreateTableStatement.ColumnDef> schema = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(metaFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    schema.add(new CreateTableStatement.ColumnDef(parts[0], parts[1]));
                }
            }
        }
        return schema;
    }

}
