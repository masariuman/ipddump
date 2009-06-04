package org.quaternions.ipddump;

import gui.IpdDump_WithGUI;
import java.io.IOException;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

public class Main {
  static private  StringBuilder temp= new StringBuilder();//fast builder!!
  public static void main( String[] args ) {
    if ( args.length > 0 && args[0].endsWith(".ipd")) {
      try {
        InteractivePagerBackup db = new IPDParser( args[ 0 ] ).parse();

        if ( db != null ) {
          dump( db, null );
        }
      } catch ( IOException ex ) {
        System.err.println(ex.getMessage());
      }
    } else if (args.length > 0){
      System.out.println( "Usage: java -jar ipdump.jar <path to ipd>" );
      System.out.println( "  Dumps a csv to stdout." );
      
    } else {
    System.out.println( "  GUI enviroment will now pop up!" );
    new IpdDump_WithGUI().setVisible(true);
    }
  }

  public static void dump( InteractivePagerBackup database, String fileName ) {
    System.out.println( "uid,sent,received,sent?,far number,text" );
    for ( SMSMessage record : database.smsRecords() ) {
        temp.append(record.getUID() + "," +record.getSent() + "," + record.getReceived() + "," + record.wasSent() + "," + record.getNumber() + ",\"" +
                          record.getText() + "\"\n");
       System.out.println( record.getUID() + "," + record.getSent() + "," + record.getReceived() + "," + record.wasSent() + "," + record.getNumber() + ",\"" +
                          record.getText() + "\"" );
    }
     }

    public static String makeItString(){
        return temp.toString();
    }

    public static int getNumberOfSMS(){
    return -1;//implementation pending
    }

}


