package com.file.comparison.dto;

import java.util.List;

public class MissingRecords {

  List<String[]> missingRecordsList;

  public List<String[]> getMissingRecordsList() {
    //Print missing records
   /* missingRecordsList.stream().forEach(strings -> {
      for (int i = 0; i < strings.length; i++) {
        if (i == 0)
          System.out.print(strings[i]);
        else {
          System.out.print("," + strings[i]);
        }
      }
      System.out.println();
    });*/
    return missingRecordsList;
  }

  public void setMissingRecordsList(List<String[]> missingRecordsList) {
    this.missingRecordsList = missingRecordsList;
  }

 /* @Override
  public String toString() {
    return "MissingRecords{" +
            "notEqualRecordsList=" + getMissingRecordsList() +
            '}';
  }*/
}