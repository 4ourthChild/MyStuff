/**
 * 
 */
package edu.byu.isys413.fbrooke;

/**
 * @author BrookieCookie
 *
 */
public class Payment extends BusinessObject{
	
	@BusinessObjectField
	private Double changeamt = 0.0;
	@BusinessObjectField
	private String paytype = null;
	@BusinessObjectField
	private String tranid=null;
	@BusinessObjectField
	private Double amount = 0.0;
	
	public Payment(String id){
		super(id);
	}
	
	public Transaction getTran() throws DataException{
		return BusinessObjectDAO.getInstance().read(tranid);
	}

	/**
	 * @return the changeamt
	 */
	public Double getChangeamt() {
		return changeamt;
	}

	/**
	 * @param changeamt the changeamt to set
	 */
	public void setChangeamt(Double changeamt) {
		this.changeamt = changeamt;
	}

	/**
	 * @return the paytype
	 */
	public String getPaytype() {
		return paytype;
	}

	/**
	 * @param paytype the paytype to set
	 */
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	/**
	 * @return the tranid
	 */
	public String getTranid() {
		return tranid;
	}

	/**
	 * @param tranid the tranid to set
	 */
	public void setTranid(String tranid) {
		this.tranid = tranid;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
