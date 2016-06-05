/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.jaspic.boilerplate;

import fish.payara.examples.jaspic.SimpleSAM;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthContext;

/**
 *
 * @author steve
 */
class SimpleSAMAuthContext implements ServerAuthContext {

    SimpleSAM sam;

    SimpleSAMAuthContext(String authContextID, Subject serviceSubject, Map properties,CallbackHandler handler, SimpleSAM sam) {
        this.sam = sam;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        return sam.validateRequest(messageInfo, clientSubject, serviceSubject);
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return sam.secureResponse(messageInfo, serviceSubject);
     }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        sam.cleanSubject(messageInfo, subject);
    }
    
}
