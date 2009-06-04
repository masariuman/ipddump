package org.quaternions.ipddump;

//~--- non-JDK imports --------------------------------------------------------

import gui.IpdDump_WithGUI;
import gui.writeBytesToFile;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static StringBuilder          temp=new StringBuilder();    // fast builder!!
    private static InteractivePagerBackup db;                          // need access of it from the hole class


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

            for (int i=1; i<args.length; i++)

            // //////////
            {
                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().equals("-txt")) {
                        if (!WriteTxt(args[0].trim(), SMStoString())) {
                            System.err.println("Failed to write the .txt");
                        }

                        continue;
                    }
                }

                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().equals("-doc")) {
                        System.out.println("\nImplementation pending for -doc");

                        continue;
                    }
                }

                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().equals("-xml")) {
                        System.out.println("\nImplementation pending for -xml");

                        continue;
                    }
                }

                if (args[i].trim().startsWith("-")) {
                    if (args[i].trim().startsWith("-help")) {
                        GiveHelp();

                        continue;
                    }
                }

                System.out.println("Unknown argument: "+args[i]);
            }

            // //////////
        } else if ((args.length>0) &&!args[0].endsWith(".ipd")) {
            GiveHelp();
        } else {
            System.out.println("  GUI enviroment will now pop up!");
            new IpdDump_WithGUI().setVisible(true);
        }
    }


    public static void dump(InteractivePagerBackup database, String fileName) {
        System.out.println("uid,sent,received,sent?,far number,text");

        for (SMSMessage record : database.smsRecords()) {
            temp.append(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
                        +record.getNumber()+",\""+record.getText()+"\"\n");
            System.out.println(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
                               +record.getNumber()+",\""+record.getText()+"\"");
        }
    }


    public static String SMStoString() {
        return temp.toString();
    }


    public static int getNumberOfSMS() {
        if (db!=null) {
            return db.smsRecords().size();
        } else {
            return -1;
        }
    }


    private static void GiveHelp() {
        System.out.println("\n");
        System.out.println("Usage: java -jar ipdump.jar <path to ipd>");
        System.out.println("  Dumps a csv to stdout.");
        System.out.println("Usage: java -jar ipdump.jar <path to ipd> -Args");
        System.out.println("  -txt: Dumps a csv to a txt.");
        System.out.println("  -doc: Dumps a csv to a doc.");
        System.out.println("  -xml: Dumps a csv to a xml.");
        System.out.println("Usage: java -jar ipdump.jar ");
        System.out.println("  for opening the GUI");
        System.out.println("\n");
    }


    public static boolean WriteTxt(String filename, String stringToWrite) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
            filename=filename+".txt";
            System.out.println("\n->Writing "+filename);

            try {
                writeBytesToFile.writeBytes2File(filename, stringToWrite);

                return true;    // the write was succesfull
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());

                return false;
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
