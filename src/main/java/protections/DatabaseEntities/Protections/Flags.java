package protections.DatabaseEntities.Protections;

public class Flags {
    private long id;
    private boolean damage_mobs;
    private boolean mob_spawning;
    private boolean block_break;
    private boolean block_place;
    private boolean ender_pearl;
    private boolean item_drop;
    private boolean item_pickup;
    private boolean leaf_decay;
    private boolean explosion;
    private boolean pvp;
    private boolean tnt;

    public Flags(long id, boolean damage_mobs, boolean mob_spawning, boolean block_break, boolean block_place, boolean ender_pearl, boolean item_drop, boolean item_pickup, boolean leaf_decay, boolean explosion, boolean pvp, boolean tnt) {
        this.id = id;
        this.damage_mobs = damage_mobs;
        this.mob_spawning = mob_spawning;
        this.block_break = block_break;
        this.block_place = block_place;
        this.ender_pearl = ender_pearl;
        this.item_drop = item_drop;
        this.item_pickup = item_pickup;
        this.leaf_decay = leaf_decay;
        this.explosion = explosion;
        this.pvp = pvp;
        this.tnt = tnt;
    }

    public Flags(boolean damage_mobs, boolean mob_spawning, boolean block_break, boolean block_place, boolean ender_pearl, boolean item_drop, boolean item_pickup, boolean leaf_decay, boolean explosion, boolean pvp, boolean tnt) {
        this.damage_mobs = damage_mobs;
        this.mob_spawning = mob_spawning;
        this.block_break = block_break;
        this.block_place = block_place;
        this.ender_pearl = ender_pearl;
        this.item_drop = item_drop;
        this.item_pickup = item_pickup;
        this.leaf_decay = leaf_decay;
        this.explosion = explosion;
        this.pvp = pvp;
        this.tnt = tnt;
    }

    public long getId() {
        return id;
    }

    public boolean isDamage_mobs() {
        return damage_mobs;
    }

    public boolean isMob_spawning() {
        return mob_spawning;
    }

    public boolean isBlock_break() {
        return block_break;
    }

    public boolean isBlock_place() {
        return block_place;
    }

    public boolean isEnder_pearl() {
        return ender_pearl;
    }

    public boolean isItem_drop() {
        return item_drop;
    }

    public boolean isItem_pickup() {
        return item_pickup;
    }

    public boolean isLeaf_decay() {
        return leaf_decay;
    }

    public boolean isExplosion() {
        return explosion;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isTnt() {
        return tnt;
    }

    public void setDamage_mobs(boolean damage_mobs) {
        this.damage_mobs = damage_mobs;
    }

    public void setMob_spawning(boolean mob_spawning) {
        this.mob_spawning = mob_spawning;
    }

    public void setBlock_break(boolean block_break) {
        this.block_break = block_break;
    }

    public void setBlock_place(boolean block_place) {
        this.block_place = block_place;
    }

    public void setEnder_pearl(boolean ender_pearl) {
        this.ender_pearl = ender_pearl;
    }

    public void setItem_drop(boolean item_drop) {
        this.item_drop = item_drop;
    }

    public void setItem_pickup(boolean item_pickup) {
        this.item_pickup = item_pickup;
    }

    public void setLeaf_decay(boolean leaf_decay) {
        this.leaf_decay = leaf_decay;
    }

    public void setExplosion(boolean explosion) {
        this.explosion = explosion;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public void setTnt(boolean tnt) {
        this.tnt = tnt;
    }

    public void setId(long id) {
        this.id = id;
    }
}
