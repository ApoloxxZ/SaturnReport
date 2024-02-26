package saturncode.report.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class PlayerReport {

    private int id;
    private String player, reported, reason;

    public Player getPlayerReported() {
        return Bukkit.getPlayer(reported);
    }
}