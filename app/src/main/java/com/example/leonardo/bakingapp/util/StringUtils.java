package com.example.leonardo.bakingapp.util;


import android.text.TextUtils;

public final class StringUtils {

    public static String removeNonASCIIChars(String input){
        String output = input;
        if(!TextUtils.isEmpty(output)){
            output = input.replaceAll("[^\\x00-\\x7F]", "");
        }
        return output;
    }
}
