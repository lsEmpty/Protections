package protections.Entities.Grid;

import protections.DatabaseEntities.Protections.Protection;
import protections.ProtectionsPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProtectionGrid {
    private final int cellSize; // Cell Size
    private static Map<String, List<ProtectionRegion>> grid;

    public ProtectionGrid(int cellSize) {
        this.cellSize = cellSize;
        grid = new HashMap<>();
    }

    // Generate cell key (for coordinates)
    private String getCellKey(long x, long z) {
        long cellX = x / cellSize;
        long cellZ = z / cellSize;
        return cellX + "," + cellZ;
    }

    // Add a protection to system
    public void addProtection(ProtectionRegion region) {
        String cellKey = getCellKey(region.getMinX(), region.getMinZ());
        grid.computeIfAbsent(cellKey, k -> new ArrayList<>()).add(region);
    }

    // Get all relevant protections for a location
    public List<ProtectionRegion> getNearbyProtections(int x, int z) {
        List<ProtectionRegion> nearbyProtections = new ArrayList<>();
        int cellX = x / cellSize;
        int cellZ = z / cellSize;

        // Review current cell and 8 adjacent cells
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                String key = (cellX + dx) + "," + (cellZ + dz);
                List<ProtectionRegion> protections = grid.get(key);
                if (protections != null) {
                    nearbyProtections.addAll(protections);
                }
            }
        }
        return nearbyProtections;
    }

    public static void addProtectionToGrid(Protection protection){
        long min_x = (long) (protection.getBlock_coordinate().getX() - protection.getBlock_coordinate().getX_dimension());
        long max_x = (long) (protection.getBlock_coordinate().getX() + protection.getBlock_coordinate().getX_dimension());
        long min_z = (long) (protection.getBlock_coordinate().getZ() - protection.getBlock_coordinate().getZ_dimension());
        long max_z = (long) (protection.getBlock_coordinate().getZ() + protection.getBlock_coordinate().getZ_dimension());
        ProtectionsPlugin.protectionGrid.addProtection(new ProtectionRegion(min_x, max_x, min_z, max_z, protection));
    }

    public static void removeProtectionToGrid(long minX, long maxX, long minZ, long maxZ){
        long startX = minX / 100;
        long endX = maxX / 100;
        long startZ = minZ / 100;
        long endZ = maxZ / 100;

        for (long gridX = startX; gridX <= endX; gridX++) {
            for (long gridZ = startZ; gridZ <= endZ; gridZ++) {
                String cellKey = gridX + "," + gridZ;
                List<ProtectionRegion> protections = grid.get(cellKey);
                if (protections != null) {
                    protections.removeIf(region -> region.intersects(minX, maxX, minZ, maxZ));
                    if (protections.isEmpty()) {
                        grid.remove(cellKey);
                    }
                }
            }
        }
    }
}
