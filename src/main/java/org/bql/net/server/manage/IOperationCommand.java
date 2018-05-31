package org.bql.net.server.manage;


public interface IOperationCommand extends Runnable{
    Object execute();
    default void broadcast(){}
}