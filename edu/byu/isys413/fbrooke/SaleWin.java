package edu.byu.isys413.fbrooke;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import swing2swt.layout.BorderLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusAdapter;


public class SaleWin {
	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected Shell shlSaleWindow;
	private Text storeField;
	private Text empField;
	private Table salesTable;
	private Text custField;
	private Text phoneField;
	private Text addressField;
	private Text searchText;
	private Text searchProd;
	private Text prodnameField;
	private Text priceField;
	private Text descField;
	private Text subtotalField;
	private Text taxField;
	private Text totalField;
	private TableViewer tableViewer;
	private Employee emp;
	private Store store;
	private Customer cust=null;
	private Commission comm = null;
	private CProduct cprod = null;
	private PhysicalProd pprod = null;
	private Transaction tran = null;
	private List<RevenueSource> saleList = new ArrayList<RevenueSource>();
	private List<RevenueSource> clist = new ArrayList<RevenueSource>();
	private Product prod = null;
	private Spinner quantityField;
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private Text statusField;


	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SaleWin window = new SaleWin();
			window.open();
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		try {
			populate();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shlSaleWindow.open();
		shlSaleWindow.layout();
		runLogin();
		while (!shlSaleWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}

		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSaleWindow = new Shell();
		shlSaleWindow.setSize(600, 450);
		shlSaleWindow.setText("Sales Window");
		shlSaleWindow.setLayout(new RowLayout(SWT.HORIZONTAL));

		Menu menu = new Menu(shlSaleWindow, SWT.BAR);
		shlSaleWindow.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");

		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);

		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");

		Menu menu_3 = new Menu(mntmView);
		mntmView.setMenu(menu_3);

		MenuItem mntmBatch = new MenuItem(menu, SWT.CASCADE);
		mntmBatch.setText("Batch");

		Menu menu_4 = new Menu(mntmBatch);
		mntmBatch.setMenu(menu_4);
		
		MenuItem mntmCommission = new MenuItem(menu_4, SWT.NONE);
		mntmCommission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TransactionCtlr.getInstance().runCommission();
				CommDialog di = new CommDialog(null);
				di.setTran(tran);
				di.open();
			}
		});
		mntmCommission.setText("Commission");
		
		MenuItem mntmGeneralLedger = new MenuItem(menu_4, SWT.NONE);
		mntmGeneralLedger.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GLDialog dia = new GLDialog(null);
				dia.setTran(tran);
				dia.open();
			}
		});
		mntmGeneralLedger.setText("General Ledger");

		MenuItem mntmMaintenance = new MenuItem(menu, SWT.CASCADE);
		mntmMaintenance.setText("Maintenance");

		Menu menu_5 = new Menu(mntmMaintenance);
		mntmMaintenance.setMenu(menu_5);

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");

		Menu menu_6 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_6);

		Composite composite = new Composite(shlSaleWindow, SWT.BORDER);
		composite.setLayout(new GridLayout(5, false));
		composite.setLayoutData(new RowData(575, 30));

		Label lblStore = new Label(composite, SWT.NONE);
		lblStore.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStore.setText("Store:");

		storeField = new Text(composite, SWT.BORDER);
		storeField.setEnabled(false);
		storeField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Employee");

		empField = new Text(composite, SWT.BORDER);
		empField.setEnabled(false);
		empField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button logoutButton = new Button(composite, SWT.NONE);
		logoutButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSalesWin();
				emp = null;
				runLogin();
			}
		});
		logoutButton.setText("Logout");

		Composite composite_1 = new Composite(shlSaleWindow, SWT.BORDER);
		composite_1.setLayout(new BorderLayout(0, 0));
		composite_1.setLayoutData(new RowData(160, 345));

		Label lblCustomer = new Label(composite_1, SWT.CENTER);
		lblCustomer.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblCustomer.setLayoutData(BorderLayout.NORTH);
		lblCustomer.setText("Customer");

		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(BorderLayout.SOUTH);
		composite_4.setLayout(new GridLayout(1, false));

		Label label_1 = new Label(composite_4, SWT.NONE);
		label_1.setText("Search:");

		searchText = new Text(composite_4, SWT.BORDER);
		searchText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchText.getText().equals("Search")) {
					searchText.setText("");
					}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (searchText.getText().equals("")) {
					searchText.setText("Search");
					}
			}
		});
		searchText.setText("Search");
		
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnSearchByPhone = new Button(composite_4, SWT.NONE);
		btnSearchByPhone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getCustomer();
			}
		});
		GridData gd_btnSearchByPhone = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSearchByPhone.widthHint = 149;
		btnSearchByPhone.setLayoutData(gd_btnSearchByPhone);
		btnSearchByPhone.setText("Search by Phone");

		Label label_2 = new Label(composite_4, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblName = new Label(composite_4, SWT.NONE);
		lblName.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblName.setText("Name:");

		custField = new Text(composite_4, SWT.BORDER);
		custField.setText("Name");
		custField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPhone = new Label(composite_4, SWT.NONE);
		lblPhone.setText("Phone:");

		phoneField = new Text(composite_4, SWT.BORDER);
		phoneField.setText("Phone");
		phoneField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAddress = new Label(composite_4, SWT.NONE);
		lblAddress.setText("Address:");

		addressField = new Text(composite_4, SWT.BORDER);
		addressField.setText("Address");
		addressField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = new Label(composite_4, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button btnNewCustomer = new Button(composite_4, SWT.NONE);
		btnNewCustomer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newCustomer();
			}
		});
		btnNewCustomer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnNewCustomer.setText("New Customer");

		Button btnSaveCustomer = new Button(composite_4, SWT.NONE);
		btnSaveCustomer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveCustomer();
			}
		});
		btnSaveCustomer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSaveCustomer.setText("Edit Customer");

		Button btnAnonymous = new Button(composite_4, SWT.NONE);
		btnAnonymous.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cust = null;
				custField.setText("none");
				phoneField.setText("none");
				addressField.setText("none");
				searchText.setText("");
			}
		});
		btnAnonymous.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAnonymous.setText("Anonymous Customer");

		Composite composite_2 = new Composite(shlSaleWindow, SWT.BORDER);
		composite_2.setLayout(new BorderLayout(0, 0));
		composite_2.setLayoutData(new RowData(160, 372));

		Label lblNewLabel_1 = new Label(composite_2, SWT.CENTER);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblNewLabel_1.setLayoutData(BorderLayout.NORTH);
		lblNewLabel_1.setText("Product");

		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		composite_5.setLayoutData(BorderLayout.CENTER);
		composite_5.setLayout(new GridLayout(1, false));

		Label lblProdSearch = new Label(composite_5, SWT.NONE);
		lblProdSearch.setText("Search:");

		searchProd = new Text(composite_5, SWT.BORDER);
		searchProd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (searchProd.getText().equals("")) {
					searchProd.setText("Search");
					}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (searchProd.getText().equals("Search")) {
					searchProd.setText("");
					}
			}
		});
		searchProd.setText("Search");
		searchProd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnSearchByNumber = new Button(composite_5, SWT.NONE);
		btnSearchByNumber.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchProduct();
			}
		});
		btnSearchByNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSearchByNumber.setText("Search by Number");

		Label label_4 = new Label(composite_5, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblName_1 = new Label(composite_5, SWT.NONE);
		lblName_1.setText("Name:");

		prodnameField = new Text(composite_5, SWT.BORDER);
		prodnameField.setText("Name");
		prodnameField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPrice = new Label(composite_5, SWT.NONE);
		lblPrice.setText("Price:");

		priceField = new Text(composite_5, SWT.BORDER);
		priceField.setText("Price");
		priceField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblQuantity = new Label(composite_5, SWT.NONE);
		lblQuantity.setText("Quantity");

		quantityField = new Spinner(composite_5, SWT.BORDER);
		quantityField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblDescription = new Label(composite_5, SWT.NONE);
		lblDescription.setText("Description:");

		descField = new Text(composite_5, SWT.BORDER);
		descField.setText("Description");
		descField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_5 = new Label(composite_5, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button btnAddProduct = new Button(composite_5, SWT.NONE);
		btnAddProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addProduct();
			}
		});
		btnAddProduct.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddProduct.setText("Add Product");
		
		Button btnRemoveProduct = new Button(composite_5, SWT.NONE);
		btnRemoveProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selection = salesTable.getSelectionIndices();
				if(selection.length != 0){
				saleList.remove(selection[0]);
				clist.remove(selection[0]);
				tableViewer.refresh();
				try {
					resetTotals();
				} catch (DataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}else{
					statusField.setText("Select a product");
				}
			}
		});
		GridData gd_btnRemoveProduct = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRemoveProduct.widthHint = 139;
		btnRemoveProduct.setLayoutData(gd_btnRemoveProduct);
		btnRemoveProduct.setText("Remove Product");

		Composite composite_3 = new Composite(shlSaleWindow, SWT.BORDER);
		composite_3.setLayout(new BorderLayout(0, 0));
		composite_3.setLayoutData(new RowData(240, 345));

		tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		salesTable = tableViewer.getTable();
		salesTable.setHeaderVisible(true);
		salesTable.setLayoutData(BorderLayout.CENTER);

		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnProduct = nameColumn.getColumn();
		tblclmnProduct.setResizable(false);
		tblclmnProduct.setWidth(115);
		tblclmnProduct.setText("Product");
		tableViewer.setContentProvider(new ContentProvider());

		TableViewerColumn quantityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnQuantity = quantityColumn.getColumn();
		tblclmnQuantity.setResizable(false);
		tblclmnQuantity.setWidth(55);
		tblclmnQuantity.setText("Quant");

		TableViewerColumn priceColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnPrice = priceColumn.getColumn();
		tblclmnPrice.setResizable(false);
		tblclmnPrice.setWidth(65);
		tblclmnPrice.setText("Price");

		Composite composite_6 = new Composite(composite_3, SWT.NONE);
		composite_6.setLayoutData(BorderLayout.SOUTH);
		composite_6.setLayout(new GridLayout(2, false));

		Label lblSubtotal = new Label(composite_6, SWT.NONE);
		lblSubtotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSubtotal.setText("Subtotal:");

		subtotalField = new Text(composite_6, SWT.BORDER);
		subtotalField.setText("subtotal");
		subtotalField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblTax = new Label(composite_6, SWT.NONE);
		lblTax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTax.setText("Tax:");

		taxField = new Text(composite_6, SWT.BORDER);
		taxField.setText("tax");
		taxField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblTotal = new Label(composite_6, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setText("Total:");

		totalField = new Text(composite_6, SWT.BORDER);
		totalField.setText("total");
		totalField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnClearAll = new Button(composite_6, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSalesWin();
			}
		});
		btnClearAll.setText("Clear All");

		Button btnFinishTransaction = new Button(composite_6, SWT.NONE);
		btnFinishTransaction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(clist.size() !=0){
					finishTransaction();
					}else{
						statusField.setText("Add a product");
					}
					
				} catch (DataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFinishTransaction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFinishTransaction.setText("Finish Transaction");
		new Label(composite_6, SWT.NONE);
		
		statusField = new Text(composite_6, SWT.BORDER);
		statusField.setEnabled(false);
		statusField.setEditable(false);
		statusField.setText("status");
		statusField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//Create Table Viewer
		nameColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				Sale p = (Sale)element;
				return p.getProductId();
			}
		});//setLabelProvider
		
		priceColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				Sale p = (Sale)element;
				return (p.getChargeAmt().toString());
			}
		});//setLabelProvider
		
		quantityColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				Sale p = (Sale)element;
				return Integer.toString(p.getQuantity());
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(clist);

	}
	
	public void populate() throws DataException{
	
			
		//Get the store from the IP address
		InetAddress ip;
		String macAdd = null;
		try {
			ip = InetAddress.getLocalHost();
			//System.out.println("Current IP address : " + ip.getHostAddress());
	 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress();
	 
			//System.out.print("Current MAC address : ");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			System.out.println(sb.toString());
			macAdd = sb.toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!macAdd.equals("")){
			
		//Get the Computer from the mac address, then the store.
		Computer comp = BusinessObjectDAO.getInstance().searchForBO("Computer", new SearchCriteria("macaddress", macAdd));
		store = comp.getStore();
		storeField.setText(store.getLocation());
		tran = BusinessObjectDAO.getInstance().create("Transaction");
		tran.setTax(store.getSalestax());
		tran.setCustId("");
		tran.setStoreId(store.getId());
		
		}else{
			shlSaleWindow.close();
		}
	}
	
	public void runLogin(){
		shlSaleWindow.setVisible(false);
		LoginDialog LS =  new LoginDialog(shlSaleWindow, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		LS.open();
		System.out.println(LS.isAuthentic());
		if (LS.isAuthentic()){
		
			emp = LS.getEmp();
			empField.setText(emp.getName());
			shlSaleWindow.setVisible(true);
		}else{
			shlSaleWindow.close();
		}
	}
	
	public void setEmp(Employee employee)
	{
		emp = employee;
	}
	
	public void getCustomer(){
		String custNum = searchText.getText();
		try {//set cust
			List<Customer> custlist = BusinessObjectDAO.getInstance().searchForList("Customer", new SearchCriteria("phone", custNum));
			
			//display a list of possible customers in a customer dialog box.
			CustomerDialog CDia = new CustomerDialog(shlSaleWindow, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			CDia.setCustomers(custlist);
			CDia.open();
			
			if(CDia.getStatus()){
			String selectedCust = CDia.getSelected();
			for(Customer c:custlist){
				
				if(c.getName().equals(selectedCust)){
					cust = c;
					custField.setText(cust.getName());
					phoneField.setText(cust.getPhone());
					addressField.setText(cust.getAddress());
				}
				if(cust==null){
					custField.setText("");
					phoneField.setText("");
					addressField.setText("");
				}
			}}
			
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void newCustomer(){

			cust = null;
			customerInfoWin();

	}
	
	public void saveCustomer(){

		customerInfoWin();
	}
	
	public void searchProduct(){
		String p = searchProd.getText();
		try {
			prod = BusinessObjectDAO.getInstance().searchForBO("Product", new SearchCriteria("id", p));
			if(prod!=null){
			String s = prod.getProdtype();
			System.out.println(s);
			quantityField.setSelection(1);
		
			if(s.equals("Physical")){
				pprod = BusinessObjectDAO.getInstance().searchForBO("PhysicalProd", new SearchCriteria("id", p));
				String cprodid = pprod.getCprodId();
				System.out.println("Physical:" + pprod.getId() + " ConcpetualID:" + cprodid);
				cprod = BusinessObjectDAO.getInstance().searchForBO("CProduct", new SearchCriteria("id", cprodid));
				prodnameField.setText(cprod.getName());
				priceField.setText(Double.toString(cprod.getPrice()));
				descField.setText(cprod.getDescription());
			}
			else
			{
				cprod = BusinessObjectDAO.getInstance().searchForBO("CProduct", new SearchCriteria("id", p));
				prodnameField.setText(cprod.getName());
				priceField.setText(Double.toString(cprod.getPrice()));
				descField.setText(cprod.getDescription());
			}
			}else{statusField.setText("Product not found.");
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addProduct(){
		if(cprod!=null){
		System.out.println("Add conceptual: " + cprod.getName());
		Sale s = null;
		int q = Integer.parseInt(quantityField.getText());
		Double prodprice = cprod.getPrice();
		try {
			s = BusinessObjectDAO.getInstance().create("Sale");
			RevenueSource s2 = BusinessObjectDAO.getInstance().create("Sale");
	
			s.setChargeAmt(prodprice);
			s.setProductId(prod.getId());
			s.setQuantity(q);
			s.setRstype("Sale");
			s.setTranId(tran.getId());
			saleList.add(s);
			resetTotals();
			
//			Double subtotal = tran.getSubtotal();
//			subtotal = subtotal + (s.getChargeAmt()*s.getQuantity()); 
//			subtotalField.setText(formatter.format(subtotal));
//			tran.setSubtotal(subtotal);
//			Double tax = (store.getSalestax())*subtotal;
//			taxField.setText(formatter.format(tax));
//			tran.setTax(tax);
//			Double total = subtotal+tax;
//			totalField.setText(formatter.format(total));
//			tran.setTotal(total);
			
			//Clear Product fields.
			searchProd.setText("");
			prodnameField.setText("");
			priceField.setText("");
			descField.setText("");
			quantityField.setSelection(1);

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clist.add(s);
		tableViewer.setInput(clist);
		tableViewer.refresh();}
		else{statusField.setText("Search for a product.");
		
		}

	}
	
	public void finishTransaction() throws Exception{
		
		//Put customer in transaction object
		if(cust!=null){
			tran.setCustId(cust.getId());
		}
		
		TranConfirm tc = new TranConfirm(shlSaleWindow, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		//tc.setConfirm(Double.toString(tran.getTotal()), Double.toString(tran.getTax()), Double.toString(tran.getSubtotal()));
		tc.setConfirm(tran);
		tc.open();
		if(tc.getAnswer())
		{
		
		tran.setStoreId(store.getId());
		tran.setEmpId(emp.getId());
		tran.setEntrydate(new Date());
		
		//Save Payment
		Payment pay = BusinessObjectDAO.getInstance().create("Payment");
		pay.setAmount(tran.getTotal());
		pay.setPaytype("Cash");
		pay.setTranid(tran.getId());
		pay.save();
		tran.setPaymentId(pay.getId());
		
		//Save Commission
		comm = BusinessObjectDAO.getInstance().create("Commission");
		tran.setCommissionId(comm.getId());
		comm.setEmpId(emp.getId());
		comm.setEntryDate(new Date());

		comm.setAmt(getComm());
		System.out.println(comm.getAmt());
		comm.save();
		
		//Commission Journal Entry
		JournalEntry JE = BusinessObjectDAO.getInstance().create("JournalEntry");
		JE.setEntrydate(new Date());
		JE.setTranId(tran.getId());
		JE.save();
		CreditDebit CD = BusinessObjectDAO.getInstance().create("CreditDebit");
		CD.setAmount(comm.getAmt());
		CD.setDorc(false);
		CD.setJournalId(JE.getId());
		CD.setGeneralname("Comm Payable");
		CD.save();
		CreditDebit CD2 = BusinessObjectDAO.getInstance().create("CreditDebit");
		CD2.setAmount(comm.getAmt());
		CD2.setDorc(true);
		CD2.setGeneralname("Comm Expense");
		CD2.setJournalId(JE.getId());
		CD2.save();
		
		
		//Save sale objects.
		for(RevenueSource s:saleList){
			s.save();
		}
		
		//Update inventory and run journal entries.
		updateInventory();
		String journalid = TransactionCtlr.getInstance().journalEntries(tran);
		tran.setJournalId(journalid);
		
		searchText.setText("");
		phoneField.setText("");
		addressField.setText("");
		custField.setText("");
		searchProd.setText("");
		prodnameField.setText("");
		priceField.setText("");
		quantityField.setSelection(0);
		descField.setText("");
		subtotalField.setText("");
		totalField.setText("");
		taxField.setText("");
		clist.clear();
		tableViewer.refresh();
		
		tran.save();
		String tempTran = tran.getId();
		System.out.println("Transation Saved.");
		clearSalesWin();
		populate();
		
		Transaction t1 = BusinessObjectDAO.getInstance().searchForBO("Transaction", new SearchCriteria("id", tempTran));
		System.out.println("Storeid: " + t1.getStoreId());
		System.out.println("Date: " + t1.getEntrydate());
		System.out.println("Subtotal: " + t1.getSubtotal());
		System.out.println("Total: " + t1.getTotal());
		System.out.println("Tax: " + t1.getTax());
		System.out.println("JournalEntry " + t1.getJe().getEntrydate());
		System.out.println("Commission " + t1.getComm().getAmt());
		System.out.println("Payment " + t1.getPayment().getAmount());
		}
	}

	public Double getComm() throws Exception{
		Double commAmt = comm.getAmt();
		
		for(RevenueSource s: clist){
			if(s.getRstype().equals("Sale"))
			{
				Sale sa = BusinessObjectDAO.getInstance().read(s.getId());
				Product p = sa.getProduct();
				if(p.getProdtype().equals("Physical")){
					PhysicalProd pr = BusinessObjectDAO.getInstance().read(p.getId());
					Double ppc = pr.getPpCommRate();
					CProduct cp = pr.getCprod();
					//System.out.println("CONCEPTUAL for physical " + cp.getName());
					ppc += cp.getCommRate();
					commAmt = commAmt + (ppc*pr.getPrice()*sa.getQuantity());
				}else{
					CProduct cp = BusinessObjectDAO.getInstance().read(p.getId());
					Double cpp = cp.getCommRate();
					commAmt = commAmt + (cpp*cp.getPrice()*sa.getQuantity());
				}
			}
		}
		return commAmt;
	}
	
	public void updateInventory() throws DataException{
		for(RevenueSource s: clist){
			if(s.getRstype().equals("Sale"))
			{
				Sale sa = BusinessObjectDAO.getInstance().read(s.getId());
				Product p = sa.getProduct();
				if(p.getProdtype().equals("Physical")){
					PhysicalProd pr = BusinessObjectDAO.getInstance().read(p.getId());
					pr.setStatus("sold");
					pr.save();
				}else{
					CProduct cp = BusinessObjectDAO.getInstance().read(p.getId());
					StoreProduct sp = BusinessObjectDAO.getInstance().searchForBO("StoreProduct", new SearchCriteria("storeid", store.getId()), new SearchCriteria("cprodid", cp.getId()));
					if(sp!=null){
					int q = sp.getQuantity();
					q = q - sa.getQuantity();
					sp.setQuantity(q);
					}
				}
			}
	}
	}//Update Inventory
	
	/**Clear the Sales Window*/
	public void clearSalesWin(){
		cust = null;
		cprod = null;
		pprod = null;
		searchText.setText("");
		phoneField.setText("");
		addressField.setText("");
		searchProd.setText("");
		prodnameField.setText("");
		descField.setText("");
		priceField.setText("");
		quantityField.setSelection(0);
		clist.clear();
		tableViewer.refresh();
		subtotalField.setText("");
		taxField.setText("");
		totalField.setText("");
		tran.setTotal(0.0);
		tran.setSubtotal(0.0);
		tran.setTax(0.0);
		tran.setCustId(null);
		saleList.clear();
	}
	
	public void resetTotals() throws DataException{
		Double tempTotal = 0.0;
		Double tempSubtotal = 0.0;
		Double tempTax = 0.0;
		
		for(RevenueSource s:saleList){
			if(s.getRstype().equals("Sale"))
			{
				Sale s2 = BusinessObjectDAO.getInstance().read(s.getId());
			try {
				System.out.println("product:" + s2.getProduct() + " price:" + s2.getProduct().getPrice());
				tempSubtotal += (s2.getProduct().getPrice())*s2.getQuantity();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		tempTax = tempSubtotal*store.getSalestax();
		tempTotal = tempTax+tempSubtotal;
		
		subtotalField.setText(formatter.format(tempSubtotal));
		taxField.setText(formatter.format(tempTax));
		totalField.setText(formatter.format(tempTotal));
		
		tran.setTax(tempTax);
		tran.setTotal(tempTotal);
		tran.setSubtotal(tempSubtotal);
		
	}
	
	private void customerInfoWin() {
		CustomerWin custwin = new CustomerWin(shlSaleWindow, 0, cust);
		custwin.open();
		if (custwin.getCust() != null) {
			this.cust = custwin.getCust();
			custField.setText(cust.getName());
			phoneField.setText(cust.getPhone());
			addressField.setText(cust.getAddress());
		} else if (cust != null) {
			custField.setText(cust.getName());
			phoneField.setText(cust.getPhone());
			addressField.setText(cust.getAddress());
		} else {
			custField.setText("");
			phoneField.setText("");
			addressField.setText("");
}
}
}

