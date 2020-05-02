package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.*;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FieldMapper;
import com.file.comparison.util.FileComparisonCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.file.comparison.util.FileComparisonCommonUtil.getValueFromExeContext;
import static com.file.comparison.util.FileComparisonConstant.*;

public class FileComparisonProcessImpl implements FileComparisonManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileComparisonProcessImpl.class);

  private static ArrayList<String> allFieldInSyncLst = new ArrayList<>();
  private static ArrayList<ComparisonDetailReport> comparisonDetailReports = new ArrayList<>();

  @Override
  public void init() throws Exception {
  }

  @Override
  public void preProcess() throws Exception {
    LOGGER.debug("Comparison Going to start");
    FieldMapper fieldMapper = (FieldMapper) getValueFromExeContext(FIELD_MAPPER_OBJECT);
    String[] master_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("MASTER_COLUMN_NAME_ARRAY");
    //Arrays.stream(master_column_name_arrays).forEach(s -> LOGGER.debug("master_column_name_arrays Value: {}",s));
    LinkedList<MasterColumnName> masterFieldsLinkedList = fieldMapper.getMasterFields();
    //LOGGER.debug("masterFieldsLinkedList.size(): {}",  masterFieldsLinkedList.size());
    //masterFieldsLinkedList.stream().forEach(masterColumnNames -> LOGGER.debug("masterColumnNames: {}", masterColumnNames));
    MasterFile masterFile = (MasterFile) getValueFromExeContext(MASTER_FILE_OBJECT);
    ComparisonFile comparisonFile = (ComparisonFile) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_FILE_OBJECT");
    MissingRecords missingRecords = new MissingRecords();
    NotEqualRecords notEqualRecords = new NotEqualRecords();
    List<String[]> missingRecordsList = new ArrayList<>();
    List<String[]> notEqualRecordsList = new ArrayList<>();
    FieldWiseReport[] fieldWiseReports = new FieldWiseReport[masterFieldsLinkedList.size()];
    String[] missingRecordArray = new String[masterFile.getMasterFileUniqueFieldId_Length()];
    //LOGGER.debug("Field mapper size: {}",fieldMapper.getFieldMapperFile().size());
    String[] tempNotEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId_Length() + fieldMapper.getFieldMapperFile().size()];
    for (int i = 0; i < masterFile.getMasterFileUniqueFieldId_Length(); i++) {
      missingRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
      tempNotEqualRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
    }
    for (int i = masterFile.getMasterFileUniqueFieldId_Length(); i < tempNotEqualRecordArray.length; i++) {
      tempNotEqualRecordArray[i] = fieldMapper.getFieldMapperFile().get(i-masterFile.getMasterFileUniqueFieldId_Length()).element();
      //LOGGER.debug("tempNotEqualRecordArray[i]: {}",tempNotEqualRecordArray[i]);
      // master_column_name_arrays[i - masterFile.getMasterFileUniqueFieldId_Length()];
    }
    //Arrays.stream(tempNotEqualRecordArray).forEach(s -> LOGGER.debug("Header: {}",s));
    missingRecordsList.add(missingRecordArray);
    notEqualRecordsList.add(tempNotEqualRecordArray);
    ComparisonReportDTO comparisonReportDTO = new ComparisonReportDTO(masterFile.getCellValues().size(),
            new int[]{comparisonFile.getCellValues().size()}, masterFile, masterFieldsLinkedList.size());
    FileComparisonCommonUtil.addToExecutionContext(COMPARISON_REPORT_DTO, comparisonReportDTO);
    if (masterFile.isCompare_only_data_present_in_master_file()) {
      ComparisonDetailReport comparisonDetailReport = new ComparisonDetailReport();
      for (Map.Entry<String[], String[]> masterFileCellValue : masterFile.getCellValues().entrySet()) {
//        LOGGER.debug("Data: {}, {}", masterFileCellValue.getKey()[0],masterFileCellValue.getKey()[1]);
        tempNotEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId_Length() + master_column_name_arrays.length];
        for (int i = 0; i < masterFile.getMasterFileUniqueFieldId_Length(); i++) {
          tempNotEqualRecordArray[i] = masterFileCellValue.getKey()[i];
        }
        String masterFileCellData[] = masterFileCellValue.getValue();
        if (comparisonFile.getCellValues().containsKey(masterFileCellValue.getKey()[0])) {
          String subFileCellData[] = comparisonFile.getCellValues().get(masterFileCellValue.getKey()[0]);
          for (int masterFileFielsCount = 0; masterFileFielsCount < masterFieldsLinkedList.size(); masterFileFielsCount++) {
            MasterColumnName masterColumnName = masterFieldsLinkedList.get(masterFileFielsCount);
            //LOGGER.debug("masterColumnName: {}", masterColumnName.toString());
            FieldWiseReport fieldWiseReport;
            if (Objects.isNull(fieldWiseReports[masterFileFielsCount])) {
              fieldWiseReport = new FieldWiseReport();
              fieldWiseReport.setMasterFieldName(master_column_name_arrays[masterFileFielsCount]);
            } else {
              fieldWiseReport = fieldWiseReports[masterFileFielsCount];
            }
            String masterFileFieldValue = "";
            String subFileFieldValue = "";
            if (Objects.nonNull(masterColumnName.getmNode())) {
              if (masterColumnName.getmNode().get(0).size() == 1) {
                masterFileFieldValue = masterFileCellData[(int) masterColumnName.getmNode().get(0).get(0)];
              } else if (masterColumnName.getmNode().get(0).size() > 1) {
                for (int a = 0; a < masterColumnName.getmNode().get(0).size(); a++) {
                  masterFileFieldValue = masterFileFieldValue.concat(masterFileCellData[(int) masterColumnName.getmNode().get(0).get(a)]);
                }
              } else {
                masterFileFieldValue = "";
              }
            } else {
              masterFileFieldValue = NO_MAPPING_WITH_OTHER_FILE;
            }
            /* Sub file going to start*/
            if (Objects.nonNull(masterColumnName.getSubFileField().getSubFileNode()) && masterColumnName.getSubFileField().getSubFileNode().size() > 0) {
              int subFileConditionSize;
              int subFilesSize = masterColumnName.getSubFileField().getSubFileNode().size();
              if (subFilesSize == 1) {
                int conditionCaseSize = masterColumnName.getSubFileField().getSubFileNode().get(0).size();
                if (conditionCaseSize == 1) {
                  subFileConditionSize = masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).size();
                  if (subFileConditionSize == 1) {
                    subFileFieldValue = subFileCellData[(int) masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).get(0)];
                  } else {
                    for (int i = 0; i < subFileConditionSize; i++) {
                      for (int j = 0; j < masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size(); j++) {
                        subFileFieldValue = subFileFieldValue.concat(subFileCellData[(int) masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(j)]);
                      }
                      if (subFileFieldValue.length() > 0) {
                        break;
                      }
                    }
                  }
                } else if (conditionCaseSize == 2) {
                  subFileFieldValue = "";
                  for (int i = 0; i < conditionCaseSize; i++) {
                    if (masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size() == 1) {
                      subFileFieldValue = subFileCellData[(int) masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).get(i)];
                    } else if (subFileFieldValue.length() == 0) {
                      for (int x = 0; x < masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size(); x++) {
                        subFileFieldValue = subFileFieldValue.concat(subFileCellData[(int) masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(x)]);
                      }
                    }
                  }
                }
              } else {
                //More than one file in sub file list
                LOGGER.debug("More than one file in sub file list");
              }
            } else {
              subFileFieldValue = NO_MAPPING_WITH_OTHER_FILE;
            }
            //LOGGER.debug("masterFileFieldValue: {}, subFileFieldValue: {}", masterFileFieldValue,subFileFieldValue);
            findMatchingCell(masterFileFieldValue, subFileFieldValue, fieldWiseReport);
            fieldWiseReports[masterFileFielsCount] = fieldWiseReport;
          }
          updateTotalSyncCount();
          addToNotEqualList(allFieldInSyncLst, notEqualRecordsList, tempNotEqualRecordArray);
          comparisonDetailReport.setRowWiseStatus(new ArrayList<>(allFieldInSyncLst));
          comparisonDetailReports.add(comparisonDetailReport);
          allFieldInSyncLst.clear();
        } else { // No record found in sub file list
//          LOGGER.debug("Missing date: {}, {}", masterFileCellValue.getKey()[0],masterFileCellValue.getKey()[1]);
          missingRecordArray = new String[]{masterFileCellValue.getKey()[0], masterFileCellValue.getKey()[1]};
          missingRecordsList.add(missingRecordArray);
        }
      }
    }
    FileComparisonCommonUtil.addToExecutionContext("COMPARISON_DETAIL_REPORT", comparisonDetailReports);
    missingRecords.setMissingRecordsList(missingRecordsList);
    notEqualRecords.setNotEqualRecordsList(notEqualRecordsList);
    comparisonReportDTO.setMissingRecords(missingRecords);
    comparisonReportDTO.setNotEqualRecords(notEqualRecords);
    comparisonReportDTO.setFieldWiseReports(fieldWiseReports);
    //LOGGER.debug("fieldWiseReports Size: {}", comparisonReportDTO.getFieldWiseReports().length);
    //Arrays.stream(comparisonReportDTO.getFieldWiseReports()).forEach(fieldWiseReport -> LOGGER.debug("fieldWiseReport :{}",fieldWiseReport));
    FileComparisonCommonUtil.addToExecutionContext(COMPARISON_REPORT_DTO, comparisonReportDTO);
  }

  private void updateTotalSyncCount() {
    ComparisonReportDTO comparisonReportDTO = (ComparisonReportDTO) getValueFromExeContext(COMPARISON_REPORT_DTO);
    //LOGGER.debug("allFieldInSyncLst: {}", allFieldInSyncLst);
    int totalNumberOfRecordInSync[] = comparisonReportDTO.getTotalNumberOfRecordInSync();
    for (int i = 0; i < totalNumberOfRecordInSync.length; i++) {
      if ((CONDITION_TRUE.equals(allFieldInSyncLst.get(i))) || ("||".equals(allFieldInSyncLst.get(i)))) {
        totalNumberOfRecordInSync[i] = totalNumberOfRecordInSync[i] + 1;
      }
    }
    comparisonReportDTO.setTotalNumberOfRecordInSync(totalNumberOfRecordInSync);
  }

  @Override
  public void postProcess() {

  }

  private void addToNotEqualList(ArrayList<String> allFieldInSyncLst, List<String[]> notEqualRecordsList, String[] tempNotEqualRecordArray) {
    boolean addToNotEqualRecordsList = false;
    MasterFile masterFile = (MasterFile) getValueFromExeContext(MASTER_FILE_OBJECT);
    //LOGGER.debug("allFieldInSyncLst: {}", allFieldInSyncLst);
    ArrayList<Integer> indexes = (ArrayList<Integer>) indexOfAll(allFieldInSyncLst);
    //LOGGER.debug("indexes : {}", indexes);
    for(int i=0;i<indexes.size();i++) {
      tempNotEqualRecordArray[indexes.get(i) + masterFile.getMasterFileUniqueFieldId_Length()] = allFieldInSyncLst.get(indexes.get(i));
    }
    for (String value : tempNotEqualRecordArray) {
      if (Objects.nonNull(value) && (value.length()>0) && !addToNotEqualRecordsList) {
        addToNotEqualRecordsList = true;
      }
    }
    if (addToNotEqualRecordsList) {
      notEqualRecordsList.add(tempNotEqualRecordArray);
    }
  }

  private void findMatchingCell(String masterFileFieldValue, String subFileFieldValue, FieldWiseReport fieldWiseReport) {
    if (("".equals(masterFileFieldValue) && "".equals(subFileFieldValue)) || (Objects.isNull(masterFileFieldValue) && Objects.isNull(subFileFieldValue))) {
      fieldWiseReport.setAllFileEmptyValueCount(fieldWiseReport.getAllFileEmptyValueCount() + 1);
      allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
    } else if ("".equals(masterFileFieldValue) || FILE_ENCAPSULATE.equals(masterFileFieldValue)) {
      fieldWiseReport.setMasterFileEmptyValueCount(fieldWiseReport.getMasterFileEmptyValueCount() + 1);
      allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
      fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
    } else if ("".equals(subFileFieldValue)) {
      //LOGGER.debug("subFileFieldValue: {} done", subFileFieldValue);
      fieldWiseReport.setSubFileEmptyValueCount(fieldWiseReport.getSubFileEmptyValueCount() + 1);
      allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
      fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
    } else if (NO_MAPPING_WITH_OTHER_FILE.equals(masterFileFieldValue) || NO_MAPPING_WITH_OTHER_FILE.equals(subFileFieldValue)) {
      allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
    } else {
      checkMatch(masterFileFieldValue, subFileFieldValue, fieldWiseReport);
    }

    /*if (Objects.isNull(masterFileFieldValue) || Objects.isNull(subFileFieldValue)) {
      LOGGER.debug("Object is null First : {}, or Second : {}", masterFileFieldValue, subFileFieldValue);
      allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
    } else {
      checkMatch(masterFileFieldValue, subFileFieldValue);
    }*/

  }

  private void checkMatch(String masterFileFieldValue, String subFileFieldValue, FieldWiseReport fieldWiseReport) {
    if (CONSIDER_CASE_IN_CHECKING) {
      if (masterFileFieldValue.equals(subFileFieldValue)) {
        allFieldInSyncLst.add(CONDITION_TRUE);
      } else {
        allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
        fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
      }
    } else {
      //LOGGER.debug("masterFileFieldValue: {},  and subFileFieldValue: {}, Condition: {}",masterFileFieldValue,subFileFieldValue,((masterFileFieldValue.equalsIgnoreCase(subFileFieldValue))));
      if (masterFileFieldValue.equalsIgnoreCase(subFileFieldValue)) {
        allFieldInSyncLst.add(CONDITION_TRUE);
      } else {
        allFieldInSyncLst.add(masterFileFieldValue.concat("||").concat(subFileFieldValue));
        fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
      }
    }
  }

  static List<Integer> indexOfAll(ArrayList<String> list) {
    return IntStream.range(0, list.size())
            .filter(i -> (CONDITION_TRUE != list.get(i)))
            .mapToObj(i -> i)
            .collect(Collectors.toList());
  }


}
