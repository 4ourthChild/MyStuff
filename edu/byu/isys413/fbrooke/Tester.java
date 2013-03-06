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
public class Tester {

    // for comparing dates
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public Tester() throws Exception {
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
//      s.setPassword("Suck Suck (on binkie)");
//      s.setBirthdate(new Date());
//      s.setIQ(200);
//      s.setDistance(1000);
      s.setSalary(100000.50);
      s.setFavoriteNumber(42);
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
//      assertEquals(s.getPassword(), s3.getPassword());
//      assertEquals(SDF.format(s.getBirthdate()), SDF.format(s3.getBirthdate()));
//      assertEquals(s.getIQ(), s3.getIQ());
//      assertEquals(s.getDistance(), s3.getDistance());
      assertTrue(s.getSalary() - s3.getSalary() < 0.1);
      assertTrue(s.getFavoriteNumber() - s3.getFavoriteNumber() < 0.1);

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // create another one with the same id (the other should be deleted)
      Employee s4 = BusinessObjectDAO.getInstance().create("Employee", "emp5");
      s4.setUsername("Maggie");
//      s4.setPassword("Suck Suck (on binkie)");
//      s4.setBirthdate(new Date());
//      s4.setIQ(200);
//      s4.setDistance(1000);
      s4.setSalary(100000.50);
      s4.setFavoriteNumber(42);
      s4.save();

      // test the search methods
      List<Employee> emps = BusinessObjectDAO.getInstance().searchForAll("Employee");
      assertEquals(emps.size(), 3);  // 2 from CreateDB, Maggie above
      Employee emp1 = BusinessObjectDAO.getInstance().searchForBO("Employee", new SearchCriteria("id", "employee1"));
      assertEquals(emp1.getId().trim(), "employee1");
      List<Employee> emps2 = BusinessObjectDAO.getInstance().searchForList("Employee", new SearchCriteria("username", "%a%", SearchCriteria.LIKE));
      assertEquals(emps2.size(), 2);


    }


    /** Test the Dog BO */
    @Test
    public void TestDog() throws Exception {
      Dog s = BusinessObjectDAO.getInstance().create("Dog", "dog11");
      s.setBreed("Mutt");
      s.setDogName("Matty");
      Person emp = BusinessObjectDAO.getInstance().read("person1");
      s.setPerson(emp);
      s.save();

      // since emp1 is in the Cache, this tests reading from the cache
      Dog s2 = BusinessObjectDAO.getInstance().read("dog11");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Dog s3 = BusinessObjectDAO.getInstance().read("dog11");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getBreed(), s3.getBreed());
      assertEquals(s.getPerson().getId().trim(), s3.getPerson().getId().trim());
      assertEquals(s.getDogName(), s3.getDogName());

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);

      // Employee tests the search methods, so no need to test them again
    }
    
    /** Test the 1-M relationship between Person and Dog (a person can have many dogs) */
    @Test
    public void TestPersonDogs() throws Exception {
        // this person will own three dogs
        Person p = BusinessObjectDAO.getInstance().create("Person", "personA");
        p.setFirstName("Jack");
        p.setLastName("O'Neill");
        p.setPhone("555-555-1234");
        p.save();
        
        // first dog
        Dog d1 = BusinessObjectDAO.getInstance().create("Dog", "dogA");
        d1.setBreed("Bassetoodle");
        d1.setDogName("Flipper");
        d1.setPerson(p);
        d1.save();
        
        // second dog
        Dog d2 = BusinessObjectDAO.getInstance().create("Dog", "dogB");
        d2.setBreed("Pug");
        d2.setDogName("Buddy");
        d2.setPerson(p);
        d2.save();
        
        // third dog
        Dog d3 = BusinessObjectDAO.getInstance().create("Dog", "dogC");
        d3.setBreed("Siberian Husky ");
        d3.setDogName("Doc");
        d3.setPerson(p);
        d3.save();
        
        // retrieve the three dogs from the Person object
        List<Dog> dogs = p.getDogs();
        assertEquals(dogs.size(), 3);
        System.out.println(dogs.get(0).getPerson().getId());
        assertSame(dogs.get(0).getPerson(), p);
        assertSame(dogs.get(1).getPerson(), p);
        assertSame(dogs.get(2).getPerson(), p);
    }
    
    /** Test the Car object */
    @Test
    public void TestCar() throws Exception {
      Car s = BusinessObjectDAO.getInstance().create("Car", "carA");
      s.setBrand("Kia");
      s.setModel("Sportage");
      s.save();

      // since the object is in the Cache, this tests reading from the cache
      Car s2 = BusinessObjectDAO.getInstance().read("carA");
      assertSame(s, s2);

      // now clear the cache (you'd never do this in the real world)
      // then we can test reading from the database
      Cache.getInstance().clear();
      Car s3 = BusinessObjectDAO.getInstance().read("carA");
      assertEquals(s.getId().trim(), s3.getId().trim());
      assertEquals(s.getBrand(), s3.getBrand());
      assertEquals(s.getModel(), s3.getModel());

      // test deleting
      BusinessObjectDAO.getInstance().delete(s);
    }    
    
    /** Test the M-M relationship between Person and Car */
    @Test
    public void TestPersonCar() throws Exception {
      // this test assumes that certain people and cars are already in the database
      // in the DB, person1 owns only car1, person2 owns car1, car2, car3
      Person person1 = BusinessObjectDAO.getInstance().read("person1");
      Person person2 = BusinessObjectDAO.getInstance().read("person2");
      Car car1 = BusinessObjectDAO.getInstance().read("car1");
      Car car2 = BusinessObjectDAO.getInstance().read("car2");
      Car car3 = BusinessObjectDAO.getInstance().read("car2");
      
      // test person1's cars
      assertEquals(person1.getCars().size(), 1);
      assertSame(person1.getCars().get(0), car1);
      
      // test person2's cars
      List<Car> cars = person2.getCars();
      assertEquals(cars.size(), 3);
      assertTrue(cars.contains(car1));
      assertTrue(cars.contains(car2));
      assertTrue(cars.contains(car3));
      
      // test car1's owners
      List<Person> owners = car1.getOwners();
      assertEquals(owners.size(), 2);
      assertTrue(owners.contains(person1));
      assertTrue(owners.contains(person2));
    }    
    
    

}