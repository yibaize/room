package org.zgl.remote;

/**
 * @作者： big
 * @创建时间： 2018/6/5
 * @文件描述：
 */
public interface IoMessage {
    String getInterfaceName();

    String getMethodName();

    Object getArgs();

}