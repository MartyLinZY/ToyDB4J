package org.example.tinydb;

import org.example.tinydb.parser.CreateTableStatement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
}
