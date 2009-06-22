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
import org.quaternions.ipddump.data.SMSMessage;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.*;

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
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        for (Contact record : database.contacts()) {
            String Email       =record.getEmail();
            String HomePhone   =record.getHomePhone();
            String WorkPhone   =record.getWorkPhone();
            String MobilePhone =record.getMobilePhone();
            String Pager       =record.getPager();
            String PIN         =record.getPIN();
            String OtherNumber =record.getOtherNumber();
            String Name        =record.getName();
            String Company     =record.getCompany();
            String WorkAddress =record.getWorkAddress();
            String WorkCity    =record.getWorkCity();
            String WorkState   =record.getWorkState();
            String WorkPostcode=record.getWorkPostcode();
            String GoogleTalk  =record.getGoogleTalk();
            String Anniversary =record.getAnniversary();
            String Birthday    =record.getBirthday();
            String Notes       =record.getNotes();
            String HomeCountry =record.getHomeCountry();
            String WorkCountry =record.getWorkCountry();
            String JobTitle    =record.getJobTitle();
            String Webpage     =record.getWebpage();
            String HomePostcode=record.getHomePostcode();
            String HomeState   =record.getHomeState();
            String HomeCity    =record.getHomeCity();
            String User        =record.getUser();
            String HomeAddress =record.getHomeAddress();
            String Categories  =record.getCategories();
            String Title       =record.getTitle();

            temp.append(Name+","+MobilePhone+"\n");
        }

        return temp.toString();
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
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("uid,sent,received,sent?,far number,text\n");

        int RecordIndex=0;
        int j          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                String Email       =record.getEmail();
                String HomePhone   =record.getHomePhone();
                String WorkPhone   =record.getWorkPhone();
                String MobilePhone =record.getMobilePhone();
                String Pager       =record.getPager();
                String PIN         =record.getPIN();
                String OtherNumber =record.getOtherNumber();
                String Name        =record.getName();
                String Company     =record.getCompany();
                String WorkAddress =record.getWorkAddress();
                String WorkCity    =record.getWorkCity();
                String WorkState   =record.getWorkState();
                String WorkPostcode=record.getWorkPostcode();
                String GoogleTalk  =record.getGoogleTalk();
                String Anniversary =record.getAnniversary();
                String Birthday    =record.getBirthday();
                String Notes       =record.getNotes();
                String HomeCountry =record.getHomeCountry();
                String WorkCountry =record.getWorkCountry();
                String JobTitle    =record.getJobTitle();
                String Webpage     =record.getWebpage();
                String HomePostcode=record.getHomePostcode();
                String HomeState   =record.getHomeState();
                String HomeCity    =record.getHomeCity();
                String User        =record.getUser();
                String HomeAddress =record.getHomeAddress();
                String Categories  =record.getCategories();
                String Title       =record.getTitle();

                temp.append(Name+","+MobilePhone+"\n");
                j++;

                if (j>=selectedContacts.length) {
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
     * @return
     */
    public String ContactsToPlainText() {
        String tmp="";

        if (database!=null) {
            for (Contact record : database.contacts()) {
                Iterator iterator2=record.fields().entrySet().iterator();

                for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                    Map.Entry entry=(Map.Entry) iterator.next();

                    tmp+=entry.getKey()+" : "+entry.getValue()+"\n";
                }

                tmp+="\n";
            }

            return tmp;
        }

        return tmp;
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

                        tmp+=entry.getKey()+" : "+entry.getValue()+"\n";
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
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("Contacts").addAttribute("TotalContacts",
                                         String.valueOf(database.contacts().size()));

        for (Contact record : database.contacts()) {
            Element  message  =root.addElement("Contact").addAttribute("UID", String.valueOf(record.getUID()));
            Iterator iterator2=record.fields().entrySet().iterator();

            for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                Map.Entry entry=(Map.Entry) iterator.next();
                String    type =removeSpaces(entry.getKey().toString());
                String    value=removeSpaces(entry.getValue().toString());

                message.addElement(type).addText(value);
            }
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
                    String    value=removeSpaces(entry.getValue().toString());

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

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param s
     *
     * @return
     */
    public String removeSpaces(String s) {
        StringTokenizer st=new StringTokenizer(s, " ", false);
        String          t ="";

        while (st.hasMoreElements()) {
            t+=st.nextElement();
        }

        return t;
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
}
