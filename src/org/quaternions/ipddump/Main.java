package org.quaternions.ipddump;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
      DATABASEVERSION,
      DATABASERECORDHANDLE,
      RECORDUNIQUEID,
      FIELDLENGTH,
      FIELDTYPE,
      FIELDDATA;
   }

   public static void main( String[] args )
   {
      parse( "C:\\Documents and Settings\\borkhobs\\Desktop\\Sample2.ipd" );
   }

   static boolean parse( String fileName )
   {
      int linefeed;
      int version;
      int numdatabases = 0;
      int dbNameSeparator;
      int dbNameLength = 0;
      int recordRead = 0;
      int fieldLength = 0;
      int fieldType = 0;

      Record record = null;
      List<String> databaseNames = new LinkedList<String>();
      List<Record> records = new LinkedList<Record>();

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
                  linefeed = value;
                  state = ReadingState.VERSION;
                  break;

               case VERSION:
                  version = value;
                  state = ReadingState.DATABASECOUNT;
                  break;

               case DATABASECOUNT:
                  numdatabases = value << 8;
                  numdatabases |= input.read();
                  state = ReadingState.DATABASENAMESEPARATOR;
                  break;

               case DATABASENAMESEPARATOR:
                  dbNameSeparator = value;
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

                  databaseNames.add( buffer.toString() );

                  // Eat terminating null
                  input.read();

                  if ( databaseNames.size() < numdatabases )
                  {
                     state = ReadingState.DATABASENAMELENGTH;
                  }
                  else
                  {
                     state = ReadingState.DATABASEID;
                  }
                  break;

               case DATABASEID:
                  record = new Record();
                  record.databaseID = value << 8;
                  record.databaseID |= input.read();
                  recordRead = 2;
                  state = ReadingState.RECORDLENGTH;
                  break;

               case RECORDLENGTH:
                  record.recordLength = value;
                  record.recordLength |= input.read() << 8;
                  record.recordLength |= input.read() << 16;
                  record.recordLength |= input.read() << 24;
                  recordRead = 0;
                  state = ReadingState.DATABASEVERSION;
                  break;

               case DATABASEVERSION:
                  record.databaseVersion = value;
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
                  record.recordUniqueID = value << 24;
                  record.recordUniqueID |= input.read() << 16;
                  record.recordUniqueID |= input.read() << 8;
                  record.recordUniqueID |= input.read();
                  recordRead += 4;
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
                  StringBuffer dataBuffer = new StringBuffer();
                  dataBuffer.append( (char) value );
                  for ( int i = 1; i < fieldLength; i++ )
                  {
                     dataBuffer.append( (char) input.read() );
                  }

                  record.fields.add( new Pair<Integer, String>( fieldType, dataBuffer.toString() ) );
                  recordRead += fieldLength;

                  if ( recordRead < record.recordLength )
                  {
                     state = ReadingState.FIELDLENGTH;
                  }
                  else
                  {
                     state = ReadingState.DATABASEID;
                     records.add( record );
                  }
                  break;
            }

            value = input.read();
         }
      }
      catch ( IOException exception )
      {
         return false;
      }

      System.out.println( records );

      return true;
   }

   static class Record
   {
      int                        databaseID;
      int                        recordLength;
      int                        databaseVersion;
      int                        databaseRecordHandle;
      int                        recordUniqueID;

      Set<Pair<Integer, String>> fields = new HashSet<Pair<Integer, String>>();

      /**
       * {@inheritDoc}
       */
      @Override
      public String toString()
      {
         return fields.toString();
      }
   }

   static class Pair<T, U>
   {
      T first;
      U second;

      public Pair( T first, U second )
      {
         this.first = first;
         this.second = second;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String toString()
      {
         return "(" + first.toString() + ", " + second.toString() + ")";
      }
   }
}
