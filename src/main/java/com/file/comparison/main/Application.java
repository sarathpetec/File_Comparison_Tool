package com.file.comparison.main;

import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.process_manager_impl.FileComparisonProcessImpl;
import com.file.comparison.process_manager_impl.FileExtractionProcessImpl;
import com.file.comparison.process_manager_impl.FileLoadProcessImpl;
import com.file.comparison.process_manager_impl.ReportGenerateProcessImpl;
import com.file.comparison.util.FileComparisonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.Map;
import java.util.Objects;

import static com.file.comparison.util.FileComparisonConstant.*;

/**
 * The Application class is a the beginning of the file comparison component
 */
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);


  public static void main(String ags[]) throws Exception {
    long startTime = System.currentTimeMillis();
    FileComparisonManager fileLoadProcess = new FileLoadProcessImpl();
    FileComparisonManager FileExtractionProcess = new FileExtractionProcessImpl();
    FileComparisonManager FileComparisonProcess = new FileComparisonProcessImpl();
    FileComparisonManager ReportGenerateProcess = new ReportGenerateProcessImpl();


    Map<Object, Object> executionContext = FileComparisonConstant.executionContext;
    fileLoadProcess.init();
    fileLoadProcess.preProcess();
    FileExtractionProcess.init();
    FileExtractionProcess.preProcess();
    FileComparisonProcess.preProcess();
    ReportGenerateProcess.preProcess();

    BufferedReader masterBufferedReader = (BufferedReader) executionContext.get(MASTER_FILE_BUFFERED_READER);
    BufferedReader subFileBufferedReader = (BufferedReader) executionContext.get(FILE_1_BUFFERED_READER);
    masterBufferedReader.close();
    subFileBufferedReader.close();
    long endTime = System.currentTimeMillis();
    LOGGER.debug("Throughput: {}  sec",(endTime - startTime));
    /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });*/

  }

  /**
   * This method will help to read the file from the resourse folder.
   *
   * @return         void
   */
  private static void readText() {

    String line = null;
    try (InputStreamReader inputStreamReader =
        new InputStreamReader(Application.class.getResourceAsStream(FILE_1))) {
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      while (Objects.nonNull((line = bufferedReader.readLine()))) {
        LOGGER.debug(line);
      }
    } catch (FileNotFoundException ex) {
      LOGGER.debug("Unable to open file : {}" , FileComparisonConstant.FILE_1);
    } catch (IOException ex) {
      LOGGER.debug("Error reading file : {}" , FileComparisonConstant.FILE_1);
    }
  }

  /**
   * This is an ongoing task
   * createAndShowGUI method help to popup a small gui and help to browse different files.
   *
   * @return         the post-incremented value
   */
  private static void createAndShowGUI() {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    //Create and set up the window.
    JFrame frame = new JFrame("HelloWorldSwing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Add the ubiquitous "Hello World" label.
    JLabel label = new JLabel("Hello World");
    frame.getContentPane().add(label);
    //Display the window.
    frame.pack();
    frame.setSize(800, 800);
    frame.setVisible(true);
  }

}
