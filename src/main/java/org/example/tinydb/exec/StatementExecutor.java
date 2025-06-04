package org.example.tinydb.exec;

import org.example.tinydb.parser.SqlStatement;

public interface StatementExecutor {
    void execute(SqlStatement stmt);
}
