package org.quaternions.ipddump.data;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An SMS message is a record representing an SMS sent or received from the
 * phone.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public class SMSMessage extends Record implements Comparable<SMSMessage> {
  /**
   * The map from the name of the field to the field value.
   */
  protected final Map<String, String> fields;

  /**
   * The list of field names this record keeps.
   */
  protected static final List<String> fieldNames;

  /**
   * The date the SMS was sent;
   */
  protected Date                      sent;

  /**
   * The date the SMS was received.
   */
  protected Date                      received;

  /**
   * Was the SMS sent from the phone, or received?
   */
  protected Boolean                   wasSent;

  /**
   * The far number.
   */
  protected String                    number;

  /**
   * The text of the SMS.
   */
  protected String                    text;

  /**
   * Initialize the list of field names with all the fields contained in this
   * record.
   */
  static {
    fieldNames = new LinkedList<String>();
    fieldNames.add( "sent" );
    fieldNames.add( "received" );
    fieldNames.add( "sent?" );
    fieldNames.add( "number" );
    fieldNames.add( "text" );
  }

  /**
   * Creates a new record with all provided data.
   *
   * @param dbID The database id
   * @param dbVersion The database version
   * @param uid The unique identifier of this record
   * @param recordLength The length of the record
   */
  SMSMessage( int dbID, int dbVersion, int uid, int recordLength ) {
    super( dbID, dbVersion, uid, recordLength );
    fields = new HashMap<String, String>();
  }

  /**
   * Gets the date the SMS was sent.
   *
   * @return The sent date
   */
  public Date getSent() {
    return sent;
  }

  /**
   * Gets the date the SMS was received.
   *
   * @return The received date
   */
  public Date getReceived() {
    return received;
  }

  /**
   * Was the SMS sent from the phone or received.
   *
   * @return True if sent
   */
  public boolean wasSent() {
    return wasSent;
  }

  /**
   * Gets the far phone number.
   *
   * @return The phone number
   */
  public String getNumber() {
    return number;
  }

  /**
   * Gets the text of the SMS.
   *
   * @return The SMS text
   */
  public String getText() {
    return text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addField( int type, char[] data ) {
    StringBuilder builder = null;

    switch ( type ) {
      case 4:
          for ( int i = 0; i < data.length; i++ ) {
          if (data[i]==0x10) {data[i]=0xc4; System.out.println("Delta");}
          else if (data[i]==0x13) {data[i]=0xc3; System.out.println("Gamma");}
          else if (data[i]==0x14) {data[i]=0xcb; System.out.println("Lamda");}
          else if (data[i]==0x16) {data[i]=0xd9; System.out.println("Omega");}
          else if (data[i]==0x16) {data[i]=0xd9; System.out.println("Omega");}
        }
        byte[] d = new byte[ data.length ];
        for ( int i = 0; i < data.length; i++ ) {
          d[ i ] = (byte)data[ i ];
        }
        try {
          text = new String( d, "ISO-8859-7" );
          fields.put( "text", text );
        } catch ( UnsupportedEncodingException exception ) {
          throw new RuntimeException( exception );
        }
        break;

      case 2:
        builder = new StringBuilder();
        for ( char c : data ) {
          if ( Character.isLetterOrDigit( c ) || c == '#' || c == '*' || c == '(' || c == ')' || c == '.' || c == '-' || c == '+'|| c == ' ' ) {
            builder.append( c );
          }
        }

        number = builder.toString();
        fields.put( "number", number );
        break;

      case 9:
        // This is a sequence number and we don't care about it for now.
        // The sequence number seems to apply only if it was sent from the
        // phone in the IPD.
        break;

      case 7:
        // This is some fixed number for my phone, I probably don't care
        // about it
        break;

      case 11:
        // This is also inconsequential, for me it's 0000 for the first sms,
        // 1000 for all the rest
        break;

      case 1:
        long val0 = 0;
        for ( int i = 13; i < 21; i++ ) {
          val0 |= (long) data[ i ] << ( ( i - 13 ) * 8 );
        }

        long val1 = 0;
        for ( int i = 21; i < 29; i++ ) {
          val1 |= (long) data[ i ] << ( ( i - 21 ) * 8 );
        }

        if ( data[ 0 ] == 0 ) {
          sent = new Date( val0 );
          received = new Date( val1 );
          wasSent = true;
        } else {
          sent = new Date( val1 );
          received = new Date( val0 );
          wasSent = false;
        }

        fields.put( "sent", sent.toString() );
        fields.put( "received", received.toString() );
        fields.put( "sent?", wasSent.toString() );

        break;

      default:
        // Should be no default
    }
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
  public List<String> fieldNames() {
    return getFieldNames();
  }

  /**
   * Gets the list of field names that may be assigned by this record.
   *
   * @return An unmodifiable list of field names
   */
  public static List<String> getFieldNames() {
    return Collections.<String> unmodifiableList( fieldNames );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( SMSMessage o ) {
    if ( sent.compareTo( o.sent ) != 0 ) {
      return sent.compareTo( o.sent );
    } else {
      return received.compareTo( o.received );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if ( wasSent() ) {
      return "To: " + getNumber() + " - " + getText();
    } else {
      return "From: " + getNumber() + " - " + getText();
    }
  }
}
