package org.quaternions.ipddump.data;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 * @created Jun 20, 2009
 */
public class BBTimeZone extends Record implements Comparable<BBTimeZone> {
    private int timezoneOfset;

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
            int timezone=data[0] << 0;

            timezone|=data[1] << 8;
            timezone|=data[2] << 16;
            timezone|=data[3] << 24;
            fields.put("TimeZoneID", String.valueOf(timezone));
        }

        case 2 :
            fields.put("PlaceName", String.valueOf(data));

            break;

        case 3 : {
            timezoneOfset=data[0] << 0;

            timezoneOfset|=data[1] << 8;
            timezoneOfset|=data[2] << 16;
            timezoneOfset|=data[3] << 24;
            fields.put("TimeZoneOffset", String.valueOf(timezoneOfset));
        }

        // default : viewItInHex(type, data);
        }
    }

    @Override
    public int compareTo(BBTimeZone o) {
        return getPlaceName().compareTo(o.getPlaceName());
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    //~--- get methods --------------------------------------------------------

    public String getPlaceName() {
        return getField("PlaceName")+" "+String.valueOf(timezoneOfset/60.0)+"h";
    }

    public String getTimeZoneID() {
        return getField("TimeZoneID");
    }

    public String getTimeZoneOffset() {
        return getField("TimeZoneOffset");
    }
    
}
