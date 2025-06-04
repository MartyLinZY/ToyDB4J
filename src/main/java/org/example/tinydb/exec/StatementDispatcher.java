package org.example.tinydb.exec;

import org.example.tinydb.parser.SqlStatement;

import java.util.HashMap;
import java.util.Map;

public class StatementDispatcher {

    private final Map<SqlStatement.StatementType, StatementExecutor> executors = new HashMap<>();

    public StatementDispatcher() {
        executors.put(SqlStatement.StatementType.CREATE, new CreateTableExecutor());
        executors.put(SqlStatement.StatementType.INSERT, new InsertExecutor());
        executors.put(SqlStatement.StatementType.SELECT, new SelectExecutor());
        // 后续可加入 SELECT、UPDATE、DELETE 等
    }

    public void dispatch(SqlStatement stmt) {
        StatementExecutor executor = executors.get(stmt.getType());
        if (executor != null) {
            executor.execute(stmt);
        } else {
            System.out.println("Unsupported statement type: " + stmt.getType());
        }
    }
}
