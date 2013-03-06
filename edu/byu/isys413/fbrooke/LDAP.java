package edu.byu.isys413.fbrooke;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.*;

/**
 *
 * @author Conan Albrecht, but many thanks to Trevor
 */
public class LDAP {

    /**
     * Main method of the program.
     */
    public static void main(String[] args) {
        // get the username
        String ryid = "cca";

        // get the password
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        Object[] obj = { "Please enter the password:\n\n", passwordField };
        Object stringArray[] = {"OK","Cancel"};
        if (JOptionPane.showOptionDialog(null, obj, "Need password", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj) == JOptionPane.YES_OPTION);
        String password = new String(passwordField.getPassword());        
        
        // run the LDAP
        LDAP ldap = new LDAP();
        if (ldap.authenticate(ryid, password)) {
            System.out.println("Authentication worked!");
        }else{
            System.out.println("Authentication didn't work.");
        }

    }

    /**
     *  Authenticates a user.  If the "new InitialDirContext" doesn't throw
     *  an exception, the user and password validated with LDAP.  We could then
     *  use this DirContext to query the user's email and address information,
     *  but all we care about is authentication.
     */
    public boolean authenticate(String NetID, String Password) {
        try{
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://ldap.byu.edu/");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "uid=" + NetID + ", ou=People, o=byu.edu");
            env.put(Context.SECURITY_CREDENTIALS, Password);
            DirContext ctx = new InitialDirContext(env);
            return true;
        }catch (NamingException e) {
            return false;
        }
    }
    
    

}