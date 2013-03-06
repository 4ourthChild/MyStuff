/**
 * 
 */
package edu.byu.isys413.fbrooke;

/**
 * @author BrookieCookie
 *
 */
public class StoreProduct extends BusinessObject{

	@BusinessObjectField
	private int quantity = 0;
	@BusinessObjectField
	private String shelfLoc = null;
	@BusinessObjectField
	private String storeId = null;
	private Store store = null;
	@BusinessObjectField
	private String cprodId = null;
	private CProduct cprod = null;
	
	public StoreProduct(String id){
		super(id);
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		setDirty();
	}

	/**
	 * @return the shelfLoc
	 */
	public String getShelfLoc() {
		return shelfLoc;
	}

	/**
	 * @param shelfLoc the shelfLoc to set
	 */
	public void setShelfLoc(String shelfLoc) {
		this.shelfLoc = shelfLoc;
		setDirty();
	}

	/**
	 * @return the store
	 * @throws DataException 
	 */
	public Store getStore() throws DataException {
		return BusinessObjectDAO.getInstance().read(storeId);
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(Store store) {
		this.store = store;
		setStoreId(store.getId());
		setDirty();
	}

	/**
	 * @return the cprod
	 * @throws DataException 
	 */
	public CProduct getCprod() throws DataException {
		return BusinessObjectDAO.getInstance().searchForBO("CProduct", new SearchCriteria("cprodid", cprodId));
	}

	/**
	 * @param cprod the cprod to set
	 */
	public void setCprod(CProduct cprod) {
		this.cprod = cprod;
		setCprodId(cprod.getId());
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
	 * @return the cprodId
	 */
	public String getCprodId() {
		return cprodId;
	}

	/**
	 * @param cprodId the cprodId to set
	 */
	public void setCprodId(String cprodId) {
		this.cprodId = cprodId;
		setDirty();
	}
	
	
}
