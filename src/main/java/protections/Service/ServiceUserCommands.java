package protections.Service;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import protections.DataBase.Procedures.ProtectionMembersProcedures;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Enums.InventoryType;
import protections.Entities.Grid.ProtectionRegion;
import protections.Entities.PlayerInventory.PlayerCustomInventory;
import protections.Listeners.ProtectionListener;
import protections.ProtectionsPlugin;
import protections.Utils.MessageUtil;

import java.util.List;
import java.util.UUID;

import static protections.ProtectionsPlugin.onCustomInventory;
import static protections.ProtectionsPlugin.prefix;

public class ServiceUserCommands {

    public static void infoCommand(Player player){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (player.getUniqueId().equals(protection.getOwner_uuid()) || player.hasPermission("protections.moderator")){
                infoCommandDashboard(player, protection);
            }
            return;
        }

        // ADD THIS MESSAGE IN CONFIG
        player.sendMessage(prefix+ MessageUtil.color("&eYou must be on a protection and be the owner of that protection."));
    }

    private static void infoCommandDashboard(Player player, Protection protection){
        PlayerCustomInventory customInventory = new PlayerCustomInventory(player, InventoryType.DASHBOARD_INVENTORY);
        // ADD THIS MESSAGE IN CONFIG
        Inventory inventory = Bukkit.createInventory(null, 27, MessageUtil.color("&8Mena Information"));
        inventory.setItem(10, PersonalizedItem.skullInformation(player));
        inventory.setItem(13, PersonalizedItem.members());
        inventory.setItem(14, PersonalizedItem.flags());
        inventory.setItem(15, PersonalizedItem.homes());
        inventory.setItem(16, PersonalizedItem.userBanned());
        player.openInventory(inventory);
        onCustomInventory.put(player.getUniqueId(), new PlayerCustomInventory(player, InventoryType.DASHBOARD_INVENTORY));
    }

    public static void infoCommandMembers(Player player, ProtectionsPlugin plugin){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
        Protection protection = currentProtection.getProtection();
        long id_protection = ProtectionsProcedures.getIdProtectionWithIdBlockCoordinateAndIdFlags(protection.getBlock_coordinate().getId(), protection.getFlags().getId());
        if (id_protection != 0){
            Inventory inventory = Bukkit.createInventory(null, 54, MessageUtil.color("&8Members"));
            List<Member> members = ProtectionsPlugin.protection_members_with_protection.get(id_protection);
            if (members != null){
                int aux = 9;
                for (Member member : members){
                    inventory.setItem(aux, PersonalizedItem.memberOnMembers(member, plugin));
                    aux++;
                }
            }
            for (int i = 0; i < 9; i++) {
                inventory.setItem(i, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
            }
            for (int i = 45; i < 54; i++) {
                inventory.setItem(i, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
            }
            inventory.setItem(49, PersonalizedItem.back());
            player.openInventory(inventory);
            ProtectionsPlugin.onCustomInventory.put(player.getUniqueId(), new PlayerCustomInventory(player, InventoryType.MEMBERS_INVENTORY));
        }
    }

    public static void infoCommandRemoveMember(Player owner, String player_uuid_to_remove, ProtectionsPlugin plugin){
        Inventory inventory = Bukkit.createInventory(null, 27, "Remove user from protection");
        inventory.setItem(10, PersonalizedItem.yes());
        inventory.setItem(13, PersonalizedItem.areYouSure(player_uuid_to_remove, plugin));
        inventory.setItem(16, PersonalizedItem.no());
        owner.openInventory(inventory);
        ProtectionsPlugin.onCustomInventory.put(owner.getUniqueId(), new PlayerCustomInventory(owner, InventoryType.REMOVE_MEMBER_INVENTORY));
    }

    public static void removeMemberOnMembers(Player owner, UUID player_uuid_to_remove){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(owner.getLocation());
        Protection protection = currentProtection.getProtection();
        long id_protection = ProtectionsProcedures.getIdProtectionWithIdBlockCoordinateAndIdFlags(protection.getBlock_coordinate().getId(), protection.getFlags().getId());
        if (id_protection != 0){
            List<Member> members = ProtectionsPlugin.protection_members_with_protection.get(id_protection);
            for (Member member : members){
                if (member.getUuid_member().equals(player_uuid_to_remove)){
                    members.remove(member);
                    ProtectionMembersProcedures.remove_member_from_protection(id_protection, player_uuid_to_remove);
                    return;
                }
            }
        }
    }
}
