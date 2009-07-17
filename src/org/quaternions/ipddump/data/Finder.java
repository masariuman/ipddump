/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.quaternions.ipddump.data;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 */
public class Finder {
    private final InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public Finder(InteractivePagerBackup database) {
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
    public String findContactByPhoneNumber(String phNumber) {
        String phoneNumber=phNumber.replaceAll(" ", "");

        for (Contact record : database.contacts()) {
            String name=record.getName();

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
            } else if (phoneNumber==null) {
                return "";
            }
        }

        return phNumber;
    }

    public int[] findSmsByContacts(int[] selectedContacts) {
        int RecordIndex=0;
        int j          =0;
        int i          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                for (SMSMessage recordsms : database.smsRecords()) {
                    String SMSnumber=recordsms.getNumber();

                    if (SMSnumber.replaceAll(" ", "").equals(record.getMobilePhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getPager().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getHomePhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getWorkPhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getOtherNumber().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getPIN().replaceAll(" ", ""))) {
                        i++;
                    }
                }

                j++;

                if (j>=selectedContacts.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        int selectedSMS[]=new int[i];
        int pointer      =0;

        RecordIndex=0;
        j          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                int smsindex=0;

                for (SMSMessage recordsms : database.smsRecords()) {
                    String SMSnumber=recordsms.getNumber();

                    if (SMSnumber.replaceAll(" ", "").equals(record.getMobilePhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getPager().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getHomePhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getWorkPhone().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getOtherNumber().replaceAll(" ", ""))
                            || SMSnumber.replaceAll(" ", "").equals(record.getPIN().replaceAll(" ", ""))) {
                        selectedSMS[pointer++]=smsindex;
                    }

                    smsindex++;
                }

                j++;

                if (j>=selectedContacts.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return selectedSMS;
    }

    public String findTimeZoneByID(String idNumber) {
        for (BBTimeZone record : database.timeZones()) {
            String id=record.getTimeZoneID();

            if (idNumber.equals(id)) {
                return record.getPlaceName();
            }
        }

        return "TimeZ db not present "+idNumber;
    }
}
