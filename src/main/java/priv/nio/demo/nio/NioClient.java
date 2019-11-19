package priv.nio.demo.nio;

/**
 * @author lyqlbst
 * @description 客户端
 * @email 1098387108@qq.com
 * @date 2019/11/19 7:38 PM
 */
public class NioClient {
    public static void main(String[] args) {
        ClientSocketHandler handler = ClientSocketHandler.init();
        handler.handle();
    }
}
