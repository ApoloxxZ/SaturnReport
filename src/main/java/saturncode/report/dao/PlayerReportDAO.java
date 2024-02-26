package saturncode.report.dao;

import lombok.Getter;
import saturncode.report.model.PlayerReport;

import java.util.ArrayList;
import java.util.List;

public class PlayerReportDAO {

    @Getter
    private static List<PlayerReport> playerReports = new ArrayList<>();

    public static PlayerReport findReportById(int id) {
        return playerReports.stream().filter(playerReport -> playerReport.getId() == id).findFirst().orElse(null);
    }
}