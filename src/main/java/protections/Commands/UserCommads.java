package protections.Commands;

import com.google.protobuf.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import protections.DataBase.Procedures.ProtectionMembersProcedures;
import protections.DataBase.Procedures.ProtectionsProcedures;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Enums.InventoryType;
import protections.Entities.Grid.ProtectionRegion;
import protections.Entities.PlayerInventory.PlayerCustomInventory;
import protections.Listeners.ProtectionListener;
import protections.ProtectionsPlugin;
import protections.Service.ServiceUserCommands;
import protections.Utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

import static protections.ProtectionsPlugin.onCustomInventory;
import static protections.ProtectionsPlugin.prefix;

public class UserCommads implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if (!sender.hasPermission("protections.user") && sender instanceof Player){
            sender.sendMessage(prefix+ MessageUtil.color("&cYou don't have permissions to use this command."));
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(prefix+MessageUtil.color("&cYou must use parameters with this command. Use &e</pc help>&c for more information."));
            return true;
        }
        Player player = (Player) sender;
        // Info - Home
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("info")){
                ServiceUserCommands.infoCommand(player);
            } else if (args[0].equalsIgnoreCase("home")){

            }
            return true;
        }

        // Ban - Add - Remove
        ProtectionRegion currentProtection = ProtectionListener.getCurrentProtection(player.getLocation());
        if (currentProtection != null){
            Protection protection = currentProtection.getProtection();
            if (protection.getOwner_uuid().equals(player.getUniqueId())){
                if (args.length == 2){
                    if (args[0].equalsIgnoreCase("add")){
                        String name = args[1];
                        if (Bukkit.getPlayer(name) != null){
                            Player player_add = Bukkit.getPlayer(name);
                            long id_flags = protection.getFlags().getId();
                            long id_block_coordinate = protection.getBlock_coordinate().getId();
                            long id_protection = ProtectionsProcedures.getIdProtectionWithIdBlockCoordinateAndIdFlags(id_block_coordinate, id_flags);
                            assert player_add != null;
                            long id_member = ProtectionMembersProcedures.getIdByUuid(player_add.getUniqueId());
                            if (ProtectionsPlugin.protection_members_with_protection.containsKey(id_protection)){
                                List<Member> members = ProtectionsPlugin.protection_members_with_protection.get(id_protection);
                                members.add(new Member(id_member, player_add.getName(), player_add.getUniqueId()));
                                ProtectionsPlugin.protection_members_with_protection.put(id_protection, members);
                            }else{
                                List<Member> members = new ArrayList<>();
                                members.add(new Member(id_member, player_add.getName(), player_add.getUniqueId()));
                                ProtectionsPlugin.protection_members_with_protection.put(id_protection, members);
                            }
                            ProtectionMembersProcedures.add_member_to_protection(id_protection, id_block_coordinate, id_flags, id_member);
                            player.sendMessage(prefix+MessageUtil.color("&e"+player_add.getName()+"&a was added to protection"));
                        }else{
                            player.sendMessage(prefix+MessageUtil.color("&eThis user doesn't exists or isn't connected"));
                        }
                    }else if (args[0].equalsIgnoreCase("remove")){

                    }else if (args[0].equalsIgnoreCase("ban"))

                        return true;
                }
            }else{
                player.sendMessage(prefix+ MessageUtil.color("&cYou can't use this command, because you aren't owner of this protection."));
                return true;
            }
        }else{
            player.sendMessage(prefix+MessageUtil.color("&eYou must be inside a protection to use this command."));
            return true;
        }

        return false;
    }
}
