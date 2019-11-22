package priv.nio.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import priv.nio.demo.BaseInfo;

/**
 * @author lyqlbst
 * @description Netty服务端
 * @email 1098387108@qq.com
 * @date 2019/11/22 5:31 PM
 */
public class NettyServer {
    public static void main(String[] args) {
        start();
    }

    /**
     * 启动服务端
     */
    private static void start() {
        // master线程组，用于处理请求连接，建立连接，分配给slave线程组
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        // slave线程组，用于处理IO操作（read、write）
        EventLoopGroup slaveGroup = new NioEventLoopGroup();

        try {
            // 建立一个辅助启动类，用于配置各种启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    // 配置线程组信息
                    .group(masterGroup, slaveGroup)
                    // 表名 ServerSocketChannel 的实现类为NIO的默认实现类
                    .channel(NioServerSocketChannel.class)
                    // 配置 TCP 参数，可配置多个
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 配置IO事件的处理类，以pipeline的形式加入到处理链中，可以加多个
                    .childHandler(initSlaveHandler());

            final int port = BaseInfo.DEFAULT_PORT;
            // 绑定端口，同步等待启动成功
            ChannelFuture channelFuture = serverBootstrap
                    .bind(port)
                    .sync();
            System.out.println("server start in port: " + port);

            // 等待监听端口关闭
            channelFuture.channel()
                    .closeFuture()
                    .sync();
            System.out.println("server stopped...");
        } catch (Exception e) {
            System.out.println("Netty服务端错误");
            e.printStackTrace();
        } finally {
            // 记得关闭线程组资源，优雅退出
            masterGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化slave进程组的Handler
     *
     * @return 用于处理IO操作的Handler
     */
    private static ChannelInitializer<SocketChannel> initSlaveHandler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) {
                channel.pipeline()
                        .addLast(new ServerSocketHandler());
            }
        };
    }
}
