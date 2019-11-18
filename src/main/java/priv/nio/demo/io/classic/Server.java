package priv.nio.demo.io.classic;

import priv.nio.demo.BaseInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lyqlbst
 * @description 服务端
 * @email 1098387108@qq.com
 * @date 2019/11/18 4:15 PM
 */
public class Server {
    public static void main(String[] args) throws IOException {
        start();
    }

    /**
     * 启动服务端
     *
     * @throws IOException IO异常
     */
    private static void start() throws IOException {
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
     * 创建一个新的线程处理socket
     *
     * @param socket 客户端对应的连接
     */
    private static void handle(Socket socket) {
        SocketHandler socketHandler = SocketHandler.newInstance(socket);
        Thread handlerThread = new Thread(socketHandler);
        handlerThread.start();
    }
}
