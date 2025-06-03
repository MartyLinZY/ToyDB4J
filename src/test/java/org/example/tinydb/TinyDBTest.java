package org.example.tinydb;

import org.junit.jupiter.api.Test;

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
}
