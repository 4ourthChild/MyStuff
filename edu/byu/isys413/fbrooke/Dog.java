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
 * Example BO
 * 
 * @author conan
 * @version 1.1
 */
public class Dog extends BusinessObject {

  @BusinessObjectField
  private String personId = null;
  @BusinessObjectField
  private String dogName = null;
  @BusinessObjectField
  private String breed = null;
  

  /** Creates a new instance of this object */
  public Dog(String id) {
      super(id);
  }//constructor

  /**
   * @return the personId
   */
  String getPersonId() {
    return personId;
  }

  /**
   * @param personId the personId to set
   */
  void setPersonId(String personId) {
    this.personId = personId;
    setDirty();
  }

  /** Sets the person object */
  public void setPerson(Person person) {
    this.personId = person.getId();
    setDirty();
  }

  /** Returns the person object */
  public Person getPerson() throws DataException {
    return BusinessObjectDAO.getInstance().read(personId);
  }

  /**
   * @return the dogName
   */
  public String getDogName() {
    return dogName;
  }

  /**
   * @param dogName the dogName to set
   */
  public void setDogName(String dogName) {
    this.dogName = dogName;
    setDirty();
  }

  /**
   * @return the breed
   */
  public String getBreed() {
    return breed;
  }

  /**
   * @param breed the breed to set
   */
  public void setBreed(String breed) {
    setDirty();
    this.breed = breed;
  }


}
