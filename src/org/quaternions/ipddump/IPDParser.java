package org.quaternions.ipddump;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.quaternions.ipddump.data.InteractivePagerBackup;
import org.quaternions.ipddump.data.Record;

/**
 * Parse an IPD file and populate a {@link InteractivePagerBackup} with the
 * records.
 *
 * @author borkholder
 * @date Jan 13, 2008
 */
public class IPDParser {
  /**
   * The name/path of the file to parse.
   */
  protected final String fileName;

  /**
   * Specifies the state of the parser, that is, the current part of the IPD
   * that the parser is reading.
   *
   * @author borkholder
   * @date Jan 13, 2008
   */
  protected enum ReadingState {
    /**
     * The file identification header - "Inter@ctive Pager Backup/Restore File".
     */
    HEADER,

    /**
     * The character used to for line feeds in text fields - 0x0A.
     */
    LINEFEED,

    /**
     * The IPD file version - 0x02.
     */
    VERSION,

    /**
     * The number of databases in this file - 2 bytes in big endian.
     */
    DATABASECOUNT,

    /**
     * The byte that separates database names - 0x00.
     */
    DATABASENAMESEPARATOR,

    /**
     * The length of the next specified database name - 2 bytes in little
     * endian. The length includes the terminating null of the name string.
     */
    DATABASENAMELENGTH,

    /**
     * The name of the database - size as specified by the preceeding database
     * name length.
     */
    DATABASENAME,

    /**
     * The 0-based index of the database that contains this record - 2 bytes
     * little endian.
     */
    DATABASEID,

    /**
     * The length of the record in number of bytes that follow this value - 4
     * bytes little endian.
     */
    RECORDLENGTH,

    /**
     * The version of the database to which this record belongs - 1 byte.
     */
    RECORDDBEVERSION,

    /**
     * A handle of the record within the database (this is a increasing sequence
     * of integers with the IPD) - 2 bytes little endian.
     */
    DATABASERECORDHANDLE,

    /**
     * The unique ID of the record within the database - 4 bytes.
     */
    RECORDUNIQUEID,

    /**
     * The length of the field data in number of bytes - 2 bytes little endian.
     */
    FIELDLENGTH,

    /**
     * The type of the field within the record - 1 byte.
     */
    FIELDTYPE,

    /**
     * The field data - as long as is specified by the preceeding length.
     */
    FIELDDATA;
  }

  /**
   * Creates a new IPDParser that will parse the file at the given path.
   *
   * @param fileName The path of the file to parse
   */
  public IPDParser( String fileName ) {
    this.fileName = fileName;
  }

  /**
   * Parses the provided IPD file into an {@link InteractivePagerBackup}.
   *
   * @return A new InteractivePagerBackup representing the IPD file
   * @throws IOException Any error in reading the IPD file
   */
  public InteractivePagerBackup parse() throws IOException {
    // Temporary variables used in parsing
    char lineFeed = 0;
    int numberOfDatabases = 0;
    int dbNameLength = 0;
    int recordRead = 0;
    int recordDBVersion = 0;
    int databaseHandle = 0;
    int fieldLength = 0;
    int fieldType = 0;
    int dbID = 0;
    int recordLength = 0;
    int uid = 0;

    Record record = null;
    InteractivePagerBackup database = null;
    FileInputStream input = null;

    try {
      input = new FileInputStream( fileName );
      FileChannel fc = input.getChannel();

      // Start reading in the header state
      ReadingState state = ReadingState.HEADER;
      while ( fc.position() < fc.size() ) {
        switch ( state ) {
          case HEADER:
            for ( int i = 0; i < "Inter@ctive Pager Backup/Restore File".length(); i++ ) {
              input.read();
            }
            state = ReadingState.LINEFEED;
            break;

          case LINEFEED:
            lineFeed = (char) input.read();
            state = ReadingState.VERSION;
            break;

          case VERSION:
            database = new InteractivePagerBackup( input.read(), lineFeed );
            state = ReadingState.DATABASECOUNT;
            break;

          case DATABASECOUNT:
            numberOfDatabases = input.read() << 8;
            numberOfDatabases |= input.read();
            state = ReadingState.DATABASENAMESEPARATOR;
            break;

          // Just eat it because we know the terminating null will be in
          // the name anyway
          case DATABASENAMESEPARATOR:
            input.read();
            state = ReadingState.DATABASENAMELENGTH;
            break;

          case DATABASENAMELENGTH:
            dbNameLength = input.read();
            dbNameLength |= input.read() << 8;
            state = ReadingState.DATABASENAME;
            break;

          case DATABASENAME:
            StringBuffer buffer = new StringBuffer();
            // Read everything but the terminating null
            for ( int i = 0; i < dbNameLength - 1; i++ ) {
              buffer.append( (char) input.read() );
            }

            database.addDatabase( buffer.toString() );

            // Eat null/separator
            input.read();

            if ( database.databaseNames().size() < numberOfDatabases ) {
              state = ReadingState.DATABASENAMELENGTH;
            } else {
              state = ReadingState.DATABASEID;
            }
            break;

          case DATABASEID:
            dbID = input.read();
            dbID |= input.read() << 8;
            state = ReadingState.RECORDLENGTH;
            break;

          case RECORDLENGTH:
            recordLength = input.read();
            recordLength |= input.read() << 8;
            recordLength |= input.read() << 16;
            recordLength |= input.read() << 24;
            recordRead = 0;
            state = ReadingState.RECORDDBEVERSION;
            break;

          case RECORDDBEVERSION:
            recordRead++;
            recordDBVersion = input.read();
            state = ReadingState.DATABASERECORDHANDLE;
            break;

          case DATABASERECORDHANDLE:
            databaseHandle = input.read();
            databaseHandle |= input.read() << 8;
            recordRead += 2;
            state = ReadingState.RECORDUNIQUEID;
            break;

          case RECORDUNIQUEID:
            uid = input.read();
            uid |= input.read() << 8;
            uid |= input.read() << 16;
            uid |= input.read() << 24;
            recordRead += 4;
            record = database.createRecord( dbID, recordDBVersion, uid, recordLength );
            record.setRecordDBHandle( databaseHandle );
            state = ReadingState.FIELDLENGTH;
            break;

          case FIELDLENGTH:
            fieldLength = input.read();
            fieldLength |= input.read() << 8;
            recordRead += 2;
            state = ReadingState.FIELDTYPE;
            break;

          case FIELDTYPE:
            fieldType = input.read();
            recordRead++;
            state = ReadingState.FIELDDATA;
            break;

          case FIELDDATA:
            char[] dataBuffer = new char[ fieldLength ];
            for ( int i = 0; i < fieldLength; i++ ) {
              dataBuffer[ i ] = (char) input.read();
            }

            record.addField( fieldType, dataBuffer );
            recordRead += fieldLength;

            if ( recordRead < record.getLength() ) {
              state = ReadingState.FIELDLENGTH;
            } else {
              state = ReadingState.DATABASEID;
            }
            break;
        }
      }
    } finally {
      input.close();
    }

    database.organize();
    return database;
  }
}

