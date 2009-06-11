package org.quaternions.ipddump;

//~--- non-JDK imports --------------------------------------------------------

import gui.IpdDump_NewGUI;
import gui.IpdDump_WithGUI;
import gui.SmsMessageToXML;
import gui.writeBytesToFile;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static StringBuilder         temp=new StringBuilder();    // fast builder!!
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
                        if (!writeTxt(args[0].trim(), getSMStoPlainText())) {
                            System.err.println("Failed to write the plain txt");
                        }

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-doc")) {
                        System.out.println("\nImplementation pending for -doc");

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-xml")) {
                        if (!writeXml(args[0].trim(), db)) {
                            System.err.println("Failed to write the .xml");
                        }

                        continue;
                    } else if (args[i].trim().equalsIgnoreCase("-csv")) {
                        if (!writeCsv(args[0].trim(), getSMStoString())) {
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
        System.out.println("uid,sent,received,sent?,far number,text");
        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        for (SMSMessage record : database.smsRecords()) {
            temp.append(record.getUID()+","+record.getSent().toString()+","+record.getReceived().toString()+","
                        +record.wasSent()+","+record.getNumber()+",\""+record.getText()+"\"\n");
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
     * Method description
     *
     *
     * @return
     */
    public static String getSMStoPlainText() {
        String tmp="";

        if (db!=null) {
            for (SMSMessage record : db.smsRecords()) {
                String number  =record.getNumber();
                String text    =record.getText();
                String sent    =record.getSent().toString();
                String recieved=record.getReceived().toString();

                if (!record.wasSent()) {
                    tmp=tmp+"From: "+number+"\nTo: My Phone\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"+text
                        +"\n\n";
                } else {
                    tmp=tmp+"From: My Phone\nTo: "+number+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"+text
                        +"\n\n";
                }
            }

            return tmp;
        }

        return tmp;
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

    /**
     * This will write a file that contains the given string
     *
     *
     * @param filename The file name with the .ipd missing!!
     * @param stringToWrite
     *
     * @return
     */
    public static boolean writeTxt(String filename, String stringToWrite) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
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
     * @param filename The file name with the .ipd missing!!
     * @param Db
     *
     * @return
     */
    public static boolean writeXml(String filename, InteractivePagerBackup Db) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
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

        public static boolean writeXml(String filename, InteractivePagerBackup Db, int[] selectedMessages) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
            filename=filename+".xml";
            System.out.println("\n->Writing "+filename);

            try {
                SmsMessageToXML.saveXML(filename, SmsMessageToXML.createSmsMessageToXML(Db,selectedMessages));

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
     * @param filename
     * @param stringToWrite
     *
     * @return
     */
    public static boolean writeCsv(String filename, String stringToWrite) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
            filename=filename+".csv";
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
}
