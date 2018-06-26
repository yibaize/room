package org.zgl.net.handler;

import org.zgl.utils.logger.LoggerUtils;

import java.util.Properties;

public class NetCnf {
    private static NetCnf instance;

    public static NetCnf getInstance() {
        if(instance == null)
            instance = new NetCnf();
        return instance;
    }
    private final PathCnf pathCnf;
    private NetCnf() {
        Properties pros = new Properties();
        pathCnf = new PathCnf();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("net.properties"));
        } catch (Exception e) {
            LoggerUtils.getPlatformLog().error(e);
        }
        pathCnf.setServerIp(pros.getProperty("server_ip"));
        pathCnf.setClientIp(pros.getProperty("client_ip"));
        pathCnf.setServerPort(Integer.parseInt(pros.getProperty("server_port")));
        pathCnf.setClientPort(Integer.parseInt(pros.getProperty("client_port")));
    }
    public PathCnf getPathCnf(){
        return pathCnf;
    }
}
