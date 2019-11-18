package priv.nio.demo.io;

import priv.nio.demo.BaseInfo;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * @author lyqlbst
 * @description 客户端
 * @email 1098387108@qq.com
 * @date 2019/11/18 4:45 PM
 */
public class Client {
    public static void main(String[] args) throws IOException {
        connect();
    }

    /**
     * 向服务端发送消息，并读取返回的消息
     *
     * @throws IOException IO异常
     */
    private static void connect() throws IOException {
        try (Socket socket = new Socket(BaseInfo.DEFAULT_HOST, BaseInfo.DEFAULT_PORT)) {
            System.out.println("socket已建立，准备发送消息...");
            sendAndReceiveMsg(socket);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("client端错误...");
            throw e;
        }
    }

    /**
     * 发送消息，等待返回消息
     *
     * @param socket 客户端连接
     */
    private static void sendAndReceiveMsg(Socket socket) {
        Resource resource = Resource.newInstance(socket);

        try {
            resource.open();
            try (Scanner s = new Scanner(System.in)) {
                BufferedReader reader = resource.getReader();
                BufferedWriter writer = resource.getWriter();
                final String quit = BaseInfo.DEFAULT_QUIT_SYMBOL;
                String body;

                // 控制台输入
                while ((body = s.nextLine()) != null && !quit.equals(body)) {
                    if (quit.equals(body)) {
                        writer.write(quit + "\n");
                        writer.flush();
                        break;
                    }

                    String newMsg = body + "(" + currentTime() + ")\n";
                    writer.write(newMsg);
                    writer.flush();

                    body = reader.readLine();
                    System.out.println("client端收到消息：" + body);
                }
                System.out.println("client端关闭...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("client发送消息或接收消息错误...");
        } finally {
            try {
                resource.close();
            } catch (IOException e) {
                System.out.println("client端关闭错误...");
            }
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    private static String currentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(BaseInfo.DEFAULT_TIME_PATTERN);
        return dtf.format(now);
    }
}
