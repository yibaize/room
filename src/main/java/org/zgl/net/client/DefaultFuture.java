package org.zgl.net.client;

import io.netty.buffer.ByteBuf;
import org.zgl.net.message.ClientRequest;
import org.zgl.net.message.ClientResponse;
import org.zgl.net.server.NettySerializable;
import org.zgl.utils.logger.LoggerUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    private final static Map<Integer,DefaultFuture> ALL_DEFAULT_FUTURE = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private long timeOut = 2*60*1000;
    private long startTime = System.currentTimeMillis();
    private ClientResponse response;
    public DefaultFuture(ClientRequest request){
        ALL_DEFAULT_FUTURE.put(request.getId(),this);
    }
    public ClientResponse get(long timeOut){
        lock.lock();
        try {
            while (!done()){
                condition.await(timeOut,TimeUnit.SECONDS);
                if(System.currentTimeMillis() - startTime > timeOut){
                    System.out.println("请求超时");
                    break;
                }
            }
        } catch (Exception e) {
            LoggerUtils.getPlatformLog().error(e);
        }finally {
            lock.unlock();
        }
        return this.response;
    }
    private boolean done(){
        //已经接收到消息
        return this.response !=null;
    }
    public static void recive(ClientResponse response){
        DefaultFuture df = ALL_DEFAULT_FUTURE.get(response.getId());
        if(response.getId() == 404){
            ByteBuf buf = NettySerializable.getBuffer(response.getData());
            int id = buf.readShort();
            df = ALL_DEFAULT_FUTURE.get(id);
        }
        if(df == null){
            throw new NullPointerException("RPC数据返回异常,获取到的id为:"+response.getId());
        }
        if(df != null){
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                ALL_DEFAULT_FUTURE.remove(response.getId());
            }catch (Exception e){
                LoggerUtils.getPlatformLog().error(e);
            }finally {
                lock.unlock();
            }
        }
    }

    public ClientResponse getResponse() {
        return response;
    }

    public void setResponse(ClientResponse response) {
        this.response = response;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 守护线程，时时扫描map移除超时消息
     */
    static class FutureThread implements Runnable{

        @Override
        public void run() {
            Set<Integer> ids = ALL_DEFAULT_FUTURE.keySet();
            for(int id : ids){
                DefaultFuture df = ALL_DEFAULT_FUTURE.get(id);
                if(df == null){
                    ALL_DEFAULT_FUTURE.remove(df);
                }else {
                    if(df.getTimeOut() < System.currentTimeMillis() - df.startTime){
                        ClientResponse response = new ClientResponse(id,null);
                        recive(response);
                    }
                }
            }
        }
    }
    static {
        FutureThread future = new FutureThread();
        Thread t = new Thread(future);
        t.setDaemon(true);//设置为守护线程
        t.start();
    }
}
