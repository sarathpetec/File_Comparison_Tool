package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.ComparisonFile;
import com.file.comparison.dto.MasterFile;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FieldMapper;
import com.file.comparison.util.FileComparisonCommonUtil;
import com.file.comparison.util.FileComparisonConstant;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.file.comparison.util.FileComparisonConstant.*;

public class FileExtractionProcessImpl implements FileComparisonManager {

  BufferedReader masterBufferedReader = null;
  BufferedReader subFileBufferedReader = null;
  BufferedReader fileMapperBufferedReader = null;

  @Override
  public void init() {
    masterBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(MASTER_FILE_BUFFERED_READER);
    subFileBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(FILE_1_BUFFERED_READER);
    fileMapperBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(FIELD_MAPPER_BUFFERED_READER);
  }

  @Override
  public void preProcess() throws Exception {

    List<String[]> allFileColumnNameList = new LinkedList<>();
    String[] masterColumnNames = masterBufferedReader.readLine().split(FileComparisonConstant.FILE_DELIMITER);
    String[] subFileColumnNames = subFileBufferedReader.readLine().split(FileComparisonConstant.FILE_DELIMITER);
    FileComparisonCommonUtil.addToExecutionContext(MASTER_COLUMN_NAME_ARRAY, masterColumnNames);
    FileComparisonCommonUtil.addToExecutionContext(SUB_FILE_COLUMN_NAME_ARRAY, subFileColumnNames);
    allFileColumnNameList.add(masterColumnNames);
    allFileColumnNameList.add(subFileColumnNames);
    FileComparisonCommonUtil.addToExecutionContext(ALL_FILE_COLUMN_NAME_LIST, allFileColumnNameList);
    System.out.println("masterColumnNames: " + masterColumnNames.length);
    System.out.println("subFileColumnNames: " + subFileColumnNames.length);

    /* -------------------------------------------------------------------------- */
    FileComparisonCommonUtil.fileMapperToObject(); //Field Mapper Extraction from resources/Field_Mapper.txt
    initFieldMappingForComparison();
    extractMasterDataFrom_BR_ToObj();
    extractSubFilesDataFrom_BR_ToObj();

  }

  @Override
  public void postProcess() {

  }


  private void initFieldMappingForComparison() {
    FieldMapper fieldMapper = (FieldMapper) FileComparisonCommonUtil.getValueFromExeContext(FIELD_MAPPER_OBJECT);
    fieldMapper = FileComparisonCommonUtil.objectToIndexLocationForFieldMapper(fieldMapper);
    FileComparisonCommonUtil.addToExecutionContext("FIELD_MAPPER_OBJECT", fieldMapper);

  }


  private void extractMasterDataFrom_BR_ToObj() throws Exception {

    //String[] master_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("MASTER_COLUMN_NAME_ARRAY");
    //MasterFile masterFile = new MasterFile((FileComparisonCommonUtil.countLinesNew(FileComparisonConstant.MASTER_FILE) - 1), master_column_name_arrays.length);
    MasterFile masterFile = new MasterFile();
    masterBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(MASTER_FILE_BUFFERED_READER);
    String line;
    Map<String[], String[]> cellValues = masterFile.getCellValues();
    //int masterUniqueField = 0; boolean multipleUniqueFields = false;
    System.out.println("masterFile.getComparisonUniqueFieldId().length: "+masterFile.getComparisonUniqueFieldId().length);
    /*if(masterFile.getComparisonUniqueFieldId().length == 1){
      masterUniqueField = masterFile.getComparisonUniqueFieldId()[0];
    } else {
      multipleUniqueFields = true;
    }*/
    String[] key;
    while (Objects.nonNull((line = masterBufferedReader.readLine()))) {
      key = new String[masterFile.getMasterFileUniqueFieldId_Length()];
      String[] lineArray = line.split(FileComparisonConstant.FILE_DELIMITER, -1);
      for(int i=0;i<masterFile.getMasterFileUniqueFieldId_Length();i++){
        key[i] = lineArray[masterFile.getMasterFileUniqueFieldId()[i]];
      }
      cellValues.put(key, lineArray);
    }
    masterFile.setCellValues(cellValues);
    System.out.println("Master file size:"+masterFile.getCellValues().size());
    FileComparisonCommonUtil.addToExecutionContext("MASTER_FILE_OBJECT", masterFile);
//   Arrays.stream(masterFile.getCellValues()).forEach(strings -> System.out.println("Dataa: "+strings[12]+","+strings[1]+","+strings[4]));
  }

  private void extractSubFilesDataFrom_BR_ToObj() throws Exception {
    for (int i = 0; i < (FileComparisonConstant.TOTAL_NUMBER_OF_FILE - 1); i++) {
      subFileBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(FILE_1_BUFFERED_READER);
      String[] sub_file_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("SUB_FILE_COLUMN_NAME_ARRAY");
      //ComparisonFile comparisonFile = new ComparisonFile((FileComparisonCommonUtil.countLinesNew(FileComparisonConstant.FILE_1) - 1), sub_file_column_name_arrays.length);
      ComparisonFile comparisonFile = new ComparisonFile();
      String line;
      Map<String, String[]> cellValues = comparisonFile.getCellValues();
      /*int subFileUniqueField = 0; boolean multipleUniqueFields = false;
      if(comparisonFile.getComparisonUniqueFieldId().length == 1){
        subFileUniqueField = comparisonFile.getComparisonUniqueFieldId()[0];
      } else {
        multipleUniqueFields = true;
      }*/
      String key;
      while (Objects.nonNull((line = subFileBufferedReader.readLine()))) {
        //key = new String[comparisonFile.getComparisonUniqueFieldId().length];
        key="";
        String[] lineArray = line.split(FileComparisonConstant.FILE_DELIMITER, -1);
        for(int j=0;j<comparisonFile.getComparisonUniqueFieldId().length;j++){
          key = key + lineArray[comparisonFile.getComparisonFileUniqueFieldId()[j]];
        }
        cellValues.put(key, lineArray);
      }
      comparisonFile.setCellValues(cellValues);
      System.out.println("Comparison file size:"+comparisonFile.getSize());
      FileComparisonCommonUtil.addToExecutionContext("COMPARISON_FILE_OBJECT", comparisonFile);
      //Arrays.stream(comparisonFile.getCellValues()).forEach(strings -> System.out.println("Dataa: " + strings[0] + "," + strings[1] + "," + strings[4]));

    }
  }


}
