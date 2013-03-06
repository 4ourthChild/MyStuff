package edu.byu.isys413.fbrooke;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

public class CustomerWin extends Dialog {

	protected Object result;
	protected Shell shlCustomerInfo;
	private Text txtFirstname;
	private Text txtPhone;
	private Text txtEmail;
	private Text txtAddress;
	private Customer cust;
	private Text txtMessage;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CustomerWin(Shell parent, int style, Customer cust) {
		super(parent, style);
		setText("SWT Dialog");
		this.cust = cust;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		populateCust();
		shlCustomerInfo.open();
		shlCustomerInfo.layout();
		Display display = getParent().getDisplay();
		while (!shlCustomerInfo.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void populateCust(){
		if(cust != null){
			txtMessage.setText("Edit customer info");
			txtFirstname.setText(cust.getName());
			txtPhone.setText(cust.getPhone());
			txtEmail.setText(cust.getEmail());
			txtAddress.setText(cust.getAddress());
		}
		else{
			try {
				cust = BusinessObjectDAO.getInstance().create("Customer");
			} catch (DataException e) {
				e.printStackTrace();
			}
			txtMessage.setText("New customer info");
			txtFirstname.setText("");
			txtPhone.setText("");
			txtEmail.setText("");
			txtAddress.setText("");
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCustomerInfo = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlCustomerInfo.setSize(300, 265);
		shlCustomerInfo.setText("Customer Info");
		shlCustomerInfo.setLayout(new GridLayout(3, false));
		new Label(shlCustomerInfo, SWT.NONE);
		
		Label lblMessage = new Label(shlCustomerInfo, SWT.NONE);
		lblMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMessage.setText("Message:");
		
		txtMessage = new Text(shlCustomerInfo, SWT.BORDER);
		txtMessage.setEditable(false);
		txtMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		
		Label lblFirstName = new Label(shlCustomerInfo, SWT.NONE);
		lblFirstName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstName.setText("Full Name:");
		
		txtFirstname = new Text(shlCustomerInfo, SWT.BORDER);
		txtFirstname.setText("fullname");
		txtFirstname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shlCustomerInfo, SWT.NONE);
		
		Label lblPhone = new Label(shlCustomerInfo, SWT.NONE);
		lblPhone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPhone.setText("Phone:");
		
		txtPhone = new Text(shlCustomerInfo, SWT.BORDER);
		txtPhone.setText("phone");
		txtPhone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shlCustomerInfo, SWT.NONE);
		
		Label lblEmail = new Label(shlCustomerInfo, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("Email:");
		
		txtEmail = new Text(shlCustomerInfo, SWT.BORDER);
		txtEmail.setText("email");
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shlCustomerInfo, SWT.NONE);
		
		Label lblAddress = new Label(shlCustomerInfo, SWT.NONE);
		lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddress.setText("Address:");
		
		txtAddress = new Text(shlCustomerInfo, SWT.BORDER);
		txtAddress.setText("address");
		txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		new Label(shlCustomerInfo, SWT.NONE);
		
		Composite composite = new Composite(shlCustomerInfo, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cust = null;
				shlCustomerInfo.close();
			}
		});
		btnCancel.setLayoutData(new RowData(76, SWT.DEFAULT));
		btnCancel.setText("Cancel");
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new RowData(26, 25));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveCust();
			}
		});
		btnSave.setLayoutData(new RowData(78, SWT.DEFAULT));
		btnSave.setText("Save");
		shlCustomerInfo.setTabList(new Control[]{txtFirstname, txtPhone, txtEmail, txtAddress, composite, txtMessage});

	}

	private void saveCust(){
		try {
			cust.setName(txtFirstname.getText());
			cust.setPhone(txtPhone.getText());
			cust.setEmail(txtEmail.getText());
			cust.setAddress(txtAddress.getText());
			cust.save();
			shlCustomerInfo.close();
		} catch (DataException e) {
			e.printStackTrace();
			txtMessage.setText("Could not save");
		}
	}
	
	/**
	 * @return the cust
	 */
	public Customer getCust() {
		return cust;
	}

	/**
	 * @param cust the cust to set
	 */
	public void setCust(Customer cust) {
		this.cust = cust;
	}
}
