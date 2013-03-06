package edu.byu.isys413.fbrooke;

public class CreditDebit extends BusinessObject{
	
	//Debit is true, Credit is false
	@BusinessObjectField
	private Boolean dorc = false;
	@BusinessObjectField
	private String generalname = null;
	@BusinessObjectField
	private Double amount = 0.0;
	@BusinessObjectField
	private String journalId = null;
	
	public CreditDebit(String id){
		super(id);
	}

	public Boolean isDorc() {
		return dorc;
	}

	public void setDorc(Boolean dorc) {
		this.dorc = dorc;
		setDirty();
	}

	public String getGeneralname() {
		return generalname;
	}

	public void setGeneralname(String generalname) {
		this.generalname = generalname;
		setDirty();
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
		setDirty();
	}

	public JournalEntry getJournal() throws DataException {
		return BusinessObjectDAO.getInstance().read(journalId);
	}

	/**
	 * @return the journalId
	 */
	public String getJournalId() {
		return journalId;
	}

	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(String journalId) {
		this.journalId = journalId;
		setDirty();
	}
	
	

}
