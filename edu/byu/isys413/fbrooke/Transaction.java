package edu.byu.isys413.fbrooke;

import java.util.*;

public class Transaction extends BusinessObject {
	
	@BusinessObjectField
	private Date entrydate = null;
	@BusinessObjectField
	private Double subtotal = 0.0;
	@BusinessObjectField
	private Double tax = 0.0;
	@BusinessObjectField
	private Double total = 0.0;
	@BusinessObjectField
	private String commissionId = null;
	@BusinessObjectField
	private String journalId = null;
	@BusinessObjectField
	private String custId = null;
	@BusinessObjectField
	private String storeId = null;
	@BusinessObjectField
	private String empId = null;
	@BusinessObjectField
	private String paymentId=null;
	
	/**Creates a transaction object*/
	public Transaction(String id){
		super(id);
	}

	public Date getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
		setDirty();
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
		setDirty();
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
		setDirty();
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
		setDirty();
	}

	public Commission getComm() throws DataException {
		return BusinessObjectDAO.getInstance().read(commissionId);

	}

	public JournalEntry getJe() throws DataException {
		return BusinessObjectDAO.getInstance().read(journalId);
	}

	public Customer getCust() throws DataException {
		return BusinessObjectDAO.getInstance().read(custId);
	}


	public Store getStore() throws DataException {
		return BusinessObjectDAO.getInstance().read(storeId);
	}

	public Employee getEmp() throws DataException {
		return BusinessObjectDAO.getInstance().read(empId);
	}

	/**
	 * @return the commissionId
	 */
	public String getCommissionId() {
		return commissionId;
	}

	/**
	 * @param commissionId the commissionId to set
	 */
	public void setCommissionId(String commissionId) {
		this.commissionId = commissionId;
		setDirty();
	}

	/**
	 * @return the journalId
	 */
	public String getJournalId() {
		return journalId;
	}

	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(String journalId) {
		this.journalId = journalId;
		setDirty();
	}

	/**
	 * @return the custId
	 */
	public String getCustId() {
		return custId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(String custId) {
		this.custId = custId;
		setDirty();
	}

	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
		setDirty();
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
		setDirty();
	}

	public List<RevenueSource> getRSs() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("RevenueSource", new SearchCriteria("tranid", id));
	}

	/**
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	public Payment getPayment() throws DataException{
		return BusinessObjectDAO.getInstance().read(paymentId);
	}
	
}
