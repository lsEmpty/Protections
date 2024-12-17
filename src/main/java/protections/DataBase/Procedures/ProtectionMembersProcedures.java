package protections.DataBase.Procedures;

import protections.DatabaseEntities.Protections.Coordinate;
import protections.DatabaseEntities.Protections.Flags;
import protections.DatabaseEntities.Protections.Member;
import protections.DatabaseEntities.Protections.Protection;
import protections.ProtectionsPlugin;
import protections.Utils.BinaryUtil;

import javax.swing.plaf.SeparatorUI;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ProtectionMembersProcedures {
    public static void add_member(long id_protection, long id_block_coordinate, long id_flags, long id_protection_member){
        String query = "{call add_member(?, ?, ?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, id_protection);
            statement.setLong(2, id_block_coordinate);
            statement.setLong(3, id_flags);
            statement.setLong(4, id_protection_member);
            statement.execute();
        }catch (SQLException e){
            System.err.println("Error: " + e);
        }
    }

    public static Map<Long, List<Member>> get_all_members(){
        String query = "{call get_all_members()}";
        Map<Long, List<Member>> members = new HashMap<>();
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
            CallableStatement statement = connection.prepareCall(query);
            ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                // Protections
                long id = resultSet.getLong("id_protection");
                // Protection Member
                long id_protection_member = resultSet.getLong("id_protection_member");
                String name_protection_member = resultSet.getString("name_protection_member");
                UUID uuid_protection_member = BinaryUtil.bytesToUUID(resultSet.getBytes("uuid_protection_member"));
                Member member = new Member(id_protection_member, name_protection_member, uuid_protection_member);
                if (!members.containsKey(id)){
                    members.put(id, new ArrayList<>());
                }
                members.get(id).add(member);
            }
        }catch (SQLException e){
            System.err.println("Error: " + e);
        }
        return members;
    }

    public static void remove_member_from_protection(long id_protection, long id_protection_member){
        String query = "{call remove_member_from_protection(?, ?)}";
        try (Connection connection = ProtectionsPlugin.connection.getConnection();
             CallableStatement statement = connection.prepareCall(query)){
            statement.setLong(1, id_protection);
            statement.setLong(2, id_protection);
            statement.execute();
        }catch(SQLException e){
            System.err.println("Error: " + e);
        }
    }
}
