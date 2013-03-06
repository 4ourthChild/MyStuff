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

import java.sql.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * A singleton object that loads/saves business objects to/from
 * a relational database.
 *
 * To use this class, adhere to the following:
 *
 * 1. The database table name is the lowercase version of the
 *    Business Object name.  For example, the Employee.java BO
 *    should have a DB tabled named "employee".
 *
 * 2. The business object extends the BusinessObject.java class.
 *
 * 3. The database table column names are the lowercase versions
 *    of the Business Object names.  For example, the "firstName"
 *    BO field matches the "firstname" DB column name.
 *
 * 4. The BO fields that match DB columns are annotated with
 *    @BusinessObjectField keywords just before them.  This DAO
 *    looks for these annotations to know which fields it should
 *    set and read values for.
 *
 * 5. Getters and setters exist for all annotated fields in the
 *    Business Object.
 *
 * 6. A businessobject table exists in the database with two
 *    columns: id (CHAR 40 PRIMARY KEY) and botype (VARCHAR 250).
 *    All other tables in the database should "extend" from this
 *    table, meaning that their id's are foreign keys to this table.
 *
 * 7. The currently-supported field types are String, int/Integer,
 *    long/Long, float/Float, double/Double, java.util.Date, and
 *    boolean/Boolean.  Note that Java DB (Derby) doesn't support
 *    boolean fields.
 *
 * See the Tester.java class for examples on the use of this class.
 *
 * @author Conan C. Albrecht <conan@warp.byu.edu>
 * @version 1.3
 */
public class BusinessObjectDAO  {

  /////////////////////////////////////////////
  ///   Singleton code

  /** Our map of BO names to DAOs for that BO */
  private static BusinessObjectDAO instance = null;

  /** Creates a new instance of BusinessObjectDAO */
  private BusinessObjectDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized BusinessObjectDAO getInstance() throws DataException {
    if (instance == null) {
      instance = new BusinessObjectDAO();
    }
    return instance;
  }//getInstance


  /////////////////////////////////////////////
  ///   Helper methods


  /** Interrogates an object and retrieves all fields and types that are annotated with "BusinessObjectField" */
  private Map<String, Class> getBusinessObjectFields(Class boClass) throws Exception {
    Map<String, Class> fields = new TreeMap<String,Class>();
    for (Field field: boClass.getDeclaredFields()) {
      if (field.getAnnotation(BusinessObjectField.class) != null) {
        fields.put(field.getName(), field.getType());
      }//if
    }//for
    return fields;
  }

  /** Returns the setter for the given field name */
  private Method getSetterMethod(Class boClass, String fieldName, Class fieldType) throws DataException {
    String setterName = "set" + fieldName.substring(0, 1).toUpperCase();
    if (fieldName.length() > 1) {
      setterName += fieldName.substring(1);
    }//if
    try {
      return boClass.getDeclaredMethod(setterName, fieldType);
    }catch (NoSuchMethodException nsme) {
      throw new DataException("Error in " + boClass.getName() + ".  No method named " + setterName + "(" + fieldType.getName() + ") to correspond with field " + fieldName + " in the database.");
    }//try
  }

  /** Returns the getter for the given field name */
  private Method getGetterMethod(Class boClass, String fieldName, Class fieldType) throws DataException {
    String getterName = "get" + fieldName.substring(0, 1).toUpperCase();
    if (fieldType == Boolean.class || fieldType == boolean.class) {
      getterName = "is" + fieldName.substring(0, 1).toUpperCase();
    }
    if (fieldName.length() > 1) {
      getterName += fieldName.substring(1);
    }//if
    try {
      Method method = boClass.getDeclaredMethod(getterName);
      if (method.getReturnType() == fieldType) {
        return method;
      }
    }catch (NoSuchMethodException nsme) {
      // pass so we throw the exception at the end
    }//try
    throw new DataException("Error in " + boClass.getName() + ".  No method named " + getterName + "(" + fieldType.getName() + ") to correspond with field " + fieldName + " in the database.");
  }


  ////////////////////////////////////////////
  ///   CREATE methods

  /** 
   * Creates a new BO in the database.  The boType should be the
   * simple class name of the BO to create.  For example,
   * "Employee" to build an employee BO. The id of the new business
   * object is automatically generated using the GUID class.
   */
  public <T extends BusinessObject> T create(String boType) throws DataException {
    String id = null;
    try {
      id = GUID.generate();
    }catch (Exception e) {
      e.printStackTrace();
      throw new DataException("Could not create a new GUID.", e);
    }
    return (T)create(boType, id);
  }

  /**
   * Creates a new BO in the database.  The boType should be the
   * simple class name of the BO to create.  For example,
   * "Employee" to build an employee BO.
   *
   * This method allows the user to specify the id (GUID) of the new
   * business object.  This is useful when testing.
   */
  public <T extends BusinessObject> T create(String boType, String id) throws DataException {
    try {
      // use reflection to create the object dynamically
      Class klass = Class.forName(this.getClass().getPackage().getName() + "." + boType);
      Class[] params = new Class[] { String.class };
      Constructor c = klass.getConstructor(params);
      T bo = (T)c.newInstance(new Object[] { id });
      bo.setObjectAlreadyInDB(false);
      Cache.getInstance().put(bo.getId(), bo);
      bo.setDirty();
      return bo;
    }catch (Exception e) {
      e.printStackTrace();
      throw new DataException("Could not create a new Business Object.", e);
    }
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** 
   * Reads an existing bo from the database.  The method first checks
   * the cache, then goes to the database if the object is not already
   * in memory.  The method discovers the business object type by
   * querying the database, and the correct type is returned.  Callers
   * do not have to downcast the returned object.
   */
  public <T extends BusinessObject> T read(String id) throws DataException {
    if (Cache.getInstance().containsKey(id)) {
      return (T)Cache.getInstance().get(id);
    }
    Connection conn = ConnectionPool.getInstance().get();
    try {
      return (T)read(id, conn);
    }catch (Exception e) {
      e.printStackTrace();
      throw new DataException("An error occurred while reading the business object information.", e);
    }finally {
      ConnectionPool.getInstance().release(conn);
    }
  }
  
  /** Internal read method (used by both read() above and search...() below) */
  synchronized <T extends BusinessObject> T read(String id, Connection conn) throws Exception {
    // check cache again now that we're synchronized
    if (Cache.getInstance().containsKey(id)) {
      return (T)Cache.getInstance().get(id);
    }
    // get it's type from the businessobject table so we know what table to query
    PreparedStatement typeStmt = conn.prepareStatement("SELECT botype FROM businessobject WHERE id=?");
    String boType = null;
    try {
      typeStmt.setString(1, id);
      ResultSet rs = typeStmt.executeQuery();
      if (rs.next()) {
        boType = rs.getString("botype");
      }else{
        throw new DataException("BusinessObject with id '" + id + "' was not in the businessobject table.");
      }//if
    }finally{
      typeStmt.close();
    }//try/finally

    // use reflection to create the object dynamically
    Class boClass = Class.forName(boType);
    Class[] params = new Class[] { String.class };
    Constructor c = boClass.getConstructor(params);
    T bo = (T)c.newInstance(new Object[] { id });
    bo.setObjectAlreadyInDB(true);

    // initialize the data of the object by stepping through its superclasses all the way to BusinessObject
    while (boClass != BusinessObject.class) {

      initialize(bo, boClass, conn);
      boClass = boClass.getSuperclass();
    }

    // set not dirty, put in the cache, and return the object
    bo.setDirty(false);
    Cache.getInstance().put(bo.getId(), bo);
    return bo;
  }

  /** Initializes an object once it has been created */
  <T extends BusinessObject> void initialize(T bo, Class boClass, Connection conn) throws Exception {
    // get the information from the database
    String tableName = boClass.getSimpleName().toLowerCase();
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE id=?");
    try{
      stmt.setString(1, bo.getId());
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        // use reflection to populate all the fields of the BO
        for (Map.Entry<String, Class> entry: getBusinessObjectFields(boClass).entrySet()) {
          String fieldName = entry.getKey();
          Class fieldType = entry.getValue();
          Method method = getSetterMethod(boClass, fieldName, fieldType);
          if (fieldType == String.class) {
            method.invoke(bo, rs.getString(fieldName));
          }else if (fieldType == Integer.class || fieldType == int.class) {
            method.invoke(bo, rs.getInt(fieldName));
          }else if (fieldType == Long.class || fieldType == long.class) {
            method.invoke(bo, rs.getLong(fieldName));
          }else if (fieldType == Float.class || fieldType == float.class) {
            method.invoke(bo, rs.getFloat(fieldName));
          }else if (fieldType == Double.class || fieldType == double.class) {
            method.invoke(bo, rs.getDouble(fieldName));
          }else if (fieldType == java.util.Date.class) {
            Timestamp ts = rs.getTimestamp(fieldName);
            if (ts != null) {
              method.invoke(bo, new java.util.Date(ts.getTime()));
            }
          }else if (fieldType == Boolean.class || fieldType == boolean.class) {
            method.invoke(bo, rs.getBoolean(fieldName));
          }else{
            throw new DataException("Cannot read " + boClass.getName() + " from the database.  The BusinessObjectDAO does not support this data type: " + fieldType.getName());
          }
        }//for

      }else{
        throw new DataException("BusinessObject with id '" + bo.getId() + "' not found in the " + boClass.getSimpleName() + " table.");
      }//if
    }finally{
      stmt.close();
    }
  }//read



  /////////////////////////////////////////////
  ///   UPDATE methods

  /** 
   * Saves an existing business object in the database. The
   * method automatically determines whether an INSERT or UPDATE
   * call should be invoked.  The object must be set "dirty" for the
   * save to run.
   */
  public <T extends BusinessObject> void save(T bo) throws DataException {
    // reset the cache timer
    Cache.getInstance().put(bo.getId(), bo);

    // if the object isn't dirty, don't bother saving
    if (!bo.isDirty()) {
      return;
    }

    // save in the database
    Connection conn = ConnectionPool.getInstance().get();
    try {
      // get the superclasses of this object, putting them in reverse order (since they need to save that way for referential integrity).
      Class boClass = bo.getClass();
      List<Class> inheritance = new LinkedList<Class>();
      while (boClass != BusinessObject.class) {
        inheritance.add(0, boClass);
        boClass = boClass.getSuperclass();
      }

      // save the information in the businessobject table
      String sql = "INSERT INTO businessobject (botype, id) VALUES (?, ?)";
      if (bo.isObjectAlreadyInDB()) {
        sql = "UPDATE businessobject SET botype=? WHERE id=?";
      }
      PreparedStatement pstmt = conn.prepareStatement(sql);
      try {
        pstmt.setString(1, bo.getClass().getName());
        pstmt.setString(2, bo.getId());
        pstmt.execute();
      }finally{
        pstmt.close();
      }

      // step through the class hierarchy under BusinessObject and let each save to the database
      for (Class klass: inheritance) {
        if (bo.isObjectAlreadyInDB()) {
          update(bo, klass, conn);
        }else{
          insert(bo, klass, conn);
        }//if
      }

      // commit the changes
      conn.commit();

      // update the variables
      bo.setDirty(false);
      bo.setObjectAlreadyInDB(true);
    }catch (Exception e) {
      e.printStackTrace();
      try{
        conn.rollback();
      }catch (SQLException e2) {
        throw new DataException("Could not roll back the database transaction!", e2);
      }
      throw new DataException("An error occurred while saving the business object information.", e);
    }finally {
      ConnectionPool.getInstance().release(conn);
    }
  }//update


  /** Saves an existing bo to the database */
  private <T extends BusinessObject> void update(T bo, Class boClass, Connection conn) throws Exception {
    // build the SQL string parts
    StringBuilder setStr = new StringBuilder();
    Map<String, Class> fields = getBusinessObjectFields(boClass);
    String[] fieldNames = fields.keySet().toArray(new String[fields.size()]); // so the order can't change between the two for loops
    for (int i = 0; i < fieldNames.length; i++) {
      String fieldName = fieldNames[i];
      if (setStr.length() > 0) {
        setStr.append(", ");
      }
      setStr.append(fieldName.toLowerCase() + "=?");
    }//for
    String sql = "UPDATE " + boClass.getSimpleName().toLowerCase() + " SET " + setStr.toString() + " WHERE id=?";

    // run the update on this class' table
    PreparedStatement stmt = conn.prepareStatement(sql);
    try {
      for (int i = 0; i < fieldNames.length; i++) {
        String fieldName = fieldNames[i];
        Class fieldType = fields.get(fieldName);
        Method method = getGetterMethod(boClass, fieldName, fieldType);
        if (fieldType == String.class) {
          stmt.setString(i+1, (String)method.invoke(bo));
        }else if (fieldType == Integer.class || fieldType == int.class) {
          stmt.setInt(i+1, (Integer)method.invoke(bo));
        }else if (fieldType == Long.class || fieldType == long.class) {
          stmt.setLong(i+1, (Long)method.invoke(bo));
        }else if (fieldType == Float.class || fieldType == float.class) {
          stmt.setFloat(i+1, (Float)method.invoke(bo));
        }else if (fieldType == Double.class || fieldType == double.class) {
          stmt.setDouble(i+1, (Double)method.invoke(bo));
        }else if (fieldType == java.util.Date.class) {
          java.util.Date d = (java.util.Date)method.invoke(bo);
          if (d != null) {
            stmt.setTimestamp(i+1, new java.sql.Timestamp(d.getTime()));
          }else{
            stmt.setTimestamp(i+1, null);
          }
        }else if (fieldType == Boolean.class || fieldType == boolean.class) {
          stmt.setBoolean(i+1, (Boolean)method.invoke(bo));
        }else{
          throw new DataException("Cannot save " + bo.getClass().getName() + " in the " + boClass.getSimpleName().toLowerCase() + " table.  The BusinessObjectDAO does not support this data type: " + fieldType.getName());
        }
      }
      stmt.setString(fieldNames.length+1, bo.getId());
      stmt.execute();
    }finally{
      stmt.close();
    }
  }


  /** Inserts a new bo into the database */
  private <T extends BusinessObject> void insert(T bo, Class boClass, Connection conn) throws Exception {
    // build the SQL string parts
    StringBuilder fieldStr = new StringBuilder("id");
    StringBuilder valueStr = new StringBuilder("?");
    Map<String, Class> fields = getBusinessObjectFields(boClass);
    String[] fieldNames = fields.keySet().toArray(new String[fields.size()]); // so the order can't change between the two for loops
    for (int i = 0; i < fieldNames.length; i++) {
      String fieldName = fieldNames[i];
      fieldStr.append(", " + fieldName.toLowerCase());
      valueStr.append(", ?");
    }//for
    String sql = "INSERT INTO " + boClass.getSimpleName().toLowerCase() + " (" + fieldStr.toString() + ") VALUES (" + valueStr.toString() + ")";
    
    // run the update on this class' table
    PreparedStatement stmt = conn.prepareStatement(sql);
    try {
      stmt.setString(1, bo.getId());
      for (int i = 0; i < fieldNames.length; i++) {
        String fieldName = fieldNames[i];
        Class fieldType = fields.get(fieldName);
        Method method = getGetterMethod(boClass, fieldName, fieldType);
        if (fieldType == String.class) {
          stmt.setString(i+2, (String)method.invoke(bo));
        }else if (fieldType == Integer.class || fieldType == int.class) {
          stmt.setInt(i+2, (Integer)method.invoke(bo));
        }else if (fieldType == Long.class || fieldType == long.class) {
          stmt.setLong(i+2, (Long)method.invoke(bo));
        }else if (fieldType == Float.class || fieldType == float.class) {
          stmt.setFloat(i+2, (Float)method.invoke(bo));
        }else if (fieldType == Double.class || fieldType == double.class) {
          stmt.setDouble(i+2, (Double)method.invoke(bo));
        }else if (fieldType == java.util.Date.class) {
          java.util.Date d = (java.util.Date)method.invoke(bo);
          if (d != null) {
            stmt.setTimestamp(i+2, new java.sql.Timestamp(d.getTime()));
          }else{
            stmt.setTimestamp(i+2, null);
          }
        }else if (fieldType == Boolean.class || fieldType == boolean.class) {
          stmt.setBoolean(i+2, (Boolean)method.invoke(bo));
        }else{
          throw new DataException("Cannot save " + bo.getClass().getName() + " in the " + boClass.getSimpleName().toLowerCase() + " table.  The BusinessObjectDAO does not support this data type: " + fieldType.getName());
        }
      }
      stmt.execute();
    }finally{
      stmt.close();
    }

  }


  /////////////////////////////////////////////////
  ///   DELETE methods

  /** 
   * Deletes a business object from the database.  Normally, BOs should not
   * be deleted (only "inactivated").  This DAO supports delete because there
   * are times when deleting should occur, such as when an association class
   * needs a record removed. 
   */
  public <T extends BusinessObject> void delete(T bo) throws DataException {
    Connection conn = ConnectionPool.getInstance().get();
    try {
      // go through each level of the class hierarchy and delete on each table
      Class boClass = bo.getClass();
      while (true) {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + boClass.getSimpleName().toLowerCase() + " WHERE id=?");
        try {
          stmt.setString(1, bo.getId());
          stmt.executeUpdate();
        }finally{
          stmt.close();
        }
        if (boClass == BusinessObject.class) {
          break;
        }
        boClass = boClass.getSuperclass();
      }
      conn.commit();

      // remove it from the cache
      Cache.getInstance().remove(bo.getId());
    }catch (Exception e) {
      e.printStackTrace();
      try{
        conn.rollback();
      }catch (SQLException e2) {
        throw new DataException("Could not roll back the database transaction!", e2);
      }
      throw new DataException("An error occurred while saving the business object information.", e);
    }finally {
      ConnectionPool.getInstance().release(conn);
    }
  }//update



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all the business objects of a given type */
  public <T extends BusinessObject> List<T> searchForAll(String boType) throws DataException {
    return (List<T>)searchForList(boType);
  }

  /**
   *  Retrieves the BOs from the database that meet the given criteria.
   *  Use this method when you expect multiple BOs to match the criteria.
   *  If no BOs match the criteria, an zero-size list is returned.
   *
   *  Use one or more SearchCriteria objects to specify the search.
   *
   *  Search for employees with name=Bart:
   *  searchForList("Employee", new SearchCriteria("name", "Bart"));
   *
   *  Search for employees with username=Bart and salary > 500:
   *  searchForList("Employee", new SearchCriteria("username", "bart"), new SearchCriteria("salary", 500, SearchCriteria.GREATER_THAN);
   *
   *  Search for employees with names starting with "B".
   *  searchForList("Employee", new SearchCriteria("name", "B%", SearchCriteria.LIKE));
   *
   */
  public <T extends BusinessObject> List<T> searchForList(String boType, SearchCriteria... criteria) throws DataException {
    Connection conn = ConnectionPool.getInstance().get();
    try {
      // generate the where clause
      StringBuilder whereClause = new StringBuilder();
      if (criteria.length > 0) {
        whereClause.append(" WHERE ");
        for (SearchCriteria sc: criteria) {
          if (whereClause.length() > 7) {
            whereClause.append(" AND ");
          }
          whereClause.append(sc.getSQLWhere());
        }//for
      }//if


      // set up the prepared statement and run the query
      String tableName = boType.toLowerCase();
      String sql = "SELECT id FROM " + tableName + whereClause.toString();
      PreparedStatement stmt = conn.prepareStatement(sql);
      int i = 1;
      for (SearchCriteria sc: criteria) {
        Class valueType = sc.getColumnValue().getClass();
        if (valueType == String.class) {
          stmt.setString(i, (String)sc.getColumnValue());
        }else if (valueType == Integer.class || valueType == int.class) {
          stmt.setInt(i, (Integer)sc.getColumnValue());
        }else if (valueType == Long.class || valueType == long.class) {
          stmt.setLong(i, (Long)sc.getColumnValue());
        }else if (valueType == Float.class || valueType == float.class) {
          stmt.setFloat(i, (Float)sc.getColumnValue());
        }else if (valueType == Double.class || valueType == double.class) {
          stmt.setDouble(i, (Double)sc.getColumnValue());
        }else if (valueType == java.util.Date.class) {
          if (sc.getColumnValue() != null) {
            stmt.setTimestamp(i, new java.sql.Timestamp(((java.util.Date)sc.getColumnValue()).getTime()));
          }
        }else if (valueType == Boolean.class || valueType == boolean.class) {
          stmt.setBoolean(i, (Boolean)sc.getColumnValue());
        }else{
          throw new DataException("Invalid value in search criteria.  Type " + valueType.getName() + " is not supported by BusinessObjectDAO.");
        }
        i++;
      }
      ResultSet rs = stmt.executeQuery();

      // create the list of objects
      List<T> bos = new LinkedList<T>();
      while (rs.next()) {
        T bo = read(rs.getString("id"), conn);
        bos.add(bo);
      }
      return bos;
    }catch (Exception e) {
      e.printStackTrace();
      try{
        conn.rollback();
      }catch (SQLException e2) {
        throw new DataException("Could not roll back the database transaction!", e2);
      }
      throw new DataException("An error occurred while reading the business object information.", e);
    }finally {
      ConnectionPool.getInstance().release(conn);
    }
  }


  /**
   *  Retrieves the first BO from the database that meets the given criteria.
   *  Use this method when you expect only one BO to match the criteria.
   *  If no BOs match the criteria, null is returned.
   *
   *  Use one or more SearchCriteria objects to specify the search.
   *
   *  Search for employees with name=Bart:
   *  searchForList("Employee", new SearchCriteria("name", "Bart"));
   *
   *  Search for employees with username=Bart and salary > 500:
   *  searchForList("Employee", new SearchCriteria("username", "bart"), new SearchCriteria("salary", 500, SearchCriteria.GREATER_THAN);
   *
   *  Search for employees with names starting with "B".
   *  searchForList("Employee", new SearchCriteria("name", "B%", SearchCriteria.LIKE));
   *
   */

  public <T extends BusinessObject> T searchForBO(String boType, SearchCriteria... criteria) throws DataException {
      // this is not the most efficient way to do this, but it is simple and works for our purposes here.
      List<T> li = searchForList(boType, criteria);
      if (li.size() > 0) {
          return (T)li.get(0);
      }else{
          return null;
      }//if
  }


}//class
