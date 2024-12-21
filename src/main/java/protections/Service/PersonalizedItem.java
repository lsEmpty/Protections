package protections.Service;

import com.google.protobuf.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.ProtectionsPlugin;
import protections.Utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static protections.ProtectionsPlugin.number_of_homes;

public class PersonalizedItem {

    public final static String MEMBER_ON_MEMBERS_UUID = "member_on_members_uuid";

    public static ItemStack skullInformation(Protection protection){
        ItemStack skull = new ItemStack(Material.LECTERN);
        ItemMeta meta = skull.getItemMeta();
        if (meta != null){
            // ADD THIS MESSAGE IN CONFIG
            meta.setDisplayName(MessageUtil.color("&7"+protection.getOwner()+"&6 information"));
            List<String> lore = new ArrayList<>();
            lore.add(MessageUtil.color(MessageUtil.color("&3☔ &bNumbers of home: &7") + number_of_homes.get(protection.getOwner_uuid())));
            meta.setLore(lore);
            skull.setItemMeta(meta);
        }
        return skull;
    }

    public static ItemStack userBanned(){
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrier_meta = barrier.getItemMeta();
        assert barrier_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        barrier_meta.setDisplayName(MessageUtil.color("&cUsers banned"));
        barrier.setItemMeta(barrier_meta);
        return barrier;
    }

    public static ItemStack flags(){
        ItemStack banner = new ItemStack(Material.RED_BANNER);
        ItemMeta banner_meta = banner.getItemMeta();
        assert banner_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        banner_meta.setDisplayName(MessageUtil.color("&4Flags"));
        banner.setItemMeta(banner_meta);
        return banner;
    }

    public static ItemStack members(){
        ItemStack head = new ItemStack(Material.SPYGLASS);
        ItemMeta head_meta = head.getItemMeta();
        assert head_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        head_meta.setDisplayName(MessageUtil.color("&eMembers"));
        head.setItemMeta(head_meta);
        return head;
    }

    public static ItemStack homes(){
        ItemStack shield = new ItemStack(Material.COMPASS);
        ItemMeta shield_meta = shield.getItemMeta();
        assert shield_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        shield_meta.setDisplayName(MessageUtil.color("&aHomes"));
        shield.setItemMeta(shield_meta);
        return shield;
    }

    public static ItemStack memberOnMembers(Member member, ProtectionsPlugin plugin){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta skull_meta = skull.getItemMeta();
        assert skull_meta != null;
        skull_meta.setDisplayName(member.getName());
        PersistentDataContainer container = skull_meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, MEMBER_ON_MEMBERS_UUID), PersistentDataType.STRING, member.getUuid_member().toString());
        skull.setItemMeta(skull_meta);
        return skull;
    }

    public static ItemStack back(){
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrier_meta = barrier.getItemMeta();
        assert barrier_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        barrier_meta.setDisplayName(MessageUtil.color("&4&lBack"));
        barrier.setItemMeta(barrier_meta);
        return barrier;
    }

    public static ItemStack yes(){
        ItemStack pane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta pane_meta = pane.getItemMeta();
        assert pane_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        pane_meta.setDisplayName(MessageUtil.color("&a&lYes"));
        pane.setItemMeta(pane_meta);
        return pane;
    }

    public static ItemStack no(){
        ItemStack pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta pane_meta = pane.getItemMeta();
        assert pane_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        pane_meta.setDisplayName(MessageUtil.color("&c&lNo"));
        pane.setItemMeta(pane_meta);
        return pane;
    }

    public static ItemStack areYouSure(String uuid, ProtectionsPlugin plugin) {
        ItemStack bedrock = new ItemStack(Material.BEDROCK);
        ItemMeta bedrock_meta = bedrock.getItemMeta();
        assert bedrock_meta != null;
        // ADD THIS MESSAGE IN CONFIG
        bedrock_meta.setDisplayName(MessageUtil.color("&f&lAre you sure?"));
        PersistentDataContainer container = bedrock_meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, MEMBER_ON_MEMBERS_UUID), PersistentDataType.STRING, uuid);
        bedrock.setItemMeta(bedrock_meta);
        return bedrock;
    }
}
