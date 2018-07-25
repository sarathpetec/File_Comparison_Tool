package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ComparisonFile extends AbstractComparisonFile {

  Map<String, String[]> cellValues = new HashMap<>();
  int[] comparisonFileUniqueFieldId = {0};//Zero'th index value should be same as comparisonUniqueFieldId
  int[] comparisonUniqueFieldId = {0};



  public int[] getComparisonFileUniqueFieldId() {
    return comparisonFileUniqueFieldId;
  }

  public void setComparisonFileUniqueFieldId(int[] comparisonFileUniqueFieldId) {
    this.comparisonFileUniqueFieldId = comparisonFileUniqueFieldId;
  }

  public int[] getComparisonUniqueFieldId() {
    return comparisonUniqueFieldId;
  }

  public void setComparisonUniqueFieldId(int[] comparisonUniqueFieldId) {
    this.comparisonUniqueFieldId = comparisonUniqueFieldId;
  }

  public Map<String, String[]> getCellValues() {
    return cellValues;
  }

  public void setCellValues(Map<String, String[]> cellValues) {
    this.cellValues = cellValues;
  }

  @Override
  public String toString() {
    return "ComparisonFile{" +
            "cellValues=" + cellValues +
            ", comparisonFileUniqueFieldId=" + Arrays.toString(comparisonFileUniqueFieldId) +
            ", comparisonUniqueFieldId=" + Arrays.toString(comparisonUniqueFieldId) +
            '}';
  }
}
