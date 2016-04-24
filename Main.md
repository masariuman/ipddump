# ipddump #

ipddump is a project aimed at providing a library for reading the [Inter@ctive Pager Backup](http://na.blackberry.com/eng/devjournals/resources/journals/jan_2006/ipd_file_format.jsp) (IPD) file format commonly used by RIM on the [Blackberry](http://blackberry.com/) smartphones.

## History ##

I started ipddump because I wanted to pull my SMS messages off my phone and back them up in an open format.  But there are no good free solutions out there.  The only ones I've found are [SMS Borer](http://www.blackberryforums.com/aftermarket-software/38695-sms_borer-export-sms-data-backup-file-ipd.html) which has significant limitations, and [ABC Amber](http://www.processtext.com/abcblackberry.html) which has many features, but is not free.

As I was working on 0.1.0 I realized that it would make a lot of sense and be useful to create a library to read in an IPD, in addition to the immediate goals of dumping SMS message.  So that's what this project aims to provide with 1.0.0, a complete library that can read any type of record in an IPD.

## Additional Information ##
  * FeatureRequests
  * ReleasePage