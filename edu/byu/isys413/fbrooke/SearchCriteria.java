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
 * Represents a column name mapped to a value we'll search for.
 *
 * @author conan@warp.byu.edu
 * @version 1.1
 */
public class SearchCriteria {

  public static final int EQUALS = 0;
  public static final int NOT_EQUALS = 1;
  public static final int GREATER_THAN = 2;
  public static final int GREATER_THAN_OR_EQUALS = 3;
  public static final int LESS_THAN = 4;
  public static final int LESS_THAN_OR_EQUALS = 5;
  public static final int LIKE = 6;

  private String columnName = null;
  private Object columnValue = null;
  private int relationship = EQUALS;

  /** Constructor to search for a given value being in a column (an equals relationship) */
  public SearchCriteria(String columnName, Object columnValue) {
    setColumnName(columnName);
    setColumnValue(columnValue);
  }

  /** 
   * Constructor to search for a given value in a column with a given relationship.
   * If a LIKE relationship, include percent signs in the columnValue parameter.
   */
  public SearchCriteria(String columnName, Object columnValue, int relationship) {
    setColumnName(columnName);
    setColumnValue(columnValue);
    setRelationship(relationship);
  }

  /** Returns the SQL where clause for this criteria */
  String getSQLWhere() {
    switch (relationship) {
      case NOT_EQUALS:              return columnName + " <> ?";
      case GREATER_THAN:            return columnName + " > ?";
      case GREATER_THAN_OR_EQUALS:  return columnName + " >= ?";
      case LESS_THAN:               return columnName + " < ?";
      case LESS_THAN_OR_EQUALS:     return columnName + " <= ?";
      case LIKE:                    return columnName + " LIKE ?";
      default:                      return columnName + " = ?";
    }
  }

  /**
   * Returns the column name to search in.
   */
  public String getColumnName() {
    return columnName;
  }

  /**
   * Sets the column name to search in.
   */
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  /**
   * Returns the column value to be searched for.
   */
  public Object getColumnValue() {
    return columnValue;
  }

  /**
   * Sets the column value to be searched for.
   */
  public void setColumnValue(Object columnValue) {
    this.columnValue = columnValue;
  }

  /**
   * Returns the relationship.
   */
  public int getRelationship() {
    return relationship;
  }

  /**
   * Sets the relationship.
   */
  public void setRelationship(int relationship) {
    this.relationship = relationship;
  }

  

}
