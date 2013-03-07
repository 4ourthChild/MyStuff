package edu.byu.isys413.fbrooke;

import java.text.DecimalFormat;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class PaymentWin extends Dialog {

	protected Object result;
	protected Shell shlPaymentWindow;
	private Transaction trans;
	private Payment payment;
	private Text txtTotal;
	private Text txtPaid;
	private Text txtChange;
	private Boolean wasPaid = false;
	private Button btnCloseWindow;
	private Button btnMakePayment;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PaymentWin(Shell parent, int style, Transaction trans) {
		super(parent, style);
		this.trans = trans;
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		try {
			payment = BusinessObjectDAO.getInstance().create("Payment");
		} catch (DataException e) {
			e.printStackTrace();
		}
		shlPaymentWindow.open();
		shlPaymentWindow.layout();
		Display display = getParent().getDisplay();
		while (!shlPaymentWindow.isDisposed()) {
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
		shlPaymentWindow = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlPaymentWindow.setSize(245, 225);
		shlPaymentWindow.setText("Payment Window");
		shlPaymentWindow.setLayout(new BorderLayout(0, 0));

		Composite composite = new Composite(shlPaymentWindow, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(new GridLayout(3, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setText("Total:");

		txtTotal = new Text(composite, SWT.BORDER);
		txtTotal.setEnabled(false);
		txtTotal.setEditable(false);
		txtTotal.setText("total");
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		DecimalFormat df1 = new DecimalFormat("###.##");
		df1.setMinimumFractionDigits(2);
		txtTotal.setText(df1.format(trans.getTotal()));
		new Label(composite, SWT.NONE);

		Label lblPaid = new Label(composite, SWT.NONE);
		lblPaid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPaid.setText("Paid:");

		txtPaid = new Text(composite, SWT.BORDER);
		txtPaid.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtPaid.getText().equals("Amount")) {
					txtPaid.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(txtPaid.getText().equals("")){
					txtPaid.setText("Amount");
				}
			}
		});
		txtPaid.setText("Amount");
		txtPaid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		btnMakePayment = new Button(composite, SWT.NONE);
		btnMakePayment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					DecimalFormat df = new DecimalFormat("###.##");
					df.setMinimumFractionDigits(2);
					payment.setAmount(Double.parseDouble(df.format(Double.parseDouble(txtPaid.getText()))));
					double cngamt = trans.getTotal() - payment.getAmount();
					if(cngamt > 0){
						txtChange.setText("Not enough $");
					} else {
						payment.setChangeamt(Math.abs(cngamt));
						trans.setPaymentId(payment.getId());
						txtChange.setText("$"+df.format(payment.getChangeamt()));
						payment.setTranid(trans.getId());
						payment.setPaytype("Cash");
						payment.save();
						wasPaid = true;
						btnCloseWindow.setEnabled(true);
						btnMakePayment.setEnabled(false);
					}
				}catch (Exception e1){
					e1.printStackTrace();
					txtChange.setText("Invalid Num");
				}
			}
		});
		btnMakePayment.setText("Make Payment");
		new Label(composite, SWT.NONE);

		Label lblChange = new Label(composite, SWT.NONE);
		lblChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblChange.setText("Change:");

		txtChange = new Text(composite, SWT.BORDER);
		txtChange.setEditable(false);
		txtChange.setText("0");
		txtChange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wasPaid = false;
				shlPaymentWindow.dispose();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 59;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");

		btnCloseWindow = new Button(composite, SWT.NONE);
		btnCloseWindow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlPaymentWindow.dispose();
			}
		});
		btnCloseWindow.setEnabled(false);
		btnCloseWindow.setText("Sale Done!");

	}

	/**
	 * @return the wasPaid
	 */
	public Boolean getWasPaid() {
		return wasPaid;
	}

	/**
	 * @param wasPaid
	 *            the wasPaid to set
	 */
	public void setWasPaid(Boolean wasPaid) {
		this.wasPaid = wasPaid;
	}

}
