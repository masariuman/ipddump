package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @Jimmys Daskalakis
 * @date Jun 20, 2009
 */
public class Memos extends Record implements Comparable<Memos> {

    /**
     * The map from the name of the field to the field value.
     */
    protected final Map<String, String> fields;
    protected String                    text;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    Memos(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
        fields=new HashMap<String, String>();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void addField(int type, char[] data) {
        String fieldName=null;

        switch (type) {
        case 1 :
            fieldName="Title";
            break;

        case 2 :
        fieldName="Memo";

            break;

        case 3 :

            break;

        default :
            System.err.println(type+": "+makeString(data));
        }

//        if (fieldName==null /* && makeString( data )!=null */) {
//            System.out.println(type+":-> "+makeString(data));
//        }

        if (fieldName!=null /* && makeString( data )!=null */) {
            //System.out.println(fieldName+" "+type+":-> "+makeString(data));
            addField(fieldName, makeString(data));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Memos o) {
         if ( getTitle().compareTo( o.getTitle() ) != 0 ) {
      return getTitle().compareTo( o.getTitle() );
    } else {
      return o.getTitle().compareTo( getTitle());
    }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> fields() {
        return Collections.<String, String>unmodifiableMap(fields);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String getTitle() {
        if (fields.containsKey("Title")) {
            return fields.get("Title");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getMemo() {
        if (fields.containsKey("Memo")) {
            return fields.get("Memo");
        }

        return "";
    }

    //~--- methods ------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getTitle()+": "+getMemo();
    }

    /**
     * Method description
     *
     *
     * @param key
     * @param value
     */
    protected void addField(String key, String value) {
        if (key.equalsIgnoreCase("name")) {
            String name=fields.get(key);

            if (name==null) {
                fields.put(key, value);
            } else {
                fields.put(key, name+" "+value);
            }
        } else if (fields.containsKey(key)) {
            int index=2;

            while (fields.containsKey(key+index)) {
                index++;
            }

            fields.put(key+index, value);
        } else {
            fields.put(key, value);
        }
    }

    /**
     * Method description
     *
     *
     * @param data
     *
     * @return
     */
    protected String makeString(char[] data) {

        //Gsm2Iso.Gsm2Iso(data);
        String str=new String(data);

        return str.substring(0, str.length()-1);
    }
}
