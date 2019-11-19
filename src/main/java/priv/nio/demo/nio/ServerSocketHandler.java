package priv.nio.demo.nio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import priv.nio.demo.CommonUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static priv.nio.demo.BaseInfo.*;

/**
 * @author lyqlbst
 * @description 服务端处理socket的类
 * @email 1098387108@qq.com
 * @date 2019/11/19 6:21 PM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ServerSocketHandler {
    /**
     * 端口号
     */
    private int port;
    /**
     * 事件选择器
     */
    private Selector selector;

    private ServerSocketHandler(int port) {
        this.port = port;
    }

    /**
     * 初始化一个handler
     *
     * @return handler实例
     */
    static ServerSocketHandler init() {
        return new ServerSocketHandler(DEFAULT_PORT);
    }

    /**
     * 开始监听并处理请求
     */
    void handle() {
        // 打开一个ServerSocketChannel
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            // 设置为非阻塞模式
            channel.configureBlocking(false);
            // 绑定监听的端口
            channel.socket().bind(new InetSocketAddress(port));
            // 创建selector线程
            try (Selector newSelector = Selector.open()) {
                selector = newSelector;

                // 将ServerSocketHandler注册到Selector，让Selector监听socket连接事件
                channel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("server handle in port: " + port);

                // 处理请求
                acceptAndAck(selector);
            }
        } catch (IOException e) {
            System.out.println("ServerSocketHandler出现错误...");
            e.printStackTrace();
        }
    }

    /**
     * 处理请求
     *
     * @param selector {@link Selector}选择器
     */
    private void acceptAndAck(Selector selector) {
        while (true) {
            try {
                // 轮询选择已监听到的事件，这个步骤会阻塞，直到有事件进来
                selector.select();

                // 轮询处理监听到的事件
                Iterator<SelectionKey> iterator = selector
                        .selectedKeys()
                        .iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 处理这个事件
                    handleEvent(key);
                    // 删除selectionKeys列表中的该事件
                    iterator.remove();
                }
            } catch (Exception e) {
                System.out.println("select出现错误");
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理事件：连接事件和可读事件
     *
     * @param key 事件key
     */
    private void handleEvent(SelectionKey key) {
        // 非法事件，不予处理
        if (!key.isValid()) {
            System.out.println("invalid key...");
            return;
        }

        // 处理不同的事件
        Channel channel = key.channel();

        try {
            if (key.isAcceptable()) {
                handleAcceptable(channel);
            } else if (key.isReadable()) {
                handleReadable(channel);
            } else {
                System.out.println("忽略事件...");
            }
        } catch (Exception e) {
            // 取消该事件
            key.cancel();
            e.printStackTrace();
        }
    }

    /**
     * 处理可连接事件
     *
     * @param channel 信道
     * @throws IOException IO异常
     */
    private void handleAcceptable(Channel channel) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) channel;
        // 接受客户端请求，其实就是建立TCP连接的过程（三次握手）
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("socket已连接");
        // 设置为非阻塞模式
        socketChannel.configureBlocking(false);
        // 注册新的可读事件到Selector上，等待读取客户端消息
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 处理可读事件
     *
     * @param channel 信道
     * @throws IOException IO异常
     */
    private void handleReadable(Channel channel) throws IOException {
        SocketChannel socketChannel = (SocketChannel) channel;

        // 给buffer分配空间
        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
        // 读取客户端发送的数据到buffer上
        int length = socketChannel.read(buffer);

        // buffer中无可读内容
        if (length <= 0) {
            throw new RuntimeException("empty data...");
        }

        // 将buffer转换为读模式
        buffer.flip();
        // 创建一段连续数组，将buffer中可读数据长度作为数组长度
        byte[] bytes = new byte[buffer.remaining()];
        // 从buffer中读取消息
        buffer.get(bytes);
        // 打印消息
        String body = new String(bytes);
        System.out.println("读取到客户端消息：" + body);
        // 响应客户端
        ack(channel);
    }

    /**
     * 给客户端返回消息
     *
     * @param channel 信道
     * @throws IOException IO异常
     */
    private void ack(Channel channel) throws IOException {
        SocketChannel socketChannel = (SocketChannel) channel;
        String ackMsg = CommonUtil.getAckMsg();

        // 给buffer分配空间
        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
        // 将消息写入buffer
        buffer.put(ackMsg.getBytes());
        // 将buffer转换为读模式
        buffer.flip();

        // 发送消息
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            System.out.println("消息发送到客户端成功！");
        } else {
            System.out.println("消息发送尚未完成...");
        }
    }
}
