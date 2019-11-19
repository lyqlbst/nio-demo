package priv.nio.demo.nio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static priv.nio.demo.BaseInfo.*;


/**
 * @author lyqlbst
 * @description 客户端请求Socket的类
 * @email 1098387108@qq.com
 * @date 2019/11/19 7:34 PM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ClientSocketHandler {
    /**
     * 连接地址
     */
    private String host;
    /**
     * 连接端口
     */
    private int port;
    /**
     * 事件选择器
     */
    private Selector selector;

    private ClientSocketHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 初始化一个handler
     *
     * @return handler实例
     */
    static ClientSocketHandler init() {
        return new ClientSocketHandler(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * 处理请求，发送消息到服务端
     */
    void handle() {
        // 打开一个ServerSocketChannel
        try (SocketChannel channel = SocketChannel.open()) {
            // 设置为非阻塞模式
            channel.configureBlocking(false);
            // 创建selector线程
            try (Selector newSelector = Selector.open()) {
                selector = newSelector;

                // 与服务端建立连接
                connect(channel);

                // 发送请求并读取服务端的返回值
                sendAndRead(selector);
            }
        } catch (IOException e) {
            System.out.println("SocketHandler出现错误...");
            e.printStackTrace();
        }
    }

    /**
     * 建立连接
     *
     * @throws IOException IO异常
     */
    private void connect(SocketChannel channel) throws IOException {
        // 与服务端创建连接，其实就是创建一个TCP连接的过程（三次握手）
        boolean connect = channel.connect(new InetSocketAddress(host, port));

        // 若连接成功，在selector上注册可读事件；否则，在selector上注册连接创建事件
        if (connect) {
            channel.register(selector, SelectionKey.OP_READ);
        } else {
            channel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 发送请求并接收服务端返回的信息
     *
     * @param selector {@link Selector}选择器
     */
    private void sendAndRead(Selector selector) {
        while (true) {
            try {
                // 轮询选择已监听到的事件
                selector.select(DEFAULT_TIME_OUT);

                // 选取监听到的事件
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
            if (key.isConnectable()) {
                handleConnectable(channel);
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
     * 处理连接创建事件
     *
     * @param channel 信道
     * @throws IOException IO异常
     */
    private void handleConnectable(Channel channel) throws IOException {
        SocketChannel socketChannel = (SocketChannel) channel;

        // 连接未完成
        if (!socketChannel.finishConnect()) {
            System.out.println("连接尚未创建完成...");
            return;
        } else {
            System.out.println("client handle in port: " + port);
        }

        // 给buffer分理空间
        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_CAPACITY);

        // 将消息写入buffer
        String msg = "hello server!";
        buffer.put(msg.getBytes());
        // 将buffer转换为读模式
        buffer.flip();

        // 发送消息
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            System.out.println("消息发送到服务端成功！");
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            System.out.println("消息发送尚未完成...");
        }
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
        System.out.println("读取到服务端消息：" + body);
    }
}
