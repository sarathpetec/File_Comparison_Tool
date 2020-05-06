package com.file.comparison.process_manager_impl;

import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FileComparisonCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.file.comparison.util.FileComparisonConstant.*;

public class FileLoadProcessImpl implements FileComparisonManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileLoadProcessImpl.class);

  public FileLoadProcessImpl() {
  }

  @Override
  public void init() {
    try {
      BufferedReader subFileBufferedReader = new BufferedReader(new InputStreamReader(FileLoadProcessImpl.class.getResourceAsStream(FILE_1)));
      BufferedReader masterBufferedReader = new BufferedReader(new InputStreamReader(FileLoadProcessImpl.class.getResourceAsStream(MASTER_FILE)));
      BufferedReader fileMapperBufferedReader = new BufferedReader(new InputStreamReader(FileLoadProcessImpl.class.getResourceAsStream(FIELD_MAPPING_FILE)));
      FileComparisonCommonUtil.addToExecutionContext(MASTER_FILE_BUFFERED_READER, masterBufferedReader);
      FileComparisonCommonUtil.addToExecutionContext(FILE_1_BUFFERED_READER, subFileBufferedReader);
      FileComparisonCommonUtil.addToExecutionContext(FIELD_MAPPER_BUFFERED_READER, fileMapperBufferedReader);
    } catch (Exception ex) {
      System.out.println("Seems like file's are missing. Please check the resources folder...!! JVM Forcefully going to EXIT");
      ex.printStackTrace();
      System.exit(0);
    }
  }

  @Override
  public void preProcess() throws Exception {

  }

  @Override
  public void postProcess() throws Exception {

  }


}
