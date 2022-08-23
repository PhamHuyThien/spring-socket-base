package com.thiendz.example.springsocket.utils;

import java.util.Random;

public class NumberUtil {
    public static Random RANDOM = new Random();

    public static String CHAR_UPPER = "QWERTYUIOPASDFGHJKLZXCVBNM";
    public static String CHAR_LOWER = CHAR_UPPER.toLowerCase();
    public static String NUMBER = "1234567890";

    public static String[] CHAR_UPPER_ARRAY = CHAR_UPPER.split("");
    public static String[] CHAR_LOWER_ARRAY = CHAR_LOWER.split("");
    public static String[] CHAR_NUMBER_ARRAY = NUMBER.split("");

    public static String createIdRoom() {
        int len = 7;
        StringBuilder randomIdRoom = new StringBuilder();
        for (int i = 0; i < len; i++)
            randomIdRoom.append(CHAR_NUMBER_ARRAY[RANDOM.nextInt(CHAR_NUMBER_ARRAY.length)]);
        return randomIdRoom.toString();
    }
}
