package edu.byu.isys413.fbrooke;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class Tester1 {

	SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	public Tester1() throws Exception {
		CreateDB.main(null);
	}
	/**
	 * Tests the employee class
	 * @throws Exception
	 */
//	@Test
	public void TestEmployee() throws Exception {
		Employee s = BusinessObjectDAO.getInstance().create("Employee", "emp1");
		s.setFirstname("bobby");
		s.setLastname("lastbobby");
		s.setPhone("801801810");
		s.setBirthdate(SDF.parse("1985-01-23"));
		s.setIQ(200);
		s.setDistance(1000);
		s.setSalary(100000.50);
		s.setFavoriteNumber(42);
		s.setEmpNum(0001);
		s.save();
		
		// since emp1 is in the Cache, this tests reading from the cache
		Employee s2 = BusinessObjectDAO.getInstance().read("emp1");
		assertSame(s, s2);
		
		//checking db read
		Cache.getInstance().clear();
		Employee s3 = BusinessObjectDAO.getInstance().read("emp1");
		assertTrue("Same", compareTwoObjects(s.getClass(), s, s3));
		
		//checking db update
		s.setFirstname("newfirstname");
		s.save();
		Cache.getInstance().clear();
		Employee s4 = BusinessObjectDAO.getInstance().read("emp1");
		assertTrue("Same", compareTwoObjects(s.getClass(), s, s4));
	}

	/**
	 * Tests the commission class
	 * @throws Exception
	 */
//	@Test
	public void TestCommission() throws Exception {
		Commission c = BusinessObjectDAO.getInstance().create("Commission", "comm111");
		c.setAmount(23);
		c.setDate(SDF.parse("1985-01-25"));
		c.setEmployee_id("employee1");
		c.save();

		//testing cache
		Commission c2 = BusinessObjectDAO.getInstance().read("comm111");
		assertTrue("Same", compareTwoObjects(c.getClass(), c, c2));

		//testing db read
		Cache.getInstance().clear();
		Commission c3 = BusinessObjectDAO.getInstance().read("comm111");
		assertTrue("Same", compareTwoObjects(c.getClass(), c, c3));

		//testing db update
		c.setAmount(10);
		c.save();
		Cache.getInstance().clear();
		Commission c4 = BusinessObjectDAO.getInstance().read("comm111");
		assertTrue("Same", compareTwoObjects(c.getClass(), c, c4));

		//if you really want to see a failure...
//		c4.setAmount(99999);
//		assertTrue("Same", compareTwoObjects(c.getClass(), c, c4));
	}

	/**
	 * Tests the prod and concept prod classes
	 * @throws Exception
	 */
//	@Test
	public void TestProdAndConceptualProd() throws Exception {
		ConceptualProd cp = BusinessObjectDAO.getInstance().create("ConceptualProd", "cprod111");
		cp.setCprod_num(111);
		cp.setDescription("It's a cprod!");
		cp.setManufacturer("Starving children in undisclosed third-world country");
		cp.setName("Thisisaname");
		cp.setPrice(2212);
		cp.setAverage_cost(11);
		cp.setCommission_rate(.078);
		cp.setStore_id("store1");
		cp.setType("conceptual");
		cp.save();

		ConceptualProd cp1 = BusinessObjectDAO.getInstance().read("cprod111");
		assertTrue("Not Same", compareTwoObjects(cp.getClass(), cp, cp1));
		
		Cache.getInstance().clear();
		ConceptualProd cp2 = BusinessObjectDAO.getInstance().read("cprod111");
		assertTrue("Not Same", compareTwoObjects(cp.getClass(), cp, cp2));
		
		cp.setCommission_rate(.111);
		cp.save();
		Cache.getInstance().clear();
		ConceptualProd cp3 = BusinessObjectDAO.getInstance().read("cprod111");
		assertTrue("Not Same", compareTwoObjects(cp.getClass(), cp, cp3));

		//if you really want to break it
//		cp.setAverage_cost(9090);
//		assertTrue("Not Same", compareTwoObjects(cp.getClass(), cp, cp3));
	}

	@Test
	public void TestProdAndPhysicalProd() throws Exception {
		PhysicalProd pp = BusinessObjectDAO.getInstance().create("PhysicalProd", "pprod111");
		pp.setLocation("top");
		pp.setPrice(2212);
		pp.setCommission_rate(.078);
		pp.setStore_id("store1");
		pp.setType("physical");
		pp.setCost(99);
		pp.setPurchase_date(SDF.parse("1985-11-25"));
		pp.setStatus("good");
		pp.save();

		PhysicalProd pp1 = BusinessObjectDAO.getInstance().read("pprod111");
		assertTrue("Not Same", compareTwoObjects(pp.getClass(), pp, pp1));
		
		Cache.getInstance().clear();
		PhysicalProd pp2 = BusinessObjectDAO.getInstance().read("pprod111");
		assertTrue("Not Same", compareTwoObjects(pp.getClass(), pp, pp2));
		
		pp.setCommission_rate(.111);
		pp.save();
		Cache.getInstance().clear();
		PhysicalProd pp3 = BusinessObjectDAO.getInstance().read("pprod111");
		assertTrue("Not Same", compareTwoObjects(pp.getClass(), pp, pp3));

		//if you really want to break it
//		pp.setPrice(21212121);
//		assertTrue("Not Same", compareTwoObjects(pp.getClass(), pp, pp3));
	}

	/**
	 * Testing the customer class
	 * @throws Exception
	 */
	// @Test
	public void TestCustomer() throws Exception {
		Customer cust = BusinessObjectDAO.getInstance().create("Customer", "cust111");
		cust.setFirstname("Billy");
		cust.setLastname("Rae");
		cust.setPhone("555-55-65465");
		cust.setEmail("billyjeanisnotmylover@justafriend.com");
		cust.setAddress("Oh oh, uh uh, Provo UT");
		cust.save();

		//testing cache
		Customer cust2 = BusinessObjectDAO.getInstance().read("cust111");
		assertTrue(compareTwoObjects(cust.getClass(), cust, cust2));

		//testing read from db
		Cache.getInstance().clear();
		Customer cust3 = BusinessObjectDAO.getInstance().read("cust111");
		assertTrue(compareTwoObjects(cust.getClass(), cust, cust3));
		
		cust.setAddress("moving to montana");
		cust.save();
		Cache.getInstance().clear();
		Customer cust4 = BusinessObjectDAO.getInstance().read("cust111");
		assertTrue(compareTwoObjects(cust.getClass(), cust, cust4));
		
		//if you really want to see a failure
//		cust4.setAddress("BLASHDFLASJDFLKJ St.");
//		assertTrue(compareTwoObjects(cust.getClass(), cust, cust4));
	}

	/**
	 * Tests the debitcredit class
	 * @throws Exception
	 */
//	@Test
	public void TestDebitCredit() throws Exception{
		DebitCredit dc = BusinessObjectDAO.getInstance().create("DebitCredit", "dc111");
		dc.setIsdebit(true);
		dc.setGenledger_name("cashmoney");
		dc.setAmount(99);
		dc.setJournalentry_id("jeid1");
		dc.save();

		DebitCredit dc2 = BusinessObjectDAO.getInstance().read("dc111");
		assertTrue(compareTwoObjects(dc.getClass(), dc, dc2));
		
		Cache.getInstance().clear();
		DebitCredit dc3 = BusinessObjectDAO.getInstance().read("dc111");
		assertTrue(compareTwoObjects(dc.getClass(), dc, dc3));

		dc.setIsdebit(false);
		dc.save();
		Cache.getInstance().clear();
		DebitCredit dc4 = BusinessObjectDAO.getInstance().read("dc111");
		assertTrue(compareTwoObjects(dc.getClass(), dc, dc4));

		//if you really want to break it...
//		dc.setAmount(11111);
//		assertTrue("FAILURE HERE", compareTwoObjects(dc.getClass(), dc, dc4));
	}

	/**
	 * Tests the general ledger class
	 * @throws Exception
	 */
//	@Test
	public void TestGenLedger() throws Exception{
		GenLedger gl = BusinessObjectDAO.getInstance().create("GenLedger", "gl111");
		gl.setName("a ledger general");
		gl.setBalance(90000);
		gl.setType("not sure");
		gl.save();

		GenLedger gl2 = BusinessObjectDAO.getInstance().read("gl111");
		assertTrue(compareTwoObjects(gl.getClass(), gl, gl2));
		
		Cache.getInstance().clear();
		GenLedger gl3 = BusinessObjectDAO.getInstance().read("gl111");
		assertTrue(compareTwoObjects(gl.getClass(), gl, gl3));

		gl.setName("othername");
		gl.save();
		Cache.getInstance().clear();
		GenLedger gl4 = BusinessObjectDAO.getInstance().read("gl111");
		assertTrue(compareTwoObjects(gl.getClass(), gl, gl4));

		//if you really want to break it...
//		gl.setType("blablabla");
//		assertTrue(compareTwoObjects(gl.getClass(), gl, gl4));		
	}

//	@Test
	public void TestJournalEntry() throws Exception{
		JournalEntry je = BusinessObjectDAO.getInstance().create("JournalEntry", "je111");
		je.setTransaction_id("transid");
		je.setDate(SDF.parse("1985-11-25"));
		je.save();

		JournalEntry je2 = BusinessObjectDAO.getInstance().read("je111");
		assertTrue(compareTwoObjects(je.getClass(), je, je2));
		
		Cache.getInstance().clear();
		JournalEntry je3 = BusinessObjectDAO.getInstance().read("je111");
		assertTrue(compareTwoObjects(je.getClass(), je, je3));

		je.setDate(SDF.parse("1985-11-29"));
		je.save();
		Cache.getInstance().clear();
		JournalEntry je4 = BusinessObjectDAO.getInstance().read("je111");
		assertTrue(compareTwoObjects(je.getClass(), je, je4));

		//if you really want to break it...
//		je.setTransaction_id("blablabla");
//		assertTrue(compareTwoObjects(je.getClass(), je, je4));		
	}

	// @Test
	public void TestPayment() throws Exception{
		Payment p = BusinessObjectDAO.getInstance().create("Payment", "p111");
		p.setTransaction_id("transid");
		p.setAmount(9987);
		p.setPay_change(234);
		p.setType("another type");
		p.setTransaction_id("trans1");
		p.save();

		Payment p2 = BusinessObjectDAO.getInstance().read("p111");
		assertTrue(compareTwoObjects(p.getClass(), p, p2));
		
		Cache.getInstance().clear();
		Payment p3 = BusinessObjectDAO.getInstance().read("p111");
		assertTrue(compareTwoObjects(p.getClass(), p, p3));

		p.setAmount(1111);
		p.save();
		Cache.getInstance().clear();
		Payment p4 = BusinessObjectDAO.getInstance().read("p111");
		assertTrue(compareTwoObjects(p.getClass(), p, p4));

		//if you really want to break it...
//		p.setTransaction_id("blablabla");
//		assertTrue(compareTwoObjects(p.getClass(), p, p4));		
	}

//	@Test
	public void TestRevenueSource() throws Exception{
		RevenueSource rs = BusinessObjectDAO.getInstance().create("RevenueSource", "rs111");
		rs.setTransaction_id("transid");
		rs.setChargeamt(111);
		rs.setType("good?");
		rs.save();

		RevenueSource rs2 = BusinessObjectDAO.getInstance().read("rs111");
		assertTrue(compareTwoObjects(rs.getClass(), rs, rs2));
		
		Cache.getInstance().clear();
		RevenueSource rs3 = BusinessObjectDAO.getInstance().read("rs111");
		assertTrue(compareTwoObjects(rs.getClass(), rs, rs3));

		rs.setChargeamt(43433);
		rs.save();
		Cache.getInstance().clear();
		RevenueSource rs4 = BusinessObjectDAO.getInstance().read("rs111");
		assertTrue(compareTwoObjects(rs.getClass(), rs, rs4));

		//if you really want to break it...
//		rs.setTransaction_id("blablabla");
//		assertTrue(compareTwoObjects(rs.getClass(), rs, rs4));		
	}

//	@Test
	public void TestStore() throws Exception{
		Store c = BusinessObjectDAO.getInstance().create("Store", "store111");
		c.setManagerid("employee1");
		c.setLocation("canada");
		c.setPhone("callmemaybe");
		c.setAddress("canada St.");
		c.setSalestaxrate(.069);
		c.setSubnetid("255.255.128.128");
		c.save();

		//Testing Cache
		Store c2 = BusinessObjectDAO.getInstance().read("store111");
		assertTrue("Same", compareTwoObjects(c.getClass(), c, c2));

		//Testing DB read and clearing cache
		Cache.getInstance().clear();
		Store c3 = BusinessObjectDAO.getInstance().read("store111");
		assertTrue("Same", compareTwoObjects(c.getClass(), c3, c2));
		c3.save();
	
		//testing update of item;
		c3.setSubnetid("255.255.253");
		c3.save();
		Cache.getInstance().clear();
		Store s4 = BusinessObjectDAO.getInstance().read("store111");
		assertTrue(compareTwoObjects(c.getClass(), c3, s4));
		
		//if you really want to break it...
//		c.setPhone("blablabla");
//		assertTrue(compareTwoObjects(c.getClass(), c, s4));
	
	}

	// @Test
	public void TestStoreProd() throws Exception{
		StoreProd sp = BusinessObjectDAO.getInstance().create("StoreProd", "StoreProd111");
		sp.setStore_id("store1");
		sp.setLocation("top");
		sp.setCprod_id("cprod1");
		sp.setQuantity(9);
		sp.save();

		//Testing Cache
		StoreProd sp2 = BusinessObjectDAO.getInstance().read("StoreProd111");
		assertTrue("Same", compareTwoObjects(sp.getClass(), sp, sp2));

		//Testing DB read and clearing cache
		Cache.getInstance().clear();
		StoreProd sp3 = BusinessObjectDAO.getInstance().read("StoreProd111");
		assertTrue("Same", compareTwoObjects(sp.getClass(), sp3, sp2));
		sp3.save();
	
		//testing update of item;
		sp3.setCprod_id("newcprod");
		sp3.save();
		Cache.getInstance().clear();
		StoreProd sp4 = BusinessObjectDAO.getInstance().read("StoreProd111");
		assertTrue(compareTwoObjects(sp.getClass(), sp3, sp4));
		
		//if you really want to break it...
//		sp.setLocation("blablabla");
//		assertTrue(compareTwoObjects(sp.getClass(), sp, sp4));
	}

//	@Test
	public void TestTransaction() throws Exception{
		Transaction t = BusinessObjectDAO.getInstance().create("Transaction", "t111");
		t.setDate(SDF.parse("1985-01-23"));
		t.setSubtotal(9987);
		t.setTax(234);
		t.setTotal(12414);
		t.setStore_id("store1");
		t.setCustomer_id("cust1");
		t.setEmployee_id("emp1");
		t.save();

		Transaction t2 = BusinessObjectDAO.getInstance().read("t111");
		assertTrue(compareTwoObjects(t.getClass(), t, t2));
		
		Cache.getInstance().clear();
		Transaction t3 = BusinessObjectDAO.getInstance().read("t111");
		assertTrue(compareTwoObjects(t.getClass(), t, t3));

		t.setTax(1111);
		t.save();
		Cache.getInstance().clear();
		Transaction t4 = BusinessObjectDAO.getInstance().read("t111");
		assertTrue(compareTwoObjects(t.getClass(), t, t4));

		//if you really want to break it...
//		t.setDate(SDF.parse("1985-01-01"));
//		assertTrue(compareTwoObjects(t.getClass(), t, t4));		
	}

	/**
	 * This awesome method compares two objects, no matter the class.  One and done!
	 * It even compares the superclasses by calling itself if there is a superclass that
	 * needs comparing.  Marvel at my genius.
	 * @param boClass
	 * @param Obj1
	 * @param Obj2
	 * @return
	 */
	private boolean compareTwoObjects(Class boClass, Object Obj1, Object Obj2) {
		try {

			Map<String, Class> firstMap = getBusinessObjectFields(boClass);
			Class tempBoClass = boClass.getSuperclass();
			if(tempBoClass != BusinessObject.class){
				if(compareTwoObjects(tempBoClass, Obj1, Obj2) == false){
					return false;
				}
				tempBoClass = tempBoClass.getSuperclass();
			}

			for (Map.Entry<String, Class> entry : firstMap.entrySet()) {
				String fieldName = entry.getKey();
				Class fieldType = entry.getValue();
				// System.out.println(fieldType.getName());
				Method method = getGetterMethod(boClass, fieldName, fieldType);
				// Method method2 = getGetterMethod(Obj2, fieldName, fieldType);

				method.invoke(Obj1);
				// System.out.println(fieldName);

				if (fieldType == String.class) {
					// System.out.println("string");
					String string1 = (String) method.invoke(Obj1);
					String string2 = (String) method.invoke(Obj2);
//					System.out.println("1: " +string1+" 2: "+string2);
					if (string1 != null && string2 != null) {
						if (method.invoke(Obj1).equals(method.invoke(Obj2))) {
//							 System.out.println("same");
						} else {
							System.out.println("not same " + fieldName + " " + string1 + " and " + string2);
							return false;
						}
					} else {
						System.out.println("one is null");
						if (string1 == null) {
							if (string2 == null) {

							} else {
								System.out.println(fieldName + "Not the same string(null1)");
								return false;
							}
						}
						if (string2 == null) {
							if (string1 == null) {

							} else {
								System.out.println(fieldName + "Not the same string(null2)");
								return false;
							}
						}
					}
//					 return true;
				} else if (fieldType == Integer.class || fieldType == int.class) {
//					System.out.println("integer");

					int value1 = (int) method.invoke(Obj1);
					int value2 = (int) method.invoke(Obj2);

					if (value1 != 0 && value2 != 0) {
						if (method.invoke(Obj1).equals(method.invoke(Obj2))) {
//							System.out.println("values are the same");
						} else {
							System.out.println("ints are not the same, int1 = " + value1 + " int 2= " + value2);
							return false;
						}
					} else {
//						System.out.println("one is 0");
						if (value1 == 0) {
							if (value2 == 0) {

							} else {
								System.out.println(fieldName + " 1 One int is 0 and the other is not");
								return false;
							}
						}
						if (value2 == 0) {
							if (value1 == 0) {

							} else {
								System.out.println(fieldName + " 2 One int is 0 and the other is not");
								return false;
							}
						}
					}
//					return true;
				} else if (fieldType == Long.class || fieldType == long.class) {
					System.out.println("there shouldn't be longs");
					return false;
				} else if (fieldType == Float.class || fieldType == float.class) {
					System.out.println("there shouldn't be floats");
					return false;
				} else if (fieldType == double.class || fieldType == double.class) {
//					System.out.println("double");
					double value1 = (double) method.invoke(Obj1);
					double value2 = (double) method.invoke(Obj2);

					if (value1 != 0 && value2 != 0) {
						if (method.invoke(Obj1).equals(method.invoke(Obj2))) {
//							System.out.println("values are the same");
						} else {
							System.out.println("doubles are not the same, double1 = " + value1 + " double 2= " + value2);
							return false;
						}
					} else {
//						System.out.println("one is 0");
						if (value1 == 0) {
							if (value2 == 0) {
								//then they are the same
							} else {
								System.out.println(fieldName + " 1 One double is 0 and the other is not");
								return false;
							}
						}
						if (value2 == 0) {
							if (value1 == 0) {
								//then they are the same
							} else {
								System.out.println(fieldName + " 2 One double is 0 and the other is not");
								return false;
							}
						}
					}
//					return true;
				} else if (fieldType == java.util.Date.class) {
//					System.out.println("date");

					java.util.Date date1 = (java.util.Date) method.invoke(Obj1);
					java.util.Date date2 = (java.util.Date) method.invoke(Obj2);
					if (date1 != null && date2 != null) {
						if (date1.equals(date2)) {
//							 System.out.println("same");
						} else {
							System.out.println("not same " + fieldName + " " + date1 + " and " + date2);
							return false;
						}
					} else {
						// System.out.println("one is null");
						if (date1 == null) {
							if (date2 == null) {
//								System.out.println("both null");
							} else {
								System.out.println(fieldName + "Not the same date(null1)");
								return false;
							}
						}
						if (date2 == null) {
							if (date1 == null) {
//								System.out.println("both null");
							} else {
								System.out.println(fieldName + "Not the same date(null2)");
								return false;
							}
						}
					}
//					return true;

				} else if (fieldType == Boolean.class || fieldType == boolean.class) {
//					System.out.println("boolean");
					boolean value1 = (boolean) method.invoke(Obj1);
					boolean value2 = (boolean) method.invoke(Obj2);

					if(value1 != value2){
						System.out.println("Boolean not the same");
						return false;
					} else {
//						System.out.println("Boolean same");
					}
//					return true;

				} else {
					throw new DataException("Cannot read " + boClass.getName() + " from the database.  The BusinessObjectDAO does not support this data type: " + fieldType.getName());
				}

				// Class fieldType2 = secondMap.get(fieldName);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets all of the fields for a class.
	 * 
	 * @param boClass
	 * @return
	 * @throws Exception
	 */
	private Map<String, Class> getBusinessObjectFields(Class boClass) throws Exception {
		Map<String, Class> fields = new TreeMap<String, Class>();
		for (Field field : boClass.getDeclaredFields()) {
			if (field.getAnnotation(BusinessObjectField.class) != null) {
				fields.put(field.getName(), field.getType());
			}// if
		}// for
		return fields;
	}

	/** Returns the getter for the given field name */
	private Method getGetterMethod(Class boClass, String fieldName, Class fieldType) throws DataException {
		String getterName = "get" + fieldName.substring(0, 1).toUpperCase();
		if (fieldType == Boolean.class || fieldType == boolean.class) {
			getterName = "is" + fieldName.substring(0, 1).toUpperCase();
		}
		if (fieldName.length() > 1) {
			getterName += fieldName.substring(1);
		}// if
		try {
			Method method = boClass.getDeclaredMethod(getterName);
			if (method.getReturnType() == fieldType) {
				return method;
			}
		} catch (NoSuchMethodException nsme) {
			// pass so we throw the exception at the end
		}// try
		throw new DataException("Error in " + boClass.getName() + ".  No method named " + getterName + "(" + fieldType.getName() + ") to correspond with field " + fieldName + " in the database.");
	}

}
