package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A Record is an entry in a particular database. Each record indexes into the
 * list of databases and identifies the type of data in the fields of this
 * record.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public abstract class Record {

    /**
     * The 0-based index of the database to which this field belongs.
     */
    protected final int databaseID;

    /**
     * The version of the database to which this field belongs.
     */
    protected final int databaseVersion;

    /**
     * The unique identifier of this record.
     */
    protected int uniqueID;

    /**
     * The length of the record.
     */
    protected int length;

    /**
     * A handle of the record in the database. This is an element in the
     * increasing sequence of integers within the IPD.
     */
    protected int recordDBHandle;

    /**
     * The map from the name of the field to the field value.
     */
    protected Map<String, String> fields=new HashMap<String, String>();

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    protected Record(int dbID, int dbVersion, int uid, int recordLength) {
        databaseID     =dbID;
        databaseVersion=dbVersion;
        uniqueID       =uid;
        length         =recordLength;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Adds the field to the record.
     *
     * @param type The type of field
     * @param data The field data
     */
    public abstract void addField(int type, char[] data);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Record) {
            return uniqueID==((Record) obj).uniqueID;
        } else {
            return false;
        }
    }

    /**
     * Gets the fields contained by this record.
     *
     * @return An unmodifiable map from the name of the field to the field data
     */
    public Map<String, String> fields() {
        return Collections.unmodifiableMap(fields);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Gets the 0-based index of the database to which this field belongs.
     *
     * @return The database index
     */
    public int getDatabaseID() {
        return databaseID;
    }

    /**
     * Gets the version of the database to which this field belongs.
     *
     * @return The database version
     */
    public int getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Gets the length of the record.
     *
     * @return The record length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the handle of the record in the database.
     *
     * @return The record handle
     */
    public int getRecordDBHandle() {
        return recordDBHandle;
    }

    /**
     * Gets the unique identifier of this record.
     */
    public int getUID() {
        return uniqueID;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public int hashCode() {
        return uniqueID;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the handle of the record in the database.
     *
     * @param recordDBHandle The record handle
     */
    public void setRecordDBHandle(int recordDBHandle) {
        this.recordDBHandle=recordDBHandle;
    }

    //~--- methods ------------------------------------------------------------

    /**
     *  Displays the type and the value of the given
     *   unknown field
     *
     *   @param type The type of field
     *   @param data The field data
     */
    public void viewIt(int type, char[] data) {
        System.out.format("Type:%d Data String:%s length:%d\n", type, String.valueOf(data), data.length);
    }

    public void viewIt(int type, String string) {
        System.out.format("Type %d Data String:%s\n", type, string);
    }

    public void viewItInHex(int type, char[] data) {
        System.out.format("Type %d Data Hex:%h length:%d\n", type, String.valueOf(data), data.length);
    }

    public void viewItInHex(int type, String string) {
        System.out.format("Type %d Data Hex:%h\n", type, string);
    }

    public void viewItInInt(int type, char[] data) {
        System.out.format("Type %d Data Int:%d\n", type, makeInt(data));
    }

    //~--- get methods --------------------------------------------------------

    protected final String getField(String key) {
        if (fields.containsKey(key)) {
            return fields.get(key);
        } else {
            return "";
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Making a date is not simple, RIM doesn't use the standard
     * "seconds since the epoch". The unit is minutes, but the zero point is
     * somewhere around the start of 1900.
     */
    protected Date makeDate(char[] data) {
        long time=data[0] << 0;

        time|=data[1] << 8;
        time|=data[2] << 16;
        time|=data[3] << 24;

        // Turn into milliseconds units
        time*=60 * 1000;

        // Zero out at Jan 1, 1900
        time-=2208970740000L;

        // Make the offset be the local timezone, minus the odd 61 minutes
        int offset=TimeZone.getDefault().getOffset(time);

        offset-=61 * 60 * 1000;

        return new Date(time+offset);
    }

    protected Date makeDate2(char[] data) {
        long val0=0;

        for (int i=0; i<8; i++) {
            val0|=(long) data[i] << (i * 8);
        }

        return new Date(val0);
    }

    protected String makeDuration(int seconds) {
        if (seconds==0) {
            return "None";
        }

        int duration=seconds;
        int hours   =0;
        int minutes =0;
        int sec     =0;

        hours  =duration / 3600;
        minutes=(duration-(3600 * hours)) / 60;
        sec    =duration-((hours * 3600)+(minutes * 60));

        String stduration="";

        if (hours>0) {
            stduration=String.valueOf(hours)+":";
        }

        if (minutes<10) {
            stduration+="0";
        }

        stduration+=String.valueOf(minutes)+":";

        if (sec<10) {
            stduration+="0";
        }

        stduration+=String.valueOf(sec);

        return stduration;
    }

    protected int makeInt(char[] data) {
        int temp=-1000;

        if (data.length==4) {
            temp=data[0] << 0;
            temp|=data[1] << 8;
            temp|=data[2] << 16;
            temp|=data[3] << 24;
        } else {
            System.out.println("Invalid Int found size: "+data.length);
        }

        return temp;
    }
}
