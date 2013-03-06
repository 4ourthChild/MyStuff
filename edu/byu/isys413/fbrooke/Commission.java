package edu.byu.isys413.fbrooke;

import java.util.*;

public class Commission extends BusinessObject{
	
	@BusinessObjectField
	private String empId = null;
	@BusinessObjectField
	private Double amt = 0.0;
	@BusinessObjectField
	private Date entryDate = null;
	@BusinessObjectField
	private String tranId = null;
	
	public Commission(String id){
		super(id);
	}

	public Employee getEmp() throws DataException {
		return BusinessObjectDAO.getInstance().read(empId);
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
		setDirty();
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
		setDirty();
	}

	public Transaction getTran() throws DataException {
		return BusinessObjectDAO.getInstance().read(tranId);

	}
	
	/**
	 * @return the tranId
	 */
	public String getTranId() {
		return tranId;
	}

	/**
	 * @param tranId the tranId to set
	 */
	public void setTranId(String tranId) {
		this.tranId = tranId;
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
	
	

}
