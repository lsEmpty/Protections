package protections.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.DataBase.Procedures.BlockCoordinateProcedures;
import protections.DataBase.Procedures.FlagsProcedures;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.Coordinate;
import protections.DatabaseEntities.Protections.Flags;
import protections.DatabaseEntities.Protections.MenaInformation;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionGrid;
import protections.ProtectionsPlugin;
import protections.Service.CheckIntersections;
import protections.Service.GiveProtection;
import protections.Utils.InventoryUtils;
import protections.Utils.MessageUtil;

import java.time.LocalDateTime;
import java.util.UUID;

import static protections.ProtectionsPlugin.number_of_homes;
import static protections.ProtectionsPlugin.prefix;
import static protections.Service.GiveProtection.NAME_TO_GIVE;

public class PlayerPlacesAndBreaksProtectionListener implements Listener {

    ProtectionsPlugin plugin;

    public PlayerPlacesAndBreaksProtectionListener(ProtectionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void whenAUserPlaceAProtection(BlockPlaceEvent event){
        ItemStack protection = event.getItemInHand();
        ItemMeta protection_meta = protection.getItemMeta();
        if (protection_meta.getPersistentDataContainer()
                .has(new NamespacedKey(plugin, GiveProtection.X_DIMENSION), PersistentDataType.INTEGER)){
            PersistentDataContainer container = protection_meta.getPersistentDataContainer();
            Location location = event.getBlock().getLocation();
            // Check if there is a protection near
            if  (!CheckIntersections.check(location, container, plugin)){
                event.setCancelled(true);
                // ADD THIS MESSAGE IN CONFIG
                event.getPlayer().sendMessage(prefix+ MessageUtil.color("&eThere is a protection near here. You can't put that protection."));
                return;
            }
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            int x_dimension = container.get(new NamespacedKey(plugin, GiveProtection.X_DIMENSION), PersistentDataType.INTEGER);
            int z_dimension = container.get(new NamespacedKey(plugin, GiveProtection.Z_DIMENSION), PersistentDataType.INTEGER);
            LocalDateTime date = LocalDateTime.now();
            String name = event.getPlayer().getName();
            String owner = event.getPlayer().getName();
            UUID uuid = event.getPlayer().getUniqueId();
            String world = event.getBlock().getWorld().getName();
            String mena_name_to_give = container.get(new NamespacedKey(plugin, NAME_TO_GIVE), PersistentDataType.STRING);
            MenaInformation mena_information = null;
            for (MenaInformation menaInformation : ProtectionsPlugin.menas_on_db){
                if (menaInformation.getName_to_give().equalsIgnoreCase(mena_name_to_give)){
                    mena_information = menaInformation;
                }
            }
            //DB and Add to lists and maps
            BlockCoordinateProcedures.createNewBlockCoordinate(x, y, z, x_dimension, z_dimension, date);
            long id_block_coordinate = BlockCoordinateProcedures.getIdFromBlockCoordinateWithCoordinate(x, y, z);
            Coordinate coordinate = new Coordinate(x, y, z, x_dimension, z_dimension, date);
            coordinate.setId(id_block_coordinate);
            Flags flags = new Flags(true, true, false, false, true, true, true, true, false, false , false);
            long id_flag = FlagsProcedures.create_flags_and_get_id(flags);
            flags.setId(id_flag);
            ProtectionsProcedures.createNewProtection(name, true, owner, uuid, world, id_block_coordinate, id_flag, mena_information.getId());
            long id_protection = ProtectionsProcedures.getIdProtectionWithIdBlockCoordinateAndIdFlags(id_block_coordinate, id_flag);
            Protection protection_to_add = new Protection(id_protection ,name, true, owner, uuid, world, coordinate, flags, mena_information);
            ProtectionsPlugin.protections
                    .put(event.getBlock().getLocation(), protection_to_add);
            // ADD THIS MESSAGE IN CONFIG
            event.getPlayer().sendMessage(prefix+MessageUtil.color("&eProtection placed."));
            ProtectionGrid.addProtectionToGrid(protection_to_add);

            //Number of homes
            if (!number_of_homes.containsKey(uuid)){
                number_of_homes.put(uuid, 1);
                return;
            }
            int homes = number_of_homes.get(event.getPlayer().getUniqueId()) + 1;
            number_of_homes.put(uuid, homes);
        }
    }

    @EventHandler
    public void whenAUserBreakAProtection(BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        if (ProtectionsPlugin.protections.containsKey(location)){
            Protection protection = ProtectionsPlugin.protections.get(location);
            if (event.getPlayer().getUniqueId().equals(protection.getOwner_uuid())){
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();
                long id_block_coordinate = BlockCoordinateProcedures.getIdFromBlockCoordinateWithCoordinate(x, y, z);
                long id_to_change_state = BlockCoordinateProcedures.getIdFromProtectionsWithBlockCoordinateId(id_block_coordinate);
                ProtectionsPlugin.protection_members_with_protection.remove(protection.getId());
                ProtectionsProcedures.changeStateProtection(id_to_change_state, false);
                ProtectionsPlugin.protections.remove(location);
                ProtectionGrid.removeProtectionToGrid(
                        (long) (x - protection.getBlock_coordinate().getX_dimension()),
                        (long) (x + protection.getBlock_coordinate().getX_dimension()),
                        (long) (z - protection.getBlock_coordinate().getZ_dimension()),
                        (long) (z + protection.getBlock_coordinate().getZ_dimension())
                );
                GiveProtection giveProtection = new GiveProtection();
                if (InventoryUtils.isInventoryFull(event.getPlayer())){
                    event.getBlock().getWorld().dropItem(location, giveProtection.giveOnlyProtection(protection.getMenaInformation().getName_to_give(), plugin));
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"pca give " + event.getPlayer().getName() + " " + protection.getMenaInformation().getName_to_give());
                }
                event.setExpToDrop(0);
                // ADD THIS MESSAGE IN CONFIG
                event.getPlayer().sendMessage(prefix+MessageUtil.color("&eMena removed."));
                event.setDropItems(false);

                UUID player_uuid = event.getPlayer().getUniqueId();
                //Number of homes
                int homes = number_of_homes.get(event.getPlayer().getUniqueId()) - 1;
                if (homes == 0){
                    number_of_homes.remove(player_uuid);
                    return;
                }
                number_of_homes.put(player_uuid, homes);
                return;
            }
            // ADD THIS MESSAGE IN CONFIG
            event.getPlayer().sendMessage(prefix+MessageUtil.color("&cYou must a owner of this protection."));
            event.setCancelled(true);
        }
    }

}
