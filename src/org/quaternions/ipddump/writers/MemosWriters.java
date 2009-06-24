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
import org.quaternions.ipddump.data.Memos;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Jimmys Daskalakis
 */
public class MemosWriters {
    FileWriters            filewriter=new FileWriters();
    InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public MemosWriters(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Get the cvs of the parsed Memo's
     *
     *
     * @return
     */
    public String MemosToCVS() {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("Title,Memo\n");

        for (Memos record : database.memos()) {
            temp.append(record.getTitle()+","+record.getMemo()+"\n");
        }

        return temp.toString();
    }

    /**
     * Get the cvs of the parsed Memo's
     *
     *
     * @return
     */
    public String MemosToCVS(int[] selectedMemos) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("Title,Memo\n");

        int RecordIndex=0;
        int j          =0;

        for (Memos record : database.memos()) {
            if ((RecordIndex==selectedMemos[j]) && (selectedMemos[j]<database.memos().size())) {
                temp.append(record.getTitle()+","+record.getMemo()+"\n");
                j++;

                if (j>=selectedMemos.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return temp.toString();
    }

    /**
     * Get a represantation of the parsed Memo's
     * in plain text
     *
     * @param database
     * @param MemoselectedRows
     *
     * @return
     */
    public String MemosToPlainText() {
        String tmp="";

        if (database!=null) {
            for (Memos record : database.memos()) {
                String title=record.getTitle();
                String memo =record.getMemo();

                tmp=tmp+"Title: "+title+"\nMemo:\n"+memo+"\n\n";
            }

            return tmp;
        }

        return tmp;
    }

    /**
     * Get a represantation of the parsed Memo's
     * in plain text
     *
     *
     * @param database
     * @param MemoselectedRows
     *
     * @return
     */
    public String MemosToPlainText(int[] MemoselectedRows) {
        String tmp="";

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (Memos record : database.memos()) {
                if ((RecordIndex==MemoselectedRows[j]) && (MemoselectedRows[j]<database.memos().size())) {
                    String title=record.getTitle();
                    String memo =record.getMemo();

                    tmp=tmp+"Title: "+title+"\nMemo:\n"+memo+"\n\n";
                    j++;

                    if (j>=MemoselectedRows.length) {
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
     * Get the XML of the parsed Memo's
     *
     *
     * @param database
     *
     * @return
     */
    public Document MemosToXML() {
        String   sSent   ="";
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("Memos").addAttribute("TotalMemos", String.valueOf(database.memos().size()));

        for (Memos record : database.memos()) {
            Element message=root.addElement("MemoMessage").addAttribute("UID", String.valueOf(record.getUID()));

            message.addElement("Title").addText(record.getTitle());
            message.addElement("Memo").addText(record.getMemo());
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
     * Get the XML of the parsed Memo's
     *
     * @param database
     * @param selectedMemos
     *
     * @return
     */
    public Document MemosToXML(int[] selectedMemos) {
        String sSent="";

        // System.out.println("uid,sent,received,sent?,far number,text");
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("Memos").addAttribute("TotalMemos", String.valueOf(selectedMemos.length));

        // System.out.println("uid,sent,received,sent?,far number,text");
        int RecordIndex=0;
        int j          =0;

        for (Memos record : database.memos()) {
            if ((RecordIndex==selectedMemos[j]) && (selectedMemos[j]<database.memos().size())) {
                Element message=root.addElement("MemoMessage").addAttribute("UID", String.valueOf(record.getUID()));

                message.addElement("Title").addText(record.getTitle());
                message.addElement("Memo").addText(record.getMemo());
                j++;

                if (j>=selectedMemos.length) {
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
     * Returns the total of the Memo messages
     *
     *
     * @return
     */
    public int getNumberOfMemos() {
        if (database!=null) {
            return database.memos().size();
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
}
