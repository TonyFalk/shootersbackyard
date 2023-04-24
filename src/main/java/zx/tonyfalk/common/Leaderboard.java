package zx.tonyfalk.common;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leaderboard {
    ShootersBackYard plugin;

    public Leaderboard(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    private static final List<ChatColor> colors = Arrays.asList(ChatColor.values());
    private Player player;
    private Scoreboard scoreboard;
    private Objective objective;

    List<String> teams = new ArrayList<String>();
    private final List<BoardLine> boardLines = new ArrayList<>();

    public void ResetLeaderboard() {
        for (int i = 0; i < boardLines.size(); i++) {
            if (boardLines.get(i) != null) {
                removeLine(i);
            }
        }
    }

    public void UpdateLeaderboard(ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) setValue(i, lines.get(i));
        player.setScoreboard(scoreboard);
        UpdatePlayerTeams();
        this.plugin.sctManager.UpdateAllPlayerNames();
    }

    public Leaderboard(String displayName, ArrayList<String> lines, Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("Scoreboard", "dummy");
        objective.setDisplayName(displayName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < colors.size(); i++) {
            final ChatColor color = colors.get(i);
            final Team team = scoreboard.registerNewTeam("line" + i);
            team.addEntry(color.toString());
            boardLines.add(new BoardLine(color, i, team));
        }
        playerTeam();
    }

    public void playerTeam() {
        teams = new ArrayList<String>();
        teams.add(0, "§9"); teams.add(1, "§c"); teams.add(2, "§7§l"); teams.add(3, "§a"); teams.add(4, "§7");

        for (int i = 0; i < teams.size(); i++) {
            Team team = scoreboard.registerNewTeam(teams.get(i));
            team.setPrefix(Color.set(teams.get(i)));
        }
        UpdatePlayerTeams();
    }

    public void UpdatePlayerTeams() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int index = 4;
            switch (this.plugin.gameManager.teams.get(player)){
                case Red -> {
                    index = 1;
                }
                case Blue -> {
                    index = 0;
                }
                case None -> {
                    index = 2;
                }
                case Infected -> {
                    index = 3;
                }
                default -> {
                    index = 4;
                }
            }
            Team team = scoreboard.getTeam(teams.get(index));
            team.addEntry(player.getName());
        }
    }

    public String PlayerTeamsString(Player player) {
            int index = 4;
            switch (this.plugin.gameManager.teams.get(player)) {
                case Red -> {
                    index = 1;
                }
                case Blue -> {
                    index = 0;
                }
                case None -> {
                    index = 2;
                }
                case Infected -> {
                    index = 3;
                }
                default -> {
                    index = 4;
                }
            }
            return teams.get(index);
    }

    public void setValue(int line, String value) {
        final BoardLine boardLine = getBoardLine(line);
        objective.getScore(boardLine.getColor().toString()).setScore(line);
        boardLine.getTeam().setPrefix(value);
    }

    public void removeLine(int line) {
        final BoardLine boardLine = getBoardLine(line);
        scoreboard.resetScores(boardLine.getColor().toString());
    }

    private BoardLine getBoardLine(int line) {
        return boardLines.stream().filter(boardLine -> boardLine.getLine() == line).findFirst().orElse(null);
    }

}

