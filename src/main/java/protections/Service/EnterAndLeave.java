package protections.Service;

import org.bukkit.entity.Player;
import protections.Entities.Grid.ProtectionRegion;
import protections.Utils.MessageUtil;

import java.util.Map;

import static protections.ProtectionsPlugin.prefix;


public class EnterAndLeave {
    public static void verifyIfUserEntersOrLeavesAProtection(Player player, Map<Player, ProtectionRegion> playerStates, ProtectionRegion currentProtection){
        ProtectionRegion previousProtection = playerStates.get(player);

        // Detect join player
        if (currentProtection != null && currentProtection != previousProtection) {
            player.sendMessage(MessageUtil.color(prefix+"&eYou have entered to " + currentProtection.getProtection().getName() + " protection."));
        }

        // Detect leave player
        if (currentProtection == null && previousProtection != null) {
            player.sendMessage(MessageUtil.color(prefix +"&eYou have leaved to " + previousProtection.getProtection().getName() + " protection."));
        }

        // Update player state
        playerStates.put(player, currentProtection);
    }
}
