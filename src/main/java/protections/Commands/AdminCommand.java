package protections.Commands;

import com.google.protobuf.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import protections.ProtectionsPlugin;
import protections.Service.GiveProtection;
import protections.Utils.InventoryUtils;
import protections.Utils.MessageUtil;

import static protections.ProtectionsPlugin.prefix;

public class AdminCommand implements CommandExecutor {

    ProtectionsPlugin plugin;

    public AdminCommand(ProtectionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("protections.admin")){
            sender.sendMessage(prefix+MessageUtil.color("&cYou don't have permissions to use this command."));
            return true;
        }
        GiveProtection giveProtection = new GiveProtection();
        if (args.length == 2){
            if (!(sender instanceof Player)){
                sender.sendMessage(prefix+ MessageUtil.color("&cTo use this command you must be a player."));
                return true;
            }
            if (args[0].equalsIgnoreCase("give")){
                Player player_sender = (Player) sender;
                if (!InventoryUtils.isInventoryFull(player_sender)){
                    ItemStack mena_to_give = giveProtection.give(player_sender, args[1], plugin);
                    if (mena_to_give != null){
                        player_sender.getInventory().addItem(mena_to_give);
                    }
                }else{
                    sender.sendMessage(prefix+"&cYou have the full inventory.");
                }
                return true;
            }
        }
        return true;
    }
}
