package com.file.comparison.util;

import com.file.comparison.dto.MasterColumnName;

import java.util.LinkedList;
import java.util.Map;

public class FieldMapper {

  Map<Integer, LinkedList<String>> fieldMapperFile;
  LinkedList<MasterColumnName> masterFields;
  int mFieldSize;
  int totalRowInFieldMapperFile;

  public LinkedList<MasterColumnName> getMasterFields() {
    return masterFields;
  }

  public void setMasterFields(LinkedList<MasterColumnName> masterFields) {
    this.masterFields = masterFields;
  }

  public int getmFieldSize() {
    return mFieldSize;
  }

  public void setmFieldSize(int mFieldSize) {
    this.mFieldSize = mFieldSize;
  }

  public Map<Integer, LinkedList<String>> getFieldMapperFile() {
    return fieldMapperFile;
  }

  public void setFieldMapperFile(Map<Integer, LinkedList<String>> fieldMapperFile) {
    this.fieldMapperFile = fieldMapperFile;
  }

  public int getTotalRowInFieldMapperFile() {
    return totalRowInFieldMapperFile;
  }

  public void setTotalRowInFieldMapperFile(int totalRowInFieldMapperFile) {
    this.totalRowInFieldMapperFile = totalRowInFieldMapperFile;
  }
}
