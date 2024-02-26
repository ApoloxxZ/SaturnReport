package saturncode.report.connections.model;

import java.sql.Connection;

public interface DBModel {

    void openConnection();

    void closeConnection();

    void executeUpdate(String paramString, Object... paramVarArgs);

    void createTables();

    Connection getConnection();

}
