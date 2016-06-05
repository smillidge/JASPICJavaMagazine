/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.jaspic;

import fish.payara.examples.jaspic.boilerplate.SimpleSAMAuthConfigProvider;
import java.util.Enumeration;
import java.util.HashMap;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author steve
 */
@WebListener
public class SimpleSAMWebListener implements ServletContextListener {
    
    private String registrationid;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
       // Passing the Servlet Initializer properties is not necessary but can 
       // enable a SAM to use them to initialise itself
       ServletContext sc = sce.getServletContext();
       Enumeration<String> names = sce.getServletContext().getInitParameterNames();
       HashMap<String,String> samProperties = new HashMap<>();
       while(names.hasMoreElements()) {
           String name = names.nextElement();
           samProperties.put(name, sc.getInitParameter(name));
       }
       registrationid = AuthConfigFactory.getFactory()
                .registerConfigProvider(new SimpleSAMAuthConfigProvider(samProperties,null), 
                       "HttpServlet" , 
                        sce.getServletContext().getVirtualServerName() + " " + sce.getServletContext().getContextPath(),
                        "Simple SAM");
        System.out.println("Context intialised");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AuthConfigFactory.getFactory().removeRegistration(registrationid);
                System.out.println("Conext destroyed");
    }
    
}
