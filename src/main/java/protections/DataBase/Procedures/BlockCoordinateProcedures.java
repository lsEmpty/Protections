package protections.DataBase.Procedures;

import protections.ProtectionsPlugin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BlockCoordinateProcedures {
    public static void createNewBlockCoordinate(double x, double y, double z, int x_dimension, int z_dimension, LocalDateTime date){
        String query = "{call create_new_block_coordinate(?, ?, ?, ?, ?, ?)}";
        try(Connection connection = ProtectionsPlugin.connection.getConnection();
            CallableStatement statement = connection.prepareCall(query)){
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            statement.setInt(4, x_dimension);
            statement.setInt(5, z_dimension);
            statement.setObject(6, date);
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error: "+e);
        }
    }

    public static long getIdFromBlockCoordinateWithCoordinate(double x, double y, double z){
        long id = 0;
        String query = "{call get_id_from_block_coordinate_with_coordinate(?, ?, ?)}";
        try(Connection connection = ProtectionsPlugin.connection.getConnection();
            CallableStatement statement = connection.prepareCall(query)){
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            try (ResultSet resultSet = statement.executeQuery();){
                while (resultSet.next()){
                    id = resultSet.getLong("id");
                }
            }
        }catch (SQLException e){
            System.err.println("Error: "+e);
        }
        return id;
    }

    public static long getIdFromProtectionsWithBlockCoordinateId(long id){
        long id_return = 0;
        String query = "{call get_id_from_protections_with_block_coordinate_id(?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
        CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    id_return = resultSet.getLong("id");
                }
            }
        }catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return id_return;
    }
}
