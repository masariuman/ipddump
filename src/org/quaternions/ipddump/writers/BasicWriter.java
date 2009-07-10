package org.quaternions.ipddump.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.Document;

import org.quaternions.ipddump.data.InteractivePagerBackup;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 */
abstract class BasicWriter {
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
    public void setDatabase(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String toCSV() {
        return toCSV(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toCSV(int[] selectedRecords);

    /**
     * Method description
     *
     *
     * @return
     */
    public String toPlainText() {
        return toPlainText(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toPlainText(int[] SelectedRecords);

    /**
     * Method description
     *
     *
     * @return
     */
    public Document toXML() {
        return toXML(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public Document toXML(int[] SelectedRecords);

    //~--- get methods --------------------------------------------------------

    /**
     * Fills a int array with the instructions
     * for 'choosing' all the records from a database
     *
     * @return
     */
    protected int[] getAllRecords() {
        int[] allRecords=new int[getSize()];

        for (int i=0; i<allRecords.length; i++) {
            allRecords[i]=i;
        }

        return allRecords;
    }

    ;
}
