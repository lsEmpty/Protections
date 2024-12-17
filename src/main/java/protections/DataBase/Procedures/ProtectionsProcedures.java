package protections.DataBase.Procedures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import protections.DatabaseEntities.Protections.Coordinate;
import protections.DatabaseEntities.Protections.Flags;
import protections.DatabaseEntities.Protections.MenaInformation;
import protections.DatabaseEntities.Protections.Protection;
import protections.Entities.Grid.ProtectionGrid;
import protections.Entities.Menas.Mena;
import protections.ProtectionsPlugin;
import protections.Utils.BinaryUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProtectionsProcedures {
    public static Map<Location, Protection> getProtectionsInUse(){
        Map<Location, Protection> mapProtections = new HashMap<>();
        String query = "{call get_protections_in_use()}";
        try(Connection connection = ProtectionsPlugin.connection.getConnection();
            CallableStatement statement = connection.prepareCall(query);
            ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                long id = resultSet.getLong("id_protection");
                String name = resultSet.getString("name");
                boolean in_use = resultSet.getBoolean("in_use");
                String owner = resultSet.getString("owner");
                UUID owner_uuid = BinaryUtil.bytesToUUID(resultSet.getBytes("owner_uuid"));
                String world = resultSet.getString("world");
                long id_block_coordinates = resultSet.getLong("id_block_coordinates");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                int x_dimension = resultSet.getInt("x_dimension");
                int z_dimension = resultSet.getInt("z_dimension");
                long id_flags = resultSet.getLong("id_flags");
                boolean damage_mobs = resultSet.getBoolean("damage_mobs");
                boolean mob_spawning = resultSet.getBoolean("mob_spawning");
                boolean block_break = resultSet.getBoolean("block_break");
                boolean block_place = resultSet.getBoolean("block_place");
                boolean ender_pearl = resultSet.getBoolean("ender_pearl");
                boolean item_drop = resultSet.getBoolean("item_drop");
                boolean item_pickup = resultSet.getBoolean("item_pickup");
                boolean leaf_decay = resultSet.getBoolean("leaf_decay");
                boolean explosion = resultSet.getBoolean("explosion");
                boolean pvp = resultSet.getBoolean("pvp");
                boolean tnt = resultSet.getBoolean("tnt");
                String mena_name = resultSet.getString("mena_name");
                String mena_name_to_give= resultSet.getString("mena_name_to_give");
                String mena_material = resultSet.getString("mena_material");
                LocalDateTime date = resultSet.getObject("date", LocalDateTime.class);
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                Coordinate coordinate = new Coordinate(id_block_coordinates, x, y, z, x_dimension, z_dimension, date);
                Flags flags = new Flags(id_flags, damage_mobs, mob_spawning, block_break, block_place, ender_pearl, item_drop, item_pickup, leaf_decay, explosion, pvp, tnt);
                MenaInformation menaInformation = new MenaInformation(mena_name, mena_name_to_give, mena_material);
                Protection protection = new Protection(id, name, in_use, owner, owner_uuid, world, coordinate, flags, menaInformation);
                mapProtections.put(location , protection);
                ProtectionGrid.addProtectionToGrid(protection);
            }
        }catch (SQLException e){
            System.err.println("Error:" + e);
        }
        return mapProtections;
    }

    public static void createNewProtection(String name, boolean in_use, String owner, UUID owner_uuid, String world, long id_block_coordinate, long id_flags, long id_mena_information){
        String query = "{call create_new_protection(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setString(1, name);
            statement.setBoolean(2, in_use);
            statement.setString(3, owner);
            statement.setBytes(4, BinaryUtil.uuidToBytes(owner_uuid));
            statement.setString(5, world);
            statement.setLong(6, id_block_coordinate);
            statement.setLong(7, id_flags);
            statement.setLong(8, id_mena_information);
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error:" + e);
        }
    }

    public static void changeStateProtection(long id, boolean in_use){
        String query = "{call change_state_protection(?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, id);
            statement.setBoolean(2, in_use);
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error:" + e);
        }
    }

    public static void changeOwnerProtection(long id, String name, String owner, UUID owner_uuid){
        String query = "{call change_owner_protection(?, ?, ?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, id);
            statement.setString(2, name);
            statement.setString(3, owner);
            statement.setBytes(4, BinaryUtil.uuidToBytes(owner_uuid));
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error:" + e);
        }
    }
}
