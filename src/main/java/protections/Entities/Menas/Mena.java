package protections.Entities.Menas;

public class Mena {
    private String name;
    private String name_to_give;
    private String block;
    private Dimension dimension;

    public Mena() {
    }

    public Mena(String name, String name_to_give, String block, Dimension dimension) {
        this.name = name;
        this.name_to_give = name_to_give;
        this.block = block;
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public String getName_to_give() {
        return name_to_give;
    }

    public String getBlock() {
        return block;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_to_give(String name_to_give) {
        this.name_to_give = name_to_give;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
}
