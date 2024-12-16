package protections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import protections.Commands.AdminCommand;
import protections.Config.Manager.ConfigManager;
import protections.DataBase.BridgeConnection;
import protections.DataBase.Procedures.MenaInformationProcedures;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.MenaInformation;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionGrid;
import protections.Entities.Menas.Mena;
import protections.Listeners.PlayerEntersAndLeavesOnProtectionListener;
import protections.Listeners.PlayerPlacesAndBreaksProtectionListener;
import protections.Listeners.ProtectionListener;
import protections.Utils.MessageUtil;

import java.util.List;
import java.util.Map;

public class ProtectionsPlugin extends JavaPlugin {

    public static String prefix = MessageUtil.color("&b&lProtection &8&l» ");
    public static ConfigManager mainConfigManager;
    public static BridgeConnection connection;
    public static Map<Location, Protection> protections;
    public static ProtectionGrid protectionGrid;
    public static List<MenaInformation> menas_on_db;

    // Credentials
    private String host;
    private String port;
    private String database_name;
    private String optional_parameters;
    private String user;
    private String password;

    public void onEnable() {
        mainConfigManager = new ConfigManager(this);
        getCredentials();
        connection = new BridgeConnection(host, port, database_name, optional_parameters, user, password);
        saveCommands();
        saveListeners();
        protectionGrid = new ProtectionGrid(100);
        protections = ProtectionsProcedures.getProtectionsInUse();
        fillMenaInformationOnDataBase();
        Bukkit.getConsoleSender().sendMessage(prefix+"enabled");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix+"disabled");
    }

    private void getCredentials(){
        this.host = mainConfigManager.getHost();
        this.port = mainConfigManager.getPort();
        this.database_name = mainConfigManager.getDatabase_name();
        this.optional_parameters = mainConfigManager.getOptional_parameters();
        this.user = mainConfigManager.getUser();
        this.password = mainConfigManager.getPassword();
    }

    private void fillMenaInformationOnDataBase() {
        boolean aux;
        menas_on_db = MenaInformationProcedures.get_all_mena_information();
        for (Mena mena : mainConfigManager.getMenas()) {
            aux = false;
            for (MenaInformation mena_on_db : menas_on_db) {
                if (mena_on_db.getName_to_give().equalsIgnoreCase(mena.getName_to_give())) {
                    aux = true;
                    break;
                }
            }
            if (!aux) {
                MenaInformationProcedures.create_mena_information(new MenaInformation(mena.getName(), mena.getName_to_give(), mena.getBlock()));
            }
        }
        menas_on_db = MenaInformationProcedures.get_all_mena_information();
    }

    private void saveCommands(){
        getCommand("pca").setExecutor(new AdminCommand(this));
    }

    private void saveListeners(){
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPlacesAndBreaksProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEntersAndLeavesOnProtectionListener(), this);
    }
}
