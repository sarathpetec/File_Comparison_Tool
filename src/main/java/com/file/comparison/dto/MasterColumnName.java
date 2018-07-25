package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.List;

public class MasterColumnName extends AbstractComparisonFile {

  int id;
  List<List> mNode;
  SubFileColumnName subFileField;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public SubFileColumnName getSubFileField() {
    return subFileField;
  }

  public void setSubFileField(SubFileColumnName subFileField) {
    this.subFileField = subFileField;
  }

  public List<List> getmNode() {
    return mNode;
  }

  public void setmNode(List<List> mNode) {
    this.mNode = mNode;
  }

  @Override
  public String toString() {
    return "MasterColumnName{" +
            "id=" + id +
            ", mNode=" + mNode +
            ", subFileField=" + subFileField +
            '}';
  }
}
