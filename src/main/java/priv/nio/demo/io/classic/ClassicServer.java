package priv.nio.demo.io.classic;

import priv.nio.demo.io.Server;
import priv.nio.demo.io.SocketHandler;

import java.io.IOException;
import java.net.Socket;

/**
 * @author lyqlbst
 * @description 传统服务端，创建一个线程
 * @email 1098387108@qq.com
 * @date 2019/11/18 4:15 PM
 */
public class ClassicServer extends Server {
    public static void main(String[] args) throws IOException {
        ClassicServer server = new ClassicServer();
        server.start();
    }

    /**
     * 创建一个新的线程处理socket
     *
     * @param socket 客户端对应的连接
     */
    @Override
    protected void handle(Socket socket) {
        SocketHandler socketHandler = SocketHandler.newInstance(socket);
        Thread handlerThread = new Thread(socketHandler);
        handlerThread.start();
    }
}
