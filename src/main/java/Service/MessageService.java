package Service;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.*;

import DAO.AccountDAO;
import DAO.MessageDAO;

public class MessageService {
    public MessageDAO messageDAO;
    public  AccountDAO accountDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    public MessageService (MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }
    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }
    public Message updateMessageById(Message message, int id) {
        return messageDAO.updateMessageById(message, id);
    }
    public List<Message> getAllMessagesFromAccountId(int id) {
        return messageDAO.getAllMessagesFromAccoundId(id);
    }
    public Message addMessage(Message message) {  
        Optional<Account> optional = Optional.ofNullable(accountDAO.getAccountById(message.getPosted_by()));
        if (optional.isPresent())
            return messageDAO.insertMessage(message);        
        else         
            return null;                  
    }
}
