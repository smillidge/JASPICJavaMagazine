/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.jaspic;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author steve
 */
public class SimpleSAM implements ServerAuthModule {
    
    private CallbackHandler handler;

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
        this.handler = handler;
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        return new Class[] {HttpServletRequest.class, HttpServletResponse.class};
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        String user = request.getParameter("user");
        String groups[] = request.getParameterValues("group");
        
        Callback callbackArray [] = null;
        if (user != null && groups != null ) {        
            // callback used to set the user Principal
            Callback userCallback = new CallerPrincipalCallback(clientSubject, user);
            Callback groupsCallback = new GroupPrincipalCallback(clientSubject,groups);
            callbackArray = new Callback[] {userCallback, groupsCallback};
        }
        else {
            callbackArray =new Callback[] { new CallerPrincipalCallback(clientSubject, "anonymous"),
                                            new GroupPrincipalCallback(clientSubject, new String[] {"anonymous"})};   
        }
        
       try {
            handler.handle(callbackArray);
        } catch (Exception ex) {
            AuthException ae =  new AuthException(ex.getMessage());
            ae.initCause(ex);
        } 
        return SUCCESS; 
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        // do nothing
    }
    
}
