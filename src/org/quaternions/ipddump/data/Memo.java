package org.quaternions.ipddump.data;

/**
 * A memo is a record with title and text.
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 * @date Jun 20, 2009
 */
public class Memo extends Record implements Comparable<Memo> {
    protected String                    text="";

    protected String                    title="ERROR";

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    Memo(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
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

    @Override
    public int compareTo(Memo o) {
      return getTitle().compareTo(o.getTitle());
    }

    //~--- get methods --------------------------------------------------------

    public String getMemo() {
      return text;
    }

    public String getTitle() {
      return title;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return getTitle()+": "+getMemo();
    }

    protected String makeString(char[] data) {
        String str=Gsm2Iso.Gsm2Iso(data);
        return str.substring(0, str.length()-1);
    }
}
