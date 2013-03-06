package edu.byu.isys413.fbrooke;

public class GeneralLedger extends BusinessObject{

	@BusinessObjectField
	private String accName = null;
	@BusinessObjectField
	private Double balance = 0.0;
	@BusinessObjectField
	private String gltype = "Left";
	
	public GeneralLedger(String id){
		super(id);
	}

	/**
	 * @return the accName
	 */
	public String getAccName() {
		return accName;
	}

	/**
	 * @param accName the accName to set
	 */
	public void setAccName(String accName) {
		this.accName = accName;
		setDirty();
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
		setDirty();
	}

	/**
	 * @return the gltype
	 */
	public String getGltype() {
		return gltype;
	}

	/**
	 * @param gltype the gltype to set
	 */
	public void setGltype(String gltype) {
		this.gltype = gltype;
		setDirty();
	}
	
	

}
