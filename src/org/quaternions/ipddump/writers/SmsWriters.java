/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

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
    FileWriters filewriter=new FileWriters();

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param database
     * @param SMSselectedRows
     *
     * @return
     */
    public String SMStoPlainText(InteractivePagerBackup database, int[] SMSselectedRows) {
        String tmp="";

        if (database!=null) {
            int smsRecord=0;
            int j        =0;

            for (SMSMessage record : database.smsRecords()) {
                if ((smsRecord==SMSselectedRows[j]) && (SMSselectedRows[j]<database.smsRecords().size())) {
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

                    j++;

                    if (j>=SMSselectedRows.length) {
                        break;
                    }
                }

                smsRecord++;
            }

            return tmp;
        }

        return tmp;
    }

    /**
     * Method description
     *
     *
     * @param database
     * @param SMSselectedRows
     *
     * @return
     */
    public String SMStoPlainText(InteractivePagerBackup db) {
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
     * Method description
     *
     *
     * @param database
     *
     * @return
     */
    public Document SMSToXML(InteractivePagerBackup database) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages").addAttribute("TotalSMS",
                                         String.valueOf(database.smsRecords().size()));

        // System.out.println("uid,sent,received,sent?,far number,text");
        for (SMSMessage record : database.smsRecords()) {
            if (record.wasSent()) {
                sSent="true";
            } else {
                sSent="false";
            }

//          System.out.println(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
//          +record.getNumber()+",\""+record.getText()+"\"");
            Element message=root.addElement("SmsMessage").addAttribute("UID", String.valueOf(record.getUID()));

            // Create the document
            // Add the "sentDate" element
            message.addElement("sentDate").addText(record.getSent().toString());

            // Add the "receivedDate" element
            message.addElement("receivedDate").addText(record.getReceived().toString());

            // Add the "sent?" element
            message.addElement("wasSent").addText(sSent);

            // Add the "to" element
            message.addElement("to").addText(record.getNumber());

            // Add the "text" element
            message.addElement("text").addText(record.getText()+"\n");
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

    /**
     * Method description
     *
     *
     * @param database
     * @param selectedMessages
     *
     * @return
     */
    public Document SMSToXML(InteractivePagerBackup database, int[] selectedMessages) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages").addAttribute("TotalSMS",
                                         String.valueOf(selectedMessages.length));

        // System.out.println("uid,sent,received,sent?,far number,text");
        int smsRecord=0;
        int j        =0;

        for (SMSMessage record : database.smsRecords()) {

//          System.out.println(smsRecord+" "+j+" "+selectedMessages[j]);
            if ((smsRecord==selectedMessages[j]) && (selectedMessages[j]<database.smsRecords().size())) {
                if (record.wasSent()) {
                    sSent="true";
                } else {
                    sSent="false";
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
                message.addElement("to").addText(record.getNumber());

                // Add the "text" element
                message.addElement("text").addText(record.getText()+"\n");
                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            smsRecord++;
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

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public String SMStoCVS(InteractivePagerBackup database) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        for (SMSMessage record : database.smsRecords()) {
            temp.append(record.getUID()+","+record.getSent().toString()+","+record.getReceived().toString()+","
                        +record.wasSent()+","+record.getNumber()+",\""+record.getText()+"\"\n");
        }

        return temp.toString();
    }

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    public String SMStoCVS(InteractivePagerBackup database, int[] selectedMessages) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        int smsRecord=0;
        int j        =0;

        for (SMSMessage record : database.smsRecords()) {
            if ((smsRecord==selectedMessages[j]) && (selectedMessages[j]<database.smsRecords().size())) {
                temp.append(record.getUID()+","+record.getSent().toString()+","+record.getReceived().toString()+","
                            +record.wasSent()+","+record.getNumber()+",\""+record.getText()+"\"\n");
                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            smsRecord++;
        }

        return temp.toString();
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public static int getNumberOfSMS(InteractivePagerBackup database) {
        if (database!=null) {
            return database.smsRecords().size();
        } else {
            return -1;
        }
    }
}
