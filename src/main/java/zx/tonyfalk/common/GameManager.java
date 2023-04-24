package zx.tonyfalk.common;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import zx.tonyfalk.Enums.GameMode;
import zx.tonyfalk.Enums.GameStage;
import zx.tonyfalk.Enums.Team;
import zx.tonyfalk.ShootersBackYard;
import zx.tonyfalk.Utils.UMath;

import java.util.HashMap;
import java.util.List;

public class GameManager {

    public ShootersBackYard plugin;
    public GameMode runningGame;
    public GameStage gameStage;
    public List<GameMode> workingGames;
    public HashMap<Player, Team> teams;
    public String arena;

    public GameManager(ShootersBackYard plugin) {
        this.plugin = plugin;

    }

    public void setupWorkingGames(){
        if (this.plugin.getCtfConfig().getBoolean("isReady")){
            workingGames.add(GameMode.CTF);
        }
        if (this.plugin.getTdmConfig().getBoolean("isReady")){
            workingGames.add(GameMode.TDM);
        }
        if (this.plugin.getDmConfig().getBoolean("isReady")){
            workingGames.add(GameMode.DM);
        }
    }

    public void setupGameStage(){
        if(workingGames.isEmpty()){
            gameStage = GameStage.Setup;
        }
    }

    public void startNextGame() {
        GameMode nextgame = workingGames.get(UMath.random(0,workingGames.size()));
        runningGame = nextgame;
        gameStage = GameStage.Waiting;
        switch (runningGame){
            case DM -> {
                this.plugin.dmGame.PrestartGame();
                this.plugin.registerDM();
                this.plugin.unregisterCTF();
                this.plugin.unregisterTDM();
            }
            case TDM -> {
                this.plugin.tdmGame.PrestartGame();
                this.plugin.unregisterDM();
                this.plugin.unregisterCTF();
                this.plugin.registerTDM();
            }
            case CTF -> {
                this.plugin.ctfGame.PrestartGame();
                this.plugin.unregisterDM();
                this.plugin.registerCTF();
                this.plugin.unregisterTDM();
            }
        }
    }
}





