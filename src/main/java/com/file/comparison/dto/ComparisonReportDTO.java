package com.file.comparison.dto;

public class ComparisonReportDTO {

  int totalMasterRecordCount;
  int[] totalSubFileRecordCount;
  MasterFile masterFile;
  FieldWiseReport[] fieldWiseReports;

  MissingRecords missingRecords;
  NotEqualRecords notEqualRecords;

  int totalNumberOfRecordInSync[];


  public ComparisonReportDTO(int totalMasterRecordCount, int[] totalSubFileRecordCount, MasterFile masterFile, int size) {
    this.totalMasterRecordCount=totalMasterRecordCount;
    this.totalSubFileRecordCount=totalSubFileRecordCount;
    this.masterFile=masterFile;
    this.fieldWiseReports = new FieldWiseReport[size];
    totalNumberOfRecordInSync = new int[size];
  }


  public MissingRecords getMissingRecords() {
    return missingRecords;
  }

  public void setMissingRecords(MissingRecords missingRecords) {
    this.missingRecords = missingRecords;
  }

  public int[] getTotalNumberOfRecordInSync() {
    return totalNumberOfRecordInSync;
  }

  public void setTotalNumberOfRecordInSync(int[] totalNumberOfRecordInSync) {
    this.totalNumberOfRecordInSync = totalNumberOfRecordInSync;
  }

  public NotEqualRecords getNotEqualRecords() {
    return notEqualRecords;
  }

  public void setNotEqualRecords(NotEqualRecords notEqualRecords) {
    this.notEqualRecords = notEqualRecords;
  }

  public FieldWiseReport[] getFieldWiseReports() {
    return fieldWiseReports;
  }

  public void setFieldWiseReports(FieldWiseReport[] fieldWiseReports) {
    this.fieldWiseReports = fieldWiseReports;
  }

 /* @Override
  public String toString() {
    return "ComparisonReportDTO{" +
            "totalMasterRecordCount=" + totalMasterRecordCount +
            ", totalSubFileRecordCount=" + Arrays.toString(totalSubFileRecordCount) +
            ", masterFile=" + masterFile +
            ", missingRecords=" + missingRecords +
            ", notEqualRecords=" + notEqualRecords +
            ", totalNumberOfRecordInSync=" + totalNumberOfRecordInSync +
            '}';
  }*/
}
