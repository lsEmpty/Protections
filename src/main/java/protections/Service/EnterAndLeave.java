package protections.Service;

import org.bukkit.entity.Player;
import protections.Entities.Grid.ProtectionRegion;

import java.util.Map;


public class EnterAndLeave {
    public static void verifyIfUserEntersOrLeavesAProtection(Player player, Map<Player, ProtectionRegion> playerStates, ProtectionRegion currentProtection){
        ProtectionRegion previousProtection = playerStates.get(player);

        // Detect join player
        if (currentProtection != null && currentProtection != previousProtection) {
            player.sendMessage("¡Has entrado a una zona protegida!");
        }

        // Detect leave player
        if (currentProtection == null && previousProtection != null) {
            player.sendMessage("¡Has salido de una zona protegida!");
        }

        // Update player state
        playerStates.put(player, currentProtection);
    }
}
