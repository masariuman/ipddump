package org.quaternions.ipddump;

//~--- non-JDK imports --------------------------------------------------------

import gui.IpdDump_WithGUI;
import gui.SmsMessageToXML;
import gui.writeBytesToFile;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static StringBuilder          temp=new StringBuilder();    // fast builder!!
    public static InteractivePagerBackup db;                          // need access of it from the hole class

    //~--- methods ------------------------------------------------------------

    /**
     * The main method
     *
     *
     * @param args
     * arg[0] must be the .ipd file
     * -help: get Help
     * -txt: Dumps a csv to a txt
     * -doc: Dumps a csv to a doc
     * -xml: Dumps a csv to a xml
     *
     */
    public static void main(String[] args) {
        if ((args.length>0) && args[0].endsWith(".ipd")) {
            try {
                db=new IPDParser(args[0]).parse();

                if (db!=null) {
                    dump(db, null);
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            for (int i=1; i<args.length; i++) {
                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().equalsIgnoreCase("-txt")) {
                        if (!writeTxt(args[0].trim(), getSMStoString())) {
                            System.err.println("Failed to write the .txt");
                        }

                        continue;
                    }

                    if (args[i].trim().equalsIgnoreCase("-doc")) {
                        System.out.println("\nImplementation pending for -doc");

                        continue;
                    }

                    if (args[i].trim().equalsIgnoreCase("-xml")) {
                        if (!writeXml(args[0].trim(), db)) {
                            System.err.println("Failed to write the .xml");
                        }

                        continue;
                    }

                    if (args[i].trim().equalsIgnoreCase("-help")) {
                        GiveHelp();

                        continue;
                    }
                }

                System.out.println("Unknown argument: "+args[i]);
            }
        } else if ((args.length>0) &&!args[0].endsWith(".ipd")) {
            GiveHelp();
        } else {
            System.out.println("  GUI enviroment will now pop up!");
            new IpdDump_WithGUI().setVisible(true);
        }
    }

    /**
     * Method description
     *
     *
     * @param database: The database to dump
     * @param fileName
     */
    public static void dump(InteractivePagerBackup database, String fileName) {
        System.out.println("uid,sent,received,sent?,far number,text");
        temp.delete(0, temp.capacity());
         temp.append("uid,sent,received,sent?,far number,text\n");
        for (SMSMessage record : database.smsRecords()) {
            temp.append(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
                        +record.getNumber()+",\""+record.getText()+"\"\n");
            System.out.println(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
                               +record.getNumber()+",\""+record.getText()+"\"");
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public static String getSMStoString() {
        return temp.toString();
    }

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public static int getNumberOfSMS() {
        if (db!=null) {
            return db.smsRecords().size();
        } else {
            return -1;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     */
    private static void GiveHelp() {
        System.out.println("\n");
        System.out.println("Usage: java -jar ipdDump.jar <path to ipd>");
        System.out.println("  Dumps a csv to stdout.");
        System.out.println("Usage: java -jar ipdDump.jar <path to ipd> -Args");
        System.out.println("  -txt: Dumps a csv to a txt.");
        System.out.println("  -doc: Dumps a csv to a doc.");
        System.out.println("  -xml: Dumps a csv to a xml.");
        System.out.println("Usage: java -jar ipdDump.jar ");
        System.out.println("  for opening the GUI");
        System.out.println("\n");
    }

    /**
     * This will write a file that contains the given string
     *
     *
     * @param filename The file name without the .ipd missing!!
     * @param stringToWrite
     *
     * @return
     */
    public static boolean writeTxt(String filename, String stringToWrite) {
        try {
            filename=filename+".txt";
            System.out.println("\n->Writing "+filename);

            try {
                writeBytesToFile.writeBytes2File(filename, stringToWrite);

                return true;    // the write was succesfull
            } catch (IOException ex) {
                System.err.println(ex.getMessage());

                return false;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Method description
     *
     *
     * @param filename The file name without the .ipd missing!!
     * @param Db
     *
     * @return
     */
    public static boolean writeXml(String filename, InteractivePagerBackup Db) {
        try {
            filename=filename+".xml";
            System.out.println("\n->Writing "+filename);

            try {
                SmsMessageToXML.saveXML(filename, SmsMessageToXML.createSmsMessageToXML(Db));

                return true;    // the write was succesfull
            } catch (IOException ex) {
                System.err.println(ex.getMessage());

                return false;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());

            return false;
        }
    }
}
