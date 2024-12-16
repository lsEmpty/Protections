package protections.Listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
import protections.Entities.Grid.ProtectionRegion;
import protections.Entities.Menas.Mena;
import protections.ProtectionsPlugin;
import protections.Service.CheckIntersections;
import protections.Service.EnterAndLeave;
import protections.Service.GiveProtection;
import protections.Utils.InventoryUtils;
import protections.Utils.MessageUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static protections.ProtectionsPlugin.prefix;
import static protections.ProtectionsPlugin.protectionGrid;
import static protections.Service.GiveProtection.NAME_TO_GIVE;

public class ProtectionListener implements Listener {

    ProtectionsPlugin plugin;
    private final Map<Player, ProtectionRegion> playerStates = new HashMap<>();

    public ProtectionListener(ProtectionsPlugin plugin) {
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
            if  (!CheckIntersections.check(location, container, plugin)){
                event.setCancelled(true);
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
            Coordinate coordinate = new Coordinate(x, y, z, x_dimension, z_dimension, date);
            BlockCoordinateProcedures.createNewBlockCoordinate(x, y, z, x_dimension, z_dimension, date);
            long id_block_coordinate = BlockCoordinateProcedures.getIdFromBlockCoordinateWithCoordinate(x, y, z);
            Flags flags = new Flags(true, true, false, false, true, true, true, true, false, false , false);
            long id_flag = FlagsProcedures.create_flags_and_get_id(flags);
            ProtectionsPlugin.protections
                    .put(event.getBlock().getLocation(),
                            new Protection(name, true, owner, uuid, world, coordinate, flags, mena_information));
            ProtectionsProcedures.createNewProtection(name, true, owner, uuid, world, id_block_coordinate, id_flag, mena_information.getId());
            event.getPlayer().sendMessage(prefix+MessageUtil.color("&eProtection placed."));
            ProtectionGrid.addProtectionToGrid(new Protection(name, true, owner, uuid, world, coordinate, flags, mena_information));
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
                ProtectionsProcedures.changeStateProtection(id_to_change_state, false);
                ProtectionsPlugin.protections.remove(location);
                ProtectionGrid.removeProtectionToGrid(((long) (x)),((long) (z)));
                if (InventoryUtils.isInventoryFull(event.getPlayer())){
                    GiveProtection giveProtection = new GiveProtection();
                    event.getBlock().getWorld().dropItem(location, giveProtection.giveOnlyProtection(protection.getMenaInformation().getName_to_give(), plugin));
                    event.getPlayer().sendMessage(prefix+MessageUtil.color("&eMena removed."));
                }
                event.setDropItems(false);
                return;
            }
            event.getPlayer().sendMessage(prefix+MessageUtil.color("&cYou must a owner of this protection."));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        // Ignore if the player doesn't move from the location
        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }
        ProtectionRegion currentProtection = getCurrentProtection(to);
        EnterAndLeave.verifyIfUserEntersOrLeavesAProtection(player, playerStates, currentProtection);
    }

    @EventHandler
    public void tntExplosionOnProtection(BlockExplodeEvent event){
        List<Block> blocks_removed = event.blockList();
        Location location = event.getBlock().getLocation();
        explodeEvent(location, blocks_removed);
    }

    @EventHandler
    public void entityExplosionOnProtection(EntityExplodeEvent event){
        List<Block> blocks_removed = event.blockList();
        Location location = event.getEntity().getLocation();
        explodeEvent(location, blocks_removed);
    }

    private ProtectionRegion getCurrentProtection(Location location){
        // Get protections nearby location - location is to
        List<ProtectionRegion> nearbyProtections = protectionGrid.getNearbyProtections(location.getBlockX(), location.getBlockZ());

        // Verify if the player enters or leaves a protection
        ProtectionRegion currentProtection = null;
        for (ProtectionRegion protection : nearbyProtections) {
            if (protection.contains(location.getBlockX(), location.getBlockZ())) {
                currentProtection = protection;
                break;
            }
        }
        return currentProtection;
    }

    private void explodeEvent(Location location, List<Block> blocks_removed){
        ProtectionRegion currentProtection = getCurrentProtection(location);
        if (currentProtection != null){
            Block protection_block = null;
            for (Block block_removed :  blocks_removed){
                long x_r = block_removed.getX();
                long y_r = block_removed.getY();
                long z_r = block_removed.getZ();
                if (x_r == currentProtection.getProtection().getBlock_coordinate().getX()
                        && y_r == currentProtection.getProtection().getBlock_coordinate().getY()
                        && z_r == currentProtection.getProtection().getBlock_coordinate().getZ()){
                    protection_block = block_removed;
                }
            }
            if (protection_block != null){
                blocks_removed.remove(protection_block);
            }
        }
    }
}
