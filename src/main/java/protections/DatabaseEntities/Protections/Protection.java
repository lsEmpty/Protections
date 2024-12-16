package protections.DatabaseEntities.Protections;

import java.util.UUID;

public class Protection {
    private long id;
    private String name;
    private boolean in_use;
    private String owner;
    private UUID owner_uuid;
    private String world;

    private Coordinate block_coordinate;
    private Flags flags;

    public Protection(long id, String name, boolean in_use, String owner, UUID owner_uuid, String world, Coordinate block_coordinate, Flags flags) {
        this.id = id;
        this.name = name;
        this.in_use = in_use;
        this.owner = owner;
        this.owner_uuid = owner_uuid;
        this.world = world;
        this.block_coordinate = block_coordinate;
        this.flags = flags;
    }

    public Protection(String name, boolean in_use, String owner, UUID owner_uuid, String world, Coordinate block_coordinate, Flags flags) {
        this.name = name;
        this.in_use = in_use;
        this.owner = owner;
        this.owner_uuid = owner_uuid;
        this.world = world;
        this.block_coordinate = block_coordinate;
        this.flags = flags;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIn_use() {
        return in_use;
    }

    public String getOwner() {
        return owner;
    }

    public UUID getOwner_uuid() {
        return owner_uuid;
    }

    public String getWorld() {
        return world;
    }

    public Coordinate getBlock_coordinate() {
        return block_coordinate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIn_use(boolean in_use) {
        this.in_use = in_use;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setOwner_uuid(UUID owner_uuid) {
        this.owner_uuid = owner_uuid;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setBlock_coordinate(Coordinate block_coordinate) {
        this.block_coordinate = block_coordinate;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }
}
