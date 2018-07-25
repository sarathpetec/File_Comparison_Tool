package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MasterFile  extends AbstractComparisonFile {

  String[] fieldNames;
  Map<String[], String[]> cellValues = new HashMap<>();
  int[] masterFileUniqueFieldId = {9,8}; //Zero'th index value should be same as comparisonUniqueFieldId
  int[] comparisonUniqueFieldId = {9};
  int fieldId;
  boolean compare_only_data_present_in_master_file = true;

  public Map<String[], String[]> getCellValues() {
    return cellValues;
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
