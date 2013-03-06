package edu.byu.isys413.fbrooke;

import java.text.NumberFormat;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TranConfirm extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text amtField;
	private Boolean okay = true;
	private Double tax = 0.0;
	private Double subtotal = 0.0;
	private Double amount = 0.0;
	private String cust = "none";
	private Composite composite_1;
	private Label lblAreYouSure;
	private Text taxField;
	private Text subtotalField;
	private Label lblNewLabel;
	private Label lblNewLabel_1;
	private Composite composite_2;
	private Text custField;
	private Label lblCustomer;
	private Button btnCanel;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TranConfirm(Shell parent, int style) {
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
		populate();
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
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(new GridLayout(2, false));
		
		lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Subtotal");
		
		subtotalField = new Text(composite, SWT.BORDER);
		subtotalField.setEnabled(false);
		subtotalField.setEditable(false);
		subtotalField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Tax");
		
		taxField = new Text(composite, SWT.BORDER);
		taxField.setEnabled(false);
		taxField.setEditable(false);
		taxField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTotalOkay = new Label(composite, SWT.NONE);
		lblTotalOkay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotalOkay.setText("Total");
		
		amtField = new Text(composite, SWT.BORDER);
		amtField.setEnabled(false);
		amtField.setEditable(false);
		amtField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblCustomer = new Label(composite, SWT.NONE);
		lblCustomer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCustomer.setText("Customer:");
		
		custField = new Text(composite, SWT.BORDER);
		custField.setEnabled(false);
		custField.setEditable(false);
		custField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		composite_2 = new Composite(composite, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.widthHint = 369;
		gd_composite_2.heightHint = 35;
		composite_2.setLayoutData(gd_composite_2);
		composite_2.setLayout(new GridLayout(3, false));
		
		Button btnFinal = new Button(composite_2, SWT.NONE);
		btnFinal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				okay = true;
				shell.close();
			}
		});
		btnFinal.setText("Finalize");
		
		Button btnCancel = new Button(composite_2, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				okay = false;
				shell.close();
			}
		});
		btnCancel.setText("Cancel All");
		
		btnCanel = new Button(composite_2, SWT.NONE);
		btnCanel.setText("Cancel");
		btnCanel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(7, false));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		lblAreYouSure = new Label(composite_1, SWT.NONE);
		lblAreYouSure.setText("Are You Sure?");

	}
	
	public void populate(){
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		amtField.setText(formatter.format(amount));
		subtotalField.setText(formatter.format(subtotal));
		taxField.setText(formatter.format(tax));
		custField.setText(cust);
	}
	
	public void setConfirm(Transaction tran){
		this.tax = tran.getTax();
		this.subtotal = tran.getSubtotal();
		this.amount = tran.getTotal();
		try {
			if(!tran.getCustId().equals("")){
				
				String c = tran.getCustId();
				Customer c2 = BusinessObjectDAO.getInstance().read(c);
				cust = c2.getName();
				
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Boolean getAnswer(){
		return okay;
	}
}
