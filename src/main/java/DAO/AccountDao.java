package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AccountDao {
    


    private Connection connection = ConnectionUtil.getConnection();


    public Account selectUserAccount (Account account) {

        try {

            String sql = "SELECT * FROM account WHERE username = ?  AND password = ?";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setString(1, account.getUsername());
            stp.setString(2, account.getPassword());

            ResultSet result = stp.executeQuery();


            while (result.next()) {

                Account returnAccount = new Account(result.getInt("account_id"),
                    result.getString("username"),
                    result.getString("password")
                );

                return returnAccount;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
   
    public Account inserUserAccount(Account account ) {

        try {

            String sql = "INSERT INTO account (username, password) VALUES(?,  ?)";

            PreparedStatement stp = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stp.setString(1, account.getUsername());
            stp.setString(2, account.getPassword());

            stp.executeUpdate();

            ResultSet result = stp.getGeneratedKeys();


            if(result.next()) {

                int account_id = (int) result.getLong(1);
                account.setAccount_id(account_id);

                return account;
            }

        }catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Account deleteUserAccount(Account account) {

        try {

            String sql = "DELETE FROM account WHERE account_id = ?";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setInt(1, account.getAccount_id());

               if (stp.executeUpdate() > 0) {

                System.out.println("The user account has been deleted");

                return account;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Message insertMessage(Message message ) {

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            PreparedStatement stp = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stp.setInt(1, message.getPosted_by());
            stp.setString(2, message.getMessage_text());
            stp.setLong(3, message.getTime_posted_epoch());

            if( stp.executeUpdate() > 0) {

                ResultSet result = stp.getGeneratedKeys();

                if(result.next()) {

                    int message_id = (int) result.getLong(1);

                    message.setMessage_id(message_id);

                    return message;
                }
            }
       
        }  catch(Exception e) {

        System.out.println( e);

        }
        return null;
    }


    public Message selecTextMessage(int message_id) {

        try {

            String sql = "SELECT * FROM message WHERE message_id = ?";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setInt(1, message_id);

            ResultSet result = stp.executeQuery();

            while(result.next()) {

                Message message = new Message (

                    result.getInt("message_id"),
                    result.getInt("posted_by"),
                    result.getString("message_text"),
                    result.getLong("time_posted_epoch"));

                return message;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> selectAllMessages() {

        List<Message> messages = new ArrayList<>();

        try {

            String sql = "SELECT * FROM message";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            ResultSet result = stp.executeQuery();

            while(result.next()) {

                Message message = new Message (

                result.getInt("message_id"),
                result.getInt("posted_by"),
                result.getString("message_text"),
                result.getLong("time_posted_epoch")
                );

                messages.add(message);
            }

            return messages;
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int message_id) {

        try {

            Message message = selecTextMessage(message_id);

            if (message == null) {
                return null;
            }

            String sql = "DELETE FROM message WHERE message_id = ? ";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setInt(1, message_id);

            if (stp.executeUpdate() > 0) {

                System.out.println("The message hass been deleted");

                return message;
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage (int message_id, String message_text) {

        try {

            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setString(1, message_text);
            stp.setInt(2, message_id);

            //int updated = stp.executeUpdate();

            if (stp.executeUpdate() > 0) {

                return selecTextMessage(message_id);
            }
        }catch(Exception e) {

            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> selectAllMessagesFromAccountId(int account_id) {

        List<Message> messages = new ArrayList<>();

        try {

            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement stp = this.connection.prepareStatement(sql);

            stp.setInt(1, account_id);

            ResultSet result = stp.executeQuery();

            while (result.next()) {

                Message message = new Message (
                    result.getInt("message_id"),
                    result.getInt("posted_by"),
                    result.getString("message_text"),
                    result.getLong("time_posted_epoch")
                );

                messages.add(message);
            }
            System.out.println(messages.toString());

            return messages;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    } 

}  
   
   
   
   
   
   
   
   
   
   
   
   
   
    /*public void createAccount(Account account) {

        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "INSERT INTO Account (username, password) VALUES(?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, account.getUsername());
            pst.setString(2, account.getPassword());

            pst.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public Account getAccountByUsername (String username) {

        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM Account  WHERE username = ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, username);

            ResultSet result = pst.executeQuery();

            if( result.next()) {

                int id = result.getInt("account_id");

                String userPassword = result.getString("password");

                return new Account( id, username, userPassword);
            }


        } catch(SQLException e) {

            e.printStackTrace();
        }
        return null;
    }
} */
