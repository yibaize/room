package org.bql;
import org.bql.net.handler.TcpHandler;
import org.bql.net.server.GameServer;
import org.bql.robot.JobMgr;
import org.bql.utils.builder_clazz.excel_init_data.ExcelUtils;
import org.bql.utils.logger.LoggerUtils;

public class GameStart {
    public static void main(String[] args) {
        LoggerUtils.getPlatformLog().info("EXCEL静态数据加载...");
        ExcelUtils.init("excel");
        LoggerUtils.getPlatformLog().info("-------------------->>EXCEL静态数据加载完成<<---------------");
        LoggerUtils.getPlatformLog().info("定时调度器启动...");
        JobMgr.getInstance().start();
        LoggerUtils.getPlatformLog().info("-------------------->>定时调度器启动成功<<---------------");
        LoggerUtils.getPlatformLog().info("-------------------->>房间线程初始化<<---------------");
        TcpHandler.getInstance();
        LoggerUtils.getPlatformLog().info("-------------------->>房间线程初始化成功<<---------------");
        LoggerUtils.getPlatformLog().info("房间服务器启动...");
        GameServer.getInstance().start();
    }
}
