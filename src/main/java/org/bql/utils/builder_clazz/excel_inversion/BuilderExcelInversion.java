package org.bql.utils.builder_clazz.excel_inversion;

import org.apache.poi.hssf.usermodel.*;
import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.StringUtils;
import org.bql.utils.builder_clazz.PublicPackage.BuilderAll;
import org.bql.utils.builder_clazz.PublicPackage.CodeModel;
import org.bql.utils.builder_clazz.PublicPackage.GetFileAllInit;
import org.bql.utils.builder_clazz.ann.ExcelInversionToAnn;
import org.bql.utils.builder_clazz.ann.ExcelValue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BuilderExcelInversion {
    /**
     * 获取拥有Service注解的所有类
     * @param path 包路径
     * @return
     */
    public static void builder(String path){
        List<Class> clazzs = GetFileAllInit.getClasssFromPackage(path);
        Set<CodeModel> excelSet = new HashSet<>();
        if (clazzs.size()<=0)
            return;
        for (Class c:clazzs) {
            Annotation excel = c.getAnnotation(ExcelInversionToAnn.class);
            if(excel instanceof ExcelInversionToAnn){
                ExcelInversionToAnn proco1 = (ExcelInversionToAnn) excel;
                excelSet.add(new CodeModel("",c));
            }
        }
        reflectBean(excelSet);
    }

    private static void reflectBean(Set<CodeModel> excelSet) {
        if (excelSet.size() <= 0) return;
        Iterator<CodeModel> iterator = excelSet.iterator();
        while (iterator.hasNext()) {
            Object o = null;
            try {
                CodeModel model = iterator.next();
                o = model.getClazz().newInstance();
                reflectField(o);
            } catch (Exception e) {
                LoggerUtils.getPlatformLog().info("反射对象是出现异常",e);
            }
        }
    }
    private static void reflectField(Object o) {
        List<ExcelErrorCodeModule> modules = new ArrayList<>();
        Field[] fields = o.getClass().getDeclaredFields();
        if (fields.length<=0)
            return;
        for (Field f:fields) {
            Annotation ann = f.getAnnotation(ExcelValue.class);
            String name = f.getName();
            String value = "";
            String id = "";
            try {
                //获取字段的值
                id = f.get(o).toString();
            } catch (IllegalAccessException e) {
                LoggerUtils.getPlatformLog().info(" 获取id时出现异常",e);
            }
            if (ann instanceof ExcelValue) {
                ExcelValue ev = (ExcelValue) ann;
                value =  ev.value();
            }
            modules.add(new ExcelErrorCodeModule(Integer.parseInt(id),name,value));
        }
        inversion(o,modules);
    }
    private static void inversion(Object o, List<ExcelErrorCodeModule> modules) {
        //创建HSSFWorkbook
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步创建sheet
        HSSFSheet sheet = wb.createSheet(o.getClass().getSimpleName());
        //第三步创建行row:添加表头0行
        HSSFRow row = null;
        for(int i = 0;i<3;i++) {
            row = sheet.createRow(i);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中
            //第四步创建单元格
            HSSFCell cell = row.createCell(0);         //第一个单元格
            cell.setCellValue("id");                  //设定值
            cell.setCellStyle(style);                   //内容居中

            cell = row.createCell(1);                   //第二个单元格
            cell.setCellValue("name");
            cell.setCellStyle(style);

            cell = row.createCell(2);                   //第三个单元格
            cell.setCellValue("value");
            cell.setCellStyle(style);
        }
        //第五步插入数据
        List<ExcelErrorCodeModule> list = modules;
        for (int i = 0; i < list.size(); i++) {
            ExcelErrorCodeModule errorCondition = list.get(i);
            //创建行
            row = sheet.createRow(i + 3);
            //创建单元格并且添加数据
            row.createCell(0).setCellValue(errorCondition.getId());
            row.createCell(1).setCellValue(errorCondition.getName());
            row.createCell(2).setCellValue(errorCondition.getValue());
        }

        //第六步将生成excel文件保存到指定路径下
        try {
            //首字母小写
            String str = StringUtils.firstChar2LowerCase(o.getClass().getSimpleName());
            FileOutputStream fout = new FileOutputStream(BuilderAll.PATH_CNF.getErrorCodeExcelPath()+str+"_"+o.getClass().getName()+".xlsx");
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            LoggerUtils.getPlatformLog().info("将生成excel文件保存到指定路径下时出现异常",e);
        }

    }
}
