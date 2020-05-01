package com.file.comparison.util;

import com.file.comparison.dto.MasterColumnName;
import com.file.comparison.dto.SubFileColumnName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

import static com.file.comparison.util.FileComparisonConstant.*;

public class FileComparisonCommonUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileComparisonCommonUtil.class);


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
        LOGGER.debug("readChars : {}", readChars);
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
    BufferedReader fileMapperBufferedReader = (BufferedReader) FileComparisonCommonUtil.getValueFromExeContext(FIELD_MAPPER_BUFFERED_READER);
    String line;
    Integer mapIndex = 0;
    Map<Integer, LinkedList<String>> fieldMapperFile = new HashMap();
    while (Objects.nonNull(line = fileMapperBufferedReader.readLine())) {
      String[] lineArray_With_Separator_Split = line.split(FileComparisonConstant.FILE_MAPPER_SEPERATOR, -1);
      LinkedList<String> mappingColumnBetweenFiles = new LinkedList<>();
      Arrays.stream(lineArray_With_Separator_Split).forEach(s -> mappingColumnBetweenFiles.add(s.trim()));
      fieldMapperFile.put(mapIndex, mappingColumnBetweenFiles);
      mapIndex++;
      fieldMapper.setTotalRowInFieldMapperFile(mapIndex);
    }
    fieldMapper.setFieldMapperFile(fieldMapperFile);
    FileComparisonCommonUtil.addToExecutionContext("FIELD_MAPPER_OBJECT", fieldMapper);
    fieldMapper.getFieldMapperFile().forEach((key, value) -> LOGGER.debug("Key: {}, Value: {}, Size: {},  Total row Number: {}", key,value,value.size(),fieldMapper.getTotalRowInFieldMapperFile()));
  }

  public static String removeSpecialCharacterFromString(String data) {
    String specialCharacter[] ={"$","\""};

    return data;
  }

  //TODO Optimize the code
  public static final FieldMapper objectToIndexLocationForFieldMapper(FieldMapper fieldMapper) {
    Map<Integer, LinkedList<String>> fieldMapperFile = fieldMapper.getFieldMapperFile();
    LinkedList<MasterColumnName> masterFieldsLinkedList = new LinkedList<>();
    int index = -2;
    List<String[]> allFileColumnNameList = (List<String[]>) FileComparisonCommonUtil.getValueFromExeContext(ALL_FILE_COLUMN_NAME_LIST);
    int idForMasterColumnName=0;
    for (Map.Entry<Integer, LinkedList<String>> entry : fieldMapperFile.entrySet()) {

      MasterColumnName masterColumnName = new MasterColumnName(++idForMasterColumnName);
      LOGGER.debug("Key : {}, LinkedList Size: {}"+entry.getKey(), entry.getValue().size());
      for (int i = 0; i < entry.getValue().size(); i++) {
        String value = entry.getValue().get(i);
        index = getStringArrayIndex(allFileColumnNameList.get(i), value);
        LOGGER.debug("Value: {}, Index: {}", value, index);
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

        SubFileColumnName subFileColumnName = new SubFileColumnName(idForMasterColumnName);
        if(i!=0 && index != -1 && index != -2){ // Other files
          conditionSplitList.add(index);
          fileSplit.add(conditionSplitList);
          cNodeValue.add(fileSplit);
          subFileColumnName.setSubFileNode(cNodeValue);
          masterColumnName.setSubFileField(subFileColumnName);
        }
        if (index == -1 && !value.isEmpty()) {
          LOGGER.debug("Value : {}",  value);
          if (value.contains(FileComparisonConstant.FILE_MAPPER_COMBINATION) && (!value.contains(FILE_MAPPER_IF_CASE + FILE_MAPPER_OPENING_BRACKET))) {
            conditionSplitList = new ArrayList();
            String[] fileSubString_FILE_MAPPER_COMBINATION = value.split(FileComparisonConstant.FILE_MAPPER_COMBINATION);
            for (int j = 0; j < fileSubString_FILE_MAPPER_COMBINATION.length; j++) {
              index = getStringArrayIndex(allFileColumnNameList.get(i), fileSubString_FILE_MAPPER_COMBINATION[j]);
              //LOGGER.debug("fileSubString_FILE_MAPPER_COMBINATION[j]: "+fileSubString_FILE_MAPPER_COMBINATION[j]+", Index: "+index);
              if(i==0 && index != -1 && index != -2){ // MASTER file
                mNodeConditionPosition.add(index);
              }
              if(i!=0 && index != -1 && index != -2){ // Other files
                conditionSplitList.add(index);
              }
              LOGGER.debug("Index: {}", index);
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
            LOGGER.debug("Value: {}", value);
            String[] conditionSplitArray = value.split(FileComparisonConstant.FILE_MAPPER_ELSE_CASE);
            for (int x =0; x<conditionSplitArray.length;x++) {
              mNodeConditionPosition = new ArrayList();
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_OPENING_BRACKET+FILE_MAPPER_IF_CASE+"\\"+FILE_MAPPER_OPENING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_CLOSING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_OPENING_BRACKET, "");
              conditionSplitArray[x] = conditionSplitArray[x].contains("\\"+FILE_MAPPER_CLOSING_BRACKET+FILE_MAPPER_CLOSING_BRACKET)?
                      conditionSplitArray[x].replaceAll("\\"+FILE_MAPPER_CLOSING_BRACKET+FILE_MAPPER_CLOSING_BRACKET, ""):conditionSplitArray[x];
              LOGGER.debug("conditionSplitArray[x]: {}", conditionSplitArray[x]);
              long charCount = conditionSplitArray[x].chars().filter(ch -> ch == '&').count();
              LOGGER.debug("charCount: {}",charCount);
              if(charCount>0) {
                String[] someMoreSplit = conditionSplitArray[x].split(FileComparisonConstant.FILE_MAPPER_COMBINATION);
                conditionSplitList = new ArrayList();
                for (int charCountTimes = 0; charCountTimes <= charCount; charCountTimes++) {
                  index = getStringArrayIndex(allFileColumnNameList.get(i), someMoreSplit[charCountTimes]);
                  LOGGER.debug("someMoreSplit[charCountTimes]: {}, Index: {}, I: {}", someMoreSplit[charCountTimes],index,i);
                  if(i==0 && index != -1 && index != -2) { // MASTER file
                    mNodeConditionPosition.add(index);
                    //mNodeValue.add(mNodeConditionPosition);
                    //masterColumnName.setmNode(mNodeValue);
                  } else { // Other files
                    LOGGER.debug("Other file Index number : {} ",index);
                    conditionSplitList.add(index);

                  }
                }
                if(i!=0){ // Other files
                  fileSplit.add(conditionSplitList);
                  conditionSplitList = null;
                }
              } else{
                index = getStringArrayIndex(allFileColumnNameList.get(i), conditionSplitArray[x]);
                LOGGER.debug("Char less tan zero then index value :{}",index);
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
                fileSplit.add(conditionSplitList);
              }
            }
            if(i==0){ // MASTER file
              mNodeValue.add(mNodeConditionPosition);
              masterColumnName.setmNode(mNodeValue);
            }
            Arrays.stream(conditionSplitArray).forEach(splitArrayString -> LOGGER.debug(splitArrayString));
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
    LOGGER.debug("---------------------END------------------------------");
    fieldMapper.setMasterFields(masterFieldsLinkedList);
    fieldMapper.setFieldMapperFile(fieldMapperFile);
    masterFieldsLinkedList.stream().forEach(masterColumnName -> LOGGER.debug("masterColumnName : {}", masterColumnName.toString()));
    LOGGER.debug("fieldMapper: {}", fieldMapper.toString());
    return fieldMapper;
  }


  private static int getStringArrayIndex(String[] stringArray, String findTheIndexOfTheString) {
    return IntStream.range(0, stringArray.length)
            .filter(i -> (stringArray[i].equals(findTheIndexOfTheString)))
            .findFirst().orElse(-1);
  }

}
