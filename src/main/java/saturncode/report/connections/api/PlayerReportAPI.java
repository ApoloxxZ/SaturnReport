package saturncode.report.connections.api;

import saturncode.report.SaturnReport;
import saturncode.report.connections.model.DBModel;
import saturncode.report.dao.PlayerReportDAO;
import saturncode.report.model.PlayerReport;

public class PlayerReportAPI {

    protected SaturnReport main;

    private DBModel dbModel;

    public PlayerReportAPI(SaturnReport main) {
        this.main = main;

        dbModel = main.getDbModel();
    }

    public void saveReport(PlayerReport playerReport) {
        dbModel.executeUpdate("INSERT INTO saturnreport_reports (report_id, player, reported, reason) VALUES(?,?,?,?)", playerReport.getId(), playerReport.getPlayer(), playerReport.getReported(), playerReport.getReason());
        PlayerReportDAO.getPlayerReports().add(playerReport);
    }

    public void removeReport(PlayerReport playerReport) {
        dbModel.executeUpdate("DELETE FROM saturnreport_reports WHERE report_id = ?", playerReport.getId());
        PlayerReportDAO.getPlayerReports().remove(playerReport);
    }
}