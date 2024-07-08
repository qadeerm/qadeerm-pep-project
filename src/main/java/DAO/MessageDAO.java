package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Connection connection;
    public MessageDAO(){
        connection = ConnectionUtil.getConnection();

    }   
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "Select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),                       
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        //(posted_by, message_text, time_posted_epoch
                messages.add(message);
            }
        } 
            catch (SQLException e) {
                System.out.println(e.getMessage());
        }
        return messages;
    }
    public Message insertMessage(Message message) {    
        try {
            // Write SQL logic here. You should only be inserting with the name column, so
            // that the database may
            // automatically generate a primary key.
            //(posted_by, message_text, time_posted_epoch
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // write preparedStatement's setString method here.
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message getMessageById(int message_id) {
        try{
            String sql = "Select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return  (new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),                       
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
               // return new Message();        
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public Message deleteMessageById(int message_id) {
        try{
            String sql = "Select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Message message = 
                    new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),                       
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                sql = "delete from message where message_id = ?;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, message_id);
                preparedStatement.executeUpdate();
                return message; 
            }       
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public Message updateMessageById(Message message, int message_id) {
        try{
            String sql = "Select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                sql = "update Message set message_text = ? where message_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                // write preparedStatement's setString method here.
                preparedStatement.setString(1, message.getMessage_text());
                preparedStatement.setInt(2, message_id);            
                preparedStatement.executeUpdate();                            
                return message; 
            }       
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public List<Message> getAllMessagesFromAccoundId(int posted_by) {
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "Select * from message where posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            ResultSet rs = preparedStatement.executeQuery();;
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),                       
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        //(posted_by, message_text, time_posted_epoch
                messages.add(message);
            }
        } 
            catch (SQLException e) {
                System.out.println(e.getMessage());
        }
        return messages;
    }
}
