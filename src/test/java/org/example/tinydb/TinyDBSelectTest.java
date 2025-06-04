package org.example.tinydb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TinyDBSelectTest {

    private static final String TABLE_DIR = "tables";

    @AfterEach
    public void cleanUp() {
        new File(TABLE_DIR, "users.meta").delete();
        new File(TABLE_DIR, "users.data").delete();
    }

    @Test
    public void testSelectOutput() {
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

        assertTrue(output.contains("id\tname"));
        assertTrue(output.contains("1\tAlice"));
        assertTrue(output.contains("2\tBob"));
    }
}
