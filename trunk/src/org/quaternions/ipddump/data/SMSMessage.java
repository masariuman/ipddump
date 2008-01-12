package org.quaternions.ipddump.data;

import java.util.Date;

/**
 * @author borkholder
 * @date Jan 1, 2008
 */
public class SMSMessage extends Record
{
   /**
    * The date the sms was sent.
    */
   protected Date    sent;

   /**
    * The date the sms was received.
    */
   protected Date    received;

   /**
    * The sending/receiving number on the other end of the sms.
    */
   protected String  number;

   /**
    * True if the sms was sent, false if received.
    */
   protected Boolean sentSMS;

   /**
    * The string of the SMS data.
    */
   protected String  text;

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
   public SMSMessage( int dbID, int dbVersion, int uid, int recordLength )
   {
      super( dbID, dbVersion, uid, recordLength );
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
            text = new StringBuilder().append( data ).toString();
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

            number = builder.toString();
            break;

         case 9:
         case 7:
         case 1:
            long val = 0;
            for ( int i = 0; i < data.length; i++ )
            {
               val |= (long) data[ i ] << ( ( i ) * 8 );
            }
            System.out.println( "-" + type + "-" + val );

         default:
            System.out.println( type );
            for ( char c : data )
            {
               System.out.print( (int) c );
            }
            System.out.println();

            break;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "(Number: " + number + ", Text: " + text + ")";
   }
}
