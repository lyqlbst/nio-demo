package priv.nio.demo.io.pool;

import priv.nio.demo.io.Server;
import priv.nio.demo.io.SocketHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lyqlbst
 * @description 服务端
 * @email 1098387108@qq.com
 * @date 2019/11/18 6:50 PM
 */
public class PoolServer extends Server {
    /**
     * 用于记录线程名
     */
    private static final AtomicLong THREAD_COUNT = new AtomicLong(0);
    /**
     * 线程池，用于处理客户端请求
     */
    private static Executor threadPool = new ThreadPoolExecutor(
            2
            , 2
            , 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(2)
            , initThreadFactory()
            , initRejectStrategy());

    public static void main(String[] args) throws IOException {
        PoolServer server = new PoolServer();
        server.start();
    }

    /**
     * 初始化线程工厂
     *
     * @return 线程工厂
     */
    private static ThreadFactory initThreadFactory() {
        return r -> {
            Thread thread = new Thread(r);
            thread.setName("bio-thread-" + THREAD_COUNT.getAndIncrement());
            return thread;
        };
    }

    /**
     * 初始化拒绝策略
     *
     * @return 拒绝策略
     */
    private static RejectedExecutionHandler initRejectStrategy() {
        return (r, executor) -> System.out.println("server端线程池已满...");
    }

    /**
     * 线程池处理socket
     *
     * @param socket 客户端对应的连接
     */
    @Override
    protected void handle(Socket socket) {
        SocketHandler socketHandler = SocketHandler.newInstance(socket);
        threadPool.execute(socketHandler);
    }
}
