package edu.byu.isys413.fbrooke;

import java.util.List;

public class Customer extends BusinessObject {
	
	@BusinessObjectField
	private String name = null;
	@BusinessObjectField
	private String phone = null;
	@BusinessObjectField
	private String email = null;
	@BusinessObjectField
	private String address = null;
	
	public Customer(String id){
		super(id);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setDirty();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		setDirty();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		setDirty();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		setDirty();
	}


	public List<Transaction> getTrans() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("Transaction", new SearchCriteria("custid", id));
	}
	
	

}
