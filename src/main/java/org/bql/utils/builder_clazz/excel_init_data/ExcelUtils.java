package org.bql.utils.builder_clazz.excel_init_data;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.StringUtils;
import org.bql.utils.builder_clazz.PublicPackage.CheckType;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ExcelUtils {
    /**
     *
     */
    private static Map<String,String> getFileName(String path)
    {
        Map<String,String> fileMap = new HashMap<>();
        File file = new File(path);
        String[] beanNames = file.list();
        File[] files = file.listFiles();
        if(files == null)
            return new HashMap<>(0);
        for (int i = 0;i<files.length;i++){
            String beanName = StringUtils.substringBeforeLast(beanNames[i],".");
            beanName = StringUtils.substringAfter(beanName,"_");
            String s = files[i].toString();
            String fileName = StringUtils.replace(s,"\\","/");
            fileMap.put(beanName,fileName);
        }
        return fileMap;
    }
    public static void init(String path) {
        Map<String,String> fileMap = getFileName(path);
        for (Map.Entry<String,String> entry:fileMap.entrySet()){
            ClassPathExcelContext(entry.getValue(),entry.getKey());
        }
    }

    public static void main(String[] args) {
        init("excel");
    }
    /**
     * 解析ecxel表
     */
    private static void ClassPathExcelContext(String fileName,String beanName) {
        List<List<String>> objs;//用于后面转对象使用
        objs = new ArrayList<>();
        Workbook book = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);//取到文件
            book = new XSSFWorkbook(fis);
            fis.close();//及时关闭流
            Sheet sheet = book.getSheetAt(0);//取到工作谱（每一页是一个工作谱）
            Iterator<Row> rows = sheet.iterator();
            List<String> obj = null;
            int i = 0;
            while (rows.hasNext()) {
                if(i == 0 || i == 3) {
                    ++i;
                    continue;
                }
                Row row = rows.next();
                Iterator<Cell> cells = row.iterator();
                obj = new ArrayList<String>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            cell.setCellType(Cell.CELL_TYPE_STRING);//全部转换成string类型
                            break;
                    }
                    obj.add(cell.getStringCellValue());
                }
                objs.add(obj);
                ++i;
            }
            serializerFile(objs, beanName);
        } catch (Exception e) {
            LoggerUtils.getPlatformLog().error("excel表在读取"+fileName+"文件时出现异常",e);
        } finally {
            try {
                book.close();
            } catch (Exception e) {
                LoggerUtils.getPlatformLog().error("excel表在关闭流",e);
            }
        }
    }
    private static void serializerFile(List<List<String>> file, String beanName) {
        List<Map<String, String>> objMap = new ArrayList<>();
        Map<String, String> objs;
        for (int i = 3; i < file.size(); i++) {
            objs = new HashMap<>();
            for (int j = 0; j < file.get(i).size(); j++) {
                objs.put(file.get(1).get(j), file.get(i).get(j));
            }
            objMap.add(objs);
        }
        serializerObj(objMap, beanName);
    }
    private static void serializerObj(List<Map<String, String>> objs, String clazzName) {
        Map<Serializable, Object> beanMap = new HashMap<>();
        Class clazz = null;
        Object beanObj = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            LoggerUtils.getPlatformLog().error("excel表在反射"+clazzName+"文件时出现异常，不存在这个类路径",e);
        }

        for (int i = 0; i < objs.size(); i++) {
            try {
                beanObj = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                LoggerUtils.getPlatformLog().error("excel表在反射初始化"+clazz+"对象时出现异常，",e);
            }
            for (Map.Entry<String,String> e:objs.get(i).entrySet()){
                try {
                    Field field = beanObj.getClass().getDeclaredField(e.getKey());
                    field.setAccessible(true);
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                    Object valueObj = null;
                    //如果是基础数据类型或者string类型
                    if(field.getType().isPrimitive() || field.getType() == String.class){
                        valueObj = CheckType.getType(field.getType(),e.getValue());
                    }else {
                        //如果是数组或者集合或者其他类型
                        valueObj = arrs(clazz,beanObj,e.getKey(),e.getValue());
                    }
                    if(valueObj == null)
                        LoggerUtils.getPlatformLog().error("excel表在反射初始化"+clazz+"类时出现异常,没有"+field+"的"+valueObj+"这个类型");
                    field.set(beanObj,valueObj);
                }catch (Exception ex){
                    LoggerUtils.getPlatformLog().error("excel表在反射初始化"+clazz+"类时出现异常",ex);
                }
            }
            if(beanObj instanceof DataTableMessage) {
                beanMap.put(((DataTableMessage) beanObj).id(), beanObj);
                ((DataTableMessage) beanObj).AfterInit();
            }
        }
        //添加到所有导表缓存类中
        StaticConfigMessage staticConfigMessage = StaticConfigMessage.getInstance();
        if(staticConfigMessage == null)
            LoggerUtils.getPlatformLog().error("获取静态初始化类"+staticConfigMessage+"时出现异常取不到这个类");
        staticConfigMessage.put(beanObj.getClass(),beanMap);
    }
    private static Object arrs(Class<?> clazz,Object bean,String fieldName,String value){
        Object obj = null;
        try {
            Method method = clazz.getDeclaredMethod(fieldName+"4Init",new Class[]{String.class});
            method.setAccessible(true);
            obj = method.invoke(bean,value);
        } catch (Exception e) {
            LoggerUtils.getPlatformLog().error("excel表在反射初始化"+clazz+"类时出现异常，不存在"+fieldName+"4Init方法",e);
        }
        return obj;
    }
}  