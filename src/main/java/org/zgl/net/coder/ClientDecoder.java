package org.zgl.net.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.zgl.net.message.ClientResponse;

import java.util.List;
public class ClientDecoder extends ByteToMessageDecoder {
    /**
     * 数据包的基本长度：包头+id+数据长度
     * 每个协议都是�?个int类型的基本数据占4个字�?
     */
    public static int BASE_LENGTH = 4 + 2 + 2;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        //防止socket大量数据攻击
        if (buffer.readableBytes() > 2048)
            buffer.skipBytes(buffer.readableBytes());
        if (buffer.readableBytes() >= BASE_LENGTH) {
            //第一个可读数据包的起始位
            int beginIndex;
            while (true) {
                //包头开始移动游标位置
                beginIndex = buffer.readerIndex();
                //标记初始读游标位
                buffer.markReaderIndex();
                if (buffer.readInt() == -777888) {//如果是包头
                    break;//一直循环直到读取到包头为止跳出循环执行下一个语句
                }
                buffer.resetReaderIndex();
                buffer.readByte();
                //不满足
                if (buffer.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }
            short id = buffer.readShort();
            //读取数据长度
            short length = buffer.readShort();
            if (length < 0) {
                //没有数据过来就关闭链
                ctx.channel().close();
            }
            //数据包没到齐
            if (buffer.readableBytes() < length) {
                buffer.readerIndex(beginIndex);//游标归到初始位置
                return;//直接返回等待数据完整
            }
            byte[] data = new byte[length];
            buffer.readBytes(data);
            ClientResponse message = new ClientResponse(id, data);
            out.add(message);
        }
        //数据不完整，等待完整的数据包
        return;
    }
}
