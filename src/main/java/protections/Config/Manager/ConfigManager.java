package protections.Config.Manager;

import org.bukkit.configuration.file.FileConfiguration;
import protections.Config.CustomConfig;
import protections.Entities.Menas.Dimension;
import protections.Entities.Menas.Mena;
import protections.ProtectionsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final CustomConfig customConfig;
    private List<Mena> menas;

    // Credentials
    private String host;
    private String port;
    private String database_name;
    private String optional_parameters;
    private String user;
    private String password;

    public ConfigManager(ProtectionsPlugin plugin) {
        customConfig = new CustomConfig("config.yml", null, plugin);
        menas = new ArrayList<>();
        loadConfig();
    }

    private void loadConfig(){
        customConfig.registerConfig();
        getConfig();
        customConfig.reloadConfig();
    }

    private void getConfig(){
        this.menas.clear();
        FileConfiguration config = customConfig.getConfig();
        List<Map<?, ?>> menas = config.getMapList("config.menas");
        menas.forEach(mena ->{
            String name = (String) mena.get("name");
            String name_to_use = (String) mena.get("name_to_give");
            String block = (String) mena.get("block");
            Map<?, ?> dimension = (Map<?, ?>) mena.get("dimension");
            Integer x = (Integer) dimension.get("x");
            Integer z = (Integer) dimension.get("z");
            this.menas.add(new Mena(name, name_to_use, block, new Dimension(x, z)));
        });
        this.host = (String) config.get("credentials.host");
        this.port = (String) config.get("credentials.port");
        this.database_name = (String) config.get("credentials.database_name");
        this.optional_parameters = (String) config.get("credentials.optional_parameters");
        this.user = (String) config.get("credentials.user");
        this.password = (String) config.get("credentials.password");
    }

    public List<Mena> getMenas() {
        return menas;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public String getOptional_parameters() {
        return optional_parameters;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
