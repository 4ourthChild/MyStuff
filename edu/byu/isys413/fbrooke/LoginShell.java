package edu.byu.isys413.fbrooke;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import edu.byu.isys413.fbrooke.LDAP;


public class LoginShell extends Shell {
	private Text userField;
	private Text passField;
	private Text status;
	private Employee emp;
	private Boolean authentic = false;
	private SaleWin s = new SaleWin();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			LoginShell shell = new LoginShell(display);
			shell.open();
			shell.layout();
			System.out.println("waht");
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public LoginShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(2, false));
		
		Label lblUsername = new Label(this, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText("Username:");
		
		userField = new Text(this, SWT.BORDER);
		userField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");
		
		passField = new Text(this, SWT.BORDER);
		passField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(this, SWT.NONE);
		
		Button btnLogin = new Button(this, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});
		btnLogin.setText("Login");
		new Label(this, SWT.NONE);
		
		status = new Text(this, SWT.BORDER);
		status.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void login(){
		 //String ryid = "fbrooke";
		
		String ryid = userField.getText();
		 
		try {
			 
		emp = BusinessObjectDAO.getInstance().searchForBO("Employee", new SearchCriteria("username", ryid));
		if(emp!=null){//If there is an employee in the db, authenticate.
		System.out.println(emp.getName());
		
		String password = passField.getText();
        // run the LDAP
        LDAP ldap = new LDAP();
        if (ldap.authenticate(ryid, password)) {
            System.out.println("Authentication worked!");
            authentic = true;
            close();
        }else{
            System.out.println("Authentication didn't work.");
           authentic = false;
        }
		
		}else{//If there isn't an employee in the database.
			
		}
		
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	        
	        public boolean isAuthentic(){
	        	return authentic;
	        }
	        
	        public Employee getEmp(){
	        	return emp;
	        }
	
}
