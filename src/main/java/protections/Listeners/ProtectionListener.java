package protections.Listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.DataBase.Procedures.BlockCoordinateProcedures;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.Coordinate;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionGrid;
import protections.Entities.Grid.ProtectionRegion;
import protections.ProtectionsPlugin;
import protections.Service.CheckIntersections;
import protections.Service.EnterAndLeave;
import protections.Service.GiveProtection;
import protections.Utils.MessageUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static protections.ProtectionsPlugin.prefix;
import static protections.ProtectionsPlugin.protectionGrid;

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
            Coordinate coordinate = new Coordinate(x, y, z, x_dimension, z_dimension, date);
            ProtectionsPlugin.protections
                    .put(event.getBlock().getLocation(),
                            new Protection(name, true, owner, uuid, world, coordinate));
            BlockCoordinateProcedures.createNewBlockCoordinate(x, y, z, x_dimension, z_dimension, date);
            long id_block_coordinate = BlockCoordinateProcedures.getIdFromBlockCoordinateWithCoordinate(x, y, z);
            ProtectionsProcedures.createNewProtection(name, true, owner, uuid, world, id_block_coordinate);
            event.getPlayer().sendMessage(prefix+MessageUtil.color("&eProtection placed."));
            ProtectionGrid.addProtectionToGrid(new Protection(name, true, owner, uuid, world, coordinate));
        }
    }

    @EventHandler
    public void whenAUserBrakeAProtection(BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        if (ProtectionsPlugin.protections.containsKey(location)){
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            long id_block_coordinate = BlockCoordinateProcedures.getIdFromBlockCoordinateWithCoordinate(x, y, z);
            long id_to_change_state = BlockCoordinateProcedures.getIdFromProtectionsWithBlockCoordinateId(id_block_coordinate);
            ProtectionsProcedures.changeStateProtection(id_to_change_state, false);
            ProtectionsPlugin.protections.remove(location);
            ProtectionGrid.removeProtectionToGrid(((long) (x)),((long) (z)));
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
        // Get protections nearby player
        List<ProtectionRegion> nearbyProtections = protectionGrid.getNearbyProtections(to.getBlockX(), to.getBlockZ());

        // Verify if the player enters or leaves a protection
        ProtectionRegion currentProtection = null;
        for (ProtectionRegion protection : nearbyProtections) {
            if (protection.contains(to.getBlockX(), to.getBlockZ())) {
                currentProtection = protection;
                break;
            }
        }
        EnterAndLeave.verifyIfUserEntersOrLeavesAProtection(player, playerStates, currentProtection);
    }
}
