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

import java.util.LinkedList;
import java.util.List;

/**
 * Example BO
 * 
 * @author conan
 * @version 1.1
 */
public class Car extends BusinessObject {

  @BusinessObjectField
  private String brand;
  @BusinessObjectField
  private String model;
  

  /** Creates a new instance of this object */
  public Car(String id) {
      super(id);
  }//constructor

  /**
   * @return the car model
   */
  public String getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(String model) {
    this.model = model;
    setDirty();
  }
  
  /** 
   * @return the car brand
   */
  public String getBrand() {
    return brand;
  }
  
  /** 
   * @brand the brand of the car
   */
  public void setBrand(String brand) {
    this.brand = brand;
  }

  /** 
   * Returns the PersonCar relationship objects that describe the people that own this particular car.
   */
  public List<PersonCar> getPersonCars() throws DataException {
    return BusinessObjectDAO.getInstance().searchForList("PersonCar", new SearchCriteria("carid", id));
  }
  
  /**
   * Retrieves the actual Person objects that own this car.
   * This is a convenience method to traverse the intermediary table.
   */
  public List<Person> getOwners() throws DataException {
    List<Person> owners = new LinkedList<Person>();
    for (PersonCar pc: this.getPersonCars()) {
      owners.add(pc.getOwner());
    }
    return owners;    
  }

}
