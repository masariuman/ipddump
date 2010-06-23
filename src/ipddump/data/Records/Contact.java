package ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author borkholder
 * @date Jun 6, 2009
 */
public class Contact extends Record implements Comparable<Contact> {
    private Image image;
    private boolean enableAdrressBookAllType=false;

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
    public Contact(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
        fields=new HashMap<String, String>();
    }

    public void enableAdrressBookAllType() {
        enableAdrressBookAllType=true;
    }

    //~--- enums --------------------------------------------------------------

    enum Field {
        Email(1), Fax(3), Work_Phone(6, 16), Home_Phone(7, 17), Mobile_Phone(8), Pager(9), PIN(10), Other_Number(18),
        Name(32), Company(33), Work_Address(35, 36), Work_City(38), Work_State(39), Work_Postcode(40), Work_Country(41),
        Job_Title(42), Webpage(54), Title(55),

        /* These are the Category tags. Null data if no tags. */
        Categories(59), Home_Address(61, 62), User(65, 66, 67, 68), Home_City(69), Home_State(70), Home_Postcode(71),
        Home_Country(72), Contact_Image(77), Notes(64), Birthday(82), Anniversary(83),

        /* This is always the same 8 characters */
        Unknown_8_Chars(false, 84),

        /* Always 4 characters, date? */
        Unknown_4_Chars(false, 85), Google_Talk(90);

        int[]   indexes;
        boolean supported;

        //~--- constructors ---------------------------------------------------

        Field(int... indexes) {
            this(true, indexes);
        }
        Field(boolean supported, int... indexes) {
            this.indexes  =indexes;
            this.supported=supported;
        }

        //~--- methods --------------------------------------------------------

        public boolean accept(int code) {
            if (supported) {
                for (int index : indexes) {
                    if (index==code) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public String toString() {
            return super.toString().replace('_', ' ');
        }
    }

    //~--- methods ------------------------------------------------------------

    public void addField(Field key, String value) {
        String keyName=key.toString();

        if (key==Field.Name) {
            String name=fields.get(keyName);

            if (name==null) {
                fields.put(keyName, value);
            } else {
                fields.put(keyName, name+" "+value);
            }
        } else if (fields.containsKey(keyName)) {
            int index=2;

            while (fields.containsKey(keyName+index)) {
                index++;
            }

            fields.put(keyName+index, value);
        } else {
            fields.put(keyName, value);
        }
    }

    @Override
    public void addField(int type, char[] data) {
        if (!enableAdrressBookAllType){
        for (Field field : Field.values()) {
            if (field.accept(type)) {
                if (field==Field.Contact_Image) {
                    image=decodeBase64(data);
                } else if (field==Field.Categories) {

                    // Microsoft Outlook Style. If sepated by commas then the CSV brakes.
                    addField(field, makeStringCropLast(data).replace(',', ';'));
                } else {
                    addField(field, makeStringCropLast(data));
                }
            }
        }
        }else {
             switch (type) {
        case 2 :{break;}
        case 3 :{break;}
        case 5 :{break;}
        case 10:{
            type  =10;
            length=0;
//            StringBuilder string = new StringBuilder();

            System.out.println(" \n--Data: "+makeStringCropLast(data.clone()));

           for (int pointer =0; pointer<data.length-3;  pointer+=0){
           length=data[pointer];
           System.out.format("\ntype: %h Length: %h Data: \n",type,length);
           System.out.print(String.valueOf(data.clone()).substring(pointer+2, length+pointer+2));


                       switch (type){
                            case 10 :{
                                System.out.println(" - Name: "+makeStringCropFirst(String.valueOf(data.clone()).substring(pointer+2, length+pointer+2).toCharArray()));
                            pointer+=21;
                            break;}
                             case 8 :{
                                 System.out.println(" - Mobile: "+makeStringCropFirst(String.valueOf(data.clone()).substring(pointer+2, length+pointer+2).toCharArray()));

                            break;}
            }

            pointer+=length;
            type = data[pointer+2];
            

            }

            
}
            
        }
        }
    }

    @Override
    public int compareTo(Contact o) {
        return getName().compareTo(o.getName());
    }

    //~--- get methods --------------------------------------------------------

    public String getAnniversary() {
        return getField(Field.Anniversary);
    }

    public String getBirthday() {
        return getField(Field.Birthday);
    }

    public String getCategories() {
        return getField(Field.Categories);
    }

    public String getCompany() {
        return getField(Field.Company);
    }

    public String getEmail() {
        return getField(Field.Email);
    }

    public String getGoogleTalk() {
        return getField(Field.Google_Talk);
    }

    public String getHomeAddress() {
        return getField(Field.Home_Address);
    }

    public String getHomeCity() {
        return getField(Field.Home_City);
    }

    public String getHomeCountry() {
        return getField(Field.Home_Country);
    }

    public String getHomePhone() {
        return getField(Field.Home_Phone);
    }

    public String getHomePostcode() {
        return getField(Field.Home_Postcode);
    }

    public String getHomeState() {
        return getField(Field.Home_State);
    }

    public Image getImage() {
        return image;
    }

    public String getJobTitle() {
        return getField(Field.Job_Title);
    }

    public String getMobilePhone() {
        return getField(Field.Mobile_Phone);
    }

    public String getName() {
        return getField(Field.Name);
    }

    public String getNotes() {
        return getField(Field.Notes);
    }

    public String getOtherNumber() {
        return getField(Field.Other_Number);
    }

    public String getPIN() {
        return getField(Field.PIN);
    }

    public String getPager() {
        return getField(Field.Pager);
    }

    public String getTitle() {
        return getField(Field.Title);
    }

    public String getUser() {
        return getField(Field.User);
    }

    public String getWebpage() {
        return getField(Field.Webpage);
    }

    public String getWorkAddress() {
        return getField(Field.Work_Address);
    }

    public String getWorkCity() {
        return getField(Field.Work_City);
    }

    public String getWorkCountry() {
        return getField(Field.Work_Country);
    }

    public String getWorkPhone() {
        return getField(Field.Work_Phone);
    }

    public String getWorkPostcode() {
        return getField(Field.Work_Postcode);
    }

    public String getWorkState() {
        return getField(Field.Work_State);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return getName();
    }

    private Image decodeBase64(char[] sb) {

        // maybe this is the correct way to display the image?
        byte[] buffer_decode=new byte[sb.length];

        for (int i=0; i<sb.length; i++) {
            buffer_decode[i]=(byte) sb[i];
        }

        return Toolkit.getDefaultToolkit().createImage(buffer_decode);
    }

    //~--- get methods --------------------------------------------------------

    private String getField(Field field) {
        String key=field.toString();

        if (fields.containsKey(key)) {
            return fields.get(key);
        } else {
            return "";
        }
    }
}
