package edu.byu.isys413.fbrooke;

public class Sale extends RevenueSource{

	@BusinessObjectField
	private int quantity=0;
	@BusinessObjectField
	private String productId = null;
	
	public Sale(String id){
		super(id);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		setDirty();
	}

	public Product getProduct() throws DataException {
		return BusinessObjectDAO.getInstance().read(productId);
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
		setDirty();
	}
	
	
	
}
