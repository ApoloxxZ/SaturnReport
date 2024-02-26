package saturncode.report.commands;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import saturncode.report.ConfigManager;
import saturncode.report.SaturnReport;
import saturncode.report.connections.api.PlayerReportAPI;
import saturncode.report.dao.PlayerReportDAO;
import saturncode.report.model.PlayerReport;
import saturncode.report.utils.SaturnUtils;

public class ReportCommand implements CommandExecutor {

    private final ConfigManager configManager = SaturnReport.getInstance().getConfigManager();

    private final PlayerReportAPI playerReportAPI = SaturnReport.getInstance().getPlayerReportAPI();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] a) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§c[ ! ] O console não executa esse comando.");
            return true;
        }

        val player = (Player)sender;
        if (a.length > 0) {
            if (a.length < 2) {
                sender.sendMessage("§c[ ! ] Utilize /reportar (jogador) (motivo)");
                return true;
            }

            val target = Bukkit.getPlayerExact(a[0]);
            if (target == null) {
                SaturnUtils.sendMessage(player, configManager.getPlayerNotFound());
                return true;
            }

            if (target == player) {
                SaturnUtils.sendMessage(player, configManager.getReportYou());
                return true;
            }

            if (target.hasPermission("saturnreport.imune")) {
                SaturnUtils.sendMessage(player, configManager.getPlayerImmune());
                return true;
            }

            val id = PlayerReportDAO.getPlayerReports().size() + 1;

            val report = new PlayerReport(id, player.getName(), target.getName(), a[1]);
            playerReportAPI.saveReport(report);

            SaturnUtils.sendMessage(player, configManager.getPlayerReported().replace("{reportado}", target.getName()).replace("{motivo}", a[1]));

            if (player.hasPermission("saturnreport.viewreport")) {
                for (val message : configManager.getReceivedReport()) {
                    player.sendMessage(message.replace("&", "§"));
                }
            }

        } else {
            sender.sendMessage("§c[ ! ] Utilize /reportar (jogador) (motivo)");
        }
        return false;
    }
}