package priv.nio.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import priv.nio.demo.CommonUtil;

/**
 * @author lyqlbst
 * @description 用于处理服务端IO请求
 * @email 1098387108@qq.com
 * @date 2019/11/22 5:45 PM
 */
public class ServerSocketHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf reqBuf = (ByteBuf) msg;

        // 获取所有缓冲区可读字节数，构建返回值
        byte[] bytes = new byte[reqBuf.readableBytes()];

        // 读取缓冲区内容
        reqBuf.readBytes(bytes);
        String body = new String(bytes);
        System.out.println("server received message: " + body);

        // 返回received消息
        String ackMsg = CommonUtil.getAckMsg();
        ByteBuf respBuf = Unpooled.copiedBuffer(ackMsg.getBytes());
        ctx.writeAndFlush(respBuf);
    }
}
