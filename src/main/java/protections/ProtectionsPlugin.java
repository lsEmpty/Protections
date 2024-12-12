package protections;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import protections.Config.Manager.ConfigManager;
import protections.DataBase.BridgeConnection;
import protections.Utils.MessageUtil;

public class ProtectionsPlugin extends JavaPlugin {

    public static String prefix = MessageUtil.Color("&b&lProtections &8&l» ");
    public static ConfigManager mainConfigManager;
    public static BridgeConnection connection;

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
}
