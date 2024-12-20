package protections.Entities.PlayerInventory;

import org.bukkit.entity.Player;
import protections.Entities.Enums.InventoryType;

public class PlayerCustomInventory {
    Player player;
    InventoryType inventoryType;

    public PlayerCustomInventory(Player player, InventoryType inventoryType) {
        this.player = player;
        this.inventoryType = inventoryType;
    }

    public Player getPlayer() {
        return player;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }
}
