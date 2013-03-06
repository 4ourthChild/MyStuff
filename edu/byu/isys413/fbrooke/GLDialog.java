package edu.byu.isys413.fbrooke;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class GLDialog extends Shell {
	private Text passField;
	private Transaction t;
	private Text statusField;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			GLDialog shell = new GLDialog(display);
			shell.open();
			shell.layout();
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
	public GLDialog(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new GridLayout(4, false));
		
		Button btnRunGeneralLedger = new Button(composite, SWT.NONE);
		btnRunGeneralLedger.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					System.out.println(new Date());
					String password = passField.getText();
					Employee emp = t.getStore().getManager();
					String ryid = emp.getUsername();
					if(!password.equals(""))
					{
						LDAP ldap = new LDAP();
						if(ldap.authenticate(ryid, password)){
						TransactionCtlr.getInstance().runGeneralLedger();
						printStuff();
						}
						else{
							statusField.setText("Password incorrect.");
						}
					}else{
						statusField.setText("Enter manager password.");
					}
				} catch (DataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRunGeneralLedger.setText("Run General Ledger Posting");
		
		Label lblManagerPassword = new Label(composite, SWT.NONE);
		lblManagerPassword.setText("Manager Password:");
		new Label(composite, SWT.NONE);
		
		passField = new Text(composite, SWT.BORDER);
		passField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		passField.setEchoChar('*');
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(1, false));
		
		statusField = new Text(composite_1, SWT.BORDER);
		statusField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
	
	protected void printStuff() throws DataException{
		List<GeneralLedger> glList = BusinessObjectDAO.getInstance().searchForAll("GeneralLedger");
		for(int i =0;i<glList.size(); i++){
			System.out.println(glList.get(i).getBalance());
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
    
    public void setTran(Transaction t){
    	this.t  =t;
    }
}
