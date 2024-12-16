package protections.DataBase.Procedures;

import protections.DatabaseEntities.Protections.MenaInformation;
import protections.Entities.Menas.Mena;
import protections.ProtectionsPlugin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenaInformationProcedures {
    public static void create_mena_information(MenaInformation menaInformation){
        String query = "{call create_mena_information(?, ?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setString(1, menaInformation.getName());
            statement.setString(2, menaInformation.getName_to_give());
            statement.setString(3, menaInformation.getMaterial());
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error: " + e);
        }
    }

    public static List<MenaInformation> get_all_mena_information(){
        List<MenaInformation> menas = new ArrayList<>();
        String query = "call get_all_mena_information()";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query);
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String name_to_give = resultSet.getString("name_to_give");
                String material = resultSet.getString("material");
                menas.add(new MenaInformation(id, name, name_to_give, material));
            }
        }catch (SQLException e){
            System.err.println("Error: " + e);
        }
        return menas;
    }
}
