package edu.byu.isys413.fbrooke;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import java.util.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CustomerDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected List custlist;
	private java.util.List<Customer> clist = new LinkedList<Customer>();
	private String custSelected = null;
	private boolean okay = false;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CustomerDialog(Shell parent, int style) {
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
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnOkay = new Button(composite, SWT.NONE);
		btnOkay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				okay = true;
				shell.close();
			}
		});
		btnOkay.setText("Okay");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setText("Cancel");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblSelectACustomer = new Label(composite_1, SWT.NONE);
		lblSelectACustomer.setText("Select a Customer:");
		
		custlist = new List(shell, SWT.BORDER);
		custlist.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selected = custlist.getSelection();
				custSelected = selected[0];
			}
		});
		custlist.setLayoutData(BorderLayout.CENTER);

	}

	/**Sets list of customers passed from the sales window*/
	public void setCustomers(java.util.List<Customer> clist) {
		this.clist = clist;
	}

	public void populate(){
		for(Customer c:clist){
			custlist.add(c.getName());
		}
	}
	
	public String getSelected(){
		return custSelected;
	}
	
	public boolean getStatus(){
		return okay;
	}
}
