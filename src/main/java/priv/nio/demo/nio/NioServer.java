package priv.nio.demo.nio;

/**
 * @author lyqlbst
 * @description 服务端
 * @email 1098387108@qq.com
 * @date 2019/11/19 6:19 PM
 */
public class NioServer {
    public static void main(String[] args) {
        ServerSocketHandler handler = ServerSocketHandler.init();
        handler.handle();
    }
}
