package org.example.tinydb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import org.example.tinydb.parser.*;
import org.example.tinydb.exec.StatementDispatcher;
import org.example.tinydb.parser.SqlParser;
import org.example.tinydb.parser.SqlStatement;

public class TinyDB {
    private static final Logger logger = LoggerFactory.getLogger(TinyDB.class);

    public static void main(String[] args) {
//        if (args.length > 0 && args[0].equals("help-test")) {
//            printHelp();
//            return;
//        }
        SqlParser parser = new SqlParser();
        StatementDispatcher dispatcher = new StatementDispatcher();
        logger.info("Welcome to TinyDB!");
        Scanner scanner = new Scanner(System.in);
        StringBuilder currentStatement = new StringBuilder();

        while (true) {
            System.out.print("tinydb> ");
            String line = scanner.nextLine();

            // 支持多行 SQL 拼接
            currentStatement.append(line.trim()).append(" ");

            if (line.replaceAll(";", "").trim().equalsIgnoreCase("exit")) {
                logger.info("Bye!");
                break;
            }else if (line.replaceAll(";", "").trim().equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }

            if (!line.trim().endsWith(";")) {
                continue; // 等待语句完整
            }
            String sql = currentStatement.toString().trim();
            currentStatement.setLength(0); // 清空语句缓存
            // 去除末尾分号
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length() - 1).trim();
            }


            SqlStatement stmt = parser.parse(sql);
            System.out.println("[DEBUG] Parsed: " + stmt.getClass().getSimpleName());

            dispatcher.dispatch(stmt);

        }

        scanner.close();
    }

    private static void printHelp() {
        System.out.println("Welcome to TinyDB!");
        System.out.println("Available commands:");
        System.out.println("  HELP;          Show this help message");
        System.out.println("  EXIT;          Exit the database shell");
        System.out.println("  CREATE TABLE   Define a new table");
        System.out.println("  INSERT INTO    Add data to a table");
        System.out.println("  SELECT         Query data from a table");
        System.out.println("Note: All SQL statements must end with a semicolon ';'.");
    }

}