package org.quaternions.ipddump;

//~--- non-JDK imports --------------------------------------------------------

import gui.IpdDump_NewGUI;
import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;
import org.quaternions.ipddump.writers.FileWriters;
import org.quaternions.ipddump.writers.SmsWriters;
import org.quaternions.ipddump.writers.writeBytesToFile;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static SmsWriters            smsWriter =new SmsWriters();
    private static FileWriters           FileWriter=new FileWriters();
    public static InteractivePagerBackup db;

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
                dump(db, null);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            for (int i=1; i<args.length; i++) {
                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().equalsIgnoreCase("-txt")) {
                        if (!FileWriter.writeTxttoFile(args[0].trim(), smsWriter.SMStoPlainText(db))) {
                            System.err.println("Failed to write the plain txt");
                        }

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-doc")) {
                        System.out.println("\nImplementation pending for -doc");

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-xml")) {
                        if (!FileWriter.writeXMLtoFile(args[0], smsWriter.SMSToXML(db))) {
                            System.err.println("Failed to write the .xml");
                        }

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-csv")) {
                        if (!FileWriter.writeCsvtoFile(args[0].trim(), getSMStoString())) {
                            System.err.println("Failed to write the .csv");
                        }

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-help")) {
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

            // new IpdDump_WithGUI().setVisible(true);
            new IpdDump_NewGUI().setVisible(true);
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
        System.out.println(getSMStoString());
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public static String getSMStoString() {
        return smsWriter.SMStoCVS(db);
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
        System.out.println("  -txt: Dumps simple readable text to a <ipdName>.txt");
        System.out.println("  -doc: Dumps a csv to a <ipdName>.doc");
        System.out.println("  -xml: Dumps xml to a <ipdName>.xml");
        System.out.println("  -csv: Dumps csv to a <ipdName>.csv");
        System.out.println("Usage: java -jar ipdDump.jar ");
        System.out.println("  for opening the GUI");
        System.out.println("\n");
    }
}
