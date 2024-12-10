package protections.Config.Manager;

import org.bukkit.configuration.file.FileConfiguration;
import protections.Config.CustomConfig;
import protections.Entities.Dimension;
import protections.Entities.Mena;
import protections.ProtectionsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final CustomConfig customConfig;
    private List<Mena> menas;

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
            Integer y = (Integer) dimension.get("y");
            Integer x = (Integer) dimension.get("x");
            this.menas.add(new Mena(name, name_to_use, block, new Dimension(y, x)));
        });
    }

    public List<Mena> getMenas() {
        return menas;
    }
}
