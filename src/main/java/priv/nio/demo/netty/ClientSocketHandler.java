package priv.nio.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lyqlbst
 * @description 用于处理客户端IO操作
 * @email 1098387108@qq.com
 * @date 2019/11/22 6:18 PM
 */
public class ClientSocketHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String msg = "Hello server!";
        // 转换为ByteBuff
        ByteBuf reqBuf = Unpooled.copiedBuffer(msg.getBytes());
        // 发送到服务端
        ctx.writeAndFlush(reqBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf respBuf = (ByteBuf) msg;

        // 获取所有缓冲区可读字节数，构建返回值
        byte[] bytes = new byte[respBuf.readableBytes()];

        // 读取缓冲区内容
        respBuf.readBytes(bytes);
        String body = new String(bytes);
        System.out.println("client received ack: " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        e.printStackTrace();
        ctx.close();
    }
}
