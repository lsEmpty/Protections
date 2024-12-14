package protections.Service;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.Entities.Menas.Mena;
import protections.ProtectionsPlugin;
import protections.Utils.MessageUtil;

import java.util.List;

import static protections.ProtectionsPlugin.prefix;

public class GiveProtection {
    public static final String X_DIMENSION = "x_dimension";
    public static final String Y_DIMENSION = "y_dimension";
    public ItemStack give(Player sender, String name_to_give, ProtectionsPlugin plugin){
        Mena mena = verifyBlock(name_to_give);
        if (mena == null){
            sender.sendMessage(prefix+MessageUtil.Color("&eYou most place a mena that exists."));
            return null;
        }
        Material material_set = null;
        for (Material material : Material.values()){
            if (material.toString().equalsIgnoreCase(mena.getBlock())){
                material_set = material;
            }
        }
        if (material_set == null){
            sender.sendMessage(prefix+MessageUtil.Color("&eError in data config."));
            return null;
        }
        ItemStack protection = new ItemStack(material_set);
        ItemMeta protection_meta = protection.getItemMeta();
        protection_meta.addEnchant(Enchantment.LUCK, 1, true);
        protection_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        protection_meta.setDisplayName(MessageUtil.Color(mena.getName()));
        PersistentDataContainer container = protection_meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, X_DIMENSION), PersistentDataType.INTEGER, mena.getDimension().getX());
        container.set(new NamespacedKey(plugin, Y_DIMENSION), PersistentDataType.INTEGER, mena.getDimension().getY());
        protection.setItemMeta(protection_meta);
        sender.sendMessage(prefix+MessageUtil.Color("&aMena delivered."));
        return protection;
    }

    public Mena verifyBlock(String name_to_give){
        List<Mena> menas = ProtectionsPlugin.mainConfigManager.getMenas();
        for (Mena mena : menas){
            if (mena.getName_to_give().equalsIgnoreCase(name_to_give)){
                return mena;
            }
        }
        return null;
    }
}
