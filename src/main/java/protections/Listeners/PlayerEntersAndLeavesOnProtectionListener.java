package protections.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import protections.Entities.Grid.ProtectionRegion;
import protections.Service.EnterAndLeave;

import static protections.Listeners.ProtectionListener.getCurrentProtection;
import static protections.Listeners.ProtectionListener.playerStates;

public class PlayerEntersAndLeavesOnProtectionListener implements Listener {
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
}
