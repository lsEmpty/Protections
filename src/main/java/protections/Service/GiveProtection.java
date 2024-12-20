package protections.Service;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
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
    public static final String NAME = "MENA_NAME";
    public static final String NAME_TO_GIVE = "MENA_NAME_TO_GIVE";
    public static final String MATERIAL = "MENA_MATERIAL";
    public static final String X_DIMENSION = "x_dimension";
    public static final String Z_DIMENSION = "z_dimension";
    public ItemStack give(CommandSender sender, String name_to_give, ProtectionsPlugin plugin){
        Mena mena = verifyBlock(name_to_give);
        if (mena == null){
            sender.sendMessage(prefix+MessageUtil.color("&eYou most place a mena that exists."));
            return null;
        }
        Material material_set = null;
        for (Material material : Material.values()){
            if (material.toString().equalsIgnoreCase(mena.getBlock())){
                material_set = material;
            }
        }
        if (material_set == null){
            sender.sendMessage(prefix+MessageUtil.color("&eError in data config."));
            return null;
        }
        if (sender instanceof Player){
            // ADD THIS MESSAGE IN CONFIG
            sender.sendMessage(prefix+MessageUtil.color("&aMena delivered."));
        }
        return createMena(plugin, material_set, mena);
    }

    public ItemStack giveOnlyProtection(String name_to_give, ProtectionsPlugin plugin){
        Mena mena = verifyBlock(name_to_give);
        Material material_set = null;
        for (Material material : Material.values()){
            if (material.toString().equalsIgnoreCase(mena.getBlock())){
                material_set = material;
            }
        }
        return createMena(plugin, material_set, mena);
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

    private ItemStack createMena(ProtectionsPlugin plugin, Material material_set, Mena mena){
        ItemStack protection = new ItemStack(material_set);
        ItemMeta protection_meta = protection.getItemMeta();
        protection_meta.addEnchant(Enchantment.LUCK, 1, true);
        protection_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        protection_meta.setDisplayName(MessageUtil.color(mena.getName()));
        PersistentDataContainer container = protection_meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, NAME), PersistentDataType.STRING, mena.getName());
        container.set(new NamespacedKey(plugin, NAME_TO_GIVE), PersistentDataType.STRING, mena.getName_to_give());
        container.set(new NamespacedKey(plugin, MATERIAL), PersistentDataType.STRING, mena.getBlock());
        container.set(new NamespacedKey(plugin, X_DIMENSION), PersistentDataType.INTEGER, mena.getDimension().getX());
        container.set(new NamespacedKey(plugin, Z_DIMENSION), PersistentDataType.INTEGER, mena.getDimension().getZ());
        protection.setItemMeta(protection_meta);
        return protection;
    }
}
