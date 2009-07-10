package org.quaternions.ipddump.data;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 * @created Jun 20, 2009
 */
public class Task extends Record implements Comparable<Task> {
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
  public Task(int dbID, int dbVersion, int uid, int recordLength) {
    super(dbID, dbVersion, uid, recordLength);
  }

  @Override
  public void addField(int type, char[] data) {
    switch (type) {
    case 1:
      // ignore, always 't'
      break;

    case 2:
      fields.put("Name", makeString(data));
      break;

    case 3:
      fields.put("Notes", makeString(data));
      break;

    // Status
    case 9:
      int status = data[0] << 0;
      status |= data[1] << 8;
      status |= data[2] << 16;
      status |= data[3] << 24;
      switch (status) {
      case 0:
        fields.put("Status", "Not Started");
        break;

      case 1:
        fields.put("Status", "In Progress");
        break;

      case 2:
        fields.put("Status", "Completed");
        break;

      case 3:
        fields.put("Status", "Waiting");
        break;

      case 4:
        fields.put("Status", "Deferred");
        break;
      }
      break;

    // Priority
    case 14:
      int priority = data[0] << 0;
      priority |= data[1] << 8;
      priority |= data[2] << 16;
      priority |= data[3] << 24;
      switch (priority) {
      case 0:
        fields.put("Priority", "Low");
        break;

      case 1:
        fields.put("Priority", "Normal");
        break;

      case 2:
        fields.put("Priority", "High");
        break;
      }
      break;

    // Timezone
    case 16:
      // ignore for now
      break;

    default:
      System.out.print(type + ": ");
      for (char c : data) {
        System.out.format("%1h", c);
      }
      System.out.println("");
      break;

    case 17:
      String existing = fields.get("Categories");
      if (existing == null) {
        existing = "";
      } else {
        existing += ",";
      }

      fields.put("Categories", existing + makeString(data));
      break;
    }
  }

  @Override
  public int compareTo(Task o) {
    return 0;
  }

  @Override
  public String toString() {
    return fields.toString();
  }

  protected String makeString(char[] data) {
    String str = new String(data);
    return str.substring(0, str.length() - 1);
  }
}