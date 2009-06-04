package gui;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jimmys Daskalakis
 */
public class writeBytesToFile {

  public static void  writeBytes2File(String pathfile, String content) throws FileNotFoundException, IOException
  {
      BufferedWriter fos;
String strFilePath = pathfile;
String strContent = content;



try

{
System.out.println(strFilePath);



File out = new File(strFilePath);
if (!out.exists()) {
                out.createNewFile();
                 fos = new BufferedWriter(new FileWriter(out));


            } else {
                fos = new BufferedWriter(new FileWriter(out));
            }

//FileOutputStream fos = new FileOutputStream(strFilePath);





/*
#
  * To write byte array to a file, use
#
  * void write(byte[] bArray) method of Java FileOutputStream class.
#
  *
#
  * This method writes given byte array to a file.
#
  */



fos.write(strContent);
//fos.write(strContent.getBytes());



/*
#
  * Close FileOutputStream using,
#
  * void close() method of Java FileOutputStream class.
#
  *
#
  */



fos.close();



}

catch(FileNotFoundException ex)

{

System.out.println("FileNotFoundException : " + ex);

}

catch(IOException ioe)

{

System.out.println("IOException : " + ioe);

}



}
}
