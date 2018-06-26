package org.zgl.net.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.zgl.net.message.ClientResponse;
import org.zgl.net.client.DefaultFuture;

public class ClientSimpleHandler extends SimpleChannelInboundHandler<ClientResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientResponse response) throws Exception {
//        if("ping".equals(msg.toString())){
//            ctx.channel().writeAndFlush("ping\r\n");
//            return;
//        }
        DefaultFuture.recive(response);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }
    private void handleAllIdle(ChannelHandlerContext ctx) {
        System.out.println("读写空闲5秒钟");
//        ctx.channel().writeAndFlush("1" + "\r\n");
    }
//    public void resetLatch(CountDownLatch initLathc){
//        this.lathc = initLathc;
//    }
//    public Response getResult() {
//        return response;
//    }

}
