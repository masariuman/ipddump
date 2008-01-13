package org.quaternions.ipddump;

import java.io.IOException;

import org.quaternions.ipddump.data.Database;
import org.quaternions.ipddump.data.SMSMessage;

public class Main
{

   public static void main( String[] args )
   {
      if ( args.length > 0 )
      {
         try
         {
            Database db = new IPDParser( args[ 0 ] ).parse();

            if ( db != null )
            {
               dump( db, null );
            }
         }
         catch ( IOException ex )
         {
            System.err.println( ex.getMessage() );
         }
      }
      else
      {
         System.out.println( "Usage: java -jar ipdump.jar <path to ipd>" );
         System.out.println( "  Dumps a csv to stdout." );
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
