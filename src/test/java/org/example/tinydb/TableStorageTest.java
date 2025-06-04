package org.example.tinydb;

import org.example.tinydb.parser.CreateTableStatement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableStorageTest {

    private static final String TABLE_DIR = "tables";
    private static final String TABLE = "users";

    @AfterEach
    public void cleanUp() {
        new File(TABLE_DIR, TABLE + ".meta").delete();
        new File(TABLE_DIR, TABLE + ".data").delete();
    }

    @Test
    public void testSaveMeta() throws Exception {
        CreateTableStatement stmt = new CreateTableStatement(
                TABLE,
                List.of(
                        new CreateTableStatement.ColumnDef("id", "INT"),
                        new CreateTableStatement.ColumnDef("name", "TEXT")
                )
        );
        TableStorage.saveTableDefinition(stmt);

        File meta = new File(TABLE_DIR, TABLE + ".meta");
        assertTrue(meta.exists());

        List<String> lines = Files.readAllLines(meta.toPath());
        assertEquals("id INT", lines.get(0));
        assertEquals("name TEXT", lines.get(1));
    }

    @Test
    public void testAppendRow() throws Exception {
        TableStorage.appendRow(TABLE, List.of("1", "Alice"));

        File data = new File(TABLE_DIR, TABLE + ".data");
        assertTrue(data.exists());

        List<String> lines = Files.readAllLines(data.toPath());
        assertEquals("1\tAlice", lines.get(0));
    }
}
