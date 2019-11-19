package priv.nio.demo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static priv.nio.demo.BaseInfo.*;

/**
 * @author lyqlbst
 * @description 通用的一些方法
 * @email 1098387108@qq.com
 * @date 2019/11/19 7:22 PM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtil {
    /**
     * 默认日期格式
     */
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    /**
     * 获取默认的ack消息
     *
     * @return 消息体（字符串）
     */
    public static String getAckMsg() {
        String currentTime = LocalDateTime.now().format(DEFAULT_FORMATTER);
        return "server received(" + currentTime + ")";
    }
}
