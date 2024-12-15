package protections.Entities.Grid;


import protections.DatabaseEntities.Protections.Protection;

public class ProtectionRegion {
    private final long minX, maxX, minZ, maxZ;
    private Protection protection;

    public ProtectionRegion(long minX, long maxX, long minZ, long maxZ, Protection protection) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.protection = protection;
    }

    public boolean contains(long x, long z) {
        return x >= minX && x <= maxX
                && z >= minZ && z <= maxZ;
    }

    public long getMinX() { return minX; }
    public long getMaxX() { return maxX; }
    public long getMinZ() { return minZ; }
    public long getMaxZ() { return maxZ; }

    public Protection getProtection() {
        return protection;
    }
}
