package edu.byu.isys413.fbrooke;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Display;

public class TransactionCtlr {
	
	
	//////////////////////////////////////////////
	///   Singelton code

	/** The singelton instance of the class */
	private static TransactionCtlr instance = null;

	/** Creates a new instance of TransactionCtlr */
	private TransactionCtlr() {
	}

	/** Returns the singelton instance of the ConnectionPool */
	public static synchronized TransactionCtlr getInstance() {
		if (instance == null) {
			instance = new TransactionCtlr();
		}//if
		return instance;
	}//getInstance

	public String journalEntries(Transaction t) throws DataException{
		
		JournalEntry JE = BusinessObjectDAO.getInstance().create("JournalEntry");
		JE.setEntrydate(new Date());
		JE.setTranId(t.getId());
		JE.save();
		CreditDebit CD = BusinessObjectDAO.getInstance().create("CreditDebit");
		CD.setDorc(true);
		CD.setAmount(t.getTotal());
		CD.setGeneralname("Cash");
		CD.setJournalId(JE.getId());
		CD.save();
		
		CreditDebit CD2 = BusinessObjectDAO.getInstance().create("CreditDebit");
		CD2.setDorc(false);
		CD2.setAmount(t.getTax());
		CD2.setGeneralname("Tax Expense");
		CD2.setJournalId(JE.getId());
		CD2.save();	
		
		CreditDebit CD3 = BusinessObjectDAO.getInstance().create("CreditDebit");
		CD3.setDorc(false);
		CD3.setAmount(t.getSubtotal());
		CD3.setGeneralname("Sales Revenue");
		CD3.setJournalId(JE.getId());
		CD3.save();
		
		System.out.println(CD.getGeneralname() + " " + CD.getAmount());
		System.out.println(CD2.getGeneralname() + " " + CD2.getAmount());
		System.out.println(CD3.getGeneralname() + " " + CD3.getAmount());
		return JE.getId();
		
	}
	
	public void runCommission(Transaction t) throws DataException{
		
		List<Employee> emps = BusinessObjectDAO.getInstance().searchForAll("Employee");
		for(Employee e: emps){
			List<Commission> comms = e.getCommsByDate();
			Double totalComm = 0.0;
			for(Commission ce:comms){
				
				totalComm += ce.getAmt();
			}
			System.out.println(e.getName() + " earned " + totalComm);
			
			JournalEntry JE = BusinessObjectDAO.getInstance().create("JournalEntry");
			JE.setEntrydate(new Date());
			JE.setTranId(t.getId());
			JE.save();
			
			CreditDebit CD = BusinessObjectDAO.getInstance().create("CreditDebit");
			CD.setAmount(totalComm);
			CD.setDorc(true);
			CD.setGeneralname("Comm Payable");
			CD.setJournalId(JE.getId());
			CD.save();
			
			CreditDebit CD2 = BusinessObjectDAO.getInstance().create("CreditDebit");
			CD2.setAmount(totalComm);
			CD2.setDorc(false);
			CD2.setGeneralname("Cash");
			CD2.setJournalId(JE.getId());
			CD2.save();
			
			System.out.println(CD.getGeneralname() + " " +  CD.getAmount());
			System.out.println(CD2.getGeneralname() + " " +  CD2.getAmount());
		}
		
	}
	
	public void runGeneralLedger() throws DataException{
		List<GeneralLedger> gllist = BusinessObjectDAO.getInstance().searchForAll("GeneralLedger");
		Map<String, GeneralLedger> glMap = new TreeMap<String, GeneralLedger>();
		for(GeneralLedger gl: gllist){
			System.out.println("here" + gl.getAccName());
			glMap.put(gl.getAccName(), gl);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24);
		Date date = calendar.getTime();
		List<JournalEntry> jeList = BusinessObjectDAO.getInstance().searchForList("JournalEntry", new SearchCriteria("entrydate", date, SearchCriteria.GREATER_THAN_OR_EQUALS));
		
		for(JournalEntry je:jeList){
			
			if(!je.isPosted()){
			List<CreditDebit> cdList = je.getCDs();
			for(CreditDebit cd:cdList){
//				System.out.println("CD: " + cd.getGeneralname());
//				System.out.println("JE: " + je.getId());
				GeneralLedger genled = glMap.get(cd.getGeneralname());
//				System.out.println(glMap.get(cd.getGeneralname()));
//				System.out.println(genled.getAccName());
				Double b = genled.getBalance();
				//If it is on the left side of the accounting equation
				if(genled.getGltype().equals("Left")){
				//If it is true, or Debit
					if(cd.isDorc()){
						b += cd.getAmount();
						
					}else{//Credit Left side
						b = b - cd.getAmount();
					}
				}else{//If it is on the right side of the accounting equation
					if(cd.isDorc()){
						b -= cd.getAmount();
					}else{//Credit Left side
						b = b + cd.getAmount();
					}
				}
				
				genled.setBalance(b);
			}
			je.setPosted(true);
			}
		}
		
		for(GeneralLedger g:glMap.values())
		{
			System.out.println(g.getAccName() +" balance: $" + g.getBalance());
			g.save();
		}
	}
	
}
