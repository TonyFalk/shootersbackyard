package zx.tonyfalk;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import zx.tonyfalk.CTF.CTFCMD;
import zx.tonyfalk.CTF.CTFGame;
import zx.tonyfalk.DM.DMCMD;
import zx.tonyfalk.DM.DMGame;
import zx.tonyfalk.MySQL.ConnectionPoolManager;
import zx.tonyfalk.MySQL.SQLManager;
import zx.tonyfalk.TDM.TDMCMD;
import zx.tonyfalk.TDM.TDMGame;
import zx.tonyfalk.Utils.CLog;
import zx.tonyfalk.common.*;

import java.io.File;
import java.sql.SQLException;

public final class ShootersBackYard extends JavaPlugin {

    private File MsgsFile = new File(getDataFolder(), "msgs.yml");
    private FileConfiguration MsgsConfig = YamlConfiguration.loadConfiguration(MsgsFile);
    public FileConfiguration getMsgsConfig() {
        return MsgsConfig;
    }
    public File getMsgsFile() { return MsgsFile; }

    private File ItemDataFile = new File(getDataFolder(), "itemdata.yml");
    private FileConfiguration ItemDataConfig = YamlConfiguration.loadConfiguration(ItemDataFile);
    public FileConfiguration getItemdataConfig() {
        return ItemDataConfig;
    }
    public File getItemDataFile() { return ItemDataFile; }

    private File CtfFile = new File(getDataFolder(), "ctf.yml");
    private FileConfiguration CtfConfig = YamlConfiguration.loadConfiguration(CtfFile);
    public FileConfiguration getCtfConfig() {
        return CtfConfig;
    }
    public File getCtfFile() { return CtfFile; }

    private File TdmFile = new File(getDataFolder(), "tdm.yml");
    private FileConfiguration TdmConfig = YamlConfiguration.loadConfiguration(TdmFile);
    public FileConfiguration getTdmConfig() {
        return TdmConfig;
    }
    public File getTdmFile() { return TdmFile; }

    private File DmFile = new File(getDataFolder(), "dm.yml");
    private FileConfiguration DmConfig = YamlConfiguration.loadConfiguration(DmFile);
    public FileConfiguration getDmConfig() {
        return DmConfig;
    }
    public File getDmFile() { return DmFile; }

    private File GuiFile = new File(getDataFolder(), "gui.yml");
    private FileConfiguration GuiConfig = YamlConfiguration.loadConfiguration(GuiFile);
    public FileConfiguration getGuiConfig() {
        return GuiConfig;
    }
    public File getGuiFile() { return GuiFile; }

    private File ServersFile = new File(getDataFolder(), "servers.yml");
    private FileConfiguration ServersConfig = YamlConfiguration.loadConfiguration(ServersFile);
    public FileConfiguration getServersConfig() {
        return ServersConfig;
    }
    public File getServersFile() { return ServersFile; }

    private File GearFile = new File(getDataFolder(), "gear.yml");
    private FileConfiguration GearConfig = YamlConfiguration.loadConfiguration(GearFile);
    public FileConfiguration getGearConfig() {
        return GearConfig;
    }
    public File getGearFile() { return GearFile; }

    private File HubFile = new File(getDataFolder(), "hub.yml");
    private FileConfiguration HubConfig = YamlConfiguration.loadConfiguration(HubFile);
    public FileConfiguration getHubConfig() {
        return HubConfig;
    }
    public File getHubFile() { return HubFile; }

    private File DatabaseFile = new File(getDataFolder(), "database.yml");
    private FileConfiguration DatabaseConfig = YamlConfiguration.loadConfiguration(DatabaseFile);
    public FileConfiguration getDatabaseConfig() {
        return DatabaseConfig;
    }
    public File getDatabaseFile() { return DatabaseFile; }

    public HealthStaminaManager healthStaminaManager;
    public ConnectionPoolManager connectionPoolManager;
    public SQLManager sqlManager;
    public PlaceholderManager placeholderManager;
    public DataManager dataManager;
    public GameManager gameManager;
    public GuiManager guiManager;
    public InventoryManager inventoryManager;
    public ItemData itemData;
    public LoadoutsCMD loadoutsCMD;
    public ChatManager chatManager;
    public SCTManager sctManager;
    public Leaderboard leaderboard;
    public Timer timer;
    public TDMGame tdmGame;
    public TDMCMD tdmcmd;
    public CTFGame ctfGame;
    public CTFCMD ctfcmd;
    public DMGame dmGame;
    public DMCMD dmcmd;
    public GamesCMD gamesCMD;

    public boolean requiresrestart = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        CLog.OffiicalMessage("Starting. . .");
        file(getDatabaseFile(),requiresrestart);
        this.connectionPoolManager = new ConnectionPoolManager(this);
        this.sqlManager = new SQLManager(this);
        try {
            connectionPoolManager.getConnection();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        try {
            sqlManager.makeTable();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        if (!getConfig().getBoolean("decisionisdone")) {
            CLog.ConsoleDefaultMessage("Restart is gonna be executed to set the server type");
            CLog.ConsoleDefaultMessage("After setting it up set desicionisdone to true!");
            Bukkit.shutdown();
        } else {
            file(getMsgsFile(), requiresrestart);
            file(getGuiFile(), requiresrestart);
            file(getItemDataFile(), requiresrestart);
            file(getGearFile(), requiresrestart);
            if (getConfig().getBoolean("serverishub")) {
                file(getServersFile(), requiresrestart);
                file(getHubFile(), requiresrestart);
            } else {
                file(getCtfFile(), requiresrestart);
                file(getTdmFile(), requiresrestart);
                file(getDmFile(), requiresrestart);
                this.timer = new Timer(this);
                this.gamesCMD = new GamesCMD(this);
                Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Timer(this), 20L, 20L);
                if (getConfig().getBoolean("setup") == true) {
                    gameManager.startNextGame();
                } else {
                    CLog.ConsoleWarningMessage("Server is on setup mode games will not start to change it once all games are ready set setup in config to true");
                }
            }
            if (requiresrestart) {
                if (!getConfig().getBoolean("setup")) {
                    CLog.ConsoleDefaultMessage("Restart is gonna be executed to finish the setup");
                    getConfig().set("setup", requiresrestart);
                } else {
                    CLog.ConsoleErrorMessage("Critical Error caused due file loss restart is gonna be executed in attempt to solve the problem");
                }
                Bukkit.getServer().shutdown();
            }
            healthStaminaManager = new HealthStaminaManager(this);
            Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this, new HealthStaminaManager(this), 5L, 5L);
            this.dataManager = new DataManager(this);
            this.gameManager = new GameManager(this);
            this.guiManager = new GuiManager(this);
            this.inventoryManager = new InventoryManager(this);
            this.loadoutsCMD = new LoadoutsCMD(this);
            this.itemData = new ItemData(this);
            this.sctManager = new SCTManager(this);
            this.leaderboard = new Leaderboard(this);
            this.dmcmd = new DMCMD(this);
            this.ctfcmd = new CTFCMD(this);
            this.tdmcmd = new TDMCMD(this);
            this.chatManager = new ChatManager(this);
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                CLog.ConsoleDefaultMessage("PAPI mananged to setup the placeholders");
                this.placeholderManager = new PlaceholderManager(this);
            } else {
                CLog.ConsoleErrorMessage("PAPI is missing! cannot setup placeholders! if PAPI is running DM Tony Falk#1631");
            }

            try {
               sqlManager.makeTable();
                } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
    public ConnectionPoolManager getConnectionPoolManager() {
        return connectionPoolManager;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public void registerTDM(){
        this.tdmGame = new TDMGame(this);
    }
    public void unregisterTDM(){
        this.tdmGame.unregister();
    }
    public void registerCTF(){
        this.ctfGame = new CTFGame(this);
    }
    public void unregisterCTF(){
        ctfGame.unregister();
    }
    public void registerDM(){
        this.dmGame = new DMGame(this);
    }
    public void unregisterDM(){
        dmGame.unregister();
    }
    public void file(File file,boolean requiresrestart) {
       if (!file.exists()) {
        saveResource(file.getName(), false);
        CLog.ConsoleChangeMessage(file.getName() + " file was created");
        requiresrestart = true;
       }
    }




    @Override
    public void onDisable() {

    }
}
