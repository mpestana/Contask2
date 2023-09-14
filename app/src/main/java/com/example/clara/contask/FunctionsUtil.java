package com.example.clara.contask;

import java.util.ArrayList;

public class FunctionsUtil {

    public static String getFirstAndLastName(String name) {
        String[] names = name.split(" ");
        if (names.length < 2) {
            return name;
        } else
            return names[0] + " " + names[1];
    }
}
