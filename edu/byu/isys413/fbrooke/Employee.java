/////////////////////////////////////////////////////////////////
///   This file is an example of an Object Relational Mapping in
///   the ISys Core at Brigham Young University.  Students
///   may use the code as part of the 413 course in their
///   milestones following this one, but no permission is given
///   to use this code is any other way.  Since we will likely
///   use this code again in a future year, please DO NOT post
///   the code to a web site, share it with others, or pass
///   it on in any way.


package edu.byu.isys413.fbrooke;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * An employee.  Example BO that has inheritance.
 *
 * @author Conan C. Albrecht <conan@warp.byu.edu>
 * @version 1.1
 */
public class Employee extends BusinessObject {

    @BusinessObjectField
    private String username = null;
    @BusinessObjectField
    private java.util.Date hireDate = null;
    @BusinessObjectField
    private String name;
    @BusinessObjectField
    private String phone = null;
    @BusinessObjectField
    private double salary = 0.0;
    @BusinessObjectField
    private int favoriteNumber = 0;
    @BusinessObjectField
    private Double commRate = 0.0;
    @BusinessObjectField
    private String storeId = null;
    
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
	 * @return the store
	 * @throws DataException 
	 */
	public Store getStore() throws DataException {
		return BusinessObjectDAO.getInstance().read(storeId);
	}


    /** Creates a new instance of BusinessObject */
    public Employee(String id) {
        super(id);
    }//constructor

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
		setDirty();
	}

	/**
	 * @return the hireDate
	 */
	public java.util.Date getHireDate() {
		return hireDate;
	}

	/**
	 * @param hireDate the hireDate to set
	 */
	public void setHireDate(java.util.Date hireDate) {
		this.hireDate = hireDate;
		setDirty();
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
		setDirty();
	}

	/**
	 * @return the salary
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(double salary) {
		this.salary = salary;
		setDirty();
	}

	/**
	 * @return the favoriteNumber
	 */
	public int getFavoriteNumber() {
		return favoriteNumber;
	}

	/**
	 * @param favoriteNumber the favoriteNumber to set
	 */
	public void setFavoriteNumber(int favoriteNumber) {
		this.favoriteNumber = favoriteNumber;
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

	public List<Commission> getComms() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("Commission", new SearchCriteria("empid", id));
	}
	
	public List<Commission> getCommsByDate() throws DataException{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		Date d = c.getTime();
		return BusinessObjectDAO.getInstance().searchForList("Commission", new SearchCriteria("empid", id), new SearchCriteria("entrydate", d, SearchCriteria.GREATER_THAN_OR_EQUALS));
	}
   
	public List<Transaction> getTrans() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("Transaction", new SearchCriteria("empid", id));
	}
   
}//class
