package org.quaternions.ipddump.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author borkholder
 * @date Jun 6, 2009
 */
public class Contact extends Record implements Comparable<Contact> {
  /**
   * The map from the name of the field to the field value.
   */
  protected final Map<String, String> fields;

  protected String                    firstName;

  protected String                    lastName;

  /**
   * Creates a new record with all provided data.
   *
   * @param dbID The database id
   * @param dbVersion The database version
   * @param uid The unique identifier of this record
   * @param recordLength The length of the record
   */
  Contact( int dbID, int dbVersion, int uid, int recordLength ) {
    super( dbID, dbVersion, uid, recordLength );
    fields = new HashMap<String, String>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addField( int type, char[] data ) {
    String fieldName = null;
    switch ( type ) {
      case 1:
        fieldName = "Email";
        break;

      case 3:
        fieldName = "Fax";
        break;

      case 6:
      case 16:
        fieldName = "Work Phone";
        break;

      case 7:
      case 17:
        fieldName = "Home Phone";
        break;

      case 8:
        fieldName = "Mobile Phone";
        break;

      case 9:
        fieldName = "Pager";
        break;

      case 10:
        fieldName = "PIN";
        break;

      case 18:
        fieldName = "Other Number";
        break;

      case 32:
        fieldName = "Name";
        break;

      case 33:
        fieldName = "Company";
        break;

      case 35:
      case 36:
        fieldName = "Work Address";
        break;

      case 38:
        fieldName = "Work City";
        break;

      case 39:
        fieldName = "Work State";
        break;

      case 40:
        fieldName = "Work Postcode";
        break;

      case 41:
        fieldName = "Work Country";
        break;

      case 42:
        fieldName = "Job Title";
        break;

      case 54:
        fieldName = "Webpage";
        break;

      case 55:
        fieldName = "Title";
        break;

      case 61:
      case 62:
        fieldName = "Home Address";
        break;

      case 65:
      case 66:
      case 67:
      case 68:
        fieldName = "User";
        break;

      case 69:
        fieldName = "Home City";
        break;

      case 70:
        fieldName = "Home State";
        break;

      case 71:
        fieldName = "Home Postcode";
        break;

      case 72:
        fieldName = "Home Country";
        break;

      case 64:
        fieldName = "Notes";
        break;

      case 82:
        fieldName = "Birthday";
        break;

      case 83:
        fieldName = "Anniversary";
        break;

      case 84:
        // This is always the same 8 characters
        break;

      case 85:
        // Always 4 characters, date?
        break;

      case 90:
        fieldName = "Google Talk";
        break;

      default:
        System.err.println( type + ": " + makeString( data ) );
    }

    if ( fieldName != null ) {
      addField( fieldName, makeString( data ) );
    }
  }

  protected String makeString( char[] data ) {
    Gsm2Iso.Gsm2Iso( data );
    String str = new String( data );
    return str.substring( 0, str.length() - 1 );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> fields() {
    return Collections.<String, String> unmodifiableMap( fields );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( Contact o ) {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "";
  }

  protected void addField( String key, String value ) {
    if ( key.equalsIgnoreCase( "name" ) ) {
      String name = fields.get( key );
      if ( name == null ) {
        fields.put( key, value );
      } else {
        fields.put( key, name + " " + value );
      }
    } else if ( fields.containsKey( key ) ) {
      int index = 2;
      while ( fields.containsKey( key + index ) ) {
        index++;
      }
      fields.put( key + index, value );
    } else {
      fields.put( key, value );
    }
  }
}
