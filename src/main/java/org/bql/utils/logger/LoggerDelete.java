package org.bql.utils.logger;

import java.io.File;

/**
 * 只留最近10天的日志
 */
public class LoggerDelete {
    public static void delete(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        if(files == null)
            return;
        if(files.length < 10)
            return;
        int j = files.length - 10;
        for(int i = 1; i <= j;i++){
            File f = files[i];
            if(f.isFile()){
                f.delete();
            }
        }
    }
}
