package protections.Service;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import protections.DatabaseEntities.Protections.Protection;
import protections.ProtectionsPlugin;

import java.util.Map;

public class CheckIntersections {

    public static boolean check(Location location, PersistentDataContainer container, ProtectionsPlugin plugin){
        Map<Location, Protection> protections_placed = ProtectionsPlugin.protections;
        double protection_to_place_x = location.getX();
        double protection_to_place_z = location.getZ();
        double protection_to_place_x_dimension = (double) container.get(new NamespacedKey(plugin, GiveProtection.X_DIMENSION), PersistentDataType.INTEGER);
        double protection_to_place_z_dimension = (double) container.get(new NamespacedKey(plugin, GiveProtection.Z_DIMENSION), PersistentDataType.INTEGER);

        double protection_to_place_min_x = protection_to_place_x - protection_to_place_x_dimension;
        double protection_to_place_max_x = protection_to_place_x + protection_to_place_x_dimension;
        double protection_to_place_min_z = protection_to_place_z - protection_to_place_z_dimension;
        double protection_to_place_max_z = protection_to_place_z + protection_to_place_z_dimension;
        for (Map.Entry<Location, Protection> entry : protections_placed.entrySet()){
            double protection_placed_x = entry.getKey().getX();
            double protection_placed_z = entry.getKey().getZ();
            double protection_placed_x_dimension = entry.getValue().getBlock_coordinate().getX_dimension();
            double protection_placed_z_dimension = entry.getValue().getBlock_coordinate().getZ_dimension();

            double protection_placed_min_x = protection_placed_x - protection_placed_x_dimension;
            double protection_placed_max_x = protection_placed_x + protection_placed_x_dimension;
            double protection_placed_min_z = protection_placed_z - protection_placed_z_dimension;
            double protection_placed_max_z = protection_placed_z + protection_placed_z_dimension;
            if ((protection_to_place_min_z <= protection_placed_max_z && protection_to_place_max_z >= protection_placed_min_z)
            && (protection_to_place_min_x <= protection_placed_max_x && protection_to_place_max_x >= protection_placed_min_x)){
                return false;
            }
        }
        return true;
    }
}
