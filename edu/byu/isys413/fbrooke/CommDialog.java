package edu.byu.isys413.fbrooke;

import java.util.Date;

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

public class CommDialog extends Shell {

	private Transaction tran = null;
	private Text passField;
	private Text statusField;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			CommDialog shell = new CommDialog(display);
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
	public CommDialog(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new GridLayout(3, false));
		
		Button btnRunCommissions = new Button(composite, SWT.NONE);
		btnRunCommissions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
					try {
						System.out.println(new Date());
						String password = passField.getText();
						Employee emp = tran.getStore().getManager();
						String ryid = emp.getUsername();
						if(!password.equals(""))
						{
							LDAP ldap = new LDAP();
							if(ldap.authenticate(ryid, password)){
							TransactionCtlr.getInstance().runCommission(tran);
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
					//TransactionCtlr.getInstance().runCommission(tran);
			}
		});
		btnRunCommissions.setText("Run Commissions");
		
		Label lblManagerPassword = new Label(composite, SWT.NONE);
		lblManagerPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblManagerPassword.setText("Manager Password:");
		
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
	
	public void setTran(Transaction tran){
		this.tran = tran;
	}

}
