package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ComparisonFile extends AbstractComparisonFile {

  Map<String, String[]> cellValues = new HashMap<>();
  int[] comparisonFileUniqueFieldId = {0};//Zero'th index value should be same as comparisonUniqueFieldId
  int[] comparisonUniqueFieldId = {0};
  int size;



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

  public int getSize() {
    return getCellValues().size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ComparisonFile)) return false;
    ComparisonFile that = (ComparisonFile) o;
    return Objects.equals(getCellValues(), that.getCellValues());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getCellValues());
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
