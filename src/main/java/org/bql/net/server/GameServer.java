package org.bql.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.bql.net.coder.ServerDecoder;
import org.bql.net.coder.ServerEncoder;
import org.bql.net.handler.NetCnf;
import org.bql.net.handler.ServerSimpleHandler;
import org.bql.utils.logger.LoggerUtils;

import java.util.concurrent.TimeUnit;

public final class GameServer {
    private final EventLoopGroup boss;
    private final EventLoopGroup work;
    private final int port;
    private static GameServer instance;
    public static GameServer getInstance() {
        if(instance == null)
            instance = new GameServer();
        return instance;
    }
    private GameServer() {
        boss = new NioEventLoopGroup(1);
        work = new NioEventLoopGroup();
        port = NetCnf.getInstance().getPathCnf().getServerPort();
    }
    public final void start(){
        //配置服务器端的nio
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .option(ChannelOption.TCP_NODELAY,true)//通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerEncoder());
                            ch.pipeline().addLast(new ServerDecoder());
                            ch.pipeline().addLast(new IdleStateHandler(60,60,60, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ServerSimpleHandler());
                        }
                    });
            //绑定端口
            ChannelFuture f = b.bind(port).sync();
            if(f.isSuccess()){
                LoggerUtils.getPlatformLog().warn("----------------->>房间服务器启动成功<<------------------");
                LoggerUtils.getPlatformLog().warn("---------------->><<"+port+">><<--端口绑定------------------");
            }
            f.channel().closeFuture().sync();//等待服务端监听关闭
        }catch (Exception e){
            LoggerUtils.getPlatformLog().error("--------------->>服务器启动失败<<------------------",e);
        }finally {
            //优雅退出线程
            boss.shutdownGracefully();
            work.shutdownGracefully();
            LoggerUtils.getPlatformLog().warn("---------------服务器关闭------------------");
        }

    }
    public void shutdown(){
        LoggerUtils.getPlatformLog().warn("---------------服务器关闭成功------------------");
        boss.shutdownGracefully();
        work.shutdownGracefully();
        LoggerUtils.getPlatformLog().warn("---------------<<"+port+">>端口解绑------------------");
    }
}