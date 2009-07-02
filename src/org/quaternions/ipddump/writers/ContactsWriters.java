/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.quaternions.ipddump.data.Contact;
import org.quaternions.ipddump.data.InteractivePagerBackup;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 *
 * @author Jimmys Daskalakis
 */
public class ContactsWriters {
    FileWriters            filewriter=new FileWriters();
    InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public ContactsWriters(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String ContactsToCVS() {
        return ContactsToCVS(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param selectedContacts
     *
     * @return
     */
    public String ContactsToCVS(int[] selectedContacts) {
        StringBuilder builder=new StringBuilder();    // fast builder!!

        /*
         * Get all the keys since we don't know all of them ahead
         * of time.  Some fields might be duplicated several times.
         */
        Set<String> keys       =new TreeSet<String>();
        for (Contact record : database.contacts()) {
          keys.addAll(record.fields().keySet());
        }

        int         RecordIndex=0;
        int         j          =0;

        List<String> names=new ArrayList<String>(keys);
        boolean      first=true;

        for (String name : names) {
            if (first) {
                first=false;
            } else {
                builder.append(",");
            }

            builder.append(name);
        }

        builder.append("\n");

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                first=true;

                Map<String, String> fields=record.fields();

                for (String name : names) {
                    if (first) {
                        first=false;
                    } else {
                        builder.append(",");
                    }

                    String value=fields.get(name);

                    if (value!=null) {
                        builder.append(value);
                    }
                }

                builder.append("\n");
                j++;

                if (j>=selectedContacts.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return builder.toString();
    }

    /**
     * Get a represantation of the parsed SMS's
     * in plain text
     *
     *
     * @return
     */
    public String ContactsToPlainText() {
        return ContactsToPlainText(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param selectedContacts
     *
     * @return
     */
    public String ContactsToPlainText(int[] selectedContacts) {
        String tmp="";

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (Contact record : database.contacts()) {
                if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                    Iterator iterator2=record.fields().entrySet().iterator();

                    for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                        Map.Entry entry=(Map.Entry) iterator.next();

                        tmp+=entry.getKey()+": "+entry.getValue()+"\n";
                    }

                    tmp+="\n";
                    j++;

                    if (j>=selectedContacts.length) {
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
     *
     *
     * @return
     */
    public Document ContactsToXML() {
        return ContactsToXML(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param selectedMessages
     *
     * @return
     */
    public Document ContactsToXML(int[] selectedMessages) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("Contacts").addAttribute("TotalContacts",
                                         String.valueOf(selectedMessages.length));

        // System.out.println("uid,sent,received,sent?,far number,text");
        int RecordIndex=0;
        int j          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedMessages[j]) && (selectedMessages[j]<database.contacts().size())) {
                Element  message  =root.addElement("Contact").addAttribute("UID", String.valueOf(record.getUID()));
                Iterator iterator2=record.fields().entrySet().iterator();

                for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                    Map.Entry entry=(Map.Entry) iterator.next();
                    String    type =removeSpaces(entry.getKey().toString());
                    String    value=entry.getValue().toString();

                    message.addElement(type).addText(value);
                }

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
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public int getNumberOfContacts() {
        if (database!=null) {
            return database.contacts().size();
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
     * @return
     */
    private int[] getAllRecords() {
        int[] allRecords=new int[getNumberOfContacts()];

        for (int i=0; i<allRecords.length; i++) {
            allRecords[i]=i;
        }

        return allRecords;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param s
     *
     * @return
     */
    private String removeSpaces(String s) {
        StringTokenizer st=new StringTokenizer(s, " ", false);
        String          t ="";

        while (st.hasMoreElements()) {
            t+=st.nextElement();
        }

        return t;
    }
}
