package protections.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionRegion;
import protections.ProtectionsPlugin;
import protections.Utils.MessageUtil;

import java.util.List;

import static protections.ProtectionsPlugin.prefix;

public class FlagListener implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event){
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(event.getBlock().getLocation());
        if (currentProtection != null){
            if (currentProtection.getProtection().getOwner_uuid().equals(event.getPlayer().getUniqueId())){
                return;
            }
            Protection protection = currentProtection.getProtection();
            List<Member> members = ProtectionsPlugin.protection_members.get(protection.getId());
            if (!protection.getFlags().isBlock_break()){
                event.setCancelled(true);
                event.getPlayer().sendMessage(prefix+ MessageUtil.color("&cYou don't have permissions in this protection."));
            }else if (members != null){
                boolean aux = false;
                for (Member member : members){
                    if (member.getUuid_member().equals(event.getPlayer().getUniqueId())){
                        aux = true;
                        break;
                    }
                }
                if (!aux){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(prefix+ MessageUtil.color("&cYou don't have permissions in this protection."));
                }
            }
        }
    }
}
