package com.rjgf.threecost.crawdata.util;

import java.util.UUID;

/**
 * @author linch
 * @create 2022/3/1 11:35
 */
public class UUIDUtils {
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };

    /**
     * 获取随机字符串
     *
     * @param prefix 前缀
     * @return 格式为 prefix-xxxx 的字符串
     */
    public static String getShortUUID(String prefix) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return prefix + "-" + shortBuffer;
    }

}
