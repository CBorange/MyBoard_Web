package com.ltj.myboard.util;
import org.springframework.core.io.ClassPathResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyResourceLoader {
    public static String loadProductionQuery(String daoDirectoryName, String sqlFileName){
        String subPath = String.format("%s/%s", daoDirectoryName, sqlFileName);
        String fullPath = String.format("static/sql/ProductionQuery/%s", subPath);
        ClassPathResource resource = new ClassPathResource(fullPath);

        try{
            Path path = Paths.get(resource.getURI());
            String query = Files.readString(path);
            return query;
        }catch (Exception e){
            return "FileNotFound : " + fullPath;
        }
    }
}
