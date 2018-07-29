package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.ComparisonDetailReport;
import com.file.comparison.dto.ComparisonFile;
import com.file.comparison.dto.ComparisonReportDTO;
import com.file.comparison.dto.MasterFile;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FileComparisonCommonUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static com.file.comparison.util.FileComparisonCommonUtil.getValueFromExeContext;

public class ReportGenerateProcessImpl implements FileComparisonManager {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void preProcess() throws Exception {
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ComparisonFile comparisonFile = (ComparisonFile) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_FILE_OBJECT");
    ComparisonReportDTO comparisonReportDTO = (ComparisonReportDTO) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_REPORT_DTO");
    ArrayList<ComparisonDetailReport> comparison_detail_reports = (ArrayList<ComparisonDetailReport>) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_DETAIL_REPORT");

    System.out.println("Master data map size                : " + masterFile.getCellValues().size());
    System.out.println("Comparison data map size            : " + comparisonFile.getCellValues().size());
    System.out.println("Missing Records List Size           : " + (comparisonReportDTO.getMissingRecords().getMissingRecordsList().size() - 1));
    System.out.println("NotEqualRecordsList Size            : " + (comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size() - 1));
    System.out.println("TotalNumberOfRecordInSync length    : " + comparisonReportDTO.getTotalNumberOfRecordInSync().length);
    System.out.println("ComparisonDetailReport              : " + comparisonReportDTO.getFieldWiseReports().length);

    Arrays.stream(comparisonReportDTO.getTotalNumberOfRecordInSync()).forEach(value -> System.out.println("TotalNumberOfRecordInSync: "+value));


    /*comparison_detail_reports.stream().forEach(comparisonDetailReport -> {
      System.out.println("Value: "+comparisonDetailReport.getRowWiseStatus());
    });*/

    //comparison_detail_reports.stream().forEach(comparisonDetailReport -> System.out.println(comparisonDetailReport.getFieldWiseReports()));

    //System.out.println("comparisonReportDTO: " + comparisonReportDTO.toString());

    /*Integer[] fieldsNotEqualCount = new Integer[master_column_name_arrays.length];
    for(int i =0;i<fieldsNotEqualCount.length;i++){
      fieldsNotEqualCount[i]=0;
    }*/
    /*for(int i =0;i<comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size();i++){
      String[] row = comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().get(i);
      for(int j=2;j<row.length;j++){
        if("C".equals(row[j])){
          fieldsNotEqualCount[j-2] = fieldsNotEqualCount[j-2] + 1;
        }
      }
    }*/
    //Arrays.stream(fieldsNotEqualCount).forEach(integer -> System.out.println("fieldsNotEqualCount: "+integer));
  }

  @Override
  public void postProcess() throws Exception {

  }
}
