package com.file.comparison.process_manager_impl;

import com.file.comparison.dto.ComparisonDetailReport;
import com.file.comparison.dto.ComparisonFile;
import com.file.comparison.dto.ComparisonReportDTO;
import com.file.comparison.dto.MasterFile;
import com.file.comparison.process_manager.FileComparisonManager;
import com.file.comparison.util.FileComparisonCommonUtil;
import com.file.comparison.util.FileComparisonConstant;
import com.file.comparison.util.ReportGeneratorCSV;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.file.comparison.util.FileComparisonCommonUtil.getValueFromExeContext;

public class ReportGenerateProcessImpl implements FileComparisonManager {

  private static NumberFormat nf = new DecimalFormat("##.####");

  @Override
  public void init() throws Exception {

  }

  @Override
  public void preProcess() throws Exception {
    MasterFile masterFile = (MasterFile) getValueFromExeContext("MASTER_FILE_OBJECT");
    ComparisonFile comparisonFile = (ComparisonFile) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_FILE_OBJECT");
    ComparisonReportDTO comparisonReportDTO = (ComparisonReportDTO) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_REPORT_DTO");
    ArrayList<ComparisonDetailReport> comparison_detail_reports = (ArrayList<ComparisonDetailReport>) FileComparisonCommonUtil.getValueFromExeContext("COMPARISON_DETAIL_REPORT");
    System.out.println("#################################################################################################");
    System.out.println("###################################-COMPARISON REPORT-###########################################");
    System.out.println("#################################################################################################");
    System.out.println("Master data map size                : " + masterFile.getCellValues().size());
    System.out.println("Comparison data map size            : " + comparisonFile.getCellValues().size());
    System.out.println("Missing Records List Size           : " + (comparisonReportDTO.getMissingRecords().getMissingRecordsList().size() - 1));
    System.out.println("NotEqualRecordsList Size            : " + (comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size() - 1));
//    System.out.println("TotalNumberOfRecordInSync length    : " + comparisonReportDTO.getTotalNumberOfRecordInSync().length);
//    System.out.println("ComparisonDetailReport              : " + comparisonReportDTO.getFieldWiseReports().length);

    Arrays.stream(comparisonReportDTO.getTotalNumberOfRecordInSync()).forEach(value -> System.out.println("TotalNumberOfRecordInSync: " + value));


    System.out.println("#####################################################################");
    FileWriter COMMON_REPORT_Writer = new FileWriter(FileComparisonConstant.COMMON_REPORT_OUTPUT_FILE);
    String[] values = new String[comparisonReportDTO.getFieldWiseReports().length + 1];
    values[0] = "";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getFieldWiseReports()[i - 1].getMasterFieldName();
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getFieldWiseReports().length + 1];
    values[0] = "Records having No value in Master File";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getFieldWiseReports()[i - 1].getMasterFileEmptyValueCount()+"";
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getFieldWiseReports().length + 1];
    values[0] = "Records having No value in Sub File";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getFieldWiseReports()[i - 1].getSubFileEmptyValueCount()+"";
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getFieldWiseReports().length + 1];
    values[0] = "Records having No value in All Files";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getFieldWiseReports()[i - 1].getAllFileEmptyValueCount()+"";
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getFieldWiseReports().length + 1];
    values[0] = "Change in value in All file";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getFieldWiseReports()[i - 1].getDifferentValuesInAllFileCount()+"";
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getTotalNumberOfRecordInSync().length + 1];
    values[0] = "Number of Records in Sync";
    for (int i = 1; i < values.length; i++) {
      values[i] = comparisonReportDTO.getTotalNumberOfRecordInSync()[i - 1]+"";
    }
    ReportGeneratorCSV.writeLine(values);
    values = new String[comparisonReportDTO.getTotalNumberOfRecordInSync().length + 1];
    values[0] = "Number of Records in Sync in Percentage";
    for (int i = 1; i < values.length; i++) {
      values[i] = calculatePercentage(comparisonReportDTO.getTotalNumberOfRecordInSync()[i - 1], masterFile.getCellValues().size())+"%";
    }
    ReportGeneratorCSV.writeLine(values);
    ReportGeneratorCSV.addNewLine();
    ReportGeneratorCSV.addNewLine();
    ReportGeneratorCSV.addNewLine();
    ReportGeneratorCSV.writeLine(new String[]{"Records in Master File", masterFile.getCellValues().size()+""});
    ReportGeneratorCSV.writeLine(new String[]{"Records in Sub File", comparisonFile.getCellValues().size()+""});
    ReportGeneratorCSV.writeLine(new String[]{"Missing Records", (comparisonReportDTO.getMissingRecords().getMissingRecordsList().size() - 1)+""});
    ReportGeneratorCSV.writeLine(new String[]{"Records not matching", (comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size() - 1)+""});
    ReportGeneratorCSV.writeLine(new String[]{"Number of Fields Compared", comparisonReportDTO.getFieldWiseReports().length+""});

    ReportGeneratorCSV.writeToFile(COMMON_REPORT_Writer);
    COMMON_REPORT_Writer.flush();
    COMMON_REPORT_Writer.close();
    ReportGeneratorCSV.resetWriter();

    System.out.println("#####################################################################");

    FileWriter Not_Equal_Records_Writer = new FileWriter(FileComparisonConstant.NOT_EQUAL_RECORDS_OUTPUT_FILE);
    int notEqualRecSize = comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList().size();
    List<String[]> notEqualRecordsList = comparisonReportDTO.getNotEqualRecords().getNotEqualRecordsList();
    for (int i = 0; i < notEqualRecSize; i++) {
      values = new String[2];
      values[0] = notEqualRecordsList.get(i)[0];
      values[1] = notEqualRecordsList.get(i)[1];
      ReportGeneratorCSV.writeLine(values);
    }
    ReportGeneratorCSV.writeToFile(Not_Equal_Records_Writer);
    Not_Equal_Records_Writer.flush();
    Not_Equal_Records_Writer.close();
    ReportGeneratorCSV.resetWriter();

    System.out.println("#####################################################################");

    FileWriter Missing_Records_Writer = new FileWriter(FileComparisonConstant.MISSING_RECORDS_OUTPUT_FILE);
    int missingRecordSize = comparisonReportDTO.getMissingRecords().getMissingRecordsList().size();
    List<String[]> missingRecordsList = comparisonReportDTO.getMissingRecords().getMissingRecordsList();
    for (int i = 0; i < missingRecordSize; i++) {
      values = new String[2];
      values[0] = missingRecordsList.get(i)[0];
      values[1] = missingRecordsList.get(i)[1];
      ReportGeneratorCSV.writeLine(values);
    }
    ReportGeneratorCSV.writeToFile(Missing_Records_Writer);
    Missing_Records_Writer.flush();
    Missing_Records_Writer.close();
    ReportGeneratorCSV.resetWriter();
    System.out.println("#####################################################################");
  }

  @Override
  public void postProcess() throws Exception {

  }

  private static String calculatePercentage(int firstNumber, int secondNumber) {
    return nf.format((((double)firstNumber/(double) secondNumber)*100));
  }
}
