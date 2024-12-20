package protections.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionRegion;
import protections.ProtectionsPlugin;

import java.util.List;

import static protections.ProtectionsPlugin.prefix;

public class FlagListener implements Listener {

    @EventHandler
    // Flag - Damage_Mobs
    public void damageMobs(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity){
                ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getEntity().getLocation());
                if (currentProtection != null){
                    Protection protection = currentProtection.getProtection();
                    if (verifyIfUserIsOwner(protection, player)){
                        return;
                    }
                    if (!verifyIfUserIsMember(protection, player)
                            && !protection.getFlags().isDamage_mobs()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    // Flag - Mob_Spawning
    public void mobSpawning(CreatureSpawnEvent event){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getEntity().getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (protection.getFlags().isMob_spawning()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    // Flag - Block_Break
    public void blockBreakEvent(BlockBreakEvent event){
        Player player = event.getPlayer();
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getBlock().getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (verifyIfUserIsOwner(protection, player)){
                return;
            }
            if (!verifyIfUserIsMember(protection, player)
                    && !protection.getFlags().isBlock_break()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    // Flag - Block_Place
    public void blockPlaceEvent(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getBlock().getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (verifyIfUserIsOwner(protection, player)){
                return;
            }
            if (!verifyIfUserIsMember(protection, player)
                    && !protection.getFlags().isBlock_place()){
                event.setCancelled(true);
            }
        }
    }

    // Flag - Enter_Pearl
    @EventHandler
    public void onEnterPearlThrow(PlayerInteractEvent event){
        if (event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType() == Material.ENDER_PEARL) {
                ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
                if (currentProtection != null){
                    Protection protection = currentProtection.getProtection();
                    if (verifyIfUserIsOwner(protection, player)){
                        return;
                    }
                    if (!verifyIfUserIsMember(protection, player)
                            && !protection.getFlags().isEnder_pearl()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    // Flag - Ender_Pearl
    @EventHandler
    public void onEnderPearlTeleport(PlayerTeleportEvent event){
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();
            Location to = event.getTo();
            if (to != null){
                ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(to);
                if (currentProtection != null){
                    Protection protection = currentProtection.getProtection();
                    if (verifyIfUserIsOwner(protection, player)){
                        return;
                    }
                    if (!verifyIfUserIsMember(protection, player)
                            && !protection.getFlags().isEnder_pearl()){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    // Flag - Drop_Item
    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (verifyIfUserIsOwner(protection, player)){
                return;
            }
            if (!verifyIfUserIsMember(protection, player)
                    && !protection.getFlags().isItem_drop()){
                event.setCancelled(true);
            }
        }
    }

    // Flag - Pickup_Item
    @EventHandler
    public void pickupItem(EntityPickupItemEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
            if (currentProtection != null){
                Protection protection = currentProtection.getProtection();
                if (verifyIfUserIsOwner(protection, player)){
                    return;
                }
                if (!verifyIfUserIsMember(protection, player)
                        && !protection.getFlags().isItem_pickup()){
                    event.setCancelled(true);
                }
            }
        }
    }

    // Flag - Leaf_Decay
    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getBlock().getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (!protection.getFlags().isLeaf_decay()){
                event.setCancelled(true);
            }
        }
    }

    // Flag - Explosion - TNT
    @EventHandler
    public void explosionEvent(EntityExplodeEvent event){
        List<Block> blocks_destroyed = event.blockList();
        ProtectionRegion currentProtection = null;
        for (Block block_destroyed : blocks_destroyed){
            currentProtection = ProtectionListener.getCurrentProtection(block_destroyed.getLocation());
            if (currentProtection != null){
                break;
            }
        }
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (event.getEntityType() != EntityType.PRIMED_TNT){
                if (!protection.getFlags().isExplosion()){
                    event.setCancelled(true);
                }
            }else if (event.getEntityType() == EntityType.PRIMED_TNT){
                if (!protection.getFlags().isTnt()){
                    event.setCancelled(true);
                }
            }
        }
    }

    // Flag - Explosion - TNT
    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)  {
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                Entity damager = damageByEntityEvent.getDamager();
                ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(damager.getLocation());
                if (currentProtection != null){
                    Protection protection = currentProtection.getProtection();
                    if (damager.getType() != EntityType.PRIMED_TNT){
                        if (!protection.getFlags().isExplosion()){
                            event.setCancelled(true);
                        }
                    }else if(damager.getType() == EntityType.PRIMED_TNT){
                        if (!protection.getFlags().isTnt()){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    // Flag - Pvp
    @EventHandler
    public void pvpFlag(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player &&
        event.getEntity() instanceof Player){
            Player player_damager = (Player) event.getDamager();
            Player player_damaged = (Player) event.getEntity();
            ProtectionRegion currentProtectionDamager = ProtectionListener.getCurrentProtection(player_damager.getLocation());
            ProtectionRegion currentProtectionDamaged = ProtectionListener.getCurrentProtection(player_damaged.getLocation());
            if (currentProtectionDamaged != null || currentProtectionDamager != null){
                Protection protection;
                if (currentProtectionDamaged != null){
                    protection = currentProtectionDamaged.getProtection();
                }else{
                    protection = currentProtectionDamager.getProtection();
                }
                if (!protection.getFlags().isPvp()){
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean verifyIfUserIsOwner(Protection protection, Player player){
        return protection.getOwner_uuid().equals(player.getUniqueId());
    }

    private boolean verifyIfUserIsMember(Protection protection, Player player){

        long id_protection = ProtectionsProcedures.getIdProtectionWithIdBlockCoordinateAndIdFlags(protection.getBlock_coordinate().getId(), protection.getFlags().getId());
        List<Member> members = ProtectionsPlugin.protection_members_with_protection.get(id_protection);
        if (members != null){
            boolean aux = false;
            for (Member member : members){
                if (member.getUuid_member().equals(player.getUniqueId())){
                    aux = true;
                    break;
                }
            }
            return aux;
        }
        return false;
    }
}
