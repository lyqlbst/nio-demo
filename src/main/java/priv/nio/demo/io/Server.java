package priv.nio.demo.io;

import priv.nio.demo.BaseInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lyqlbst
 * @description 服务端
 * @email 1098387108@qq.com
 * @date 2019/11/18 6:50 PM
 */
public abstract class Server {
    /**
     * 启动服务端
     *
     * @throws IOException IO异常
     */
    protected void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(BaseInfo.DEFAULT_PORT)) {
            System.out.println("server端socket已建立，等待客户端连接...");
            while (true) {
                Socket socket = serverSocket.accept();
                handle(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("server端错误...");
            throw e;
        }
    }

    /**
     * 处理socket
     *
     * @param socket 客户端对应的连接
     */
    protected void handle(Socket socket) {
    }
}
