package saturncode.report.commands;

import lombok.val;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import saturncode.report.inventories.ReportsInventory;
import saturncode.report.utils.SaturnUtils;

public class ReportsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] a) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§c[ ! ] O console não executa esse comando.");
            return true;
        }

        val player = (Player)sender;
        if (!player.hasPermission("saturnreport.viewreports")) {
            sender.sendMessage("§c[ ! ] Você não possui permissão para esse comando.");
            return true;
        }

        new ReportsInventory().open(player);
        SaturnUtils.playSound(player, Sound.CHEST_OPEN);


        return false;
    }
}