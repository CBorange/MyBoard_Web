package com.ltj.myboard.util;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyResourceLoader {
    public static String loadProductionQuery(String daoDirectoryName, String sqlFileName){
        String subPath = String.format("%s/%s", daoDirectoryName, sqlFileName);
        String fullPath = String.format("static/sql/ProductionQuery/%s", subPath);
        ClassPathResource resource = new ClassPathResource(fullPath);

        if(!resource.exists())
            return "File Not Exist : " + fullPath;

        try(InputStream is = new BufferedInputStream(resource.getInputStream())){
            byte[] sqlFile = is.readAllBytes();
            String query = new String(sqlFile, StandardCharsets.UTF_8);
            return query;
        }catch (Exception e){
            return "Faild To Read FileData From Stream : " + fullPath;
        }
    }
}
