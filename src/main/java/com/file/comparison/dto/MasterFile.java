package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.file.comparison.util.FileComparisonConstant.COMPARISON_UNIQUE_FIELD_ID;
import static com.file.comparison.util.FileComparisonConstant.MASTER_FILE_UNIQUE_FIELD_ID;

public class MasterFile  extends AbstractComparisonFile {

  private String[] fieldNames;
  private Map<String[], String[]> cellValues = new HashMap<>();
  private int[] masterFileUniqueFieldId = MASTER_FILE_UNIQUE_FIELD_ID;
  private int[] comparisonUniqueFieldId = COMPARISON_UNIQUE_FIELD_ID;
  private int fieldId;
  private int masterFileUniqueFieldId_Length = MASTER_FILE_UNIQUE_FIELD_ID.length;
  private boolean compare_only_data_present_in_master_file = true;

  public Map<String[], String[]> getCellValues() {
    return cellValues;
  }

  public int getMasterFileUniqueFieldId_Length() {
    return masterFileUniqueFieldId_Length;
  }

  public void setCellValues(Map<String[], String[]> cellValues) {
    this.cellValues = cellValues;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public void setFieldNames(String[] fieldNames) {
    this.fieldNames = fieldNames;
  }

  public int[] getMasterFileUniqueFieldId() {
    return masterFileUniqueFieldId;
  }

  public void setMasterFileUniqueFieldId(int[] masterFileUniqueFieldId) {
    this.masterFileUniqueFieldId = masterFileUniqueFieldId;
  }

  public int[] getComparisonUniqueFieldId() {
    return comparisonUniqueFieldId;
  }

  public void setComparisonUniqueFieldId(int[] comparisonUniqueFieldId) {
    this.comparisonUniqueFieldId = comparisonUniqueFieldId;
  }

  public int getFieldId() {
    return fieldId;
  }

  public void setFieldId(int fieldId) {
    this.fieldId = fieldId;
  }

  public boolean isCompare_only_data_present_in_master_file() {
    return compare_only_data_present_in_master_file;
  }

  public void setCompare_only_data_present_in_master_file(boolean compare_only_data_present_in_master_file) {
    this.compare_only_data_present_in_master_file = compare_only_data_present_in_master_file;
  }

  @Override
  public String toString() {
    return "MasterFile{" +
            "fieldNames=" + Arrays.toString(fieldNames) +
            ", cellValues=" + cellValues +
            ", masterFileUniqueFieldId=" + Arrays.toString(masterFileUniqueFieldId) +
            ", comparisonUniqueFieldId=" + Arrays.toString(comparisonUniqueFieldId) +
            ", fieldId=" + fieldId +
            ", compare_only_data_present_in_master_file=" + compare_only_data_present_in_master_file +
            '}';
  }
}
