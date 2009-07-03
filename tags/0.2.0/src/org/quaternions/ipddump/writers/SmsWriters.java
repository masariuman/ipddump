/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



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
 * @author Jimmys Daskalakis
 */
public class SmsWriters {
    FileWriters            filewriter=new FileWriters();
    InteractivePagerBackup database;
    boolean                resolveNames=false;
    ContactFinder          contactFinder;

    //~--- constructors -------------------------------------------------------

    public SmsWriters(InteractivePagerBackup database) {
        this.database    =database;
        this.resolveNames=false;
    }
    public SmsWriters(InteractivePagerBackup database, boolean resolveNames) {
        this.database    =database;
        this.resolveNames=resolveNames;
        contactFinder    =new ContactFinder(database);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public String SMSToCVS() {
        return SMSToCVS(getAllRecords());
    }

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public String SMSToCVS(int[] selectedMessages) {
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
     * @param database
     * @param SMSselectedRows
     *
     * @return
     */
    public String SMSToPlainText() {
        return SMSToPlainText(getAllRecords());
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
    public String SMSToPlainText(int[] SMSselectedRows) {
        String tmp="";

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
                        tmp=tmp+"From: "+Name+"\nTo: My Phone"+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"+text
                            +"\n\n";
                    } else {
                        tmp=tmp+"From: My Phone"+"\nTo: "+Name+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"+text
                            +"\n\n";
                    }

                    j++;

                    if (j>=SMSselectedRows.length) {
                        break;
                    }
                }

                RecordIndex++;
            }

            return tmp;
        }

        return tmp;
    }

    /**
     * Get the XML of the parsed SMS's
     *
     *
     * @param database
     *
     * @return
     */
    public Document SMSToXML() {
        return SMSToXML(getAllRecords());
    }

    /**
     * Get the XML of the parsed SMS's
     *
     * @param database
     * @param selectedMessages
     *
     * @return
     */
    public Document SMSToXML(int[] selectedMessages) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages").addAttribute("TotalSMS",
                                         String.valueOf(selectedMessages.length));

        // System.out.println("uid,sent,received,sent?,far number,text");
        int RecordIndex=0;
        int j          =0;

        for (SMSMessage record : database.smsRecords()) {

//          System.out.println(smsRecord+" "+j+" "+selectedMessages[j]);
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

        OutputFormat format=OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");

        // format.setTrimText(true);
//      Save it
        XMLWriter    writer;
        StringWriter str=new StringWriter();

        writer=new XMLWriter(str, format);

        try {
            writer.write(document);
            writer.close();
            document=DocumentHelper.parseText(str.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
        }

        // System.out.println(document.getDocument().getText());
        return document;

        // root.addAttribute("DbID", String.valueOf(record.getDatabaseID()));
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public int getNumberOfSMS() {
        if (database!=null) {
            return database.smsRecords().size();
        } else {
            return -1;
        }
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param database
     */
    public void setDatabase(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Fills a int array with the instructions
     * for 'choosing' all the records from a database
     *
     *
     * @return
     */
    private int[] getAllRecords() {
        int[] allRecords=new int[getNumberOfSMS()];

        for (int i=0; i<allRecords.length; i++) {
            allRecords[i]=i;
        }

        return allRecords;
    }
}
