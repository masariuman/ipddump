package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author borkholder
 * @date Jun 6, 2009
 */
public class Contact extends Record implements Comparable<Contact> {

    /**
     * The map from the name of the field to the field value.
     */
    protected final Map<String, String> fields;
    protected String                    firstName;
    protected String                    lastName;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    Contact(int dbID, int dbVersion, int uid, int recordLength) {
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
            fieldName="Email";

            break;

        case 3 :
            fieldName="Fax";

            break;

        case 6 :
        case 16 :
            fieldName="Work Phone";

            break;

        case 7 :
        case 17 :
            fieldName="Home Phone";

            break;

        case 8 :
            fieldName="Mobile Phone";

            break;

        case 9 :
            fieldName="Pager";

            break;

        case 10 :
            fieldName="PIN";

            break;

        case 18 :
            fieldName="Other Number";

            break;

        case 32 :
            fieldName="Name";

            break;

        case 33 :
            fieldName="Company";

            break;

        case 35 :
        case 36 :
            fieldName="Work Address";

            break;

        case 38 :
            fieldName="Work City";

            break;

        case 39 :
            fieldName="Work State";

            break;

        case 40 :
            fieldName="Work Postcode";

            break;

        case 41 :
            fieldName="Work Country";

            break;

        case 42 :
            fieldName="Job Title";

            break;

        case 54 :
            fieldName="Webpage";

            break;

        case 55 :
            fieldName="Title";

            break;

        case 59 :
            if (data!=null) {
                fieldName="Categories";
            }

            break;    // These are the Category tags. Null data if no tags

        case 61 :
        case 62 :
            fieldName="Home Address";

            break;

        case 65 :
        case 66 :
        case 67 :
        case 68 :
            fieldName="User";

            break;

        case 69 :
            fieldName="Home City";

            break;

        case 70 :
            fieldName="Home State";

            break;

        case 71 :
            fieldName="Home Postcode";

            break;

        case 72 :
            fieldName="Home Country";

            break;

        case 77 :

            // fieldName = null; //Jibrish?
            break;

        case 64 :
            fieldName="Notes";

            break;

        case 82 :
            fieldName="Birthday";

            break;

        case 83 :
            fieldName="Anniversary";

            break;

        case 84 :

            // This is always the same 8 characters
            break;

        case 85 :

            // Always 4 characters, date?
            break;

        case 90 :
            fieldName="Google Talk";

            break;

        default :
            System.err.println(type+": "+makeString(data));
        }

        if (fieldName!=null /* && makeString( data )!=null */) {
            addField(fieldName, makeString(data));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Contact o) {
        if (getName().compareTo(o.getName())!=0) {
            return getName().compareTo(o.getName());
        } else {
            return o.getName().compareTo(getName());
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
    public String getAnniversary() {
        if (fields.containsKey("Anniversary")) {
            return fields.get("Anniversary");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getBirthday() {
        if (fields.containsKey("Birthday")) {
            return fields.get("Birthday");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getCategories() {
        if (fields.containsKey("Categories")) {
            return fields.get("Categories");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getCompany() {
        if (fields.containsKey("Company")) {
            return fields.get("Company");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getEmail() {
        if (fields.containsKey("Email")) {
            return fields.get("Email");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getGoogleTalk() {
        if (fields.containsKey("Google Talk")) {
            return fields.get("Google Talk");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomeAddress() {
        if (fields.containsKey("Home Address")) {
            return fields.get("Home Address");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomeCity() {
        if (fields.containsKey("Home City")) {
            return fields.get("Home City");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomeCountry() {
        if (fields.containsKey("Home Country")) {
            return fields.get("Home Country");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomePhone() {
        if (fields.containsKey("Home Phone")) {
            return fields.get("Home Phone");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomePostcode() {
        if (fields.containsKey("Home Postcode")) {
            return fields.get("Home Postcode");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getHomeState() {
        if (fields.containsKey("Home State")) {
            return fields.get("Home State");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getJobTitle() {
        if (fields.containsKey("Job Title")) {
            return fields.get("Job Title");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getMobilePhone() {
        if (fields.containsKey("Mobile Phone")) {
            return fields.get("Mobile Phone");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName() {
        if (fields.containsKey("Name")) {
            return fields.get("Name");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getNotes() {
        if (fields.containsKey("Notes")) {
            return fields.get("Notes");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getOtherNumber() {
        if (fields.containsKey("Other Number")) {
            return fields.get("Other Number");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPIN() {
        if (fields.containsKey("PIN")) {
            return fields.get("PIN");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPager() {
        if (fields.containsKey("Pager")) {
            return fields.get("Pager");
        }

        return "";
    }

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
    public String getUser() {
        if (fields.containsKey("User")) {
            return fields.get("User");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWebpage() {
        if (fields.containsKey("Webpage")) {
            return fields.get("Webpage");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkAddress() {
        if (fields.containsKey("Work Address")) {
            return fields.get("Work Address");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkCity() {
        if (fields.containsKey("Work City")) {
            return fields.get("Work City");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkCountry() {
        if (fields.containsKey("Work Country")) {
            return fields.get("Work Country");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkPhone() {
        if (fields.containsKey("Work Phone")) {
            return fields.get("Work Phone");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkPostcode() {
        if (fields.containsKey("Work Postcode")) {
            return fields.get("Work Postcode");
        }

        return "";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getWorkState() {
        if (fields.containsKey("Work State")) {
            return fields.get("Work State");
        }

        return "";
    }

    //~--- methods ------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "";
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
        Gsm2Iso.Gsm2Iso(data);

        String str=new String(data);

        return str.substring(0, str.length()-1);
    }
}
