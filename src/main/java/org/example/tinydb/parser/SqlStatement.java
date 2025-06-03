package org.example.tinydb.parser;

public interface SqlStatement {
    StatementType getType();

    enum StatementType {
        CREATE, INSERT, SELECT, UNKNOWN
    }
}
