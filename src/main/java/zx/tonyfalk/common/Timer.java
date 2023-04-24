package zx.tonyfalk.common;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import zx.tonyfalk.Enums.GameMode;
import zx.tonyfalk.Enums.GameStage;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.Color;

public class Timer implements Runnable {
    ShootersBackYard plugin;

    public Timer(ShootersBackYard plugin) {
        this.plugin = plugin;
    }

    int Counter = 780;


    @Override
    public void run() {
        if(this.plugin.gameManager.gameStage == null) {
            this.plugin.gameManager.gameStage = GameStage.Setup;
        }
        switch (this.plugin.gameManager.gameStage){
            case Waiting -> {
                if (Counter < 1){
                    Counter = 780;
                }
                if (Bukkit.getOnlinePlayers().size() > 2) {
                    Counter = -1;
                }
                if (Counter < 615);
                Bukkit.broadcastMessage(Color.set(this.plugin.getMsgsConfig().getString("beforestarts".replace("%T%",String.valueOf(Counter - 600)))));
                if (Counter <= 600) {
                    switch (this.plugin.gameManager.runningGame){
                        case DM -> {
                            this.plugin.dmGame.startGame();
                        }
                        case CTF -> {
                            this.plugin.ctfGame.startGame();
                        }
                        case TDM -> {
                            this.plugin.tdmGame.startGame();
                        }
                        default -> {
                            return;
                        }
                    }
                }
            }
            case Running -> {
                Counter = -1;
                if (Counter >= 1){
                    this.plugin.gameManager.startNextGame();
                }
            }
            case Setup -> {
                return;
            }
        }

    }

    public String timeinstring(int timeInSeconds){
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
