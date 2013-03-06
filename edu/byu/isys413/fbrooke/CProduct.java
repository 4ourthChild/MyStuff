/**
 * 
 */
package edu.byu.isys413.fbrooke;

import java.util.LinkedList;
import java.util.List;

/**
 * @author BrookieCookie
 *
 */
public class CProduct extends Product{
	
	@BusinessObjectField
	private String name = null;
	@BusinessObjectField
	private String description = null;
	@BusinessObjectField
	private String manufacturer = null;
	@BusinessObjectField
	private Double avgCost = 0.0;
	@BusinessObjectField
	private Double commRate = 0.0;
	@BusinessObjectField
	private String SKU = null;
	@BusinessObjectField
	private String CProdId = null;
//	@BusinessObjectField
//	private String storeId = null;
//	private Store store = null;
	
	
	public CProduct(String id){
		super(id);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		setDirty();
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
		setDirty();
	}


	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}


	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
		setDirty();
	}


	/**
	 * @return the avgCost
	 */
	public Double getAvgCost() {
		return avgCost;
	}


	/**
	 * @param avgCost the avgCost to set
	 */
	public void setAvgCost(Double avgCost) {
		this.avgCost = avgCost;
		setDirty();
	}


	/**
	 * @return the commRate
	 */
	public Double getCommRate() {
		return commRate;
	}


	/**
	 * @param commRate the commRate to set
	 */
	public void setCommRate(Double commRate) {
		this.commRate = commRate;
		setDirty();
	}


	/**
	 * @return the sKU
	 */
	public String getSKU() {
		return SKU;
	}


	/**
	 * @param sKU the sKU to set
	 */
	public void setSKU(String sKU) {
		SKU = sKU;
		setDirty();
	}


	/**
	 * @return the cProdId
	 */
	public String getCProdId() {
		return CProdId;
	}


	/**
	 * @param cProdId the cProdId to set
	 */
	public void setCProdId(String cProdId) {
		CProdId = cProdId;
		setDirty();
	}


//	/**
//	 * @return the store
//	 */
//	public Store getStore() {
//		return store;
//	}
//
//
//	/**
//	 * @param store the store to set
//	 */
//	public void setStore(Store store) {
//		this.store = store;
//		setDirty();
//		setStoreId(store.getId());
//	}
//
//
//	/**
//	 * @return the storeId
//	 */
//	public String getStoreId() {
//		return storeId;
//	}
//
//
//	/**
//	 * @param storeId the storeId to set
//	 */
//	public void setStoreId(String storeId) {
//		this.storeId = storeId;
//		setDirty();
//	}
	
	  /** 
	   * Returns the PersonCar relationship objects that describe the people that own this particular car.
	   */
	  public List<StoreProduct> getStoreProds() throws DataException {
	    return BusinessObjectDAO.getInstance().searchForList("StoreProduct", new SearchCriteria("cprodid", CProdId));
	  }
	  
	  /**
	   * Retrieves the actual Person objects that own this car.
	   * This is a convenience method to traverse the intermediary table.
	   */
	  public List<Store> getStores() throws DataException {
	    List<Store> owners = new LinkedList<Store>();
	    for (StoreProduct sp: this.getStoreProds()) {
	      owners.add(sp.getStore());
	    }
	    return owners;    
	  }
	  
	  public List<PhysicalProd> getPhysicalProds() throws DataException{
		  return BusinessObjectDAO.getInstance().searchForList("PhysicalProd", new SearchCriteria("cprodid", id));
	  }
}
