package org.zgl.net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.zgl.net.message.ServerResponse;

public class ServerEncoder extends MessageToByteEncoder<ServerResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ServerResponse response, ByteBuf buffer) throws Exception {
        buffer.writeInt(-777888);//包头:请使用一个不常用到的int类型数据
        buffer.writeShort(response.getId());
        if (response.getDataLength() <= 0 || response.getData() == null) {
            buffer.writeShort(response.getDataLength());
        } else {
            buffer.writeShort(response.getDataLength());
            buffer.writeBytes(response.getData());//数据
        }
    }
}
