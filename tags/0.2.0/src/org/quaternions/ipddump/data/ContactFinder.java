/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.data;

/**
 *
 * @author Jimmys Daskalakis
 */
public class ContactFinder {
    private final InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public ContactFinder(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    /**
     *  You can find the contact via home
     *  work, mobile, pager or PIN number
     *
     *
     * @param phoneNumber
     *
     * @return The name of the contact if found, otherwise the phone number
     */
    public String findContactByPhoneNumber(String phoneNumber) {
        for (Contact record : database.contacts()) {
            String name       =record.getName();
             if (phoneNumber.equalsIgnoreCase(record.getMobilePhone())) {
                return name+" (M)";
            } else if (phoneNumber.equalsIgnoreCase(record.getHomePhone())) {
                return name+" (H)";
            } else if (phoneNumber.equalsIgnoreCase(record.getWorkPhone())) {
                return name+" (W)";
            } else if (phoneNumber.equalsIgnoreCase(record.getOtherNumber())) {
                return name+" (O)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPager())) {
                return name+" (P)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPIN())) {
                return name+" (Pin)";
            } else if (phoneNumber == null) {
              return "";
            }
        }

        return phoneNumber;
    }
}
