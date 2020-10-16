package com.blog.blog.utils;

/**
 * @author Li Yao Bing* @Date 2020/5/15
 **/


import javax.validation.constraints.NotNull;

/**
 * 伪加密工具，实现将字符串一部分转成其他字符 ，实现对返回的数据进行加密
 */
public class EncryptionUtils {

    private static String replaceChar = "*";

    private static int start = 0;
    private static int end = 0;

    /**
     * 将字符串替换为等长的其他字符
     * 索引为 string默认索引，下标从0开始，默认情况下全部替换
     *
     * @param str 需要加密的字符串
     * @return
     */
    public static String stringEncrypt(@NotNull String str) {
        end = end == 0 ? str.length() - 1 : end;
        String startString = str.substring(0, start);
        String endString = str.substring(end + 1);
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            builder.append(replaceChar);
        }

        return startString + builder.toString() + endString;
    }

    /**
     * 根据索引位置，选择加密的部分
     *
     * @param str
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return
     */
    public static String stringEncrypt(@NotNull String str, int startIndex, int endIndex) {
        start = startIndex;
        end = endIndex;
        return stringEncrypt(str);
    }

    /**
     * 根据索引位置，选择加密的部分,从0开始
     *
     * @param str
     * @param endIndex 结束索引
     * @return
     */
    public static String stringEncrypt(@NotNull String str, int endIndex) {
        end = endIndex;
        return stringEncrypt(str);
    }

    /**
     * 自定义替换后的其他字符
     *
     * @param str     字符串
     * @param newChar 加密后的字符
     * @return
     */
    public static String stringEncrypt(@NotNull String str, String newChar) {
        replaceChar = newChar;
        stringEncrypt(str);
        return stringEncrypt(str);
    }

    /**
     * 自定义替换后的其他字符
     * 根据索引位置，选择加密的部分
     *
     * @param newChar    加密后的字符
     * @param str        字符串
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return
     */
    public static String stringEncrypt(@NotNull String str, String newChar, int startIndex, int endIndex) {
        replaceChar = newChar;
        start = startIndex;
        end = endIndex;
        return stringEncrypt(str);
    }

    /**
     * 自定义替换后的其他字符
     * 根据索引位置，选择加密的部分 从0开始
     *
     * @param newChar  加密后的字符
     * @param str      字符串
     * @param endIndex 结束索引
     * @return
     */
    public static String stringEncrypt(@NotNull String str, String newChar, int endIndex) {
        replaceChar = newChar;
        end = endIndex;
        return stringEncrypt(str);
    }


    public static String mchEncrypt(String str) {
        char[] chars = str.toCharArray();
        StringBuilder string = new StringBuilder();
        for (char aChar : chars) {

            // 对数字 0-9 1-8....对换
            if (aChar > 47 && aChar < 58) {
                aChar = (char) (105 - aChar);
            } else if (aChar > 64 && aChar < 91 && aChar % 2 != 0) {// 对大写字母转成小写
                aChar += 32;
            } else if (aChar > 96 && aChar < 123 && aChar % 2 != 0) {// 对小写转成大写字母
                aChar -= 32;
            }
            string.append(aChar);
        }
        return string.toString();
    }


    public static void main(String[] args) {
        String str = "zImMKgswLQY2cV7fJAMC6Uzwu8e5kP5w";
        String s = mchEncrypt(str);
        System.out.println(s);
        String s1 = mchEncrypt(s);
        System.out.println(s1);
        System.out.println(s1.equals(str));
    }
}
