package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.util.List;
import java.util.Optional;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::userRegistration);
        app.get("/users", this::getAllAccounts);
        app.post("/login", this::userLogin);
        app.post("/messages", this::postMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::retrieveMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountId);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */    
    //insert into account (username varchar(255), password) values ('testuser1', 'password');
    //insert into message (posted_by, message_text varchar(255), time_posted_epoch bigint) values (1,'test message 1',1669947792);
    
    private void userLogin(Context context) throws JsonProcessingException {
        boolean loginSuccess = false;
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Optional<Account> optional = Optional.ofNullable(accountService.getAccount(account));
        if (optional.isPresent()) {     
            Account accountActual = optional.get(); 
            account.setAccount_id(accountActual.getAccount_id());   
            if (account.equals(accountActual)){
                loginSuccess = true;
                context.json(mapper.writeValueAsString(optional.get()));
            }
        }
        if (loginSuccess)
            context.status(200);
        else
            context.status(401);
    }
    private void userRegistration(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        boolean posted = false;
        if (account.getUsername().length()>0) {
            Optional<Account> optionalDuplicate = Optional.ofNullable(accountService.getAccount(account));
            if (optionalDuplicate.isEmpty()) {
                if (account.getPassword().length()>=4){
                    Optional<Account> optional = Optional.ofNullable(accountService.addAccount(account));
                    if (optional.isPresent())
                    {
                        posted=true;
                        context.json(mapper.writeValueAsString(optional.get()));
                    }
                }                            
            }
        } 
        if (posted)
            context.status(200);
        else
            context.status(400);

    }
    private void postMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Optional<Account> optional = Optional.ofNullable(accountService.getAccountById(message.getPosted_by()));
        boolean posted = false;
        if (optional.isPresent()) {
            if ((message.getMessage_text().length()>0) && (message.getMessage_text().length()<=255)){
                Optional<Message> optionalMessage = Optional.ofNullable(messageService.addMessage(message));
                if (optionalMessage.isPresent()){
                    posted = true;
                   // context.status(200);
                    context.json(mapper.writeValueAsString(optionalMessage.get()));
                }
            }
        }
        if (posted)
            context.status(200);
        else
            context.status(400);
    }
    private void updateMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        boolean updated = false;
        Message message = mapper.readValue(context.body(), Message.class);
        if ((message.getMessage_text().length()>0) && (message.getMessage_text().length()<=255))
        {
            Integer messageId = Integer.parseInt(context.pathParam("message_id"));
            Optional<Message> optional = Optional.ofNullable(messageService.getMessageById(messageId));
            if (optional.isPresent()) {

                Message originalMessage = optional.get();
                originalMessage.setMessage_text(message.getMessage_text());
                messageService.updateMessageById(originalMessage, message.getMessage_id());
                updated = true;
                context.json(originalMessage);
            }  
        } 
        if (updated)
            context.status(200);
        else
            context.status(400);

    }
    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getAllAccounts(Context context) {
        List<Account> accounts = accountService.getAllAccounts();
        context.json(accounts);
    }
    private void getAllMessagesFromAccountId(Context context) {
        Integer accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> list = messageService.getAllMessagesFromAccountId(accountId);                 
        context.json(list);   
        context.status(200);
    }
    private void retrieveMessageById(Context context) {
        Integer messageId = Integer.parseInt(context.pathParam("message_id"));
        Optional<Message> optional = Optional.ofNullable(messageService.getMessageById(messageId));
        if (optional.isPresent()) {
            Message message = optional.get();
            context.json(message);
            context.status(200);
        }        
    }
    private void deleteMessageById(Context context) {
        Integer messageId = Integer.parseInt(context.pathParam("message_id"));
        Optional<Message> optional = Optional.ofNullable(messageService.getMessageById(messageId));
        if (optional.isPresent()) {
            Message message = optional.get();
            messageService.deleteMessageById(message.getMessage_id());
            context.json(message);            
        }
        context.status(200);
    }

}