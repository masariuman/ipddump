package org.quaternions.ipddump.data;

//~--- non-JDK imports --------------------------------------------------------

import org.quaternions.ipddump.data.Records.*;
import org.quaternions.ipddump.tools.Finder;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * The InteractivePagerBackup represents a single IPD file and provides an easy
 * way to generate records based on the initial record data.
 * </p>
 * <p>
 * In general, in the ipddump project IPD refers to the <em>file</em> and
 * <code>InteractivePagerBackup </code> refers to the <em>datastructure</em>
 * representing the file.
 * </p>
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public class InteractivePagerBackup {

    /**
     * The character used as the line feed.
     */
    private char lineFeed;

    /**
     * The version of the IPD.
     */
    private final int version;

    /**
     * The list of databases, or rather the names of the databases.
     */
    private final List<String> databases;

    /**
     * The set of SMS messages.
     */
    private final List<SMSMessage> smsRecords;

    /**
     * The set of contacts.
     */
    private final List<Contact> contacts;

    /**
     * The set of contacts.
     */
    private final List<Calendar> calendar;

    /**
     * The set of Tasks Entries.
     */
    private final List<Task> tasks;

    /**
     * The set of Memos.
     */
    private final List<Memo> memos;

    /**
     * The set of TimeZones.
     */
    private final List<BBTimeZone> timeZones;

    /**
     * The set of Phone Call Logs.
     */
    private final List<CallLog> callLogs;

    /**
     * Reports If there were Errors while parsing
     */
    private boolean errorFlag      =false;
    private boolean debugingEnabled=false;
    private boolean valuePeeking   =false;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new database.
     *
     * @param version The IPD version
     * @param lineFeed The line feed character
     */
    public InteractivePagerBackup(int version, char lineFeed) {
        this.version =version;
        this.lineFeed=lineFeed;
        databases    =new ArrayList<String>();
        smsRecords   =new ArrayList<SMSMessage>();
        contacts     =new ArrayList<Contact>();
        tasks        =new ArrayList<Task>();
        memos        =new ArrayList<Memo>();
        timeZones    =new ArrayList<BBTimeZone>();
        callLogs     =new ArrayList<CallLog>();
        calendar     =new ArrayList<Calendar>();
    }

    //~--- methods ------------------------------------------------------------

    public void enableValuePeeking() {
        valuePeeking=true;
    }

    /**
     * Adds a new database to the list of contained databases.
     *
     * @param name The name of the database to add
     */
    public void addDatabase(String name) {
        databases.add(name);
    }

    /**
     * Gets the collection of contacts.
     *
     * @return An unmodifiable collection of contacts
     */
    public Collection<Contact> contacts() {
        return Collections.<Contact>unmodifiableCollection(contacts);
    }

    /**
     * Creates a new {@link Record} to represent the type of data for the database
     * given by the dbIndex value.
     *
     * @param dbIndex The index of the database that this record will be in
     * @param version The version of the database to which this record belongs
     * @param uid The unique identifier of the Record
     * @param length The length of the Record in the data
     * @return A new Record
     */
    public Record createRecord(int dbIndex, int version, int uid, int length) {

        /*
         * Fix for bug #2, there might be an error in parsing, but for now, this seems to fix it.
         */
        if ((dbIndex>=databases.size()) || (dbIndex<0)) {
            if (valuePeeking || debugingEnabled) {
                System.out.println("-------dbID: "+dbIndex+"-------");

                return new DummyRecord(dbIndex, version, uid, length).enableValuePeeking();
            } else {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            }
        } else if ("SMS Messages".equals(databases.get(dbIndex))) {
            SMSMessage record=new SMSMessage(dbIndex, version, uid, length);

            smsRecords.add(record);

            return record;
        } else if ("Address Book".equals(databases.get(dbIndex))) {
            Contact record=new Contact(dbIndex, version, uid, length);

            contacts.add(record);

            return record;
        } else if ("Memos".equals(databases.get(dbIndex))) {
            org.quaternions.ipddump.data.Records.Memo record=new org.quaternions.ipddump.data.Records.Memo(dbIndex,
                                                                 version, uid, length);

            memos.add(record);

            return record;
        } else if ("Tasks".equals(databases.get(dbIndex))) {
            Task record=new Task(dbIndex, version, uid, length);

            tasks.add(record);

            return record;
        } else if ("Time Zones".equals(databases.get(dbIndex))) {
            BBTimeZone record=new BBTimeZone(dbIndex, version, uid, length);

            timeZones.add(record);

            return record;
        } else if ("Phone Call Logs".equals(databases.get(dbIndex))) {
            CallLog record=new CallLog(dbIndex, version, uid, length);

            callLogs.add(record);

            return record;

//          } else if ("Calendar".equals(databases.get(dbIndex))) {
//              Calendar record=new Calendar(dbIndex, version, uid, length);
//
//              distinguishRecord(dbIndex);
//              calendar.add(record);
//
//              return record;
        } else {
            if (valuePeeking) {
                distinguishRecord(dbIndex);

                return new DummyRecord(dbIndex, version, uid, length).enableValuePeeking();
            } else {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            }
        }
    }

    public void enableDebuging() {
        debugingEnabled=true;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Gets the collection of the Calendar.
     *
     * @return An unmodifiable collection of Calendar records
     */
    public Collection<Calendar> getCalendar() {
        return Collections.unmodifiableCollection(calendar);
    }

    /**
     * Gets the collection of the Phone Call Logs.
     *
     * @return An unmodifiable collection of Phone Call Logs records
     */
    public Collection<CallLog> getCallLogs() {
        return Collections.unmodifiableCollection(callLogs);
    }

    /**
     * Gets the list of database names that have been added so far.
     *
     * @return An unmodifiable list of database names
     */
    public List<String> getDatabaseNames() {
        return Collections.<String>unmodifiableList(databases);
    }

    /**
     * Gets the collection of memos.
     *
     * @return An unmodifiable collection of memos
     */
    public Collection<Memo> getMemos() {
        return Collections.<Memo>unmodifiableCollection(memos);
    }

    /**
     * Gets the collection of SMS records.
     *
     * @return An unmodifiable collection of SMS records
     */
    public Collection<SMSMessage> getSMSRecords() {
        return Collections.<SMSMessage>unmodifiableCollection(smsRecords);
    }

    /**
     * Gets the collection of task records.
     *
     * @return An unmodifiable collection of task records
     */
    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(tasks);
    }

    /**
     * Gets the collection of the Time Zones records.
     *
     * @return An unmodifiable collection of Time Zones records
     */
    public Collection<BBTimeZone> getTimeZones() {
        return Collections.unmodifiableCollection(timeZones);
    }

    //~--- methods ------------------------------------------------------------

    public void organize() {
        Collections.sort(memos);
        Collections.sort(smsRecords);
        Collections.sort(tasks);
        Collections.sort(contacts);
        Collections.sort(timeZones);
        Collections.sort(callLogs);
        Collections.sort(calendar);

        Finder finder=new Finder(this);

        for (Task recordt : this.tasks) {
            String name=finder.findTimeZoneByID(recordt.getTimeZone());

            recordt.setTimeZoneName(name);
        }
    }

    //~--- set methods --------------------------------------------------------

    public void setErrorFlag() {
        errorFlag=true;
    }

    //~--- methods ------------------------------------------------------------

    public boolean wereErrors() {
        return errorFlag;
    }

    private void distinguishRecord(int dbIndex) {
        System.out.println("----New "+getDatabaseNames().get(dbIndex)+"----");
    }
}
