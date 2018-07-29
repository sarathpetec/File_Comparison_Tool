package com.file.comparison.util;

import com.file.comparison.dto.MasterColumnName;
import com.file.comparison.dto.SubFileColumnName;

import java.io.*;
import java.util.*;

import static com.file.comparison.util.FileComparisonConstant.*;

public class FileComparisonCommonUtil {

  public static void addToExecutionContext(Object key, Object value) {
    FileComparisonConstant.executionContext.put(key, value);
  }

  public static Object getValueFromExeContext(Object key) {
    return FileComparisonConstant.executionContext.get(key);
  }

  public static int countLinesNew(String filename) throws IOException {
    InputStream iStream = new BufferedInputStream(new FileInputStream(filename));
    try {
      byte[] c = new byte[1024];
      int readChars = iStream.read(c);
      if (readChars == -1) {
        // bail out if nothing to read
        return 0;
      }

      // make it easy for the optimizer to tune this loop
      int count = 0;
      while (readChars == 1024) {
        for (int i = 0; i < 1024; ) {
          if (c[i++] == '\n') {
            ++count;
          }
        }
        readChars = iStream.read(c);
      }

      // count remaining characters
      while (readChars != -1) {
        System.out.println(readChars);
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\n') {
            ++count;
          }
        }
        readChars = iStream.read(c);
      }

      return count == 0 ? 1 : count;
    } finally {
      iStream.close();
    }
  }

  public static final void fileMapperToObject() throws IOException {

    FieldMapper fieldMapper = new FieldMapper();
    BufferedReader fileMapperBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext("FIELD_MAPPER_BUFFERED_READER");
    String line;
    int i = 0;
    Map<Integer, LinkedList<String>> fieldMapperFile = new HashMap<>();
    while (Objects.nonNull((line = fileMapperBufferedReader.readLine()))) {
      String[] lineArray_with_seperator_split = line.split(FileComparisonConstant.FILE_MAPPER_SEPERATOR, -1);
      LinkedList<String> mappingColumnBetweenFiles = new LinkedList<>();
      Arrays.stream(lineArray_with_seperator_split).forEach(s -> mappingColumnBetweenFiles.add(s.trim()));
      fieldMapperFile.put(i, mappingColumnBetweenFiles);
      i++;
    }
    fieldMapper.setFieldMapperFile(fieldMapperFile);
    FileComparisonCommonUtil.addToExecutionContext("FIELD_MAPPER_OBJECT", fieldMapper);
    fieldMapper.getFieldMapperFile().forEach((integer, strings) -> System.out.println("Key: " + integer + ", Value: " + strings));

  }

  public static final FieldMapper objectToIndexLocationForFieldMapper(FieldMapper fieldMapper) {
    Map<Integer, LinkedList<String>> fieldMapperFile = fieldMapper.getFieldMapperFile();
    LinkedList<MasterColumnName> masterFieldsLinkedList = new LinkedList<>();
    short index = -2;
    List<String[]> fileColumnNameList = (List<String[]>) FileComparisonCommonUtil.getValueFromExeContext("FILE_COLUMN_LIST");

    for (Map.Entry<Integer, LinkedList<String>> entry : fieldMapperFile.entrySet()) {
      MasterColumnName masterColumnName = new MasterColumnName();
      //System.out.println("--------:"+entry.getValue().size());
      for (int i = 0; i < entry.getValue().size(); i++) {
        SubFileColumnName subFileColumnName = new SubFileColumnName();
        String value = entry.getValue().get(i);
        index = getStringArrayIndex(fileColumnNameList.get(i), value);
        //System.out.println("-----------------"+value+", Index: "+index);
        List<List> mNodeValue = new ArrayList();
        List mNodeConditionPosition = new ArrayList();
        List<List<List>> cNodeValue = new ArrayList();
        List<List> fileSplit = new ArrayList();
        List conditionSplitList = new ArrayList();
        if(i==0 && index != -1 && index != -2){ // MASTER file
          mNodeConditionPosition.add(index);
          mNodeValue.add(mNodeConditionPosition);
          masterColumnName.setmNode(mNodeValue);
        }
        if(i!=0 && index != -1 && index != -2){ // Other files
          conditionSplitList.add(index);
          fileSplit.add(conditionSplitList);
          cNodeValue.add(fileSplit);
          subFileColumnName.setSubFileNode(cNodeValue);
          masterColumnName.setSubFileField(subFileColumnName);
        }
        if (index == -1 && !value.isEmpty()) {
          System.out.println("Value 1: " + value);
          if (value.contains(FileComparisonConstant.FILE_MAPPER_COMBINATION) && (!value.contains(FILE_MAPPER_IF_CASE + FILE_MAPPER_OPENING_BRACKET))) {
            conditionSplitList = new ArrayList();
            String[] fileSubString_FILE_MAPPER_COMBINATION = value.split(FileComparisonConstant.FILE_MAPPER_COMBINATION);
            for (int j = 0; j < fileSubString_FILE_MAPPER_COMBINATION.length; j++) {
              index = getStringArrayIndex(fileColumnNameList.get(i), fileSubString_FILE_MAPPER_COMBINATION[j]);
              //System.out.println("fileSubString_FILE_MAPPER_COMBINATION[j]: "+fileSubString_FILE_MAPPER_COMBINATION[j]+", Index: "+index);
              if(i==0 && index != -1 && index != -2){ // MASTER file
                mNodeConditionPosition.add(index);
              }
              if(i!=0 && index != -1 && index != -2){ // Other files
                conditionSplitList.add(index);
              }
             // System.out.println("Index: " + index);
            }
            if(i ==0) {
              mNodeValue.add(mNodeConditionPosition);
              masterColumnName.setmNode(mNodeValue);
            } else {
              fileSplit.add(conditionSplitList);
              cNodeValue.add(fileSplit);
              subFileColumnName.setSubFileNode(cNodeValue);
              masterColumnName.setSubFileField(subFileColumnName);
            }
          }
          if ((value.contains(FILE_MAPPER_IF_CASE+FILE_MAPPER_OPENING_BRACKET)) && value.contains(FILE_MAPPER_CLOSING_BRACKET+FILE_MAPPER_ELSE_CASE+FILE_MAPPER_OPENING_BRACKET)) {
            System.out.println("Value: " + value);
            String[] conditionSplitArray = value.split(FileComparisonConstant.FILE_MAPPER_ELSE_CASE);
            for (int x =0; x<conditionSplitArray.length;x++) {
              mNodeConditionPosition = new ArrayList();
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_OPENING_BRACKET+FILE_MAPPER_IF_CASE+"\\"+FILE_MAPPER_OPENING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_CLOSING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_OPENING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].contains("\\"+FILE_MAPPER_CLOSING_BRACKET+FILE_MAPPER_CLOSING_BRACKET)?
                      conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_CLOSING_BRACKET+FILE_MAPPER_CLOSING_BRACKET, ""):conditionSplitArray[x];
              System.out.println("conditionSplitArray[x]: "+conditionSplitArray[x]);
              long charCount = conditionSplitArray[x].chars().filter(ch -> ch == '&').count();
              //System.out.println("charCount: "+charCount);
              if(charCount>0) {
                String[] someMoreSplit = conditionSplitArray[x].split(FileComparisonConstant.FILE_MAPPER_COMBINATION);
                conditionSplitList = new ArrayList();
                for (int charCountTimes = 0; charCountTimes <= charCount; charCountTimes++) {
                  index = getStringArrayIndex(fileColumnNameList.get(i), someMoreSplit[charCountTimes]);
                  System.out.println("someMoreSplit[charCountTimes]: "+someMoreSplit[charCountTimes]+", Index: "+index+", I: "+i);
                  if(i==0 && index != -1 && index != -2) { // MASTER file
                    mNodeConditionPosition.add(index);
                    //mNodeValue.add(mNodeConditionPosition);
                    //masterColumnName.setmNode(mNodeValue);
                  } else { // Other files
                    //System.out.println("elseeeeeeeeeeeeeeeee: "+index);
                    conditionSplitList.add(index);

                  }
                }
                if(i!=0){ // Other files
                  fileSplit.add(conditionSplitList);
                  conditionSplitList = null;
                }
              } else{
                index = getStringArrayIndex(fileColumnNameList.get(i), conditionSplitArray[x]);
                //System.out.println("index*****:"+index);
                if(i==0 && index != -1 && index != -2){ // MASTER file
                  mNodeConditionPosition.add(index);
                  mNodeValue.add(mNodeConditionPosition);
                  masterColumnName.setmNode(mNodeValue);
                } else{
                  conditionSplitList.add(index);
                  cNodeValue.add(fileSplit);
                  subFileColumnName.setSubFileNode(cNodeValue);
                }
              }
              if(i!=0 && index != -1 && index != -2 && Objects.nonNull(conditionSplitList)){ // Other files
                //System.out.println("***************////");
                fileSplit.add(conditionSplitList);
              }
            }
            if(i==0){ // MASTER file
              mNodeValue.add(mNodeConditionPosition);
              masterColumnName.setmNode(mNodeValue);
            }
            //Arrays.stream(conditionSplitArray).forEach(s -> System.out.println(s));
          }
        }
        //subFileColumnName.setSubFileNode(cNodeValue);
        masterColumnName.setSubFileField(subFileColumnName);
        /*fileSplit.add(conditionSplitList);
        cNodeValue.add(fileSplit);
        subFileColumnName.setSubFileNode(cNodeValue);
        masterColumnName.setSubFileField(subFileColumnName);*/
      }
      masterFieldsLinkedList.add(masterColumnName);
    }
    fieldMapper.setMasterFields(masterFieldsLinkedList);
    fieldMapper.setFieldMapperFile(fieldMapperFile);
    //masterFieldsLinkedList.stream().forEach(masterColumnName1 -> System.out.println("masterColumnName+++: "+masterColumnName1.toString()));
    return fieldMapper;
  }


  private static short getStringArrayIndex(String[] stringArray, String findTheIndexOfTheString) {
    short index = -1;
    for (short i = 0; i < stringArray.length; i++) {
      if (stringArray[i].equals(findTheIndexOfTheString)) {
        index = i;
        break;
      }
    }
    return index;
  }

}
