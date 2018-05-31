package org.bql.utils.builder_clazz.PublicPackage;

import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.builder_clazz.excel_data_builder_clazz.BuilderExcel;
import org.bql.utils.builder_clazz.builder_protocol_clazz.BuilderProtocol;
import org.bql.utils.builder_clazz.builder_protostuff_clazz.BuilderProtostuff;
import org.bql.utils.builder_clazz.excel_inversion.BuilderExcelInversion;

import java.util.Properties;

public class BuilderAll {
    public static final PathCnf PATH_CNF;
    static {
        Properties pros = new Properties();
        PATH_CNF = new PathCnf();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("code_builder_path.properties"));
        } catch (Exception e) {
            LoggerUtils.getPlatformLog().error(e);
        }
        PATH_CNF.setScanPath(pros.getProperty("scanPath"));
        PATH_CNF.setCsPath(pros.getProperty("csPath"));
        PATH_CNF.setCommandCodePath(pros.getProperty("commandCodePath"));
        PATH_CNF.setOperateCommandRecivePath(pros.getProperty("operateCommandRecivePath"));
        PATH_CNF.setErrorCodeExcelPath(pros.getProperty("errorCodeExcelPath"));
        PATH_CNF.setOperateCommandRecivePackage(pros.getProperty("operateCommandRecivePackage"));
        PATH_CNF.setOperateCommandAbstractPackage(pros.getProperty("operateCommandAbstractPackage"));
        PATH_CNF.setCommandCodePackage(pros.getProperty("commandCodePackage"));
    }

    public static void main(String[] args) {
        BuilderProtocol.builder(PATH_CNF.getScanPath());
        System.err.println("--------------通讯协议号生成完成-------------------");
        BuilderProtostuff.builder(PATH_CNF.getScanPath());
        System.err.println("--------------通讯协议类生成完成-------------------");
        BuilderExcelInversion.builder(PATH_CNF.getScanPath());
        System.err.println("--------------创建excel表完成----------------------");
        BuilderExcel.builder("excel");
        System.err.println("--------------excel表对应的类生成完成--------------");
    }
}
