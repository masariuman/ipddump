package org.quaternions.ipddump.data;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author borkholder
 * @date Jun 6, 2009
 */
public class Contact extends Record implements Comparable<Contact> {
  enum Field {
    Email(1),
    Fax(3),
    Work_Phone(6, 16),
    Home_Phone(7, 17),
    Mobile_Phone(8),
    Pager(9),
    PIN(10),
    Other_Number(18),
    Name(32),
    Company(33),
    Work_Address(35, 36),
    Work_City(38),
    Work_State(39),
    Work_Postcode(40),
    Work_Country(41),
    Job_Title(42),
    Webpage(54),
    Title(55),
    /* These are the Category tags. Null data if no tags. */
    Categories(59),
    Home_Address(61, 62),
    User(65, 66, 67, 68),
    Home_City(69),
    Home_State(70),
    Home_Postcode(71),
    Home_Country(72),
    Contact_Image(77),
    Notes(64),
    Birthday(82),
    Anniversary(83),
    /* This is always the same 8 characters */
    Unknown_8_Chars(false, 84),
    /* Always 4 characters, date? */
    Unknown_4_Chars(false, 85),
    Google_Talk(90);

    int[] indexes;

    boolean supported;

    Field(int... indexes) {
      this(true, indexes);
    }

    Field(boolean supported, int... indexes) {
      this.indexes = indexes;
      this.supported = supported;
    }

    public boolean accept(int code) {
      if (supported) {
        for (int index : indexes) {
          if (index == code) {
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

  private Image image;

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
  Contact(int dbID, int dbVersion, int uid, int recordLength) {
    super(dbID, dbVersion, uid, recordLength);
    fields = new HashMap<String, String>();
  }

  @Override
  public void addField(int type, char[] data) {
    for (Field field : Field.values()) {
      if (field.accept(type)) {
        if (field == Field.Contact_Image) {
          image = decodeBase64(String.valueOf(data));
        } else {
          addField(field, makeString(data));
        }
      }
    }
  }

  protected String makeString(char[] data) {
    Gsm2Iso.Gsm2Iso(data);
    String str = new String(data);
    return str.substring(0, str.length() - 1);
  }

  @Override
  public int compareTo(Contact o) {
    return getName().compareTo(o.getName());
  }

  @Override
  public String toString() {
    return getName();
  }

  public void addField(Field key, String value) {
    String keyName = key.toString();
    if (key == Field.Name) {
      String name = fields.get(keyName);
      if (name == null) {
        fields.put(keyName, value);
      } else {
        fields.put(keyName, name + " " + value);
      }
    } else if (fields.containsKey(keyName)) {
      int index = 2;
      while (fields.containsKey(keyName + index)) {
        index++;
      }
      fields.put(keyName + index, value);
    } else {
      fields.put(keyName, value);
    }
  }

  private String getField(Field field) {
    String key = field.toString();
    if (fields.containsKey(key)) {
      return fields.get(key);
    } else {
      return "";
    }
  }

  private Image decodeBase64(String sb) {
    try {
      byte[] buffer_decode = new sun.misc.BASE64Decoder().decodeBuffer(sb);
      return Toolkit.getDefaultToolkit().createImage(buffer_decode);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getEmail() {
    return getField(Field.Email);
  }

  public Image getImage() {
    return image;
  }

  public String getHomePhone() {
    return getField(Field.Home_Phone);
  }

  public String getWorkPhone() {
    return getField(Field.Work_Phone);
  }

  public String getMobilePhone() {
    return getField(Field.Mobile_Phone);
  }

  public String getPager() {
    return getField(Field.Pager);
  }

  public String getPIN() {
    return getField(Field.PIN);
  }

  public String getOtherNumber() {
    return getField(Field.Other_Number);
  }

  public String getName() {
    return getField(Field.Name);
  }

  public String getCompany() {
    return getField(Field.Company);
  }

  public String getWorkAddress() {
    return getField(Field.Work_Address);
  }

  public String getWorkCity() {
    return getField(Field.Work_City);
  }

  public String getWorkState() {
    return getField(Field.Work_State);
  }

  public String getWorkPostcode() {
    return getField(Field.Work_Postcode);
  }

  public String getGoogleTalk() {
    return getField(Field.Google_Talk);
  }

  public String getAnniversary() {
    return getField(Field.Anniversary);
  }

  public String getBirthday() {
    return getField(Field.Birthday);
  }

  public String getNotes() {
    return getField(Field.Notes);
  }

  public String getHomeCountry() {
    return getField(Field.Home_Country);
  }

  public String getWorkCountry() {
    return getField(Field.Work_Country);
  }

  public String getJobTitle() {
    return getField(Field.Job_Title);
  }

  public String getWebpage() {
    return getField(Field.Webpage);
  }

  public String getHomePostcode() {
    return getField(Field.Home_Postcode);
  }

  public String getHomeState() {
    return getField(Field.Home_State);
  }

  public String getHomeCity() {
    return getField(Field.Home_City);
  }

  public String getUser() {
    return getField(Field.User);
  }

  public String getHomeAddress() {
    return getField(Field.Home_Address);
  }

  public String getCategories() {
    return getField(Field.Categories);
  }

  public String getTitle() {
    return getField(Field.Title);
  }
}
