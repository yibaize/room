package org.bql.utils.builder_clazz.PublicPackage;

public class PathCnf {
    /**扫描路劲*/
    private String scanPath;
    /**cs生成路劲*/
    private String csPath;
    private String commandCodePath;
    private String operateCommandRecivePath;
    private String errorCodeExcelPath;
    /**OperateCommandRecivePackage 的包路劲*/
    private String operateCommandRecivePackage;
    /**OperateCommandAbstract 的包路劲*/
    private String operateCommandAbstractPackage;
    private String commandCodePackage;
    public String getScanPath() {
        return scanPath;
    }

    public String getCommandCodePackage() {
        return commandCodePackage;
    }

    public void setCommandCodePackage(String commandCodePackage) {
        this.commandCodePackage = commandCodePackage;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public String getCsPath() {
        return csPath;
    }

    public void setCsPath(String csPath) {
        this.csPath = csPath;
    }

    public String getCommandCodePath() {
        return commandCodePath;
    }

    public void setCommandCodePath(String commandCodePath) {
        this.commandCodePath = commandCodePath;
    }

    public String getOperateCommandRecivePath() {
        return operateCommandRecivePath;
    }

    public void setOperateCommandRecivePath(String operateCommandRecivePath) {
        this.operateCommandRecivePath = operateCommandRecivePath;
    }

    public String getOperateCommandRecivePackage() {
        return operateCommandRecivePackage;
    }

    public void setOperateCommandRecivePackage(String operateCommandRecivePackage) {
        this.operateCommandRecivePackage = operateCommandRecivePackage;
    }

    public String getErrorCodeExcelPath() {
        return errorCodeExcelPath;
    }

    public void setErrorCodeExcelPath(String errorCodeExcelPath) {
        this.errorCodeExcelPath = errorCodeExcelPath;
    }

    public String getOperateCommandAbstractPackage() {
        return operateCommandAbstractPackage;
    }

    public void setOperateCommandAbstractPackage(String operateCommandAbstractPackage) {
        this.operateCommandAbstractPackage = operateCommandAbstractPackage;
    }
}
