package org.quaternions.ipddump.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author borkholder
 * @date Jan 1, 2008
 */
public class Database
{
   /**
    * The character used as the line feed.
    */
   protected char                           lineFeed;

   /**
    * The version of the database.
    */
   protected final int                      version;

   /**
    * The list of databases, or rather the names of the databases.
    */
   protected final List<String>             databases;

   /**
    * The records - this simulates a database by providing a mapping from each
    * database name to a set of records.
    */
   protected final Map<String, Set<Record>> records;

   /**
    * Creates a new database.
    *
    * @param version
    *           The database version
    * @param lineFeed
    *           The line feed character
    */
   public Database( int version, char lineFeed )
   {
      this.version = version;
      this.lineFeed = lineFeed;
      databases = new LinkedList<String>();
      records = new HashMap<String, Set<Record>>();
   }

   /**
    * Adds a new database to the list of contained databases.
    *
    * @param name
    *           The name of the database to add
    */
   public void addDatabase( String name )
   {
      databases.add( name );
   }

   /**
    * Gets the list of database names that have been added so far.
    *
    * @return An unmodifiable list of database names
    */
   public List<String> databaseNames()
   {
      return Collections.<String> unmodifiableList( databases );
   }

   /**
    * Adds a new record to the database.
    *
    * @param record
    *           The record to add
    */
   public void add( Record record )
   {
      Set<Record> set = records.get( databases.get( record.getDatabaseID() ) );

      if ( set == null )
      {
         set = new HashSet<Record>();
         records.put( databases.get( record.getDatabaseID() ), set );
      }

      set.add( record );
   }

   /**
    * Creates a new {@link Record} to represent the type of data for the
    * database given by the dbIndex value.
    *
    * @param dbIndex
    *           The index of the database that this record will be in
    * @param uid
    *           The unique identifier of the Record
    * @param length
    *           The length of the Record in the data
    * @return A new Record
    */
   public Record createRecord( int dbIndex, int uid, int length )
   {
      if ( "SMS Messages".equals( databases.get( dbIndex ) ) )
      {
         return new SMSMessage( dbIndex, version, uid, length );
      }
      else
      {
         return null;
      }
   }

   /**
    * Gets the set of records for the given database.
    *
    * @param name
    *           The name of the database
    * @return An unmodifiable set of records in the database
    */
   public Set<Record> records( String name )
   {
      return Collections.<Record> unmodifiableSet( records.get( name ) );
   }
}
