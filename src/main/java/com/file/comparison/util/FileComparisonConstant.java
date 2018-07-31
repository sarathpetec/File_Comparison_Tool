package com.file.comparison.util;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FileComparisonConstant {

  static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
  static LocalDateTime now = LocalDateTime.now();
  public static URL FIELD_MAPPING_FILE;
  public static URL MASTER_FILE;
  public static URL FILE_1;
  public static String COMMON_REPORT_OUTPUT_FILE ="src\\main\\resources\\Comparison_Common_Report_"+dtf.format(now)+".csv";
  public static String COMPARISON_RESULT_OUTPUT_FILE ="src\\main\\resources\\Comparison_Result_Report_"+dtf.format(now)+".csv";
  public static String NOT_EQUAL_RECORDS_OUTPUT_FILE ="src\\main\\resources\\Not_Equal_Records_"+dtf.format(now)+".csv";
  public static String MISSING_RECORDS_OUTPUT_FILE ="src\\main\\resources\\Missing_Records_"+dtf.format(now)+".csv";


  public FileComparisonConstant() {
    FIELD_MAPPING_FILE = this.getClass().getResource("/Field_Mapper.txt");
    MASTER_FILE = this.getClass().getResource("/Master_File.txt");
    FILE_1 = this.getClass().getResource("/Sub_File_1.txt");
  }

  public static Map<Object, Object> executionContext = new HashMap<>();
  public static final short TOTAL_NUMBER_OF_FILE = 2;
  public static final String FILE_DELIMITER = ",";
  public static final String FILE_MAPPER_SEPERATOR = "\\|";
  public static final String FILE_MAPPER_COMBINATION = "&";
  public static final String FILE_MAPPER_IF_CASE = "if";
  public static final String FILE_MAPPER_ELSE_CASE = "else";
  public static final String FILE_MAPPER_OPENING_BRACKET = "(";
  public static final String FILE_MAPPER_CLOSING_BRACKET = ")";
  public static final String FILE_ENCAPSULATE = "$$";
  public static final Boolean CONSIDER_CASE_IN_CHECKING = false;
  public static final String NOT_EQUAL_REPERSENTING_STRING = "C";
  public static final char DEFAULT_SEPARATOR_FOR_CSV = ',';
  public static final String NO_MAPPPING_WITH_OTHER_FILE = "NO_MAPPPING_WITH_OTHER_FILE";



}
