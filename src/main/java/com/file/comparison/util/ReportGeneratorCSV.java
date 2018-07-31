package com.file.comparison.util;

import java.io.IOException;
import java.io.Writer;

import static com.file.comparison.util.FileComparisonConstant.DEFAULT_SEPARATOR_FOR_CSV;

public class ReportGeneratorCSV {

  static StringBuffer sb = new StringBuffer();

  public static void writeLine(String[] values) {
    for (String data : values){
      sb.append(data).append(DEFAULT_SEPARATOR_FOR_CSV);
    }
    sb.replace(sb.length()-1, sb.length(),"");
    sb.append("\n");
  }

  public static void addNewLine() {
    sb.append("\n");
  }

  private static String followCVSformat(String value) {

    String result = value;
    if (result.contains("\"")) {
      result = result.replace("\"", "\"\"");
    }
    return result;

  }

  public static void writeToFile(Writer w) throws IOException {
    w.append(sb.toString());
  }

  public static void resetWriter() {
    sb = new StringBuffer();
  }

}
