package org.quaternions.ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This record is here for any records that this utility doesn't handle yet.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public class DummyRecord extends Record {
    List<String> fields=new ArrayList<String>();

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    public DummyRecord(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        fields.add(""+type+"|"+new String(data));
    }

    @Override
    public Map<String, String> fields() {
        return Collections.<String, String>emptyMap();
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
