package saturncode.report;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import saturncode.report.commands.ReportCommand;
import saturncode.report.commands.ReportsCommand;
import saturncode.report.connections.MySQL;
import saturncode.report.connections.SQLite;
import saturncode.report.connections.api.PlayerReportAPI;
import saturncode.report.connections.model.DBModel;
import saturncode.report.connections.transform.PlayerReportTransform;
import saturncode.report.inventories.listeners.ClickInventoryListener;
import saturncode.report.utils.DateManager;

@Getter
public class SaturnReport extends JavaPlugin {

    @Getter
    private static SaturnReport Instance;
    private ConfigManager configManager;

    private DBModel dbModel;
    private PlayerReportAPI playerReportAPI;

    public void onEnable() {
        Instance = this;
        verifySaturnCore();
        registerYaml();
        registerConnections();
        registerCommands();
        registerEvents();

        sendMessage();

    }

    public void onDisable() {
        dbModel.closeConnection();
    }

    private void verifySaturnCore() {
        if (Bukkit.getPluginManager().getPlugin("SaturnCore") == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage("§a[SaturnReport] §cO plugin §bSaturnCore §cnão foi encontrado.");
        }
    }

    private void registerCommands() {
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());
    }

    private void registerConnections() {
        dbModel = !getConfig().getBoolean("MySQL.Ativar") ? new SQLite() : new MySQL(this);
        playerReportAPI = new PlayerReportAPI(this);

        new PlayerReportTransform(this).loadReports();
    }

    private void registerEvents() {
        new ClickInventoryListener(this);
    }

    private void registerYaml() {
        configManager = new ConfigManager();
        configManager.loadConfig();
        saveDefaultConfig();
        DateManager.createFolder("cache");
        DateManager.createConfig("reportes");
    }

    private void sendMessage() {
        Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §fCriado por §b[Apolo]");
        Bukkit.getConsoleSender().sendMessage("§b[SaturnReport] §aO plugin foi iniciado com sucesso.");
    }
}