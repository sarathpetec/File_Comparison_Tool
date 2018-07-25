package com.file.comparison.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileComparisonConstant {

  public static URL FIELD_MAPPING_FILE;
  public static URL MASTER_FILE;
  public static URL FILE_1;


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
  public static final String FILE_ENCAPSULATE = "$";



}
