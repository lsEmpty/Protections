package protections.DataBase.Procedures;

import protections.DatabaseEntities.Protections.Flags;
import protections.ProtectionsPlugin;

import java.sql.*;

public class FlagsProcedures {
    public static long create_flags_and_get_id(Flags flags){
        long id = 0;
        String query = "SELECT create_flags_and_get_id(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = ProtectionsPlugin.connection.getConnection();
            PreparedStatement statement = connection.prepareCall(query)){
            statement.setBoolean(1, flags.isDamage_mobs());
            statement.setBoolean(2, flags.isMob_spawning());
            statement.setBoolean(3, flags.isBlock_break());
            statement.setBoolean(4, flags.isBlock_place());
            statement.setBoolean(5, flags.isEnder_pearl());
            statement.setBoolean(6, flags.isItem_drop());
            statement.setBoolean(7, flags.isItem_pickup());
            statement.setBoolean(8, flags.isLeaf_decay());
            statement.setBoolean(9, flags.isExplosion());
            statement.setBoolean(10, flags.isPvp());
            statement.setBoolean(11, flags.isTnt());
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    id = resultSet.getLong(1);
                }
            }
        }catch(SQLException e){
            System.err.println("Error: " + e);
        }
        return id;
    }
}
