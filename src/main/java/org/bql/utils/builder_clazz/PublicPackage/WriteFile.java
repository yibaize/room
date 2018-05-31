package org.bql.utils.builder_clazz.PublicPackage;

import org.bql.utils.logger.LoggerUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteFile {
    /**
     * 生成cs文件
     * @param beanName
     * @param content
     */
    public static void writeText(String beanName,String content,String path){
        BufferedWriter ow = null;
        try {
            String Divpath = path;//文件保存路径
            File dirFile = new File(path);
            if(!dirFile.exists()){//文件路径不存在时，自动创建目录
                dirFile.mkdir();
            }
            String clazzName = Divpath+"//"+beanName;//文件名字
            File file = new File(clazzName);
            //创建一个使用指定大小输出缓冲区的新缓冲字符输出流
            ow = new BufferedWriter(new FileWriter(file));
            ow.write(content);
            ow.newLine();
        }catch (Exception e){
            LoggerUtils.getPlatformLog().info(e);
        }finally {
            try {
                if(ow != null)
                    ow.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}