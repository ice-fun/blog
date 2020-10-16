package com.knowswift.myspringboot.utils;

public class RandomStringUtils {
    public static String createCode() {
        //产生(0,999999]之间的随机数
        Integer randNum = (int) (Math.random() * (999999) + 1);
        return String.format("%06d", randNum);
    }

    public static String createRandomString(int stringLength) {
        String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            int index = (int) Math.floor(Math.random() * string.length());
            stringBuilder.append(string.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(createRandomString(8));
    }
}
