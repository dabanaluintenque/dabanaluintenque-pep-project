package Service;

import java.util.List;
import DAO.AccountDao;
import Model.Account;
import Model.Message;

public class AccountService {
    
     AccountDao accountDao = new AccountDao();

     public AccountService () {

        this.accountDao = new AccountDao();
     }

     public Account registerUser(Account account) {

        try {

           if(account.getUsername(). equals("") || account.getPassword().length() < 4) {

                return null;
            } else {

                return this.accountDao.inserUserAccount(account);
            } 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
     }

     public Account logUserIn(Account account) {

        return this.accountDao.selectUserAccount(account);
     }

     public Message createMessage( Message message) {
        
        try {

            if(message.getMessage_text().length() > 254 || message.getMessage_text().equals("")) {

                return null;
            } else {

                return this.accountDao.insertMessage(message);
            } 
        } catch( Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public Message readMessage (int message_id) {

        try {

        return this.accountDao.selecTextMessage(message_id);
        } catch(Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Message> readAllMessages() {

        try {

            return this.accountDao.selectAllMessages();
        } catch(Exception e) {
            System.out.println(e);
        }
       return null; 
    }

    public List<Message> readAllMessagesById( int account_id) {

        try {

            return this.accountDao.selectAllMessagesFromAccountId(account_id);
        } catch(Exception e) {

            System.out.println(e);
        }
        return null;
    }

    public Message updateMessage(Message message) {

        try {

            if(message.getMessage_text().length() > 254 || message.getMessage_text().equals("")) {

                return null;
            } else {

                return this.accountDao.updateMessage(message.getMessage_id(), message.getMessage_text());
            } 

        } catch(Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public Message deleteMessage (int message_id) {

        try {

            return this.accountDao.deleteMessage(message_id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
