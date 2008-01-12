package org.quaternions.ipddump;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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
      dump( parse( "data/Sample2.ipd" ), "data/sample.csv" );
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

         ReadingState state = ReadingState.HEADER;
         int value = input.read();
         while ( value >= 0 )
         {
            switch ( state )
            {
               case HEADER:
                  for ( int i = 1; i < "Inter@ctive Pager Backup/Restore File".length(); i++ )
                  {
                     input.read();
                  }
                  state = ReadingState.LINEFEED;
                  break;

               case LINEFEED:
                  linefeed = (char) value;
                  state = ReadingState.VERSION;
                  break;

               case VERSION:
                  database = new Database( value, linefeed );
                  state = ReadingState.DATABASECOUNT;
                  break;

               case DATABASECOUNT:
                  numdatabases = value << 8;
                  numdatabases |= input.read();
                  state = ReadingState.DATABASENAMESEPARATOR;
                  break;

               case DATABASENAMESEPARATOR:
                  // Just eat it
                  state = ReadingState.DATABASENAMELENGTH;
                  break;

               case DATABASENAMELENGTH:
                  dbNameLength = value;
                  // Eat the null
                  input.read();
                  state = ReadingState.DATABASENAME;
                  break;

               case DATABASENAME:
                  StringBuffer buffer = new StringBuffer();
                  buffer.append( (char) value );
                  // Eat everything but the terminating null
                  for ( int i = 1; i < dbNameLength - 1; i++ )
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
                  dbID = value << 8;
                  dbID |= input.read();
                  recordRead = 2;
                  state = ReadingState.RECORDLENGTH;
                  break;

               case RECORDLENGTH:
                  recordLength = value;
                  recordLength |= input.read() << 8;
                  recordLength |= input.read() << 16;
                  recordLength |= input.read() << 24;
                  recordRead = 0;
                  state = ReadingState.RECORDDBEVERSION;
                  break;

               case RECORDDBEVERSION:
                  recordRead++;
                  state = ReadingState.DATABASERECORDHANDLE;
                  break;

               case DATABASERECORDHANDLE:
                  // Just toss this
                  input.read();
                  recordRead += 2;
                  state = ReadingState.RECORDUNIQUEID;
                  break;

               case RECORDUNIQUEID:
                  uid = value << 24;
                  uid |= input.read() << 16;
                  uid |= input.read() << 8;
                  uid |= input.read();
                  recordRead += 4;
                  record = database.createRecord( dbID, uid, recordLength );
                  state = ReadingState.FIELDLENGTH;
                  break;

               case FIELDLENGTH:
                  fieldLength = value;
                  fieldLength |= input.read() << 8;
                  recordRead += 2;
                  state = ReadingState.FIELDTYPE;
                  break;

               case FIELDTYPE:
                  fieldType = value;
                  recordRead++;
                  state = ReadingState.FIELDDATA;
                  break;

               case FIELDDATA:
                  char[] dataBuffer = new char[ fieldLength ];
                  dataBuffer[ 0 ] = (char) value;
                  for ( int i = 1; i < fieldLength; i++ )
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
                     database.add( record );
                  }
                  break;
            }

            value = input.read();
         }

         return database;
      }
      catch ( IOException exception )
      {
         return null;
      }
   }

   public static void dump( Database database, String fileName )
   {
      List<String> order = SMSMessage.getFieldNames();

      for ( Record record : database.records( "SMS Messages" ) )
      {
         for ( String name : order )
         {
            System.out.print( record.fields().get( name ) + "," );
         }

         System.out.println();
      }
   }
}
