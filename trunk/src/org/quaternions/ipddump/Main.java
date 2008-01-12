package org.quaternions.ipddump;

import java.io.FileInputStream;
import java.io.IOException;
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
      if ( args.length > 0 )
      {
         Database db = parse( args[ 0 ] );

         if ( db != null )
         {
            dump( db, null );
         }
      }
      else
      {
         System.out.println( "Usage: java -jar ipdump.jar <path to ipd>" );
         System.out.println( "  Dumps a csv to stdout." );
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

         ReadingState state = ReadingState.HEADER;
         while ( fc.position() < fc.size() )
         {
            switch ( state )
            {
               case HEADER:
                  for ( int i = 0; i < "Inter@ctive Pager Backup/Restore File".length(); i++ )
                  {
                     input.read();
                  }
                  state = ReadingState.LINEFEED;
                  break;

               case LINEFEED:
                  linefeed = (char) input.read();
                  state = ReadingState.VERSION;
                  break;

               case VERSION:
                  database = new Database( input.read(), linefeed );
                  state = ReadingState.DATABASECOUNT;
                  break;

               case DATABASECOUNT:
                  numdatabases = input.read() << 8;
                  numdatabases |= input.read();
                  state = ReadingState.DATABASENAMESEPARATOR;
                  break;

               case DATABASENAMESEPARATOR:
                  // Just eat it
                  input.read();
                  state = ReadingState.DATABASENAMELENGTH;
                  break;

               case DATABASENAMELENGTH:
                  dbNameLength = input.read();
                  // Eat the null
                  input.read();
                  state = ReadingState.DATABASENAME;
                  break;

               case DATABASENAME:
                  StringBuffer buffer = new StringBuffer();
                  // Eat everything but the terminating null
                  for ( int i = 0; i < dbNameLength - 1; i++ )
                  {
                     buffer.append( (char) input.read() );
                  }

                  database.addDatabase( buffer.toString() );

                  // Eat null/separator
                  input.read();

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
                  int tmp0 = input.read();
                  dbID = tmp0;
                  int tmp1 = input.read();
                  dbID |= tmp1 << 8;
                  state = ReadingState.RECORDLENGTH;
                  break;

               case RECORDLENGTH:
                  int tmp2 = input.read();
                  recordLength = tmp2;
                  int tmp3 = input.read();
                  recordLength |= tmp3 << 8;
                  int tmp4 = input.read();
                  recordLength |= tmp4 << 16;
                  int tmp5 = input.read();
                  recordLength |= tmp5 << 24;
                  recordRead = 0;
                  state = ReadingState.RECORDDBEVERSION;
                  break;

               case RECORDDBEVERSION:
                  recordRead++;
                  input.read();
                  state = ReadingState.DATABASERECORDHANDLE;
                  break;

               case DATABASERECORDHANDLE:
                  // Just toss this
                  input.read();
                  input.read();
                  recordRead += 2;
                  state = ReadingState.RECORDUNIQUEID;
                  break;

               case RECORDUNIQUEID:
                  uid = input.read() << 24;
                  uid |= input.read() << 16;
                  uid |= input.read() << 8;
                  uid |= input.read();
                  recordRead += 4;
                  record = database.createRecord( dbID, uid, recordLength );
                  state = ReadingState.FIELDLENGTH;
                  break;

               case FIELDLENGTH:
                  fieldLength = input.read();
                  fieldLength |= input.read() << 8;
                  recordRead += 2;
                  state = ReadingState.FIELDTYPE;
                  break;

               case FIELDTYPE:
                  fieldType = input.read();
                  recordRead++;
                  state = ReadingState.FIELDDATA;
                  break;

               case FIELDDATA:
                  char[] dataBuffer = new char[ fieldLength ];
                  for ( int i = 0; i < fieldLength; i++ )
                  {
                     dataBuffer[ i ] = (char) input.read();
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

         input.close();
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
      System.out.println( "sent,received,sent?,far number,text" );
      for ( SMSMessage record : database.smsRecords() )
      {
         System.out.println( "" + record.getSent() + "," + record.getReceived() + "," + record.wasSent() + "," + record.getNumber() + ",\"" +
                             record.getText() + "\"" );
      }
   }
}
