package org.quaternions.ipddump;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import org.quaternions.ipddump.data.Database;
import org.quaternions.ipddump.data.Record;
import org.quaternions.ipddump.data.SMSMessage;

public class Main
{
   public enum ReadingState
   {
      HEADER,
      LINEFEED,
      VERSION,
      DATABASECOUNT,
      DATABASENAMESEPARATOR,
      DATABASENAMELENGTH,
      DATABASENAME,
      DATABASEID,
      RECORDLENGTH,
      RECORDDBEVERSION,
      DATABASERECORDHANDLE,
      RECORDUNIQUEID,
      FIELDLENGTH,
      FIELDTYPE,
      FIELDDATA;
   }

   public static void main( String[] args )
   {
      Database db = parse( args[ 0 ] );

      if ( db != null )
      {
         dump( db, null );
      }
   }

   static Database parse( String fileName )
   {
      char linefeed = 0;
      int numdatabases = 0;
      int dbNameLength = 0;
      int recordRead = 0;
      int fieldLength = 0;
      int fieldType = 0;

      int dbID = 0;
      int recordLength = 0;
      int uid = 0;

      Record record = null;
      Database database = null;

      try
      {
         FileInputStream input = new FileInputStream( fileName );
         FileChannel fc = input.getChannel();
         int[] data = new int[ (int) ( fc.size() ) ];
         for ( int i = 0; i < data.length; i++ )
         {
            data[ i ] = input.read();
         }

         input.close();

         ReadingState state = ReadingState.HEADER;
         for ( int index = 0; index < data.length; )
         {
            switch ( state )
            {
               case HEADER:
                  for ( int i = 0; i < "Inter@ctive Pager Backup/Restore File".length(); i++ )
                  {
                     index++;
                  }
                  state = ReadingState.LINEFEED;
                  break;

               case LINEFEED:
                  linefeed = (char) data[ index++ ];
                  state = ReadingState.VERSION;
                  break;

               case VERSION:
                  database = new Database( data[ index++ ], linefeed );
                  state = ReadingState.DATABASECOUNT;
                  break;

               case DATABASECOUNT:
                  numdatabases = data[ index++ ] << 8;
                  numdatabases |= data[ index++ ];
                  state = ReadingState.DATABASENAMESEPARATOR;
                  break;

               case DATABASENAMESEPARATOR:
                  // Just eat it
                  index++;
                  state = ReadingState.DATABASENAMELENGTH;
                  break;

               case DATABASENAMELENGTH:
                  dbNameLength = data[ index++ ];
                  // Eat the null
                  index++;
                  state = ReadingState.DATABASENAME;
                  break;

               case DATABASENAME:
                  StringBuffer buffer = new StringBuffer();
                  // Eat everything but the terminating null
                  for ( int i = 0; i < dbNameLength - 1; i++ )
                  {
                     buffer.append( (char) data[ index++ ] );
                  }

                  database.addDatabase( buffer.toString() );

                  // Eat null/separator
                  index++;

                  if ( database.databaseNames().size() < numdatabases )
                  {
                     state = ReadingState.DATABASENAMELENGTH;
                  }
                  else
                  {
                     state = ReadingState.DATABASEID;
                  }
                  break;

               case DATABASEID:
                  dbID = data[ index++ ];
                  dbID |= data[ index++ ] << 8;
                  recordRead = 2;
                  state = ReadingState.RECORDLENGTH;
                  break;

               case RECORDLENGTH:
                  recordLength = data[ index++ ];
                  recordLength |= data[ index++ ] << 8;
                  recordLength |= data[ index++ ] << 16;
                  recordLength |= data[ index++ ] << 24;
                  recordRead = 0;
                  state = ReadingState.RECORDDBEVERSION;
                  break;

               case RECORDDBEVERSION:
                  recordRead++;
                  index++;
                  state = ReadingState.DATABASERECORDHANDLE;
                  break;

               case DATABASERECORDHANDLE:
                  // Just toss this
                  index += 2;
                  recordRead += 2;
                  state = ReadingState.RECORDUNIQUEID;
                  break;

               case RECORDUNIQUEID:
                  uid = data[ index++ ] << 24;
                  uid |= data[ index++ ] << 16;
                  uid |= data[ index++ ] << 8;
                  uid |= data[ index++ ];
                  recordRead += 4;
                  record = database.createRecord( dbID, uid, recordLength );
                  state = ReadingState.FIELDLENGTH;
                  break;

               case FIELDLENGTH:
                  fieldLength = data[ index++ ];
                  fieldLength |= data[ index++ ] << 8;
                  recordRead += 2;
                  state = ReadingState.FIELDTYPE;
                  break;

               case FIELDTYPE:
                  fieldType = data[ index++ ];
                  recordRead++;
                  state = ReadingState.FIELDDATA;
                  break;

               case FIELDDATA:
                  char[] dataBuffer = new char[ fieldLength ];
                  if ( fieldLength > 0 )
                  {
                     for ( int i = 0; i < fieldLength; i++ )
                     {
                        dataBuffer[ i ] = (char) data[ index++ ];
                     }
                  }

                  record.addField( fieldType, dataBuffer );
                  recordRead += fieldLength;

                  if ( recordRead < record.getLength() )
                  {
                     state = ReadingState.FIELDLENGTH;
                  }
                  else
                  {
                     state = ReadingState.DATABASEID;
                  }
                  break;
            }
         }

         return database;
      }
      catch ( IOException exception )
      {
         System.err.println( "File not found!" );
         return null;
      }
   }

   public static void dump( Database database, String fileName )
   {
      for ( SMSMessage record : database.smsRecords() )
      {
         System.out.println( "" + record.getSent() + "," + record.getReceived() + "," + record.wasSent() + "," + record.getNumber() + "," +
                             record.getText() );
      }
   }
}
