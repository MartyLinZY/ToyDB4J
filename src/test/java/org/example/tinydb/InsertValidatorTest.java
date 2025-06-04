package org.example.tinydb;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsertValidatorTest {

    private static final String TABLE_DIR = "tables";
    private static final String TABLE = "users";

    private ByteArrayOutputStream out;

    @BeforeEach
    public void setup() throws Exception {
        // 创建目录 & 表结构
        File dir = new File(TABLE_DIR);
        if (!dir.exists()) dir.mkdirs();

        File meta = new File(TABLE_DIR, TABLE + ".meta");
        try (FileWriter writer = new FileWriter(meta)) {
            writer.write("id INT\n");
            writer.write("name TEXT\n");
        }

        // 删除旧数据
        new File(TABLE_DIR, TABLE + ".data").delete();

        // 重定向输出
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));
    }

    @AfterEach
    public void cleanup() {
        new File(TABLE_DIR, TABLE + ".meta").delete();
        new File(TABLE_DIR, TABLE + ".data").delete();

        // 恢复标准输出（可选）
        System.setOut(System.out);
        System.setErr(System.err);
    }

    private String runSql(String... sqlLines) {
        String joined = String.join("\n", sqlLines) + "\nEXIT;\n";
        System.setIn(new ByteArrayInputStream(joined.getBytes()));

        TinyDB.main(new String[0]);
        return out.toString();
    }

    @Test
    @Order(1)
    public void testInsertTooFewColumns() {
        String output = runSql("INSERT INTO users VALUES (1);");
        assertTrue(output.contains("Column count mismatch"), "Expected column count error");
    }

    @Test
    @Order(2)
    public void testInsertTooManyColumns() {
        String output = runSql("INSERT INTO users VALUES (1, 'Alice', 'Extra');");
        assertTrue(output.contains("Column count mismatch"), "Expected column count error");
    }

    @Test
    @Order(3)
    public void testInsertWrongType() {
        String output = runSql("INSERT INTO users VALUES ('Bob', 'Alice');");
        assertTrue(output.contains("Invalid INT value"), "Expected type mismatch error");
    }

    @Test
    @Order(4)
    public void testInsertValidRow() throws Exception {
        String output = runSql("INSERT INTO users VALUES (1, 'Alice');");
        assertTrue(output.contains("1 row inserted"), "Expected insert success");

        File dataFile = new File(TABLE_DIR, TABLE + ".data");
        assertTrue(dataFile.exists(), "Data file should exist");

        List<String> lines = Files.readAllLines(dataFile.toPath());
        assertEquals(1, lines.size());
        assertEquals("1\tAlice", lines.get(0));
    }
}
