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
    public static final Map<Player, ProtectionRegion> playerStates = new HashMap<>();

    public ProtectionListener(ProtectionsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ProtectionRegion getCurrentProtection(Location location){
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
