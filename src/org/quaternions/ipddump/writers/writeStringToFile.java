package org.quaternions.ipddump.writers;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jimmys Daskalakis
 */
public class writeStringToFile {

    /**
     * Method description
     *
     *
     * @param pathfile
     * @param content
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeStringToFile(String pathfile, String content) throws IOException {
        BufferedWriter fos;
        String         strFilePath=pathfile;
        String         strContent =content;

        try {

//          System.out.println(strFilePath);
            File out=new File(strFilePath);

            if (!out.exists()) {
                out.createNewFile();
                fos=new BufferedWriter(new FileWriter(out));
            } else {
                fos=new BufferedWriter(new FileWriter(out));
            }

//          FileOutputStream fos = new FileOutputStream(strFilePath);
            fos.write(strContent);

//          fos.write(strContent.getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : "+ex);
        } catch (IOException ioe) {
            System.out.println("IOException : "+ioe);
        }
    }
}
