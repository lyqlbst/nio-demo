package priv.nio.demo.io.classic;

import lombok.AllArgsConstructor;
import lombok.Data;
import priv.nio.demo.BaseInfo;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lyqlbst
 * @description 处理客户端请求的类
 * @email 1098387108@qq.com
 * @date 2019/11/18 4:20 PM
 */
@Data
@AllArgsConstructor
public class SocketHandler implements Runnable {
    /**
     * 客户端的socket
     */
    private Socket socket;

    /**
     * 创建一个新的SocketHandler对象
     *
     * @param socket 客户端连接对应的socket
     * @return 新的SocketHandler对象
     */
    static SocketHandler newInstance(Socket socket) {
        return new SocketHandler(socket);
    }

    @Override
    public void run() {
        handle();
    }

    /**
     * 读取客户端发送的消息，并回复ack
     */
    private void handle() {
        Resource resource = Resource.newInstance(socket);
        try {
            resource.open();

            final String quit = BaseInfo.DEFAULT_QUIT_SYMBOL;
            BufferedReader reader = resource.getReader();
            BufferedWriter writer = resource.getWriter();
            String body;

            while ((body = reader.readLine()) != null && !quit.equals(body)) {
                System.out.println("server端接收到消息：" + body);
                String ackMsg = "server received(" + currentTime() + ")\n";
                writer.write(ackMsg);
                writer.flush();
            }

            System.out.println("server端socket关闭...");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SocketHandler错误...");
        } finally {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("关闭资源错误...");
            }
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    private String currentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(BaseInfo.DEFAULT_TIME_PATTERN);
        return dtf.format(now);
    }
}
