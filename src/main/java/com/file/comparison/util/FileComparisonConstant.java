package com.file.comparison.util;

import java.util.HashMap;
import java.util.Map;

public interface FileComparisonConstant {


  Map<Object, Object> executionContext = new HashMap<>();

  short TOTAL_NUMBER_OF_FILE = 2;
  String FIELD_MAPPING_FILE = "FieldMapper.txt";
  String MASTER_FILE = "Master_File.txt";
  String FILE_1 = "Sub_File_1.txt";

  String FILE_DELIMITER = ",";
  String FILE_MAPPER_SEPERATOR = "\\|";
  String FILE_MAPPER_COMBINATION = "&";
  String FILE_MAPPER_IF_CASE = "if";
  String FILE_MAPPER_ELSE_CASE = "else";
  String FILE_MAPPER_OPENING_BRACKET = "(";
  String FILE_MAPPER_CLOSING_BRACKET = ")";
  String FILE_ENCAPSULATE = "$";



}
