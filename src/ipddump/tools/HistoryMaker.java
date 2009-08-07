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

    //~--- constructors -------------------------------------------------------

    public HistoryMaker(InteractivePagerBackup database, boolean resolveNames) {
        this.database    =database;
        finder           =new Finder(database);
        this.resolveNames=resolveNames;
        smsWriters       =new SmsWriters(database, resolveNames);
        callLogsWriters  =new CallLogsWriters(database);
    }

    //~--- methods ------------------------------------------------------------

    public String makeHistory(int[] SelectedContacts) {
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

        return str.toString();
    }
}
