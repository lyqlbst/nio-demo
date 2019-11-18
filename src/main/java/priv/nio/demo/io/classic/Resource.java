package priv.nio.demo.io.classic;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.Socket;

/**
 * @author lyqlbst
 * @description 所有的资源信息
 * @email 1098387108@qq.com
 * @date 2019/11/18 5:31 PM
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Resource {
    /**
     * socket连接
     */
    private Socket socket;
    /**
     * 读资源
     */
    private BufferedReader reader;
    /**
     * 写资源
     */
    private BufferedWriter writer;

    /**
     * 初始化
     *
     * @param socket socket连接
     * @return 当前类的实例对象
     */
    static Resource newInstance(Socket socket) {
        Resource resource = new Resource();
        resource.setSocket(socket);
        return resource;
    }

    /**
     * 初始化资源
     *
     * @throws IOException IO异常
     */
    void open() throws IOException {
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        this.reader = new BufferedReader(isr);

        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        this.writer = new BufferedWriter(pw);
    }

    /**
     * 关闭资源
     *
     * @throws IOException IO异常
     */
    void close() throws IOException {
        reader.close();
        writer.close();
    }
}
