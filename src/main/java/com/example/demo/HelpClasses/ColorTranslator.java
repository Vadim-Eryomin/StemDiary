package com.example.demo.HelpClasses;

public class ColorTranslator {
    public static String translateColor(int color){
        String colorString = Integer.toHexString(color);
        for (int i = colorString.length(); i < 6; i++) {
            colorString = "0" + colorString;
        }
        return colorString;
    }
}
