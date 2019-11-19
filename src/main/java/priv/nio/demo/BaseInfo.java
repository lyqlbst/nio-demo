package priv.nio.demo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyqlbst
 * @description 一些基本信息
 * @email 1098387108@qq.com
 * @date 2019/11/18 4:15 PM
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BaseInfo {
    /**
     * 默认地址
     */
    public static final String DEFAULT_HOST = "127.0.0.1";
    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 8080;
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 退出标志符
     */
    public static final String DEFAULT_QUIT_SYMBOL = "quit";
    /**
     * {@link java.nio.ByteBuffer}默认分配空间
     */
    public static final int DEFAULT_CAPACITY = 1024;
    /**
     * 默认超时时间
     */
    public static final int DEFAULT_TIME_OUT = 1000;
}
