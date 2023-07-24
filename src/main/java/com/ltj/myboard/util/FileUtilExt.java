package com.ltj.myboard.util;

public class FileUtilExt {
    public static String getFileExtension(String fileName){
        int index = fileName.lastIndexOf(".");
        if(index > 0){
            String extension = fileName.substring(index + 1);
            return extension;
        }
        return "";
    }
}
