package protections.DataBase.Procedures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import protections.DatabaseEntities.Protections.Coordinate;
import protections.DatabaseEntities.Protections.Protection;
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
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                boolean in_use = resultSet.getBoolean("in_use");
                String owner = resultSet.getString("owner");
                UUID owner_uuid = BinaryUtil.bytesToUUID(resultSet.getBytes("owner_uuid"));
                String world = resultSet.getString("world");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                int x_dimension = resultSet.getInt("x_dimension");
                int z_dimension = resultSet.getInt("z_dimension");
                LocalDateTime date = resultSet.getObject("date", LocalDateTime.class);
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                Coordinate coordinate = new Coordinate(x, y, z, x_dimension, z_dimension, date);
                Protection protection = new Protection(id, name, in_use, owner, owner_uuid, world, coordinate);
                mapProtections.put(location , protection);
            }
        }catch (SQLException e){
            System.err.println("Error:" + e);
        }
        return mapProtections;
    }

    public static void createNewProtection(String name, boolean in_use, String owner, UUID owner_uuid, String world, long id_block_coordinate){
        String query = "{call create_new_protection(?, ?, ?, ?, ?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setString(1, name);
            statement.setBoolean(2, in_use);
            statement.setString(3, owner);
            statement.setBytes(4, BinaryUtil.uuidToBytes(owner_uuid));
            statement.setString(5, world);
            statement.setLong(6, id_block_coordinate);
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
