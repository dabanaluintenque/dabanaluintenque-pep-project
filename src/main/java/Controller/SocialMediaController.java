package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {


    private AccountService accountService = new AccountService();

    int statusCodeOk = 200;
    int statuscodeError400 = 400;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this:: registratrionPage);
        app.post("/login", this::loginPage);
        app.post("/messages", this::postTextMessage);
        app.get("/messages", this::getAllTheTextMessages);
        app.delete("/messages/{message_id}", this::deleteTextMessage);
        app.get("/messages/{message_id}", this::getTextMessage);
        app.get("/accounts/{account_id}/messages", this::getMessageFromID);
        app.patch("/messages/{message_id}", this::patchTextMessage);
       

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void registratrionPage(Context context) throws JsonMappingException, JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        int statuscode = 400;

        Account userAccount = map.readValue(context.body(), Account.class);

        Account addAccount = accountService.registerUser(userAccount);


       context = (addAccount == null) ? context.status(statuscode) : context.json(map.writeValueAsString(addAccount));

    }

    private void loginPage(Context context) throws JsonMappingException, JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        int statuscode = 401;

        Account userAccount = map.readValue(context.body(), Account.class);

        Account addAccount = accountService.logUserIn(userAccount);

        context = (addAccount == null) ? context.status(statuscode) : context.json(map.writeValueAsString(addAccount));

    }


    private void getTextMessage(Context context) throws JsonProcessingException  {

        ObjectMapper map = new ObjectMapper();

        

        int message_id = Integer.parseInt(context.pathParam("message_id"));


        Message message = accountService.readMessage(message_id);

        context = (message == null) ? context.status(statusCodeOk) : context.json(map.writeValueAsString(message));

    }


    private void getAllTheTextMessages (Context context) throws  JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        List<Message> messages = accountService.readAllMessages();

        context = ( messages == null) ? context.status(statusCodeOk) : context.json(map.writeValueAsString(messages));

    }

    private void getMessageFromID(Context context) throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        int account_id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messages = accountService.readAllMessagesById(account_id);
        
        context = (messages == null) ? context.status(statusCodeOk) :  context.json(map.writeValueAsString(messages));

    }

    private void deleteTextMessage (Context context ) throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();
    
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = accountService.readMessage(message_id);

        context = (message == null) ? context.status(statusCodeOk) : context.json(map.writeValueAsString(message));

    }

    private void postTextMessage(Context context) throws JsonMappingException, JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        Message message = map.readValue(context.body(), Message.class);

        message = accountService.createMessage(message);
        
        context = (message == null)? context.status(statuscodeError400) : context.status(200).json(map.writeValueAsString(message));

    }

    private void patchTextMessage(Context context) throws JsonProcessingException {

        ObjectMapper map = new ObjectMapper();

        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = map.readValue(context.body(), Message.class);

        message.setMessage_id(message_id); 

        message = accountService.updateMessage(message);

        context = ( message == null) ?  context.status(statuscodeError400) : context.json(map.writeValueAsString(message));

    }   
}