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
import org.quaternions.ipddump.writers.writeStringToFile;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jimmys Daskalakis
 */
public class FileWriters {

    /**
     *     Method description
     *
     *
     *     @param filename
     *     @param stringToWrite
     *
     *     @return
     */
    public boolean writeCsvtoFile(String filename, String stringToWrite) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
            filename=filename+".csv";
            System.out.println("\n->Writing "+filename);

            try {
                writeStringToFile.writeStringToFile(filename, stringToWrite);

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
    public boolean writeTxttoFile(String filename, String stringToWrite) {
        try {
            int last=filename.lastIndexOf('.');

            filename=filename.substring(0, last);
            filename=filename+".txt";
            System.out.println("\n->Writing "+filename);

            try {
                writeStringToFile.writeStringToFile(filename, stringToWrite);

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
    public boolean writeXMLtoFile(String filename, Document document) {
        int last=filename.lastIndexOf('.');

        filename=filename.substring(0, last);
        filename=filename+".csv";
        System.out.println("\n->Writing "+filename);

        // Make a pretty output
        OutputFormat format=OutputFormat.createPrettyPrint();

        format.setEncoding("UTF-8");

        // format.setTrimText(true);
//      Save it
        XMLWriter writer;

        try {
            writer=new XMLWriter(new FileWriter(filename), format);
            writer.write(document);
            writer.close();

            return true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());

            return false;
        }
    }
}
