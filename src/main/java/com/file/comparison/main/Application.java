package com.file.comparison.main;

import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.process_manager_impl.FileComparisonProcessImpl;
import com.file.comparison.process_manager_impl.FileExtractionProcessImpl;
import com.file.comparison.process_manager_impl.FileLoadProcessImpl;
import com.file.comparison.process_manager_impl.ReportGenerateProcessImpl;
import com.file.comparison.util.FileComparisonConstant;
import jdk.nashorn.api.scripting.URLReader;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Application {

  public static void main(String ags[]) throws Exception {
    long startTime = System.currentTimeMillis();
    FileComparisonConstant fileComparisonConstant = new FileComparisonConstant();
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

    BufferedReader masterBufferedReader = (BufferedReader) executionContext.get("MASTER_FILE_BUFFERED_READER");
    BufferedReader subFileBufferedReader = (BufferedReader) executionContext.get("FILE_1_BUFFERED_READER");
    masterBufferedReader.close();
    subFileBufferedReader.close();
    long endTime = System.currentTimeMillis();
    System.out.println("Throughput: "+((endTime - startTime)/1000)+" sec");
    /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });*/

  }

  private static void readText() {
    // The name of the file to open.


    // This will reference one line at a time
    String line = null;

    try (BufferedReader bufferedReader = new BufferedReader(new URLReader(FileComparisonConstant.FILE_1));) {

      while (Objects.nonNull((line = bufferedReader.readLine()))) {
        System.out.println(line);
      }
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to open file '" + FileComparisonConstant.FILE_1 + "'");
    } catch (IOException ex) {
      System.out.println("Error reading file '"+ FileComparisonConstant.FILE_1 + "'");
    }
  }


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
