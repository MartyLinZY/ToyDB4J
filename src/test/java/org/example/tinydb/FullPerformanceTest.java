package org.example.tinydb;

import org.example.tinydb.exec.StatementDispatcher;
import org.example.tinydb.parser.InsertStatement;
import org.example.tinydb.parser.SelectStatement;
import org.example.tinydb.parser.CreateTableStatement;
import org.example.tinydb.parser.SqlStatement;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("性能测试，默认不执行")
public class FullPerformanceTest {

    private static final String TABLE = "users";
    private static final String TABLE_DIR = "tables";
    private static final int ROWS = 1_000_0000;

    private final StatementDispatcher dispatcher = new StatementDispatcher();

    @BeforeAll
    public static void prepareMeta() throws IOException {
        File dir = new File(TABLE_DIR);
        if (!dir.exists()) dir.mkdirs();

        File metaFile = new File(TABLE_DIR, TABLE + ".meta");
        try (FileWriter writer = new FileWriter(metaFile)) {
            writer.write("id INT\n");
            writer.write("name TEXT\n");
        }

        File dataFile = new File(TABLE_DIR, TABLE + ".data");
        if (dataFile.exists()) {
            dataFile.delete();
        }
    }

    @Test
    public void testInsertAndSelectWithoutRedirection() {
        System.out.println("---- 插入阶段 ----");
        long insertStart = System.currentTimeMillis();

        for (int i = 1; i <= ROWS; i++) {
            InsertStatement insert = new InsertStatement(TABLE, List.of(String.valueOf(i), "name" + i));
            dispatcher.dispatch(insert);

            // 可以每10w条打印进度
            if (i % 100_000 == 0) {
                System.out.println("Inserted " + i + " rows...");
            }
        }

        long insertEnd = System.currentTimeMillis();
        long insertTime = insertEnd - insertStart;
        System.out.println("插入总耗时: " + insertTime + " ms");
        System.out.printf("平均每条: %.4f ms\n", insertTime / (double) ROWS);

        System.out.println("---- 查询阶段 ----");
        long selectStart = System.currentTimeMillis();

        SelectStatement select = new SelectStatement(TABLE, "id", "9999999");
        dispatcher.dispatch(select);

        long selectEnd = System.currentTimeMillis();
        long selectTime = selectEnd - selectStart;
        System.out.println("查询耗时: " + selectTime + " ms");

        // 手动验证控制台有输出 999999\tname999999 即可
        assertTrue(select.getWhereValue().equals("9999999"));
    }
}
