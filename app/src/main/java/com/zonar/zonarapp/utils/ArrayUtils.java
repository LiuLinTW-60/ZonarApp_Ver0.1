package com.zonar.zonarapp.utils;

import java.util.ArrayList;

public class ArrayUtils {

    public static ArrayList<Float> newArrayList(int size) {
        ArrayList list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            list.add(1);
        }

        return list;
    }
}
