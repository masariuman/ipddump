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
               sent = new Date( val0 );
               received = new Date( val1 );
               sentSMS = true;
            }
            else
            {
               sent = new Date( val1 );
               received = new Date( val0 );
               sentSMS = false;
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
   public String toString()
   {
      return "(Number: " + number + /* ", Text: " + text + */", Sent: " + sent + ", Received: " + received + ")";
   }
}
