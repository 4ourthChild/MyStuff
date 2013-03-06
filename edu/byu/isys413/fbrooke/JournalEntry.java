package edu.byu.isys413.fbrooke;

import java.util.*;

public class JournalEntry extends BusinessObject{
	
	@BusinessObjectField
	private Date entrydate = null;
	@BusinessObjectField
	private String tranId = null;
	@BusinessObjectField
	private boolean posted = false;
	
	/**Create Journal Entry Class*/
	public JournalEntry(String id){
		super(id);
	}

	public Date getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
		setDirty();
	}

	public Transaction getTran() throws DataException {
		return BusinessObjectDAO.getInstance().read(tranId);
	}


	/**
	 * @return the tranId
	 */
	public String getTranId() {
		return tranId;
	}

	/**
	 * @param tranId the tranId to set
	 */
	public void setTranId(String tranId) {
		this.tranId = tranId;
		setDirty();
	}
	
	public List<CreditDebit> getCDs() throws DataException{
		return BusinessObjectDAO.getInstance().searchForList("CreditDebit", new SearchCriteria("journalid", id));

	}

	/**
	 * @return the posted
	 */
	public boolean isPosted() {
		return posted;
	}

	/**
	 * @param posted the posted to set
	 */
	public void setPosted(boolean posted) {
		this.posted = posted;
		setDirty();
	}

	
}
