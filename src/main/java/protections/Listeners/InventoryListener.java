package protections.Listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.Entities.Enums.InventoryType;
import protections.Entities.PlayerInventory.PlayerCustomInventory;
import protections.ProtectionsPlugin;
import protections.Service.PersonalizedItem;
import protections.Service.ServiceUserCommands;

import java.util.UUID;

import static protections.ProtectionsPlugin.onCustomInventory;

public class InventoryListener implements Listener {

    ProtectionsPlugin plugin;

    public InventoryListener(ProtectionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void moveCustomInventory(InventoryClickEvent event){
        UUID player_uuid = event.getWhoClicked().getUniqueId();
        Player player = (Player) event.getWhoClicked();
        if (onCustomInventory.containsKey(player_uuid)){
            PlayerCustomInventory customInventory = onCustomInventory.get(player_uuid);
            if (event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())){
                if (customInventory.getInventoryType().equals(InventoryType.DASHBOARD_INVENTORY)){
                    switch (event.getSlot()){
                        case 13:
                            ServiceUserCommands.infoCommandMembers(player, plugin);
                            break;
                        case 14:
                            break;
                        case 15:
                            break;
                        case 16:
                            break;
                    }
                }else if (customInventory.getInventoryType().equals(InventoryType.MEMBERS_INVENTORY)){
                    ItemStack item = event.getCurrentItem();
                    if (item != null){
                        if (item.getType().equals(Material.PLAYER_HEAD)){
                            ItemMeta meta = item.getItemMeta();
                            if (meta == null){
                                return;
                            }
                            PersistentDataContainer container = meta.getPersistentDataContainer();
                            String uuid = container.get(new NamespacedKey(plugin, PersonalizedItem.MEMBER_ON_MEMBERS_UUID), PersistentDataType.STRING);
                            assert uuid != null;
                            ServiceUserCommands.infoCommandRemoveMember(player, uuid, plugin);
                        }
                    }
                    if (event.getSlot() == 49){
                        ServiceUserCommands.infoCommand(player);
                    }
                }else if (customInventory.getInventoryType().equals(InventoryType.REMOVE_MEMBER_INVENTORY)){
                    ItemStack item = player.getOpenInventory().getTopInventory().getItem(13);
                    if (item != null){
                        switch (event.getSlot()){
                            case 10:
                                ItemMeta meta = item.getItemMeta();
                                if (meta == null){
                                    return;
                                }
                                PersistentDataContainer container = meta.getPersistentDataContainer();
                                String player_uuid_to_remove = container.get(new NamespacedKey(plugin, PersonalizedItem.MEMBER_ON_MEMBERS_UUID), PersistentDataType.STRING);
                                assert player_uuid_to_remove != null;
                                ServiceUserCommands.removeMemberOnMembers(player, UUID.fromString(player_uuid_to_remove));
                                ServiceUserCommands.infoCommandMembers(player, plugin);
                                break;
                            case 16:
                                ServiceUserCommands.infoCommandMembers(player, plugin);
                                break;
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void closeCustomInventory(InventoryCloseEvent event){
        UUID player_uuid = event.getPlayer().getUniqueId();
        onCustomInventory.remove(player_uuid);
    }
}
