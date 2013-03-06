package edu.byu.isys413.fbrooke;

public abstract class RevenueSource extends BusinessObject{

	@BusinessObjectField
	private String rstype = null;
	@BusinessObjectField
	private Double chargeAmt = 0.0;
	@BusinessObjectField
	private String tranId = null;
	
	public RevenueSource(String id) {
		super(id);
	}

	public String getRstype() {
		return rstype;
	}

	public void setRstype(String rstype) {
		this.rstype = rstype;
		setDirty();
	}

	public Double getChargeAmt() {
		return chargeAmt;
	}

	public void setChargeAmt(Double amt) {
		this.chargeAmt = amt;
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

	

}
