package com.example.clara.contask;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FunctionsUtil {

    public static String getFirstAndLastName(String name) {
        String[] names = name.split(" ");
        if (names.length < 2) {
            return name;
        } else
            return names[0] + " " + names[1];
    }


}
