package ipddump.tools;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.CallLog;
import ipddump.data.Records.HistoryRecord;
import ipddump.data.Records.SMSMessage;

import ipddump.tools.writers.CallLogsWriters;
import ipddump.tools.writers.SmsWriters;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Jimmys Daskalakis, jimdaskalakis01@gmail.com
 */
public class HistoryMaker {
    private final InteractivePagerBackup database;
    private Finder                       finder;
    StringBuilder                        str         =new StringBuilder();
    private boolean                      resolveNames=false;
    private SmsWriters                   smsWriters;
    private CallLogsWriters              callLogsWriters;
    private List<HistoryRecord>          historyRecords;
    int[]                                tempArraySMS=new int[0];
    int[]                                tempArrayCalls=new int[0];

    //~--- constructors -------------------------------------------------------

    public HistoryMaker(InteractivePagerBackup database, boolean resolveNames) {
        this.database    =database;
        finder           =new Finder(database);
        this.resolveNames=resolveNames;
        smsWriters       =new SmsWriters(database, resolveNames);
        callLogsWriters  =new CallLogsWriters(database);
    }

    //~--- get methods --------------------------------------------------------

    public int[] getSelectedSMS() {
        return tempArraySMS;
    }
    public int[] getSelectedCallLogs() {
        return tempArrayCalls;
    }
    //~--- methods ------------------------------------------------------------

    public String makeSMSandCallHistoryTXT(int[] SelectedContacts) {
        str           =new StringBuilder();
        historyRecords=new LinkedList<HistoryRecord>();

        int[]         allSMS  =finder.findSmsByContacts(SelectedContacts);
        int[]         allCalls=finder.findCallLogsByContacts(SelectedContacts);
        int           max     =allSMS.length;
        Vector<int[]> vecSMS  =new Vector<int[]>();
        Vector<int[]> vecCalls  =new Vector<int[]>();

        if (allSMS.length<allCalls.length) {
            max=allCalls.length;
        }
        vecSMS.add(allSMS);
        vecCalls.add(allCalls);
        for (int i=0; i<max; i++) {
            CallLog    calllog    =null;
            SMSMessage sms        =null;
            Date       SMSdate    =new Date(0);
            Date       CalLogldate=new Date(0);

            if (i<allSMS.length) {
                sms=finder.findSpesificSms(allSMS[i]);

                if (sms!=null) {
                    if (sms.wasSent()) {
                        SMSdate=sms.getSent();
                    } else {
                        SMSdate=sms.getReceived();
                    }

                    HistoryRecord smsrec=new HistoryRecord(SMSdate, smsWriters.toPlainText(new int[] {allSMS[i]}));

                    historyRecords.add(smsrec);
                }
            }

            if (i<allCalls.length) {
                calllog=finder.findSpesificCallLog(allCalls[i]);

                if (calllog!=null) {
                    CalLogldate=calllog.getDate();

                    HistoryRecord callrec=new HistoryRecord(CalLogldate,
                                              callLogsWriters.toPlainText(new int[] {allCalls[i]}));

                    historyRecords.add(callrec);
                }
            }
        }

        Collections.sort(historyRecords);

        for (HistoryRecord his : historyRecords) {
            str.append(his.getText());
        }

        int temp=0;

        for (int i=0; i<vecSMS.size(); i++) {
            temp+=vecSMS.get(i).length;
        }

        tempArraySMS=new int[temp];
        temp     =0;

        for (int i=0; i<vecSMS.size(); i++) {

//          if (temp>=smsMessages.length)break;
            for (int j=temp; j<vecSMS.get(i).length+temp; j++) {
                tempArraySMS[j]=vecSMS.get(i)[j-temp];
            }

            temp+=vecSMS.get(i).length;
        }
                temp=0;

        for (int i=0; i<vecCalls.size(); i++) {
            temp+=vecCalls.get(i).length;
        }

        tempArrayCalls=new int[temp];
        temp     =0;

        for (int i=0; i<vecCalls.size(); i++) {

//          if (temp>=smsMessages.length)break;
            for (int j=temp; j<vecCalls.get(i).length+temp; j++) {
                tempArrayCalls[j]=vecCalls.get(i)[j-temp];
            }

            temp+=vecCalls.get(i).length;
        }

        return str.toString();
    }

    public String makeSMSandCallHistoryXML(int[] SelectedContacts) {
        str           =new StringBuilder();
        historyRecords=new LinkedList<HistoryRecord>();

        int[] allSMS  =finder.findSmsByContacts(SelectedContacts);
        int[] allCalls=finder.findCallLogsByContacts(SelectedContacts);
        int   max     =allSMS.length;

        if (allSMS.length<allCalls.length) {
            max=allCalls.length;
        }

        for (int i=0; i<max; i++) {
            CallLog    calllog    =null;
            SMSMessage sms        =null;
            Date       SMSdate    =new Date(0);
            Date       CalLogldate=new Date(0);

            if (i<allSMS.length) {
                sms=finder.findSpesificSms(allSMS[i]);

                if (sms!=null) {
                    if (sms.wasSent()) {
                        SMSdate=sms.getSent();
                    } else {
                        SMSdate=sms.getReceived();
                    }

                    HistoryRecord smsrec=new HistoryRecord(SMSdate,
                                             smsWriters.toXML(new int[] {
                                                 allSMS[i]}).getRootElement().element("SmsMessage").asXML()+"\n\n");

                    historyRecords.add(smsrec);
                }
            }

            if (i<allCalls.length) {
                calllog=finder.findSpesificCallLog(allCalls[i]);

                if (calllog!=null) {
                    CalLogldate=calllog.getDate();

                    HistoryRecord callrec=new HistoryRecord(CalLogldate,
                                              callLogsWriters.toXML(new int[] {
                                                  allCalls[i]}).getRootElement().element("CallLog").asXML()+"\n");

                    historyRecords.add(callrec);
                }
            }
        }

        Collections.sort(historyRecords);

        if ((allCalls.length>0) || (allSMS.length>0)) {
            str.append("<?xml version=\"1.0\" encoding=\"UNICODE\"?>\n");
            str.append("<History TotalSMS=\""+allSMS.length+"\" ");
            str.append("<TotalCallLogs=\""+allCalls.length+"\"\n");
        }

        for (HistoryRecord his : historyRecords) {
            str.append(his.getText());
        }

        if ((allCalls.length>0) || (allSMS.length>0)) {
            str.append("</History>");
        }

        return str.toString();
    }

    public String seeTheSMSCoversationTXT(int[] smsMessages) {
        str           =new StringBuilder();
        historyRecords=new LinkedList<HistoryRecord>();

        String[]      PhoneNumber=new String[smsMessages.length];
        SMSMessage    sms        =null;
        Date          SMSdate    =new Date(0);
        int[]         allSMS     =new int[0];
        Vector<int[]> vecSMS     =new Vector<int[]>();

        for (int i=0; i<smsMessages.length; i++) {
            sms           =finder.findSpesificSms(smsMessages[i]);
            PhoneNumber[i]=sms.getNumber().replaceAll(" ", "").replaceAll("!", "");
        }

        for (int i=0; i<PhoneNumber.length; i++) {
            if (PhoneNumber[i]==null) {
                continue;
            }

            // if you find dublicate phone numbers, skip them
            for (int k=i+1; k<PhoneNumber.length; k++) {
                if (PhoneNumber[i].equals(PhoneNumber[k])) {
                    PhoneNumber[k]=null;
                }
            }

            allSMS=finder.findSmsByNumber(PhoneNumber[i]);
            vecSMS.add(allSMS);

            if (allSMS.length==0) {
                continue;
            }

            for (int j=0; j<allSMS.length; j++) {
                sms=finder.findSpesificSms(allSMS[j]);

                if (sms!=null) {
                    if (sms.wasSent()) {
                        SMSdate=sms.getSent();
                    } else {
                        SMSdate=sms.getReceived();
                    }

                    HistoryRecord callrec=new HistoryRecord(SMSdate, smsWriters.toPlainText(new int[] {allSMS[j]}));

                    historyRecords.add(callrec);
                }
            }
        }

        Collections.sort(historyRecords);

        for (HistoryRecord his : historyRecords) {
            str.append(his.getText());
        }

        int temp=0;

        for (int i=0; i<vecSMS.size(); i++) {
            temp+=vecSMS.get(i).length;
        }

        tempArraySMS=new int[temp];
        temp     =0;

        for (int i=0; i<vecSMS.size(); i++) {

//          if (temp>=smsMessages.length)break;
            for (int j=temp; j<vecSMS.get(i).length+temp; j++) {
                tempArraySMS[j]=vecSMS.get(i)[j-temp];
            }

            temp+=vecSMS.get(i).length;
        }

        return str.toString();
    }

    public String seeTheSMSCoversationXML(int[] smsMessages) {
        str           =new StringBuilder();
        historyRecords=new LinkedList<HistoryRecord>();

        int           totals     =0;
        String[]      PhoneNumber=new String[smsMessages.length];
        SMSMessage    sms        =null;
        Date          SMSdate    =new Date(0);
        int[]         allSMS     =new int[0];
        Vector<int[]> vecSMS     =new Vector<int[]>();

        for (int i=0; i<smsMessages.length; i++) {
            sms           =finder.findSpesificSms(smsMessages[i]);
            PhoneNumber[i]=sms.getNumber().replaceAll(" ", "").replaceAll("!", "");
        }

        for (int i=0; i<PhoneNumber.length; i++) {
            if (PhoneNumber[i]==null) {
                continue;
            }

            // if you find dublicate phone numbers, skip them
            for (int k=i+1; k<PhoneNumber.length; k++) {
                if (PhoneNumber[i].equals(PhoneNumber[k])) {
                    PhoneNumber[k]=null;
                }
            }

            allSMS=finder.findSmsByNumber(PhoneNumber[i]);
            vecSMS.add(allSMS);
            totals+=allSMS.length;

            if (allSMS.length==0) {
                continue;
            }

            for (int j=0; j<allSMS.length; j++) {
                sms=finder.findSpesificSms(allSMS[j]);

                if (sms!=null) {
                    if (sms.wasSent()) {
                        SMSdate=sms.getSent();
                    } else {
                        SMSdate=sms.getReceived();
                    }

                    HistoryRecord callrec=new HistoryRecord(SMSdate,
                                              smsWriters.toXML(new int[] {
                                                  allSMS[j]}).getRootElement().element("SmsMessage").asXML()+"\n");

                    historyRecords.add(callrec);
                }
            }
        }

        Collections.sort(historyRecords);

        if ((allSMS.length>0)) {
            str.append("<?xml version=\"1.0\" encoding=\"UNICODE\"?>\n");
            str.append("<History TotalSMS=\""+totals+"\"\n");
        }

        for (HistoryRecord his : historyRecords) {
            str.append(his.getText());
        }

        if ((allSMS.length>0)) {
            str.append("</History>");
        }

        int temp=0;

        for (int i=0; i<vecSMS.size(); i++) {
            temp+=vecSMS.get(i).length;
        }

        tempArraySMS=new int[temp];
        temp     =0;

        for (int i=0; i<vecSMS.size(); i++) {

//          if (temp>=smsMessages.length)break;
            for (int j=temp; j<vecSMS.get(i).length+temp; j++) {
                tempArraySMS[j]=vecSMS.get(i)[j-temp];
            }

            temp+=vecSMS.get(i).length;
        }

        return str.toString();
    }
}
