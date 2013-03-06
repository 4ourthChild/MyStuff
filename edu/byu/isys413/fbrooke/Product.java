package edu.byu.isys413.fbrooke;

import java.util.List;

public abstract class Product extends BusinessObject{
	
	@BusinessObjectField
	private String prodtype = null;
	@BusinessObjectField
	private Double price = 0.0;
	
	public Product(String id){
		super(id);
	}

	/**
	 * @return the type
	 */
	public String getProdtype() {
		return prodtype;
	}

	/**
	 * @param type the type to set
	 */
	public void setProdtype(String type) {
		this.prodtype = type;
		setDirty();
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
		setDirty();
	}
	
	public List<Sale> getSales() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("Sale", new SearchCriteria("productid", id));
	}

}
