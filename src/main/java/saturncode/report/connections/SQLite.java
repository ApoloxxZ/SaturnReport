package saturncode.report.connections;

import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import saturncode.report.connections.model.DBModel;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements DBModel {

    @Getter
    private Connection connection;

    public SQLite() {
        openConnection();
        createTables();
    }

    public void openConnection() {
        val file = new File("plugins/SaturnReport/cache/database.db");
        String url = "jdbc:sqlite:" + file;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);

            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §aA conexão com §eSQLite §afoi iniciada com sucesso.");
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §cHouve um erro ao fazer conexão com o §eSQLite§c.");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §aA conexão com §eSQLite §foi fechada com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String query, Object... params) {
        try (val ps = connection.prepareStatement(query)) {
            if (params != null && params.length > 0)
                for (int index = 0; index < params.length; index++)
                    ps.setObject(index + 1, params[index]);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `saturnreport_reports` (report_id INT NOT NULL, player VARCHAR(24) NOT NULL, reported VARCHAR(24), reason VARCHAR(24) NOT NULL)");
    }
}