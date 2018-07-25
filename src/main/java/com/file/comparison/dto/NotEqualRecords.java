package com.file.comparison.dto;

import java.util.List;

public class NotEqualRecords {

  List<String[]> notEqualRecordsList;

  public List<String[]> getNotEqualRecordsList() {
    /*notEqualRecordsList.stream().forEach(strings -> {
      for (int i=0;i<strings.length;i++){
        for (int x=0;x<strings.length;x++){
          if("C".equals(strings[x])){
            if(i==0) {
              System.out.println(strings[i]);
            }
            else {
              //System.out.print("," + strings[i]);
            }
            //System.out.println();
          }
        }
      }
    });*/
    boolean check = false;
    for (String[] strings : notEqualRecordsList) {
      for (int i = 0; i < strings.length; i++) {
        if (i == 0) {
          for (int x = 0; x < strings.length; x++) {
            if ("C".equals(strings[x])) {
              check = true;
            }
          }
        }
        if (check) {
          if ((i + 1) == strings.length) {
            check = false;
          }
          if (i == 0) {
            //System.out.print(strings[i]);
          }
          else {
            //System.out.print("," + strings[i]);
          }
        }
      }
      //System.out.println();
    }

    return notEqualRecordsList;
  }

  public void setNotEqualRecordsList(List<String[]> notEqualRecordsList) {
    this.notEqualRecordsList = notEqualRecordsList;
  }

  @Override
  public String toString() {
    return "NotEqualRecords{" +
            "notEqualRecordsList=" + getNotEqualRecordsList() +
            '}';
  }
}