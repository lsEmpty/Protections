package protections.DataBase.Procedures;

import protections.DatabaseEntities.Protections.Flags;
import protections.ProtectionsPlugin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class FlagsProcedures {
    public static void create_flags(Flags flags){
        String query = "{call create_flags(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try(Connection connection = ProtectionsPlugin.connection.getConnection();
            CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, flags.getId());
            statement.setBoolean(2, flags.isDamage_mobs());
            statement.setBoolean(3, flags.isMob_spawning());
            statement.setBoolean(4, flags.isBlock_break());
            statement.setBoolean(5, flags.isBlock_place());
            statement.setBoolean(6, flags.isEnder_pearl());
            statement.setBoolean(7, flags.isItem_drop());
            statement.setBoolean(8, flags.isItem_pickup());
            statement.setBoolean(9, flags.isLeaf_decay());
            statement.setBoolean(10, flags.isExplosion());
            statement.setBoolean(11, flags.isPvp());
            statement.setBoolean(12, flags.isTnt());
            statement.execute();
        }catch(SQLException e){
            System.err.println("Error: " + e);
        }
    }
}
