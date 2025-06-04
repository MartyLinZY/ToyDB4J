package org.example.tinydb.parser;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqlParserTest {

    @Test
    public void testParseCreateTable() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("CREATE TABLE users (id INT, name TEXT);");

        assertTrue(stmt instanceof CreateTableStatement);
        CreateTableStatement create = (CreateTableStatement) stmt;
        assertEquals("users", create.getTableName());
        assertEquals(2, create.getColumns().size());
        assertEquals("id", create.getColumns().get(0).getName());
        assertEquals("INT", create.getColumns().get(0).getType());
    }

    @Test
    public void testParseInsert() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO users VALUES (1, 'Alice');");

        assertTrue(stmt instanceof InsertStatement);
        InsertStatement insert = (InsertStatement) stmt;
        assertEquals("users", insert.getTableName());
        assertEquals(List.of("1", "Alice"), insert.getValues());
    }
}
