package org.quaternions.ipddump.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.quaternions.ipddump.data.ContactFinder;
import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 */
public class SmsWriters extends BasicWriter {
    private boolean       resolveNames=false;
    private ContactFinder contactFinder;

    //~--- constructors -------------------------------------------------------

    public SmsWriters(InteractivePagerBackup database, boolean resolveNames) {
        super(database);
        this.resolveNames=resolveNames;
        contactFinder    =new ContactFinder(super.database);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public int getSize() {
        if (database!=null) {
            return database.smsRecords().size();
        } else {
            return 0;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public String toCSV(int[] selectedMessages) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        int RecordIndex=0;
        int j          =0;

        for (SMSMessage record : database.smsRecords()) {
            if ((RecordIndex==selectedMessages[j]) && (selectedMessages[j]<database.smsRecords().size())) {
                String Name="";

                if (resolveNames) {
                    Name=contactFinder.findContactByPhoneNumber(record.getNumber());
                } else {
                    Name=record.getNumber();
                }

                temp.append(record.getUID()+","+record.getSent().toString()+","+record.getReceived().toString()+","
                            +record.wasSent()+","+Name+",\""+record.getText()+"\"\n");
                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return temp.toString();
    }

    /**
     * Get a represantation of the parsed SMS's
     * in plain text
     *
     *
     * @param database
     * @param SMSselectedRows
     *
     * @return
     */
    public String toPlainText(int[] SMSselectedRows) {
        StringBuilder tmp=new StringBuilder();

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (SMSMessage record : database.smsRecords()) {
                if ((RecordIndex==SMSselectedRows[j]) && (SMSselectedRows[j]<database.smsRecords().size())) {
                    String number  =record.getNumber();
                    String text    =record.getText();
                    String sent    =record.getSent().toString();
                    String recieved=record.getReceived().toString();
                    String Name    ="";

                    if (resolveNames) {
                        Name=contactFinder.findContactByPhoneNumber(number);
                    } else {
                        Name=number;
                    }

                    if (!record.wasSent()) {
                        tmp.append("From: "+Name+"\nTo: My Phone"+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"
                                   +text+"\n\n");
                    } else {
                        tmp.append("From: My Phone"+"\nTo: "+Name+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"
                                   +text+"\n\n");
                    }

                    j++;

                    if (j>=SMSselectedRows.length) {
                        break;
                    }
                }

                RecordIndex++;
            }

            return tmp.toString();
        }

        return tmp.toString();
    }

    /**
     * Get the XML of the parsed SMS's
     *
     * @param database
     * @param selectedMessages
     *
     * @return
     */
    public Document toXML(int[] selectedMessages) {
        String sSent="";

        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages").addAttribute("TotalSMS",
                                         String.valueOf(selectedMessages.length));

        int RecordIndex=0;
        int j          =0;

        for (SMSMessage record : database.smsRecords()) {

            if ((RecordIndex==selectedMessages[j]) && (selectedMessages[j]<database.smsRecords().size())) {
                if (record.wasSent()) {
                    sSent="true";
                } else {
                    sSent="false";
                }

                String Name="";

                if (resolveNames) {
                    Name=contactFinder.findContactByPhoneNumber(record.getNumber());
                } else {
                    Name=record.getNumber();
                }

//              System.out.println(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
//              +record.getNumber()+",\""+record.getText()+"\"");
                Element message=root.addElement("SmsMessage").addAttribute("UID", String.valueOf(record.getUID()));

                // Create the document
                // Add the "sentDate" element
                message.addElement("sentDate").addText(record.getSent().toString());

                // Add the "receivedDate" element
                message.addElement("receivedDate").addText(record.getReceived().toString());

                // Add the "sent?" element
                message.addElement("wasSent").addText(sSent);

                // Add the "to" element
                message.addElement("to").addText(Name);

                // Add the "text" element
                message.addElement("text").addText(record.getText()+"\n");
                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return createPrettyPrint(document);
    }
}
