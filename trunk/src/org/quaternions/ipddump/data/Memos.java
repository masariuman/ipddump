package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
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

    protected String                    title;

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

    @Override
    public void addField(int type, char[] data) {
        String fieldName=null;
        switch (type) {
        case 1 :
            title = makeString(data);
            fields.put("Title", title);
            break;

        case 2 :
            text = makeString(data);
            fields.put("Memo", text);
            break;

        case 3 :
            break;

        default :
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Memos o) {
      return getTitle().compareTo(o.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> fields() {
        return Collections.unmodifiableMap(fields);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String getMemo() {
      return text;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getTitle() {
      return title;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getTitle()+": "+getMemo();
    }

    protected String makeString(char[] data) {
        String str=new String(data);
        return str.substring(0, str.length()-1);
    }
}
