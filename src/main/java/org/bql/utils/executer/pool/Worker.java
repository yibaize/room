package org.bql.utils.executer.pool;

/**
 * @作者： big
 * @创建时间： 18-6-1
 * @文件描述：
 */
public interface Worker {
    /**
     * 加入一个新的客户端会话
     * @param runnable
     */
    void registerNewChannelTask(Runnable runnable);
}
