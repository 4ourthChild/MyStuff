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

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Tests for the program.  
 *
 * See http://www.junit.org/apidocs/org/junit/Assert.html for the
 * available assertions you can make.
 * 
 * @version 1.2
 */
public class Tester2 {

    // for comparing dates
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public Tester2() throws Exception {
        CreateDB.main(null);
    }

    /** Example test */
    @Test
    public void TestExample() throws Exception {
      String st1 = "Hi There";
      String st2 = "Hi There";
      assertEquals(st1, st2);
    }





    /** Test the Employee BO (also tests the Person BO) */
    @Test
    public void TestEmployee() throws Exception {
      Employee s = BusinessObjectDAO.getInstance().create("Employee", "emp1");
      s.setUsername("Maggie");
      s.setName("Suck Suck (on binkie)");
      s.setHireDate(new Date());
      s.setSalary(100000.50);
      s.setFavoriteNumber(42);
      s.setStoreId("store1");
      s.save();

      // since emp1 is in the Cache, this tests reading from the cache
      Employee s2 = BusinessObjectDAO.getInstance().read("emp1");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Employee s3 = BusinessObjectDAO.getInstance().read("emp1");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getUsername(), s3.getUsername());
      assertEquals(s.getName(), s3.getName());
      assertEquals(SDF.format(s.getHireDate()), SDF.format(s3.getHireDate()));
      assertTrue(s.getSalary() - s3.getSalary() < 0.1);
      assertTrue(s.getFavoriteNumber() - s3.getFavoriteNumber() < 0.1);
      assertEquals(s.getStoreId().trim(), s3.getStoreId().trim());

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Employee s4 = BusinessObjectDAO.getInstance().create("Employee", "emp5");
      s4.setUsername("Maggie");
      s4.setName("Suck Suck (on binkie)");
      s4.setHireDate(new Date());
      s4.setSalary(100000.50);
      s4.setFavoriteNumber(42);
      s4.setStoreId("store2");
      s4.save();

      // test the search methods
      List<Employee> emps = BusinessObjectDAO.getInstance().searchForAll("Employee");
      assertEquals(emps.size(), 3);  // 2 from CreateDB, Maggie above
      Employee emp1 = BusinessObjectDAO.getInstance().searchForBO("Employee", new SearchCriteria("id", "employee1"));
      assertEquals(emp1.getId().trim(), "employee1");
      List<Employee> emps2 = BusinessObjectDAO.getInstance().searchForList("Employee", new SearchCriteria("username", "%r%", SearchCriteria.LIKE));
      assertEquals(emps2.size(), 2);

      List<Commission> comms = emps2.get(0).getComms();
      assertEquals(comms.size(), 1);
      List<Transaction> trans = emps2.get(0).getTrans();
      assertEquals(trans.size(), 1);
      Store st = s4.getStore();
      assertEquals(st.getId(), "store2");
    }


    /** Test the Physical Product BO (also tests the Product BO) */
    @Test
    public void TestPhProduct() throws Exception {
    
    	//Product p = BusinessObjectDAO.getInstance().searchForBO("Product", new SearchCriteria("id", "product1"));
    	CProduct p = BusinessObjectDAO.getInstance().searchForBO("Product", new SearchCriteria("id", "product3"));
    	p.getPrice();
    	PhysicalProd p2 =  BusinessObjectDAO.getInstance().searchForBO("Product", new SearchCriteria("id", "product1"));
    	p2.getStatus();
    	Product p3 = BusinessObjectDAO.getInstance().searchForBO("Product", new SearchCriteria("id", "product2"));
    	p3.getPrice();
    	
    	PhysicalProd p4 = BusinessObjectDAO.getInstance().create("PhysicalProd", "productA");
        p4.setPrice(500.0);
        p4.setProdtype("Physical");
        p4.setCost(500.);
        p4.setCprodId("product3");
        p4.setDatePurch(new Date());
        p4.setPpCommRate(.66);
        p4.setSerialNum("45U");
        p4.setStatus("New");
        p4.setShelfLoc("Juniors");
        p4.setStoreId("store1");
        p4.save();
        
        // since emp1 is in the Cache, this tests reading from the cache
        Product p5 = BusinessObjectDAO.getInstance().read("productA");
        assertSame(p4, p5);

        // now clear the cache (you'd never do this in the real world)
        // then we can test reading from the database
        Cache.getInstance().clear();
        PhysicalProd p6 = BusinessObjectDAO.getInstance().read("productA");
        assertEquals(p4.getId().trim(), p6.getId().trim());
        assertEquals(p4.getPrice(), p6.getPrice());
        assertEquals(p4.getProdtype(), p6.getProdtype());
        assertEquals(p4.getCprodId().trim(), p6.getCprodId().trim());
        assertTrue(p4.getCost() - p6.getCost() < 0.1);
        assertEquals(SDF.format(p4.getDatePurch()), SDF.format(p6.getDatePurch()));
        assertTrue(p4.getPpCommRate() - p6.getPpCommRate() <.1);
        assertEquals(p4.getSerialNum(), p6.getSerialNum());
        assertEquals(p4.getStatus(), p6.getStatus());
        assertEquals(p4.getProdtype(), p6.getProdtype());
        assertEquals(p4.getShelfLoc(), p6.getShelfLoc());
        assertEquals(p4.getStoreId().trim(), p6.getStoreId().trim());


        // test deleting
        BusinessObjectDAO.getInstance().delete(p4);
        
    	PhysicalProd p7 = BusinessObjectDAO.getInstance().create("PhysicalProd", "productA");
        p7.setPrice(501.0);
        p7.setProdtype("Physical");
        p7.setCost(500.);
        p7.setCprodId("product4");
        p7.setDatePurch(new Date());
        p7.setPpCommRate(.63);
        p7.setSerialNum("45K");
        p7.setStatus("Newy");
        p7.setShelfLoc("Juniorsz");
        p7.setStoreId("store2");
        p7.save();
        
        // test the search methods
        List<PhysicalProd> prods = BusinessObjectDAO.getInstance().searchForAll("PhysicalProd");
        assertEquals(prods.size(), 3);  // 2 from CreateDB, Maggie above
        PhysicalProd prod1 = BusinessObjectDAO.getInstance().searchForBO("PhysicalProd", new SearchCriteria("id", "productA"));
        assertEquals(prod1.getId().trim(), "productA");
        List<Product> prods2 = BusinessObjectDAO.getInstance().searchForList("PhysicalProd", new SearchCriteria("serialnum", "%k%", SearchCriteria.LIKE));
        assertEquals(prods2.size(), 3);

        CProduct cp = p7.getCprod();
        assertEquals(cp.getId(), "product4");
    }
    
    /** Test the Conceptual Product object. Also tests Product */
    @Test
    public void TestCProduct() throws Exception {
    	CProduct p1 = BusinessObjectDAO.getInstance().create("CProduct", "product10");
        p1.setPrice(500.0);
        p1.setProdtype("Physical");
        p1.setAvgCost(500.);
        p1.setName("Hot Mamma");
        p1.setDescription("Really Hip");
        p1.setManufacturer("Boston");
        p1.setCommRate(.22);
        p1.setSKU("7878");
        p1.setCProdId("cprod10");
        p1.save();
        
        // since emp1 is in the Cache, this tests reading from the cache
        Product p2 = BusinessObjectDAO.getInstance().read("product10");
        assertSame(p1, p2);

        // now clear the cache (you'd never do this in the real world)
        // then we can test reading from the database
        Cache.getInstance().clear();
        CProduct p3 = BusinessObjectDAO.getInstance().read("product10");
        assertEquals(p1.getId().trim(), p3.getId().trim());
        assertEquals(p1.getPrice(), p3.getPrice());
        assertEquals(p1.getProdtype(), p3.getProdtype());
        assertEquals(p1.getCProdId().trim(), p3.getCProdId().trim());
        assertTrue(p1.getAvgCost() - p3.getAvgCost() < 0.1);
        assertEquals(p1.getSKU(), p3.getSKU());
        assertEquals(p1.getManufacturer(), p3.getManufacturer());
        assertEquals(p1.getDescription(), p3.getDescription());
        assertTrue(p1.getCommRate() - p3.getCommRate() <.1);


        // test deleting
        BusinessObjectDAO.getInstance().delete(p1);
        
    	CProduct p4 = BusinessObjectDAO.getInstance().create("CProduct", "productB");
        p4.setPrice(500.0);
        p4.setProdtype("Physical");
        p4.setAvgCost(500.);
        p4.setName("Hot Mamma");
        p4.setDescription("Really Hip");
        p4.setManufacturer("Boston");
        p4.setCommRate(.22);
        p4.setSKU("7878");
        p4.save();
        
        // test the search methods
        List<CProduct> prods = BusinessObjectDAO.getInstance().searchForAll("CProduct");
        assertEquals(prods.size(), 5);  // 2 from CreateDB, Maggie above
       	CProduct prod1 = BusinessObjectDAO.getInstance().searchForBO("CProduct", new SearchCriteria("id", "productB"));
        assertEquals(prod1.getId().trim(), "productB");
        List<Product> prods2 = BusinessObjectDAO.getInstance().searchForList("CProduct", new SearchCriteria("manufacturer", "sony", SearchCriteria.EQUALS));
        assertEquals(prods2.size(), 2);
        
        List<PhysicalProd> pprods = prods.get(0).getPhysicalProds();
        //assertEquals(pprods.size(), );
    }
    
    /** Test the Sale object. Also tests Revenue Source */
    @Test
    public void TestSale() throws Exception {
      Sale s = BusinessObjectDAO.getInstance().create("Sale", "saleA");
      s.setQuantity(20);
      s.setProductId("product1");
      s.setChargeAmt(40.3);
      s.setRstype("Sale");
      s.setTranId("transaction1");
      s.save();

      // since the object is in the Cache, this tests reading from the cache
      Sale s2 = BusinessObjectDAO.getInstance().read("saleA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Sale s3 = BusinessObjectDAO.getInstance().read("saleA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getQuantity(), s3.getQuantity());
      assertEquals(s.getProductId(), s3.getProductId());

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);
      
      // create another one with the same id (the other should be deleted)
      Sale s4 = BusinessObjectDAO.getInstance().create("Sale", "SaleA");
      s4.setQuantity(20);
      s4.setProductId("product1");
      s4.setChargeAmt(40.3);
      s4.setRstype("Sale");
      s4.setTranId("transaction1");
      s4.save();
      
    // test the search methods
      List<RevenueSource> rs = BusinessObjectDAO.getInstance().searchForAll("Sale");
      assertEquals(rs.size(), 3);  // 2 from CreateDB, Maggie above
      RevenueSource rs1 = BusinessObjectDAO.getInstance().searchForBO("RevenueSource", new SearchCriteria("id", "SaleA"));
//      assertEquals(rs1.getId().trim(), "SaleA");
      Product p = s4.getProduct();
      assertEquals(p.getId(), "product1");
      
    }
    
    /** Test the Customer BO */
    @Test
    public void TestCustomer() throws Exception {
      Customer s = BusinessObjectDAO.getInstance().create("Customer", "customerA");
      s.setName("Maggie Joe");
      s.setEmail("jojo@gmail.com");
      s.setAddress("NumaNumaland");
      s.setPhone("4545454545");
      s.save();

      // since customer1 is in the Cache, this tests reading from the cache
      Customer s2 = BusinessObjectDAO.getInstance().read("customerA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Customer s3 = BusinessObjectDAO.getInstance().read("customerA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getName(), s3.getName());
      assertEquals(s.getEmail(), s3.getEmail());
      assertEquals(s.getPhone(), s3.getPhone());
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Customer s4 = BusinessObjectDAO.getInstance().create("Customer", "customerA");
      s4.setName("Maggie");
      s4.setEmail("magglyapple@gmail.com");
      s4.setAddress("juniper road");
      s4.setPhone("3333333333");
      s4.save();

      // test the search methods
      List<Customer> custs = BusinessObjectDAO.getInstance().searchForAll("Customer");
      assertEquals(custs.size(), 5);  // 2 from CreateDB, Maggie above
      Customer cust1 = BusinessObjectDAO.getInstance().searchForBO("Customer", new SearchCriteria("id", "customerA"));
      assertEquals(cust1.getId().trim(), "customerA");
      List<Customer> custs2 = BusinessObjectDAO.getInstance().searchForList("Customer", new SearchCriteria("name", "%a%", SearchCriteria.LIKE));
      assertEquals(custs2.size(), 4);
      
      List<Transaction> trans = custs.get(0).getTrans();
      assertEquals(trans.size(), 1);
    }
    
    /** Test the Commission BO */
    @Test
    public void TestCommission() throws Exception {
      Commission s = BusinessObjectDAO.getInstance().create("Commission", "commissionA");
      s.setEntryDate(new Date());
      s.setAmt(200.2);
      s.setTranId("transaction1");
      s.setEmpId("employee1");
      s.save();

      // since customer1 is in the Cache, this tests reading from the cache
      Commission s2 = BusinessObjectDAO.getInstance().read("commissionA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Commission s3 = BusinessObjectDAO.getInstance().read("commissionA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(SDF.format(s.getEntryDate()), SDF.format(s3.getEntryDate()));
      assertTrue(s.getAmt() - s3.getAmt() <.1);
      assertEquals(s.getTranId(), s3.getTranId());
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Commission s4 = BusinessObjectDAO.getInstance().create("Commission", "commissionA");
      s4.setEntryDate(new Date());
      s4.setAmt(5.5);
      s4.setTranId("transaction2");
      s4.setEmpId("employee1");
      s4.save();

      // test the search methods
      List<Commission> comms = BusinessObjectDAO.getInstance().searchForAll("Commission");
      assertEquals(comms.size(), 3);  // 2 from CreateDB, one above
      Commission comm1 = BusinessObjectDAO.getInstance().searchForBO("Commission", new SearchCriteria("id", "commissionA"));
      assertEquals(comm1.getId().trim(), "commissionA");
      List<Commission> comms2 = BusinessObjectDAO.getInstance().searchForList("Commission", new SearchCriteria("entrydate", "%2013%", SearchCriteria.LIKE));
      assertEquals(comms2.size(), 3);

      Transaction t = s4.getTran();
      assertEquals(t.getId(), "transaction2");
      Employee e = s4.getEmp();
      assertEquals(e.getId(), "employee1");
    }  
    
    /** Test the Store BO */
    @Test
    public void TestStore() throws Exception {
      Store s = BusinessObjectDAO.getInstance().create("Store", "storeA");
      s.setLocation("Orem");
      s.setManagerId("employee1");
      s.setAddress("1566 S");
      s.setPhone("1234567891");
      s.setSalestax(.067);
      s.setSubNetId("34566");
      s.save();

      // since customer1 is in the Cache, this tests reading from the cache
      Store s1 = BusinessObjectDAO.getInstance().read("storeA");
      assertSame(s, s1);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Store s3 = BusinessObjectDAO.getInstance().read("storeA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getLocation(), s3.getLocation());
      assertEquals(s.getManagerId().trim(), s3.getManagerId().trim());
      assertEquals(s.getPhone(), s3.getPhone());
      assertEquals(s.getSubNetId(), s3.getSubNetId());
      assertTrue(s.getSalestax() - s3.getSalestax()<.1);
      assertEquals(s.getAddress(), s3.getAddress());

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Store s4 = BusinessObjectDAO.getInstance().create("Store", "storeA");
      s4.setLocation("Orem");
      s4.setManagerId("employee1");
      s4.setAddress("1566 S");
      s4.setPhone("1234567891");
      s4.setSalestax(.067);
      s4.setSubNetId("34566");
      s4.save();

      // test the search methods
      List<Store> stores = BusinessObjectDAO.getInstance().searchForAll("Store");
      assertEquals(stores.size(), 3);  // 2 from CreateDB, Maggie above
      Store store1 = BusinessObjectDAO.getInstance().searchForBO("Store", new SearchCriteria("id", "storeA"));
      assertEquals(store1.getId().trim(), "storeA");
      List<Customer> stores2 = BusinessObjectDAO.getInstance().searchForList("Store", new SearchCriteria("location", "%r%", SearchCriteria.LIKE));
      assertEquals(stores2.size(), 2);

      List<Employee> emps = stores.get(0).getEmps();
      assertEquals(emps.size(), 1);
      List<Transaction> trans = stores.get(0).getTrans();
      assertEquals(trans.size(), 1);
      List<PhysicalProd> pprods = stores.get(0).getPhysicalProds();
      assertEquals(pprods.size(), 2);
    }
    
    /** Test the Transaction BO */
    @Test
    public void TestTransaction() throws Exception {
      Transaction s = BusinessObjectDAO.getInstance().create("Transaction", "transactionA");
      s.setEntrydate(new Date());
      s.setSubtotal(234.1);
      s.setTotal(546.2);
      s.setCommissionId("commission1");
      s.setCustId("cust1");
      s.setJournalId("journal1");
      s.setStoreId("store1");
      s.setTax(300.1);
      s.setEmpId("employee1");
      s.save();

      // since customer1 is in the Cache, this tests reading from the cache
      Transaction s2 = BusinessObjectDAO.getInstance().read("transactionA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Transaction s3 = BusinessObjectDAO.getInstance().read("transactionA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(SDF.format(s.getEntrydate()), SDF.format(s3.getEntrydate()));
      assertTrue(s.getSubtotal() - s3.getSubtotal() <.1);
      assertTrue(s.getTotal() - s3.getTotal() <.1);
      assertTrue(s.getTax() - s3.getTax() <.1);
      assertEquals(s.getCustId().trim(), s3.getCustId().trim());
      assertEquals(s.getEmpId().trim(), s3.getEmpId().trim());
      assertEquals(s.getJournalId().trim(), s3.getJournalId().trim());
      assertEquals(s.getCommissionId().trim(), s3.getCommissionId().trim());
      assertEquals(s.getStoreId().trim(), s3.getStoreId().trim());
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Transaction s4 = BusinessObjectDAO.getInstance().create("Transaction", "transactionA");
      s4.setEntrydate(new Date());
      s4.setSubtotal(234.1);
      s4.setTotal(546.2);
      s4.setCommissionId("commission1");
      s4.setCustId("cust1");
      s4.setJournalId("journal1");
      s4.setStoreId("store1");
      s4.setTax(300.1);
      s4.setEmpId("employee1");
      s4.save();

      // test the search methods
      List<Transaction> trans = BusinessObjectDAO.getInstance().searchForAll("Transaction");
      assertEquals(trans.size(), 3);  // 2 from CreateDB, one above
      Transaction tran1 = BusinessObjectDAO.getInstance().searchForBO("Transaction", new SearchCriteria("id", "transactionA"));
      assertEquals(tran1.getId().trim(), "transactionA");
      List<Transaction> trans2 = BusinessObjectDAO.getInstance().searchForList("Transaction", new SearchCriteria("custid", "cust1", SearchCriteria.EQUALS));
      assertEquals(trans2.size(), 2);
      
      List<RevenueSource> rs = trans.get(0).getRSs();
      assertEquals(rs.size(), 2);
      Commission c = s4.getComm();
      assertEquals(c.getId(), "commission1");
      Employee e = s4.getEmp();
      assertEquals(e.getId(), "employee1");
      Store st = s4.getStore();
      assertEquals(st.getId(), "store1");
      Customer cust = s4.getCust();
      assertEquals(cust.getId(), "cust1");
      JournalEntry je = s4.getJe();
      assertEquals(je.getId(), "journal1");
    }  
    
    /** Test the Journal Entry BO */
    @Test
    public void TestJournalEntry() throws Exception {
      JournalEntry s = BusinessObjectDAO.getInstance().create("JournalEntry", "journalA");
      s.setEntrydate(new Date());
      s.setTranId("transaction2");
      s.save();

      // since journal1 is in the Cache, this tests reading from the cache
      JournalEntry s2 = BusinessObjectDAO.getInstance().read("journalA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      JournalEntry s3 = BusinessObjectDAO.getInstance().read("journalA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(SDF.format(s.getEntrydate()), SDF.format(s3.getEntrydate()));
      assertEquals(s.getTranId().trim(), s3.getTranId().trim());
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      JournalEntry s4 = BusinessObjectDAO.getInstance().create("JournalEntry", "journalA");
      s4.setEntrydate(new Date());
      s4.setTranId("transaction2");
      s4.save();

      // test the search methods
      List<JournalEntry> je = BusinessObjectDAO.getInstance().searchForAll("JournalEntry");
      assertEquals(je.size(), 3);  // 2 from CreateDB, one above
      JournalEntry je1 = BusinessObjectDAO.getInstance().searchForBO("JournalEntry", new SearchCriteria("id", "journalA"));
      assertEquals(je1.getId().trim(), "journalA");
      List<Transaction> jes2 = BusinessObjectDAO.getInstance().searchForList("JournalEntry", new SearchCriteria("tranid", "transaction1", SearchCriteria.EQUALS));
      assertEquals(jes2.size(), 1);
      
      Transaction t = s4.getTran();
      assertEquals(t.getId(), "transaction2");
      List<CreditDebit> cds = je.get(0).getCDs();
      assertEquals(cds.size(), 2);
    } 
    
    /** Test the CreditDebit BO */
    @Test
    public void TestCreditDebit() throws Exception {
      CreditDebit s = BusinessObjectDAO.getInstance().create("CreditDebit", "cdA");
      s.setDorc(false);
      s.setGeneralname("Cash");
      s.setAmount(345.2);
      s.setJournalId("journal1");
      s.save();

      // since journal1 is in the Cache, this tests reading from the cache
      CreditDebit s2 = BusinessObjectDAO.getInstance().read("cdA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      CreditDebit s3 = BusinessObjectDAO.getInstance().read("cdA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getJournalId().trim(), s3.getJournalId().trim());
      assertEquals(s.getGeneralname(), s3.getGeneralname());
      assertEquals(s.isDorc(), s3.isDorc());
      assertTrue(s.getAmount() - s3.getAmount()<.1);
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      CreditDebit s4 = BusinessObjectDAO.getInstance().create("CreditDebit", "cdA");
      s4.setDorc(false);
      s4.setGeneralname("Cash");
      s4.setAmount(345.2);
      s4.setJournalId("journal1");
      s4.save();

      // test the search methods
      List<CreditDebit> cd = BusinessObjectDAO.getInstance().searchForAll("CreditDebit");
      assertEquals(cd.size(), 5);  // 2 from CreateDB, one above
      CreditDebit cd1 = BusinessObjectDAO.getInstance().searchForBO("CreditDebit", new SearchCriteria("id", "cdA"));
      assertEquals(cd1.getId().trim(), "cdA");
      //List<Transaction> cds2 = BusinessObjectDAO.getInstance().searchForList("CreditDebit", new SearchCriteria("dorc", true, SearchCriteria.EQUALS));
      //assertEquals(cds2.size(), 1);

      JournalEntry je = s4.getJournal();
      assertEquals(je.getId(), "journal1");
      
    } 
    
    /** Test the GeneralLedger BO */
    @Test
    public void TestGeneralLedger() throws Exception {
      GeneralLedger s = BusinessObjectDAO.getInstance().create("GeneralLedger", "glA");
      s.setBalance(0.0);
      s.setAccName("Cash2");
      s.setGltype("Left");
      s.save();

      // since journal1 is in the Cache, this tests reading from the cache
      GeneralLedger s2 = BusinessObjectDAO.getInstance().read("glA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      GeneralLedger s3 = BusinessObjectDAO.getInstance().read("glA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getAccName(), s3.getAccName());
      assertTrue(s.getBalance() - s3.getBalance()<.1);
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      GeneralLedger s4 = BusinessObjectDAO.getInstance().create("GeneralLedger", "glA");
      s4.setBalance(0.0);
      s4.setAccName("Cash2");
      s4.setGltype("Left");
      s4.save();

      //get a map of gl <glname, globject>
      // test the search methods
      List<GeneralLedger> jls = BusinessObjectDAO.getInstance().searchForAll("GeneralLedger");
      //Map<String, GeneralLedger> GenMap = new TreeMap<String, GeneralLedger>();
      assertEquals(jls.size(), 6);  // 2 from CreateDB, one above
      GeneralLedger jl1 = BusinessObjectDAO.getInstance().searchForBO("GeneralLedger", new SearchCriteria("id", "glA"));
      assertEquals(jl1.getId().trim(), "glA");

    } 
    
    /** Test the StoreProduct BO */
    @Test
    public void TestStoreProduct() throws Exception {
      StoreProduct s = BusinessObjectDAO.getInstance().create("StoreProduct", "storeprodA");
      s.setQuantity(45);
      s.setShelfLoc("Isle10");
      s.setCprodId("product3");
      s.setStoreId("store1");
      s.save();

      // since journal1 is in the Cache, this tests reading from the cache
      StoreProduct s2 = BusinessObjectDAO.getInstance().read("storeprodA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      StoreProduct s3 = BusinessObjectDAO.getInstance().read("storeprodA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertTrue(s.getQuantity() - s3.getQuantity()<.1);
      assertEquals(s.getShelfLoc(), s3.getShelfLoc());
      assertEquals(s.getStoreId().trim(), s3.getStoreId().trim());
      assertEquals(s.getCprodId().trim(), s3.getCprodId().trim());
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      StoreProduct s4 = BusinessObjectDAO.getInstance().create("StoreProduct", "storeprodA");
      s4.setQuantity(45);
      s4.setShelfLoc("Isle10");
      s4.setCprodId("product3");
      s4.setStoreId("store1");
      s4.save();

      // test the search methods
      List<StoreProduct> sp = BusinessObjectDAO.getInstance().searchForAll("StoreProduct");
      assertEquals(sp.size(), 3);  // 2 from CreateDB, one above
      StoreProduct sp1 = BusinessObjectDAO.getInstance().searchForBO("StoreProduct", new SearchCriteria("id", "storeprodA"));
      assertEquals(sp1.getId().trim(), "storeprodA");
      List<StoreProduct> sp2 = BusinessObjectDAO.getInstance().searchForList("StoreProduct", new SearchCriteria("cprodid", "cprod1", SearchCriteria.EQUALS));
      assertEquals(sp2.size(), 1);

    } 
    /** Test the M-M relationship between Person and Car */
    @Test
    public void TestStoreProductRelationship() throws Exception {
      // this test assumes that certain stores and cproducts are already in the database
      // in the DB, store 1 has only cprod1, store2 has cprod1 and cprod2
      Store store1 = BusinessObjectDAO.getInstance().read("store1");
      Store store2 = BusinessObjectDAO.getInstance().read("store2");
      CProduct cprod1 = BusinessObjectDAO.getInstance().read("product3");
      CProduct cprod2 = BusinessObjectDAO.getInstance().read("product4");
      
      // test person1's cars
      assertEquals(store1.getCProds().size(), 1);
      assertSame(store1.getCProds().get(0), cprod1);
      
      // test person2's cars
      List<CProduct> cprods = store2.getCProds();
      assertEquals(cprods.size(), 1);
      assertTrue(cprods.contains(cprod2));
      
      // test cprod1's stores
      List<Store> stores = cprod1.getStores();
      assertEquals(stores.size(), 1);
      assertTrue(stores.contains(store1));
    }    
    
    /** Test the Payment BO */
    @Test
    public void TestPayment() throws Exception {
      Payment s = BusinessObjectDAO.getInstance().create("Payment", "payA");
      s.setAmount(15.);
      s.setPaytype("Cash");
      s.setTranid("transaction1");
      s.setChangeamt(5.);
      s.save();

      // since journal1 is in the Cache, this tests reading from the cache
      Payment s2 = BusinessObjectDAO.getInstance().read("payA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Payment s3 = BusinessObjectDAO.getInstance().read("payA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getTranid().trim(), s3.getTranid().trim());
      assertTrue(s.getAmount() - s3.getAmount()<.1);
      assertTrue(s.getChangeamt() - s3.getChangeamt()<.1);
      assertTrue(s.getAmount() - s3.getAmount()<.1);
      
      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Payment s4 = BusinessObjectDAO.getInstance().create("Payment", "payA");
      s4.setAmount(15.);
      s4.setPaytype("Cash");
      s4.setTranid("transaction1");
      s4.setChangeamt(5.);
      s4.save();

      // test the search methods
      List<Payment> pays = BusinessObjectDAO.getInstance().searchForAll("Payment");
      assertEquals(pays.size(), 3);  // 2 from CreateDB, one above
      Payment pay1 = BusinessObjectDAO.getInstance().searchForBO("Payment", new SearchCriteria("id", "payA"));
      assertEquals(pay1.getId().trim(), "payA");
      //List<Transaction> cds2 = BusinessObjectDAO.getInstance().searchForList("CreditDebit", new SearchCriteria("dorc", true, SearchCriteria.EQUALS));
      //assertEquals(cds2.size(), 1);

      Transaction tran = s4.getTran();
      assertEquals(tran.getId(), "transaction1");
      
    } 
}