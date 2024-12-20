package protections.DatabaseEntities.Protections;


import java.time.LocalDateTime;

public class Coordinate {
    private long id;
    private double x;
    private double y;
    private double z;
    private double x_dimension;
    private double z_dimension;
    private final LocalDateTime date_the_block_was_placed;

    public Coordinate(long id, double x, double y, double z, double x_dimension, double z_dimension, LocalDateTime date_the_block_was_placed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.x_dimension = x_dimension;
        this.z_dimension = z_dimension;
        this.date_the_block_was_placed = date_the_block_was_placed;
    }

    public Coordinate(double x, double y, double z, double x_dimension, double z_dimension, LocalDateTime date_the_block_was_placed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.x_dimension = x_dimension;
        this.z_dimension = z_dimension;
        this.date_the_block_was_placed = date_the_block_was_placed;
    }

    public long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getX_dimension() {
        return x_dimension;
    }

    public double getZ_dimension() {
        return z_dimension;
    }

    public LocalDateTime getDate_the_block_was_placed() {
        return date_the_block_was_placed;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setX_dimension(double x_dimension) {
        this.x_dimension = x_dimension;
    }

    public void setZ_dimension(double z_dimension) {
        this.z_dimension = z_dimension;
    }

    public void setId(long id) {
        this.id = id;
    }
}
