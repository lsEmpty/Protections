package protections.DatabaseEntities.Protections;

import java.util.UUID;

public class Member {
    private long id;
    private String name;
    private UUID uuid_member;

    public Member(long id, String name, UUID uuid_member) {
        this.id = id;
        this.name = name;
        this.uuid_member = uuid_member;
    }

    public Member(String name, UUID uuid_member) {
        this.name = name;
        this.uuid_member = uuid_member;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid_member() {
        return uuid_member;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid_member(UUID uuid_member) {
        this.uuid_member = uuid_member;
    }
}
