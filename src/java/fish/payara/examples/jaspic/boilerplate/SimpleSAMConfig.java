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
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

/**
 *
 * @author steve
 */
class SimpleSAMConfig implements ServerAuthConfig {
    
    private final String layer;
    private final String appContext;
    private final CallbackHandler handler;
    private final Map constructedProperties;
    private SimpleSAM sam;


    SimpleSAMConfig(String layer, String appContext, CallbackHandler handler, Map properties) {
        this.layer = layer;
        this.appContext = appContext;
        this.handler = handler;
        this.constructedProperties = properties;
    }

    @Override
    public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject, Map properties) throws AuthException {
        // combine constructed properties with passed in properties
        if (constructedProperties != null)
            properties.putAll(constructedProperties);
        
        if (sam == null) {
            sam = new SimpleSAM();
        }
        return new SimpleSAMAuthContext(authContextID, serviceSubject, properties, handler, sam);
    }

    @Override
    public String getMessageLayer() {
        return layer;
    }

    @Override
    public String getAppContext() {
        return appContext;
    }

    @Override
    public String getAuthContextID(MessageInfo messageInfo) {
        return null;
    }

    @Override
    public void refresh() {
    }

    @Override
    public boolean isProtected() {
        return false;
    }
    
}
