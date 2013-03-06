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

/**
 * Represents the many-to-many part of a Person - Car relationship.
 * 
 * @author conan
 * @version 1.1
 */
public class PersonCar extends BusinessObject {

  @BusinessObjectField
  private String ownerId;
  @BusinessObjectField
  private String carId;
  

  /** Creates a new instance of this object */
  public PersonCar(String id) {
      super(id);
  }//constructor
  
  
  
  //////////////////////////////////////////////////////
  ///  These next methods are our public interface
  
  /**
   * @return the car
   */
  public Car getCar() throws DataException {
    return BusinessObjectDAO.getInstance().read(getCarId());  
  }

  /**
   * @param car the car
   */
  public void setCar(Car car) {
    this.setCarId(car.getId());
    setDirty();
  }
  
  /** 
   * @return the owner of the car
   */
  public Person getOwner() throws DataException {
    return BusinessObjectDAO.getInstance().read(getOwnerId());
  }
  
  /** 
   * @owner the owner of the car
   */
  public void setOwner(Person owner) {
    this.setOwnerId(owner.getId());
  }
  
  

  //////////////////////////////////////////////////
  ///  These next methods are for the DAO to use
  
  /**
   * @return the ownerId
   */
  String getOwnerId() {
    return ownerId;
  }

  /**
   * @param ownerId the ownerId to set
   */
  void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  /**
   * @return the carId
   */
  String getCarId() {
    return carId;
  }

  /**
   * @param carId the carId to set
   */
  void setCarId(String carId) {
    this.carId = carId;
  }


}
