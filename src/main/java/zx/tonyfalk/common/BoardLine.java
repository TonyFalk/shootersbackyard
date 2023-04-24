package zx.tonyfalk.common;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;
import zx.tonyfalk.ShootersBackYard;

public class BoardLine {

    private final ChatColor color;
    private final int line;
    private final Team team;

    public BoardLine(ChatColor color, int line, Team team) {
        this.color = color;
        this.line = line;
        this.team = team;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getLine() {
        return line;
    }

    public Team getTeam() {
        return team;
    }

}
