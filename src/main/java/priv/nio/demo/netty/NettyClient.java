package priv.nio.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import priv.nio.demo.BaseInfo;

/**
 * @author lyqlbst
 * @description 客户端
 * @email 1098387108@qq.com
 * @date 2019/11/22 6:18 PM
 */
public class NettyClient {
    public static void main(String[] args) {
        connect();
    }

    /**
     * 连接服务端，发送消息
     */
    private static void connect() {
        // 创建线程组组，即处理连接，又处理IO操作
        EventLoopGroup eventGroup = new NioEventLoopGroup();

        try {
            // 建立一个辅助启动类，用于配置各种参数
            Bootstrap bootstrap = new Bootstrap()
                    // 配置线程组为一个组，不区分
                    .group(eventGroup)
                    // 配置 SocketChannel 的实现类为NIO默认的实现类
                    .channel(NioSocketChannel.class)
                    // 配置 TCP 参数，可配置多个
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 配置请求的处理类，以pipeline的形式增加处理链
                    .handler(initHandler());

            final String host = BaseInfo.DEFAULT_HOST;
            final int port = BaseInfo.DEFAULT_PORT;
            // 阻塞等待连接成功
            ChannelFuture channelFuture = bootstrap.connect(host, port)
                    .sync();
            System.out.println("client connected in port: " + port);

            // 等待客户端端口关闭
            channelFuture.channel()
                    .closeFuture()
                    .sync();
            System.out.println("client disconnected...");
        } catch (Exception e) {
            System.out.println("客户端连接错误...");
            e.printStackTrace();
        } finally {
            // 优雅退出
            eventGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化处理事件的pipeline
     *
     * @return 初始化策略
     */
    private static ChannelInitializer<SocketChannel> initHandler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                channel.pipeline()
                        .addLast(new ClientSocketHandler());
            }
        };
    }
}
