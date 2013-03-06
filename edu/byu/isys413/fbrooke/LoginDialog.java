package edu.byu.isys413.fbrooke;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class LoginDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Employee emp = null;
	boolean authentic = false;
	private Text userField;
	private Text passField;
	private Text statusField;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnLogin = new Button(composite, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});
		btnLogin.setText("Login");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				authentic = false;
				shell.close();
			}
		});
		btnCancel.setText("Cancel");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.WEST);
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setLayoutData(BorderLayout.EAST);
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setLayoutData(BorderLayout.CENTER);
		composite_3.setLayout(new GridLayout(3, false));
		new Label(composite_3, SWT.NONE);
		
		Label lblStatus = new Label(composite_3, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("Status:");
		
		statusField = new Text(composite_3, SWT.BORDER);
		statusField.setEnabled(false);
		statusField.setEditable(false);
		statusField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		Label label = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 223;
		label.setLayoutData(gd_label);
		new Label(composite_3, SWT.NONE);
		
		Label lblUsername = new Label(composite_3, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText("Username:");
		
		userField = new Text(composite_3, SWT.BORDER);
		userField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_3, SWT.NONE);
		
		Label lblPassword = new Label(composite_3, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password:");
		
		passField = new Text(composite_3, SWT.BORDER);
		passField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		passField.setEchoChar('*');

	}
	
	private void login(){
		 //String ryid = "fbrooke";
		
		String ryid = userField.getText();
		 
		try {
			 
		emp = BusinessObjectDAO.getInstance().searchForBO("Employee", new SearchCriteria("username", ryid));
		if(emp!=null){//If there is an employee in the db, authenticate.
		System.out.println(emp.getName());
		
		String password = passField.getText();
		//System.out.println("pass:" + password);
       // run the LDAP
       LDAP ldap = new LDAP();
       if (ldap.authenticate(ryid, password)) {
           System.out.println("Authentication worked!");
           authentic = true;
           shell.close();
       }else{
           System.out.println("Authentication didn't work.");
          authentic = false;
          statusField.setText("Username or Password incorrect.");
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
