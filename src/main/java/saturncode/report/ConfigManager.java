package saturncode.report;

import lombok.Getter;
import lombok.val;
import saturncode.report.utils.DateManager;

import java.util.List;

@Getter
public class ConfigManager {

    private String playerNotFound, reportYou, playerImmune, playerReported, reportTeleport, reportDeleted, reportTitle, inventoryName;
    private List<String> receivedReport, descriptionReport;
    private int inventorySize;
    private List<Integer> inventorySlots;

    public void loadConfig() {

        val config = SaturnReport.getInstance().getConfig();
        val inventory = DateManager.getConfig("reportes");

        playerNotFound = config.getString("Mensagens.Gerais.JogadorNaoEncontrado");
        reportYou = config.getString("Mensagens.Gerais.ReportarASiMesmo");
        playerImmune = config.getString("Mensagens.Gerais.JogadorImune");
        playerReported = config.getString("Mensagens.Gerais.JogadorReportado");
        reportTeleport = config.getString("Mensagens.Staffs.Teleportado");
        reportDeleted = config.getString("Mensagens.Staffs.ReporteExcluido");
        reportTitle = inventory.getString("Inventario.Itens.Titulo");
        inventoryName = inventory.getString("Inventario.Nome");
        inventorySize = inventory.getInt("Inventario.Tamanho");

        receivedReport = config.getStringList("Mensagens.Staffs.ReporteRebido");
        descriptionReport = inventory.getStringList("Inventario.Itens.Lore");
        inventorySlots = inventory.getIntegerList("Inventario.Slots");

    }
}