/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quaternions.ipddump.data;

//~--- non-JDK imports --------------------------------------------------------
import org.quaternions.ipddump.data.InteractivePagerBackup;

/**
 *
 * @author Jimmys Daskalakis
 */
public class ContactFinder {

    private InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------
    public ContactFinder(InteractivePagerBackup database) {
        this.database = database;
    }

    //~--- methods ------------------------------------------------------------
    /**
     *  You can find the contact via home
     *  work, mobile, pager or PIN number
     *
     *
     * @param phoneNumber
     *
     * @return
     */
    public String findContactByPhoneNumber(String phoneNumber) {
        for (Contact record : database.contacts()) {
            String Name = record.getName();
            String HomePhone = record.getHomePhone();
            String WorkPhone = record.getWorkPhone();
            String MobilePhone = record.getMobilePhone();
            String Pager = record.getPager();
            String PIN = record.getPIN();
            String OtherNumber = record.getOtherNumber();

            if (OtherNumber.equalsIgnoreCase(HomePhone) && !OtherNumber.equals("")) {
                return Name;
            } else if (HomePhone.equalsIgnoreCase(phoneNumber) && !HomePhone.equals("")) {
                return Name;
            } else if (WorkPhone.equalsIgnoreCase(phoneNumber) && !WorkPhone.equals("")) {
                return Name;
            } else if (MobilePhone.equalsIgnoreCase(phoneNumber) && !MobilePhone.equals("")) {
                return Name;
            } else if (Pager.equalsIgnoreCase(phoneNumber) && !Pager.equals("")) {
                return Name;
            } else if (PIN.equalsIgnoreCase(phoneNumber) && !PIN.equals("")) {
                return Name;
            }
        }

        return phoneNumber;
    }
}
