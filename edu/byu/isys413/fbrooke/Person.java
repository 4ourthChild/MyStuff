/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.fbrooke;

import java.util.*;

/**
 * Example superclass BO.
 * 
 * @author conan
 * @version 1.1
 */
public class Person extends BusinessObject {

  @BusinessObjectField
  private String firstName = null;
  @BusinessObjectField
  private String lastName = null;
  @BusinessObjectField
  private String phone = null;


  /** Creates a new instance of this object */
  public Person(String id) {
      super(id);
  }//constructor

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
    setDirty();
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
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
    setDirty();
    this.phone = phone;
  }

  /**
   * Retrieves the dogs for this owner.
   */
  public List<Dog> getDogs() throws DataException {
    return BusinessObjectDAO.getInstance().searchForList("Dog", new SearchCriteria("personid", id));
  }

  /**
   * Retrieves the PersonCar relationship object for this owner.
   */
  public List<PersonCar> getPersonCars() throws DataException {
    return BusinessObjectDAO.getInstance().searchForList("PersonCar", new SearchCriteria("ownerid", id));
  }
  
  /**
   * Retrieves the actual Car objects for this owner.
   * This is a convenience method to traverse the intermediary table.
   */
  public List<Car> getCars() throws DataException {
    List<Car> cars = new LinkedList<Car>();
    for (PersonCar pc: this.getPersonCars()) {
      cars.add(pc.getCar());
    }
    return cars;
  }

}
