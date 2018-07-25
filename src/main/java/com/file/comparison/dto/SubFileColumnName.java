package com.file.comparison.dto;

import com.file.comparison.conf.AbstractComparisonFile;

import java.util.List;

public class SubFileColumnName extends AbstractComparisonFile {

  int id;
  List<List<List>> subFileNode;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<List<List>> getSubFileNode() {
    return subFileNode;
  }

  public void setSubFileNode(List<List<List>> subFileNode) {
    this.subFileNode = subFileNode;
  }

  @Override
  public String toString() {
    return "SubFileColumnName{" +
            "id=" + id +
            ", subFileNode=" + subFileNode +
            '}';
  }
}
