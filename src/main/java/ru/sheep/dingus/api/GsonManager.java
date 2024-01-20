package ru.sheep.dingus.api;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class GsonManager {

    @SneakyThrows
    public static void writeJson(String json,File file){

        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        fileWriter.write(json);
        fileWriter.close();

        System.out.println("writing "  + json);
    }
    @SneakyThrows
    public static @Nullable String readJson(File file) {

        System.out.println("FILE " + file + " EXIST " + file.exists());

        if(!file.exists()){
            return null;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder uuids = new StringBuilder();
        String temp = "";
        while ((temp = reader.readLine()) != null){
            uuids.append(temp);
        }

        System.out.println("reading " + uuids);
        reader.close();
        return uuids.toString();
    }




}
