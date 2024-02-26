package saturncode.report.connections.transform;

import lombok.val;
import saturncode.report.SaturnReport;
import saturncode.report.connections.model.DBModel;
import saturncode.report.dao.PlayerReportDAO;
import saturncode.report.model.PlayerReport;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerReportTransform {

    protected SaturnReport main;

    private DBModel dbModel;

    public PlayerReportTransform(SaturnReport main) {
        this.main = main;

        dbModel = main.getDbModel();
    }

    public PlayerReport ticketTransform(ResultSet rs) throws SQLException {
        val report_id = rs.getInt("report_id");
        val player = rs.getString("player");
        val reported = rs.getString("reported");
        val reason = rs.getString("reason");

        return new PlayerReport(report_id, player, reported, reason);

    }

    public void loadReports() {
        try (val ps = dbModel.getConnection().prepareStatement("SELECT * FROM `saturnreport_reports`")) {
            val rs = ps.executeQuery();

            while (rs.next()) {
                val reports = ticketTransform(rs);
                if (reports == null)
                    continue;

                PlayerReportDAO.getPlayerReports().add(reports);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}