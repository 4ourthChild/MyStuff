/**
 * 
 */
package edu.byu.isys413.fbrooke;

/**
 * @author BrookieCookie
 *
 */
public class Computer extends BusinessObject{
	
	@BusinessObjectField
	private String macAddress = null;
	@BusinessObjectField
	private String storeId = null;
	
	public Computer(String id){
		super(id);
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
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
	
	public Store getStore() throws DataException{
		return BusinessObjectDAO.getInstance().read(storeId);
	}

}
