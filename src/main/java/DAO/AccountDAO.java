package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account getAccountByName(String name)
    {
        Connection connection = ConnectionUtil.getConnection();
        Account account=null;   
        try {

            String sql = "select * from account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
           if (rs.next()) 
                account = new Account(rs.getInt("account_id"), rs.getString("username"),rs.getString("password"));
                          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }
    public Account getAccountById(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        Account account=null;   
        try {

            String sql = "select * from account where account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
           if (rs.next()) 
                account = new Account(rs.getInt("account_id"), rs.getString("username"),rs.getString("password"));
                          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }
    public List<Account> getAllAaccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "select * from Account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),rs.getString("password"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }
    
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "insert into account (username, password) values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // write preparedStatement's setString method here.
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
