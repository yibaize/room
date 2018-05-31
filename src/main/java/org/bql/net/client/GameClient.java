package org.bql.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.bql.net.coder.ClientDecoder;
import org.bql.net.coder.ClientEncoder;
import org.bql.net.handler.ClientSimpleHandler;
import org.bql.net.handler.NetCnf;
import org.bql.net.handler.PathCnf;
import org.bql.net.message.ClientRequest;
import org.bql.net.message.ClientResponse;
import org.bql.utils.logger.LoggerUtils;

public class GameClient {
    private final String host;
    private final int port;
//    private Channel ctx;
//    private SimpleChatClientInitializer clientInitializer;
//    private CountDownLatch lathc = new CountDownLatch(1);
    private static GameClient instance;
    private final Bootstrap bootstrap = new Bootstrap();
    private static ChannelFuture future;
    public static GameClient getInstance() {
        if(instance == null)
            instance = new GameClient();
        return instance;
    }
    private GameClient() {
        PathCnf cnf = NetCnf.getInstance().getPathCnf();
        host = cnf.getClientIp();
        port = cnf.getClientPort();
    }
    public ClientResponse writeAndFuture(ClientRequest request){
        future.channel().writeAndFlush(request);
        DefaultFuture df = new DefaultFuture(request);
        return df.get(3000L);
    }
    public void start(){
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.TCP_NODELAY,true)//通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 5));
                            ch.pipeline().addLast(new ClientDecoder());
                            ch.pipeline().addLast(new ClientEncoder());
                            ch.pipeline().addLast(new ClientSimpleHandler());
                        }
                    });

            future = bootstrap.connect(host,port).sync();
            if(future.isSuccess()){
                LoggerUtils.getPlatformLog().warn("-------------------->>RPC客户端启动成功ip<<"+host+">>：端口<<"+port+">><<---------------");
            }
        } catch (InterruptedException e) {
            LoggerUtils.getPlatformLog().error("--------------->>网络冲断异常<<-----------------",e);
        }
    }
    public void showdown(){

    }
}
