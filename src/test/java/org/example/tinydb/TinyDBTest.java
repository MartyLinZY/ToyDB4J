package org.example.tinydb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TinyDBTest {

    @Test
    public void testHelpAndExit() {
        // 模拟用户输入：HELP; 换行 EXIT;
        String input = "HELP;\nEXIT;\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // 捕获输出
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // 运行主函数（REPL 模式）
        TinyDB.main(new String[0]);

        // 提取输出内容
        String output = out.toString();

        // 验证包含 HELP 内容
        assertTrue(output.contains("Available commands"));
        assertTrue(output.contains("HELP"));
        assertTrue(output.contains("EXIT"));
        assertTrue(output.contains("Bye!"));

        // 恢复标准输入输出
        System.setIn(System.in);
        System.setOut(System.out);
    }
    private static final String TABLE_DIR = "tables";

    @AfterEach
    public void cleanUp() {
        // 测试后删除生成的文件
        File meta = new File(TABLE_DIR, "users.meta");
        File data = new File(TABLE_DIR, "users.data");
        meta.delete();
        data.delete();
    }

    @Test
    public void testCreateInsertSelect() {
        String input = String.join("\n",
                "CREATE TABLE users (id INT, name TEXT);",
                "INSERT INTO users VALUES (1, 'Alice');",
                "INSERT INTO users VALUES (2, 'Bob');",
                "SELECT * FROM users;",
                "EXIT;"
        );

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TinyDB.main(new String[0]);

        String output = out.toString();

        // 验证表结构、数据是否输出正确
        assertTrue(output.contains("Parsed CREATE TABLE: users"));
        assertTrue(output.contains("1 row inserted into users"));
        assertTrue(output.contains("id\tname"));
        assertTrue(output.contains("1\tAlice"));
        assertTrue(output.contains("2\tBob"));
        assertTrue(output.contains("Bye!"));
    }

}
