package com.file.comparison.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FileComparisonConstant {

  static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
  static LocalDateTime now = LocalDateTime.now();
  public static String FIELD_MAPPING_FILE = "/Field_Mapper.txt";
  public static String MASTER_FILE = "/Master_File.txt";
  public static String FILE_1 = "/Sub_File_1.txt";
  public static String COMMON_REPORT_OUTPUT_FILE ="src\\main\\resources\\Comparison_Common_Report_"+dtf.format(now)+".csv";
  public static String COMPARISON_RESULT_OUTPUT_FILE ="src\\main\\resources\\Comparison_Result_Report_"+dtf.format(now)+".csv";
  public static String NOT_EQUAL_RECORDS_OUTPUT_FILE ="src\\main\\resources\\Not_Equal_Records_"+dtf.format(now)+".csv";
  public static String MISSING_RECORDS_OUTPUT_FILE ="src\\main\\resources\\Missing_Records_"+dtf.format(now)+".csv";


/*  public FileComparisonConstant() {
    FIELD_MAPPING_FILE = this.getClass().getResource("/Field_Mapper.txt");
    MASTER_FILE = this.getClass().getResource();
    FILE_1 = this.getClass().getResource();
  }*/

  public static Map<Object, Object> executionContext = new HashMap<>();
  public static final int TOTAL_NUMBER_OF_FILE = 2;
  public static final String FILE_DELIMITER = ",";
  public static final String FILE_MAPPER_SEPERATOR = "\\|";
  public static final String FILE_MAPPER_COMBINATION = "&";
  public static final String FILE_MAPPER_IF_CASE = "if";
  public static final String FILE_MAPPER_ELSE_CASE = "else";
  public static final String FILE_MAPPER_OPENING_BRACKET = "(";
  public static final String FILE_MAPPER_CLOSING_BRACKET = ")";
  public static final String FILE_ENCAPSULATE = "$$";
  public static final Boolean CONSIDER_CASE_IN_CHECKING = false;
  public static final char DEFAULT_SEPARATOR_FOR_CSV = ',';
  public static final String NO_MAPPING_WITH_OTHER_FILE = "NO_MAPPING_WITH_OTHER_FILE";

  public static final int[] MASTER_FILE_UNIQUE_FIELD_ID = {0,0}; //Zero'th index value should be same as comparisonUniqueFieldId
  public static final int[] COMPARISON_UNIQUE_FIELD_ID = {0};

  public static final String MASTER_FILE_BUFFERED_READER = "MASTER_FILE_BUFFERED_READER";
  public static final String FILE_1_BUFFERED_READER = "FILE_1_BUFFERED_READER";
  public static final String FIELD_MAPPER_BUFFERED_READER = "FIELD_MAPPER_BUFFERED_READER";
  public static final String MASTER_COLUMN_NAME_ARRAY = "MASTER_COLUMN_NAME_ARRAY";
  public static final String SUB_FILE_COLUMN_NAME_ARRAY = "SUB_FILE_COLUMN_NAME_ARRAY";
  public static final String ALL_FILE_COLUMN_NAME_LIST = "ALL_FILE_COLUMN_NAME_LIST";
  public static final String FIELD_MAPPER_OBJECT = "FIELD_MAPPER_OBJECT";
  public static final String COMPARISON_REPORT_DTO = "COMPARISON_REPORT_DTO";
  public static final String MASTER_FILE_OBJECT = "MASTER_FILE_OBJECT";
  public static final String CONDITION_TRUE = "true";
  public static final String CONDITION_FALSE = "false";


  public static final String NULL = "null" ;
  public static final String EMPTY_STRING = "";



}
