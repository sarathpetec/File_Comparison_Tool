package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.*;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FieldMapper;
import com.file.comparison.util.FileComparisonCommonUtil;

import java.util.*;

import static com.file.comparison.util.FileComparisonCommonUtil.getValueFromExeContext;
import static com.file.comparison.util.FileComparisonConstant.*;

public class FileComparisonProcessImpl implements FileComparisonManager {

  private static ArrayList<Boolean> allFieldInSyncLst = new ArrayList<>();
  private static ArrayList<ComparisonDetailReport> comparisonDetailReports = new ArrayList<>();

  @Override
  public void init() throws Exception {
  }

  @Override
  public void preProcess() throws Exception {
    System.out.println("Comparison Going to start");
    FieldMapper fieldMapper = (FieldMapper) getValueFromExeContext("FIELD_MAPPER_OBJECT");
    String[] master_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("MASTER_COLUMN_NAME_ARRAY");
    LinkedList<MasterColumnName> masterFieldsLinkedList = fieldMapper.getMasterFields();
    //System.out.println("masterFieldsLinkedList.size(): " + masterFieldsLinkedList.size());
    //masterFieldsLinkedList.stream().forEach(masterColumnNames -> System.out.println("test: " + masterColumnNames));
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ComparisonFile comparisonFile = (ComparisonFile) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_FILE_OBJECT");
    MissingRecords missingRecords = new MissingRecords();
    NotEqualRecords notEqualRecords = new NotEqualRecords();
    List<String[]> missingRecordsList = new ArrayList<>();
    List<String[]> notEqualRecordsList = new ArrayList<>();
    FieldWiseReport[] fieldWiseReports = new FieldWiseReport[masterFieldsLinkedList.size()];
    String[] missingRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length];
    String[] tempNotEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length + master_column_name_arrays.length];
    for (int i = 0; i < masterFile.getMasterFileUniqueFieldId().length; i++) {
      missingRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
      tempNotEqualRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
    }
    for (int i = masterFile.getMasterFileUniqueFieldId().length; i < tempNotEqualRecordArray.length; i++) {
      tempNotEqualRecordArray[i] = master_column_name_arrays[i - masterFile.getMasterFileUniqueFieldId().length];
    }
    missingRecordsList.add(missingRecordArray);
    notEqualRecordsList.add(tempNotEqualRecordArray);
    ComparisonReportDTO comparisonReportDTO = new ComparisonReportDTO(masterFile.getCellValues().size(),
            new int[]{comparisonFile.getCellValues().size()}, masterFile, masterFieldsLinkedList.size());
    FileComparisonCommonUtil.addToExecutionContext("COMPARISON_REPORT_DTO", comparisonReportDTO);
    if (masterFile.isCompare_only_data_present_in_master_file()) {
      ComparisonDetailReport comparisonDetailReport = new ComparisonDetailReport();
      for (Map.Entry<String[], String[]> masterFileCellValue : masterFile.getCellValues().entrySet()) {
//        System.out.println("Data: "+masterFileCellValue.getKey()[0]+", "+masterFileCellValue.getKey()[1]);
        tempNotEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length + master_column_name_arrays.length];
        for (int i = 0; i < masterFile.getMasterFileUniqueFieldId().length; i++) {
          tempNotEqualRecordArray[i] = masterFileCellValue.getKey()[i];
        }
        String masterFileCellData[] = masterFileCellValue.getValue();
        if (comparisonFile.getCellValues().containsKey(masterFileCellValue.getKey()[0])) {
          String subFileCellData[] = comparisonFile.getCellValues().get(masterFileCellValue.getKey()[0]);
          for (int masterFileFielsCount = 0; masterFileFielsCount < masterFieldsLinkedList.size(); masterFileFielsCount++) {
            MasterColumnName masterColumnName = masterFieldsLinkedList.get(masterFileFielsCount);
            //System.out.println("masterColumnName: "+masterColumnName.toString());
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
                masterFileFieldValue = masterFileCellData[(short) masterColumnName.getmNode().get(0).get(0)];
              } else if (masterColumnName.getmNode().get(0).size() > 1) {
                for (int a = 0; a < masterColumnName.getmNode().get(0).size(); a++) {
                  masterFileFieldValue = masterFileFieldValue.concat(masterFileCellData[(short) masterColumnName.getmNode().get(0).get(a)]);
                }
              } else {
                masterFileFieldValue = "";
              }
            } else {
              masterFileFieldValue = NO_MAPPPING_WITH_OTHER_FILE;
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
                    subFileFieldValue = subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).get(0)];
                  } else {
                    for (int i = 0; i < subFileConditionSize; i++) {
                      for (int j = 0; j < masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size(); j++) {
                        subFileFieldValue = subFileFieldValue.concat(subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(j)]);
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
                      subFileFieldValue = subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).get(i)];
                    } else if (subFileFieldValue.length() == 0) {
                      for (int x = 0; x < masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size(); x++) {
                        subFileFieldValue = subFileFieldValue.concat(subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(x)]);
                      }
                    }
                  }
                }
              } else {
                //More than one file in sub file list
                System.out.println("More than one file in sub file list");
              }
            } else {
              subFileFieldValue = NO_MAPPPING_WITH_OTHER_FILE;
            }
            findMatchingCell(masterFileFieldValue, subFileFieldValue, fieldWiseReport);
            fieldWiseReports[masterFileFielsCount] = fieldWiseReport;
          }
          updateTotalSyncCount();
          addToNotEqualList(allFieldInSyncLst, notEqualRecordsList, tempNotEqualRecordArray);
          comparisonDetailReport.setRowWiseStatus(new ArrayList<>(allFieldInSyncLst));
          comparisonDetailReports.add(comparisonDetailReport);
          allFieldInSyncLst.clear();
        } else { // No record found in sub file list
//          System.out.println("Missing date: "+masterFileCellValue.getKey()[0]+", "+masterFileCellValue.getKey()[1]);
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
    System.out.println("fieldWiseReports Size: " + comparisonReportDTO.getFieldWiseReports().length);
    Arrays.stream(comparisonReportDTO.getFieldWiseReports()).forEach(fieldWiseReport -> System.out.println(fieldWiseReport.toString()));
    FileComparisonCommonUtil.addToExecutionContext("COMPARISON_REPORT_DTO", comparisonReportDTO);
  }

  private void updateTotalSyncCount() {
    ComparisonReportDTO comparisonReportDTO = (ComparisonReportDTO) getValueFromExeContext("COMPARISON_REPORT_DTO");
    //System.out.println("allFieldInSyncLst: " + allFieldInSyncLst);
    int totalNumberOfRecordInSync[] = comparisonReportDTO.getTotalNumberOfRecordInSync();
    for (int i = 0; i < totalNumberOfRecordInSync.length; i++) {
      if (allFieldInSyncLst.get(i) == Boolean.TRUE) {
        totalNumberOfRecordInSync[i] = totalNumberOfRecordInSync[i] + 1;
      }
    }
    comparisonReportDTO.setTotalNumberOfRecordInSync(totalNumberOfRecordInSync);
  }

  @Override
  public void postProcess() {

  }

  private void addToNotEqualList(ArrayList<Boolean> allFieldInSyncLst, List<String[]> notEqualRecordsList, String[] tempNotEqualRecordArray) {
    boolean addToNotEqualRecordsList = false;
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ArrayList<Integer> indexes = indexOfAll(allFieldInSyncLst);
    for (Integer index : indexes) {
      tempNotEqualRecordArray[index + masterFile.getMasterFileUniqueFieldId().length] = NOT_EQUAL_REPERSENTING_STRING;
    }
    for (String value : tempNotEqualRecordArray) {
      if (NOT_EQUAL_REPERSENTING_STRING.equals(value) && !addToNotEqualRecordsList) {
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
      allFieldInSyncLst.add(true);
    } else if ("".equals(masterFileFieldValue) || FILE_ENCAPSULATE.equals(masterFileFieldValue)) {
      fieldWiseReport.setMasterFileEmptyValueCount(fieldWiseReport.getMasterFileEmptyValueCount() + 1);
      allFieldInSyncLst.add(false);
    } else if ("".equals(subFileFieldValue)) {
      fieldWiseReport.setSubFileEmptyValueCount(fieldWiseReport.getSubFileEmptyValueCount() + 1);
      allFieldInSyncLst.add(false);
    } else if (NO_MAPPPING_WITH_OTHER_FILE.equals(masterFileFieldValue) || NO_MAPPPING_WITH_OTHER_FILE.equals(subFileFieldValue)) {
      allFieldInSyncLst.add(false);
    } else {
      checkMatch(masterFileFieldValue, subFileFieldValue, fieldWiseReport);
    }

    /*if (Objects.isNull(masterFileFieldValue) || Objects.isNull(subFileFieldValue)) {
      System.out.println("Object is null First :"+masterFileFieldValue+", or Second :"+subFileFieldValue);
      allFieldInSyncLst.add(false);
    } else {
      checkMatch(masterFileFieldValue, subFileFieldValue);
    }*/

  }

  private void checkMatch(String masterFileFieldValue, String subFileFieldValue, FieldWiseReport fieldWiseReport) {
    if (CONSIDER_CASE_IN_CHECKING) {
      if (masterFileFieldValue.equals(subFileFieldValue)) {
        allFieldInSyncLst.add(true);
      } else {
        allFieldInSyncLst.add(false);
        fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
      }
    } else {
      if (masterFileFieldValue.equalsIgnoreCase(subFileFieldValue)) {
        allFieldInSyncLst.add(true);
      } else {
        allFieldInSyncLst.add(false);
        fieldWiseReport.setDifferentValuesInAllFileCount(fieldWiseReport.getDifferentValuesInAllFileCount() + 1);
      }
    }
  }

  static ArrayList<Integer> indexOfAll(ArrayList<Boolean> list) {
    ArrayList<Integer> indexList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++)
      if (false == list.get(i))
        indexList.add(i);
    return indexList;
  }


}
