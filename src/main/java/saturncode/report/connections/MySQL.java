package saturncode.report.connections;

import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import saturncode.report.SaturnReport;
import saturncode.report.connections.model.DBModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL implements DBModel {

    @Getter
    private Connection connection;

    private final FileConfiguration config;

    public MySQL(SaturnReport main) {
        config = main.getConfig();

        openConnection();
        createTables();
    }



    public void openConnection() {
        String host = config.getString("MySQL.Host");
        String user = config.getString("MySQL.User");
        String password = config.getString("MySQL.Password");
        String db = config.getString("MySQL.Database");
        String url = "jdbc:mysql://" + host + "/" + db + "?autoReconnect=true";
        try {
            connection = DriverManager.getConnection(url, user, password);
            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §aA conexão com §eMySQL §afoi iniciado com sucesso.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §cHouve um erro ao fazer conexão com §eMySQL§c.");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §aA conexão com §eMySQL §afoi fechada com sucesso.");
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
        executeUpdate("CREATE TABLE IF NOT EXISTS `saturnreport_reports` (report_id INT NOT NULL, player VARCHAR(24) NOT NULL, reported VARCHAR(24), reason TEXT NOT NULL)");
    }
}