package saturncode.report.inventories;

import lombok.val;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import saturncode.core.inventories.Scroller;
import saturncode.core.items.ItemBuilder;
import saturncode.report.ConfigManager;
import saturncode.report.SaturnReport;
import saturncode.report.dao.PlayerReportDAO;
import saturncode.report.utils.SaturnUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsInventory {

    private final ConfigManager configManager = SaturnReport.getInstance().getConfigManager();

    public void open(Player player) {

        List<ItemStack> reportItens = new ArrayList<>();
        PlayerReportDAO.getPlayerReports().forEach(playerReport -> {

            val author = configManager.getReportTitle().replace("{vitima}", playerReport.getPlayer());
            List<String> lore = configManager.getDescriptionReport();
            lore = lore.stream().map(l -> l.replace("{reportado}", playerReport.getReported()).replace("{motivo}", playerReport.getReason())).collect(Collectors.toList());

            val icon = new ItemBuilder(SaturnUtils.getPlayerHead(playerReport.getReported())).setName(author).setLore(lore).build();

            val nmsItem = CraftItemStack.asNMSCopy(icon);
            val itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

            itemCompound.setInt("SaturnReport-ID", playerReport.getId());
            nmsItem.setTag(itemCompound);

            reportItens.add(CraftItemStack.asBukkitCopy(nmsItem));

        });

        val scroller = new Scroller.ScrollerBuilder().withName(configManager.getInventoryName().replace("&", "§")).withSize(configManager.getInventorySize()).withAllowedSlots(configManager.getInventorySlots()).withItems(reportItens).build();
        scroller.open(player);
    }
}