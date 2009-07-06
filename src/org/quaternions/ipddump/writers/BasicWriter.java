/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.Document;

import org.quaternions.ipddump.data.InteractivePagerBackup;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 */
abstract class BasicWriter {
    protected FileWriters            filewriter=new FileWriters();
    protected InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public BasicWriter(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    abstract public int getSize();

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param database
     */
    abstract public void setDatabase(InteractivePagerBackup database);

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    abstract public String toCVS();

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toCVS(int[] totalnumber);

    /**
     * Method description
     *
     *
     * @return
     */
    abstract public String toPlainText();

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toPlainText(int[] totalnumber);

    /**
     * Method description
     *
     *
     * @return
     */
    abstract public Document toXML();

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public Document toXML(int[] totalnumber);

    //~--- get methods --------------------------------------------------------

    /**
     * Fills a int array with the instructions
     * for 'choosing' all the records from a database
     *
     * @return
     */
    protected int[] getAllRecords(int totalnumber) {
        int[] allRecords=new int[totalnumber];

        for (int i=0; i<allRecords.length; i++) {
            allRecords[i]=i;
        }

        return allRecords;
    }

    ;
}