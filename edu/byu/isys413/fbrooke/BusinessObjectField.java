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

import java.lang.annotation.*;

/**
 * An annotation for a database column in a table.
 *
 * @author conan@warp.byu.edu
 * @version 1.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BusinessObjectField {

  // I could put option s for table name and/or column name in the database,
  // but I'll just assume that the column name in the db is exactly the same
  // as the variable name in the BO.

}
