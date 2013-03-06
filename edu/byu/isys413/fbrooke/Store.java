package edu.byu.isys413.fbrooke;

import java.util.LinkedList;
import java.util.List;

public class Store extends BusinessObject{
	
	@BusinessObjectField
	private String location = null;
	@BusinessObjectField
	private String managerId = null;
	@BusinessObjectField
	private String address = null;
	@BusinessObjectField
	private String phone = null;
	@BusinessObjectField
	private Double salestax = .067;
	@BusinessObjectField
	private String SubNetId;
	
	public Store(String id)
	{
		super(id);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
		setDirty();
	}

	public Employee getManager() throws DataException {
		return BusinessObjectDAO.getInstance().read(managerId);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		setDirty();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		setDirty();
	}

	public Double getSalestax() {
		return salestax;
	}

	public void setSalestax(Double salestax) {
		this.salestax = salestax;
		setDirty();
	}

	public String getSubNetId() {
		return SubNetId;
	}

	public void setSubNetId(String subNetId) {
		SubNetId = subNetId;
		setDirty();
	}

	/**
	 * @return the managerId
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId;
		setDirty();
	}

	public List<StoreProduct> getStoreProds(){
		try {
			return BusinessObjectDAO.getInstance().searchForList("StoreProduct", new SearchCriteria("storeid", id));
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	   * Retrieves the actual Cprod objects for this store.
	   * This is a convenience method to traverse the intermediary table.
	   */
	  public List<CProduct> getCProds() throws DataException {
	    List<CProduct> CProducts = new LinkedList<CProduct>();
	    for (StoreProduct sp: this.getStoreProds()) {
	      CProducts.add(sp.getCprod());
	    }
	    return CProducts;
	  }
	  
	  public List<Transaction> getTrans() throws DataException{
			return BusinessObjectDAO.getInstance().searchForList("Transaction", new SearchCriteria("storeid", id));
	  }
	  
	  public List<Employee> getEmps() throws DataException{
			return BusinessObjectDAO.getInstance().searchForList("Employee", new SearchCriteria("storeid", id));

	  }
	  
	  public List<PhysicalProd> getPhysicalProds() throws DataException{
			return BusinessObjectDAO.getInstance().searchForList("PhysicalProd", new SearchCriteria("storeid", id));
	  }
	  
	  public List<Computer> getComputers() throws DataException{
		  return BusinessObjectDAO.getInstance().searchForList("Computer", new SearchCriteria("storeid", id));
	  }
}
