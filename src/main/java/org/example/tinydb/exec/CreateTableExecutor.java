package org.example.tinydb.exec;

import org.example.tinydb.TableStorage;
import org.example.tinydb.parser.CreateTableStatement;
import org.example.tinydb.parser.SqlStatement;

public class CreateTableExecutor implements StatementExecutor {
    @Override
    public void execute(SqlStatement stmt) {
        if (stmt instanceof CreateTableStatement createStmt) {
            System.out.println("Parsed CREATE TABLE: " + createStmt.getTableName());
            TableStorage.saveTableDefinition(createStmt);
        }
    }
}
