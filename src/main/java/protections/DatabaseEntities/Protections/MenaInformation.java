package protections.DatabaseEntities.Protections;

public class MenaInformation {
    private long id;
    private String name;
    private String name_to_give;
    private String material;

    public MenaInformation(long id, String name, String name_to_give, String material) {
        this.id = id;
        this.name = name;
        this.name_to_give = name_to_give;
        this.material = material;
    }

    public MenaInformation(String name, String name_to_give, String material) {
        this.name = name;
        this.name_to_give = name_to_give;
        this.material = material;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName_to_give() {
        return name_to_give;
    }

    public String getMaterial() {
        return material;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_to_give(String name_to_give) {
        this.name_to_give = name_to_give;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setId(long id) {
        this.id = id;
    }
}
