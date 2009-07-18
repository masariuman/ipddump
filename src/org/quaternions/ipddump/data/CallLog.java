package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 * @created Jun 20, 2009
 */
public class CallLog extends Record implements Comparable<CallLog> {

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
    public CallLog(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        switch (type) {
        case 1 :
            break;    // always the same

        case 6 :
            break;    // always the same

        case 8 :
            break;    // always the same

        case 16 :
            break;    // always the same

        case 10 :
            //viewItInInt(type, data);
            // Probably nothing special
            break;

        case 11 :
            //viewItInInt(type, data);
            // Probably nothing special
            break;

        case 13 :
           // viewItInInt(type, data);
            // Probably nothing special
            break;

        case 2 :
            if (makeInt(data)==0) {
                fields.put("Call", "Received Call");
            } else if (makeInt(data)==1) {
                fields.put("Call", "Placed Call");
            } else if (makeInt(data)==2) {
                fields.put("Call", "Placed, Aborted");
            } else if (makeInt(data)==3) {
                fields.put("Call", "Missed Call");
            }

            //viewItInInt(type, data);
            System.out.println(getField("Call"));
            break;

        case 31 :
            fields.put("Name", makeString(data));
            System.out.println(getField("Name"));

            break;

        case 3 : {
            String duration=makeDuration(makeInt(data));

            fields.put("Duration", duration);
            System.out.println(getField("Duration"));

            break;
        }

        case 4 : {
            fields.put("Date", makeDate2(data).toString());
            System.out.println(getField("Date"));

            break;
        }

        case 12 : {
            fields.put("Number", makeString(data));
            System.out.println(getField("Number"));

            break;
        }

        default :
            //viewItInInt(type, data);
        }
    }

    @Override
    public int compareTo(CallLog o) {
        return 0;    // getPlaceName().compareTo(o.getPlaceName());
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    protected String makeString(char[] data) {
        String str=Gsm2Iso.Gsm2Iso(data);

        return str.substring(0, str.length()-1);
    }
}
