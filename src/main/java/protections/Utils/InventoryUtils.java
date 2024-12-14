package protections.Utils;

import org.bukkit.entity.Player;

public class InventoryUtils {
    public static boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }
}
