package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.*;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FieldMapper;
import com.file.comparison.util.FileComparisonCommonUtil;

import java.util.*;

import static com.file.comparison.util.FileComparisonCommonUtil.getValueFromExeContext;

public class FileComparisonProcessImpl implements FileComparisonManager {

  private static ArrayList<Boolean> allFieldInSyncLst = new ArrayList<>();

  @Override
  public void init() throws Exception {
  }

  @Override
  public void preProcess() throws Exception {
    System.out.println("Comparison Going to start");
    FieldMapper fieldMapper = (FieldMapper) getValueFromExeContext("FIELD_MAPPER_OBJECT");
    String[] master_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("MASTER_COLUMN_NAME_ARRAY");
    LinkedList<MasterColumnName> masterFieldsLinkedList = fieldMapper.getMasterFields();
    masterFieldsLinkedList.stream().forEach(masterColumnNames -> System.out.println("test: " + masterColumnNames));
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ComparisonFile comparisonFile = (ComparisonFile) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_FILE_OBJECT");
    MissingRecords missingRecords = new MissingRecords();
    NotEqualRecords notEqualRecords = new NotEqualRecords();
    List<String[]> missingRecordsList = new ArrayList<>();
    List<String[]> notEqualRecordsList = new ArrayList<>();
    String[] missingRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length];
    String[] notEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length + master_column_name_arrays.length];
    for (int i = 0; i < masterFile.getMasterFileUniqueFieldId().length; i++) {
      missingRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
      notEqualRecordArray[i] = master_column_name_arrays[masterFile.getMasterFileUniqueFieldId()[i]].toUpperCase();
    }
    for (int i = masterFile.getMasterFileUniqueFieldId().length; i < notEqualRecordArray.length; i++) {
      notEqualRecordArray[i] = master_column_name_arrays[i - masterFile.getMasterFileUniqueFieldId().length];
    }
    missingRecordsList.add(missingRecordArray);
    notEqualRecordsList.add(notEqualRecordArray);
    ComparisonReportDTO comparisonReportDTO = new ComparisonReportDTO(masterFile.getCellValues().size(), new int[]{comparisonFile.getCellValues().size()}, masterFile);
    FileComparisonCommonUtil.addToExecutionContext("COMPARISON_REPORT_DTO", comparisonReportDTO);
    if (masterFile.isCompare_only_data_present_in_master_file()) {
      //String[] master_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("MASTER_COLUMN_NAME_ARRAY");
      //String[] sub_file_column_name_arrays = (String[]) FileComparisonCommonUtil.getValueFromExeContext("SUB_FILE_COLUMN_NAME_ARRAY");
      //String[][] masterCellValues
      //System.out.println("comparison file map size: " + comparisonFile.getCellValues().size());
      for (Map.Entry<String[], String[]> masterFileCellValue : masterFile.getCellValues().entrySet()) {
        //System.out.println("#####################################################---" + masterFileCellValue.getKey()[0] + ", Florg: " + masterFileCellValue.getKey()[1]);
        notEqualRecordArray = new String[masterFile.getMasterFileUniqueFieldId().length + master_column_name_arrays.length];
        for (int i = 0; i < masterFile.getMasterFileUniqueFieldId().length; i++) {
          notEqualRecordArray[i] = masterFileCellValue.getKey()[i];
        }
        String masterFileCellData[] = masterFileCellValue.getValue();
        //System.out.println("Key to get from Master: " + entry.getKey()[0] + ", Second: " + entry.getKey()[1]);
        //System.out.println("Size:----"+comparisonFile.getCellValues().size());
        //comparisonFile.getCellValues().forEach((s, strings) -> System.out.println("Key: " + s + ", Value: " + strings));
//        String[] key = new String[]{entry.getKey()[0]};
        //System.out.println("value: "+comparisonFile.getCellValues().get(key));
        //System.out.println("comparisonFile.getCellValues().containsKey(entry.getKey()[0]): "+comparisonFile.getCellValues().containsKey(masterFileCellValue.getKey()[0]));
        if (comparisonFile.getCellValues().containsKey(masterFileCellValue.getKey()[0])) {
          String subFileCellData[] = comparisonFile.getCellValues().get(masterFileCellValue.getKey()[0]);
          //System.out.println("Array size: "+subFileCellData.length);
          for (MasterColumnName masterColumnName : masterFieldsLinkedList) {
            //System.out.println("111***********************************************************************************");
            String masterFileFieldValue = "";
            String subFileFieldValue = "";
            //System.out.println("masterColumnName.getmNode().size(): "+masterColumnName.getmNode().get(0).size()+", value: "+masterColumnName.getmNode().get(0).get(0));
            if (Objects.nonNull(masterColumnName.getmNode())) {
              if (masterColumnName.getmNode().get(0).size() == 1) {
                masterFileFieldValue = masterFileCellData[(short) masterColumnName.getmNode().get(0).get(0)];
              } else if (masterColumnName.getmNode().get(0).size() > 1) {
                for (int a = 0; a < masterColumnName.getmNode().get(0).size(); a++) {
                  //System.out.println("elseeeeeeee");
                  masterFileFieldValue = masterFileFieldValue + masterFileCellData[(short) masterColumnName.getmNode().get(0).get(a)];
                }
              }
            }
            //System.out.println("MASTER File value: " + masterFileFieldValue);

            /* Sub file going to start*/
            if (Objects.nonNull(masterColumnName.getSubFileField().getSubFileNode())) {
              int subFileConditionSize;
              int subFilesSize = masterColumnName.getSubFileField().getSubFileNode().size();
              //System.out.println("subFilesSize: "+subFilesSize+", Data: "+masterColumnName.getSubFileField().getSubFileNode());
              if (subFilesSize == 1) {
                subFileConditionSize = masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).size();
                //System.out.println("subFileConditionSize: " + subFileConditionSize);
                if (subFileConditionSize == 1) {
                  subFileFieldValue = subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(0).get(0)];
                  //System.out.println("subFileFieldValue: "+subFileFieldValue);
                } else {
                  for (int i = 0; i < subFileConditionSize; i++) {
                    // System.out.println("Sizeeee: "+masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size());
                    for (int j = 0; j < masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).size(); j++) {
                      //System.out.println("^^^^: "+masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(j));
                      //System.out.println("----------------subFileCellData.length: "+subFileCellData.length);
                      //System.out.println("subFileCellData: "+subFileCellData[13]);
                      //System.out.println("1.subFileFieldValue length: "+subFileFieldValue.length());
                      subFileFieldValue = subFileFieldValue + subFileCellData[(short) masterColumnName.getSubFileField().getSubFileNode().get(0).get(i).get(j)];
                      //System.out.println("2.subFileFieldValue length: "+subFileFieldValue.length());
                      //System.out.println("subFileFieldValue updated---: "+subFileFieldValue);
                    }
                    //System.out.println("subFileFieldValue final: "+subFileFieldValue);
                    if (subFileFieldValue.length() > 0) {
                      //System.out.println("Going to break");
                      break;
                    }
                  }
                }
              } else {
                //More than one file in sub file list
                System.out.println("Elseeee");
              }
            }
            //System.out.println("SUBFILE Value: " + subFileFieldValue);
            //System.out.println("masterFileFieldValue: "+masterFileFieldValue);
            findMatchingCell(masterFileFieldValue, subFileFieldValue);
            //masterFileCellData[];
            //String subFileFieldValue = "";
            //System.out.println("222***********************************************************************************");
          }
          updateTotalSyncCount();
          addToNotEqualList(allFieldInSyncLst, notEqualRecordsList, notEqualRecordArray);
          allFieldInSyncLst.clear();
        } else { // No record found in sub file list
          missingRecordArray = new String[]{masterFileCellValue.getKey()[0], masterFileCellValue.getKey()[1]};
          missingRecordsList.add(missingRecordArray);
        }
      }
    }
    missingRecords.setMissingRecordsList(missingRecordsList);
    notEqualRecords.setNotEqualRecordsList(notEqualRecordsList);
    comparisonReportDTO.setMissingRecords(missingRecords);
    comparisonReportDTO.setNotEqualRecords(notEqualRecords);
    //System.out.println("comparisonReportDTO: " + comparisonReportDTO.toString());
    System.out.println("Master data map size        : " + masterFile.getCellValues().size());
    System.out.println("Comparison data map size    : " + comparisonFile.getCellValues().size());
    System.out.println("Missing Records List Size   : " + (comparisonReportDTO.getMissingRecords().getMissingRecordsList().size() - 1));
    System.out.println("NotEqualRecordsList Size    : " + (comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size() - 1));
    System.out.println("TotalNumberOfRecordInSync   : " + comparisonReportDTO.getTotalNumberOfRecordInSync());
    /*Integer[] fieldsNotEqualCount = new Integer[master_column_name_arrays.length];
    for(int i =0;i<fieldsNotEqualCount.length;i++){
      fieldsNotEqualCount[i]=0;
    }*/
    /*for(int i =0;i<comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size();i++){
      String[] row = comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().get(i);
      for(int j=2;j<row.length;j++){
        if("C".equals(row[j])){
          fieldsNotEqualCount[j-2] = fieldsNotEqualCount[j-2] + 1;
        }
      }

    }*/
    //Arrays.stream(fieldsNotEqualCount).forEach(integer -> System.out.println("fieldsNotEqualCount: "+integer));
  }

  private void updateTotalSyncCount() {
    ComparisonReportDTO comparisonReportDTO = (ComparisonReportDTO) getValueFromExeContext("COMPARISON_REPORT_DTO");
    //System.out.println("allFieldInSyncLst: " + allFieldInSyncLst);
    comparisonReportDTO.setTotalNumberOfRecordInSync((allFieldInSyncLst.contains(Boolean.FALSE)) ? (comparisonReportDTO.getTotalNumberOfRecordInSync()) : (comparisonReportDTO.getTotalNumberOfRecordInSync() + 1));
  }

  @Override
  public void postProcess() {

  }

  private List<String[]> addToNotEqualList(ArrayList<Boolean> allFieldInSyncLst, List<String[]> notEqualRecordsList, String[] notEqualRecordArray) {
    boolean addToNotEqualRecordsList = false;
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ArrayList<Integer> indexes = indexOfAll(allFieldInSyncLst);
    //System.out.println("indexes: "+indexes);
    for (Integer index : indexes) {
      notEqualRecordArray[index + masterFile.getMasterFileUniqueFieldId().length] = "C";
    }
    for (String test : notEqualRecordArray) {
      if ("C".equals(test)) {
        addToNotEqualRecordsList = true;
      }
    }
    if (addToNotEqualRecordsList) {
      notEqualRecordsList.add(notEqualRecordArray);
    }
    return notEqualRecordsList;
  }


  private void findMatchingCell(String masterFileFieldValue, String subFileFieldValue) {

    if (Objects.isNull(masterFileFieldValue)) {
      allFieldInSyncLst.add(false);
      //System.out.println("No data in Master file. Value: Null");
    } else if (Objects.isNull(subFileFieldValue)) {
      allFieldInSyncLst.add(false);
      //System.out.println("No data in Sub file. Value: Null");
    } else if (masterFileFieldValue.equals(subFileFieldValue)) {
      allFieldInSyncLst.add(true);
      //System.out.println("Equal Master date: " + masterFileFieldValue + ", File Data: " + subFileFieldValue);
    } else {
      allFieldInSyncLst.add(false);
      //System.out.println("NOT Equal. Master date: " + masterFileFieldValue + ", File Data: " + subFileFieldValue);
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
