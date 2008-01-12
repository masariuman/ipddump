package org.quaternions.ipddump.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author borkholder
 * @date Jan 1, 2008
 */
public class SMSMessage extends Record
{
   /**
    * The map from the name of the field to the field value.
    */
   protected final Map<String, String> fields;

   /**
    * The list of field names this record keeps.
    */
   protected static final List<String> fieldNames;

   static
   {
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
    * @param dbID
    *           The database id
    * @param dbVersion
    *           The database version
    * @param uid
    *           The unique identifier of this record
    * @param recordLength
    *           The length of the record
    */
   SMSMessage( int dbID, int dbVersion, int uid, int recordLength )
   {
      super( dbID, dbVersion, uid, recordLength );
      fields = new HashMap<String, String>();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addField( int type, char[] data )
   {
      StringBuilder builder = null;

      switch ( type )
      {
         case 4:
            fields.put( "text", new StringBuilder().append( data ).toString() );
            break;

         case 2:
            builder = new StringBuilder();
            for ( char c : data )
            {
               if ( Character.isLetterOrDigit( c ) )
               {
                  builder.append( c );
               }
            }

            fields.put( "number", builder.toString() );
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
            for ( int i = 13; i < 21; i++ )
            {
               val0 |= (long) data[ i ] << ( ( i - 13 ) * 8 );
            }

            long val1 = 0;
            for ( int i = 21; i < 29; i++ )
            {
               val1 |= (long) data[ i ] << ( ( i - 21 ) * 8 );
            }

            if ( data[ 0 ] == 0 )
            {
               fields.put( "sent", new Date( val0 ).toString() );
               fields.put( "received", new Date( val1 ).toString() );
               fields.put( "sent?", "true" );
            }
            else
            {
               fields.put( "sent", new Date( val1 ).toString() );
               fields.put( "received", new Date( val0 ).toString() );
               fields.put( "sent?", "false" );
            }

            break;

         default:
            // Should be no default
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, String> fields()
   {
      return Collections.<String, String> unmodifiableMap( fields );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> fieldNames()
   {
      return getFieldNames();
   }

   /**
    * Gets the list of field names that may be assigned by this record.
    *
    * @return An unmodifiable list of field names
    */
   public static List<String> getFieldNames()
   {
      return Collections.<String> unmodifiableList( fieldNames );
   }
}
