package com.file.comparison.dto;

public class FieldWiseReport {

  String masterFieldName;
  int masterFileEmptyValueCount;
  int subFileEmptyValueCount;
  int AllFileEmptyValueCount;
  int differentValuesInAllFileCount;


  public int getMasterFileEmptyValueCount() {
    return masterFileEmptyValueCount;
  }

  public void setMasterFileEmptyValueCount(int masterFileEmptyValueCount) {
    this.masterFileEmptyValueCount = masterFileEmptyValueCount;
  }

  public int getSubFileEmptyValueCount() {
    return subFileEmptyValueCount;
  }

  public void setSubFileEmptyValueCount(int subFileEmptyValueCount) {
    this.subFileEmptyValueCount = subFileEmptyValueCount;
  }

  public int getAllFileEmptyValueCount() {
    return AllFileEmptyValueCount;
  }

  public void setAllFileEmptyValueCount(int allFileEmptyValueCount) {
    AllFileEmptyValueCount = allFileEmptyValueCount;
  }

  public int getDifferentValuesInAllFileCount() {
    return differentValuesInAllFileCount;
  }

  public void setDifferentValuesInAllFileCount(int differentValuesInAllFileCount) {
    this.differentValuesInAllFileCount = differentValuesInAllFileCount;
  }

  public String getMasterFieldName() {
    return masterFieldName;
  }

  public void setMasterFieldName(String masterFieldName) {
    this.masterFieldName = masterFieldName;
  }

  @Override
  public String toString() {
    return "FieldWiseReport{" +
            "masterFieldName='" + masterFieldName + '\'' +
            ", masterFileEmptyValueCount=" + masterFileEmptyValueCount +
            ", subFileEmptyValueCount=" + subFileEmptyValueCount +
            ", AllFileEmptyValueCount=" + AllFileEmptyValueCount +
            ", differentValuesInAllFileCount=" + differentValuesInAllFileCount +
            '}';
  }
}
