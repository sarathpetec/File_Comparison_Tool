package com.file.comparison.process_manager_impl;

import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FileComparisonCommonUtil;
import com.file.comparison.util.FileComparisonConstant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Objects;

public class FileLoadProcessImpl implements FileComparisonManager {

  public FileLoadProcessImpl() {
  }

  @Override
  public void init() throws Exception {
    BufferedReader subFileBufferedReader = new BufferedReader(new FileReader(FileComparisonConstant.FILE_1));
    BufferedReader masterBufferedReader = new BufferedReader(new FileReader(FileComparisonConstant.MASTER_FILE));
    BufferedReader fileMapperBufferedReader = new BufferedReader(new FileReader(FileComparisonConstant.FIELD_MAPPING_FILE));
    FileComparisonCommonUtil.addToExecutionContext("MASTER_FILE_BUFFERED_READER",masterBufferedReader);
    FileComparisonCommonUtil.addToExecutionContext("FILE_1_BUFFERED_READER",subFileBufferedReader);
    FileComparisonCommonUtil.addToExecutionContext("FIELD_MAPPER_BUFFERED_READER",fileMapperBufferedReader);

  }

  @Override
  public void preProcess() throws Exception {
    BufferedReader masterBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext("MASTER_FILE_BUFFERED_READER");
    BufferedReader subFileBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext("FILE_1_BUFFERED_READER");
    String line1;
    String line2;
    /*while (Objects.nonNull((line1 = masterBufferedReader.readLine()))) {
      System.out.println(line1);
    }
    System.out.println("*********************************************");
    while (Objects.nonNull((line2 = subFileBufferedReader.readLine()))) {
      System.out.println(line2);
    }*/
  }

  @Override
  public void postProcess() throws Exception {

  }


}
