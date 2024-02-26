package saturncode.report.inventories.listeners;

import lombok.val;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import saturncode.report.ConfigManager;
import saturncode.report.SaturnReport;
import saturncode.report.connections.api.PlayerReportAPI;
import saturncode.report.dao.PlayerReportDAO;
import saturncode.report.utils.SaturnUtils;

public class ClickInventoryListener implements Listener {

    protected SaturnReport main;

    private ConfigManager configManager;

    private PlayerReportAPI playerReportAPI;

    public ClickInventoryListener(SaturnReport main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        configManager = main.getConfigManager();

        playerReportAPI = main.getPlayerReportAPI();
    }

    @EventHandler
    void onClick(InventoryClickEvent e) {
        val title = e.getView().getTitle();
        val item = e.getCurrentItem();
        val player = (Player)e.getWhoClicked();
        val click = e.getClick();

        if (!title.equals(configManager.getInventoryName().replace("&", "§"))) return;
        e.setCancelled(true);

        if (item == null || item.getType() == Material.AIR) return;

        val reports = PlayerReportDAO.findReportById(itemCompound.getInt("SaturnReport-ID"));
        val reported = reports.getPlayerReported();

        if (click == ClickType.LEFT) {
            if (reported == null) {
                SaturnUtils.sendMessage(player, configManager.getPlayerNotFound());
                player.closeInventory();
            }

            player.teleport(reported);
            player.closeInventory();
            SaturnUtils.sendMessage(player, configManager.getReportTeleport().replace("{reportado}", reports.getReported()));
            playerReportAPI.removeReport(reports);

        } else if (click == ClickType.RIGHT) {
            player.closeInventory();
            SaturnUtils.sendMessage(player, configManager.getReportDeleted().replace("{reportado}", reports.getReported()));
            playerReportAPI.removeReport(reports);
        }
    }
}