/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.data;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
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
            phoneNumber=phoneNumber.replaceAll(" ", "");
             if (phoneNumber.equalsIgnoreCase(record.getMobilePhone().replaceAll(" ", ""))) {
                return name+" (M)";
            } else if (phoneNumber.equalsIgnoreCase(record.getHomePhone().replaceAll(" ", ""))) {
                return name+" (H)";
            } else if (phoneNumber.equalsIgnoreCase(record.getWorkPhone().replaceAll(" ", ""))) {
                return name+" (W)";
            } else if (phoneNumber.equalsIgnoreCase(record.getOtherNumber().replaceAll(" ", ""))) {
                return name+" (O)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPager().replaceAll(" ", ""))) {
                return name+" (P)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPIN().replaceAll(" ", ""))) {
                return name+" (Pin)";
            } else if (phoneNumber == null) {
              return "";
            }
        }

        return phoneNumber;
    }
}
