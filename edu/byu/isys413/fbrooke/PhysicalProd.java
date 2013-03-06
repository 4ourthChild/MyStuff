package edu.byu.isys413.fbrooke;

//import java.sql.Date;
import java.util.*;
/**
 * 
 */

/**
 * @author BrookieCookie
 *
 */
public class PhysicalProd extends Product{
	
	@BusinessObjectField
	private String serialNum = null;
	@BusinessObjectField
	private String shelfLoc = null;
	@BusinessObjectField
	private Date datePurch = null;
	@BusinessObjectField
	private Double cost = 0.0;
	@BusinessObjectField
	private String status = null;
	@BusinessObjectField
	private Double ppCommRate = 0.0;
	@BusinessObjectField
	private String storeId = null;
	@BusinessObjectField
	private String cprodId = null;
	
	public PhysicalProd(String id){
		super(id);
	}

	/**
	 * @return the serialNum
	 */
	public String getSerialNum() {
		return serialNum;
	}

	/**
	 * @param serialNum the serialNum to set
	 */
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
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
	 * @return the datePurch
	 */
	public Date getDatePurch() {
		return datePurch;
	}

	/**
	 * @param datePurch the datePurch to set
	 */
	public void setDatePurch(Date datePurch) {
		this.datePurch = datePurch;
		setDirty();
	}

	/**
	 * @return the cost
	 */
	public Double getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(Double cost) {
		this.cost = cost;
		setDirty();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
		setDirty();
	}

	/**
	 * @return the ppCommRate
	 */
	public Double getPpCommRate() {
		return ppCommRate;
	}

	/**
	 * @param ppCommRate the ppCommRate to set
	 */
	public void setPpCommRate(Double ppCommRate) {
		this.ppCommRate = ppCommRate;
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

	/**
	 * @return the cprod
	 * @throws DataException 
	 */
	public CProduct getCprod() throws DataException {
		return BusinessObjectDAO.getInstance().searchForBO("CProduct", new SearchCriteria("id", cprodId));

	}

}
