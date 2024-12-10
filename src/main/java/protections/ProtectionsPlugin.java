package protections;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import protections.Config.Manager.ConfigManager;
import protections.Entities.Mena;
import protections.Utils.MessageUtil;

public class ProtectionsPlugin extends JavaPlugin {

    public static String prefix = MessageUtil.Color("&b&lProtections &8&l» ");
    ConfigManager mainConfigManager;

    public void onEnable() {
        mainConfigManager = new ConfigManager(this);
        Bukkit.getConsoleSender().sendMessage(prefix+"enabled");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix+"disabled");
    }
}
