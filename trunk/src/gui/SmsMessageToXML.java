/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package gui;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jimmys Daskalakis
 */
public class SmsMessageToXML {

    /**
     * Parces the db to an xml document
     *
     *
     * @param database
     */
    public static Document createSmsMessageToXML(InteractivePagerBackup database) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages");

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

        // System.out.println(document.getDocument().getText());
        return document;

        // root.addAttribute("DbID", String.valueOf(record.getDatabaseID()));
    }

    /**
     * Writes the Xml into a file.
     * The diference of the method writeBytes2File is that
     * this one makes the xml 'pretty' and then it writes it.
     *
     *
     * @param path
     * @param document
     *
     * @throws IOException
     */
    public static void saveXML(String path, Document document) throws IOException {

        // Make a pretty output
        OutputFormat format=OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");

        // format.setTrimText(true);

//      Save it
        XMLWriter writer;

        writer=new XMLWriter(new FileWriter(path), format);
        writer.write(document);
        writer.close();
    }
}
