package org.quaternions.ipddump.data;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 * @created Jun 20, 2009
 */
public class BBTimeZone extends Record implements Comparable<BBTimeZone> {
    private int timezoneOfset;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID
     *          The database id
     * @param dbVersion
     *          The database version
     * @param uid
     *          The unique identifier of this record
     * @param recordLength
     *          The length of the record
     */
    public BBTimeZone(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        switch (type) {
        case 1 : {
            int timezone=makeInt(data);

            fields.put("TimeZoneID", String.valueOf(timezone));
        }

        case 2 :
            fields.put("PlaceName", String.valueOf(data));

            break;

        case 3 : {
            timezoneOfset=makeInt(data);
            fields.put("TimeZoneOffset", String.valueOf(timezoneOfset));
        }

        // default : viewItInHex(type, data);
        }
    }

    @Override
    public int compareTo(BBTimeZone o) {
        return getPlaceName().compareTo(o.getPlaceName());
    }

    //~--- get methods --------------------------------------------------------

    public String getPlaceName() {
        return getField("PlaceName")+" "+String.valueOf(timezoneOfset / 60.0)+"h";
    }

    public String getTimeZoneID() {
        return getField("TimeZoneID");
    }

    public String getTimeZoneOffset() {
        return getField("TimeZoneOffset");
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return fields.toString();
    }
}
