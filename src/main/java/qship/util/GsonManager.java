package qship.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import java.io.*;

public class GsonManager {

    public static Gson gson = new Gson();
    public static void writeJson(String json,File file) throws IOException {

        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        fileWriter.write(json);
        fileWriter.close();
    }
    public static String readJson(File file) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder uuids = new StringBuilder();
        String temp = "";
        while ((temp = reader.readLine()) != null){
            uuids.append(temp);
        }

        reader.close();
        return uuids.toString();
    }

    @SneakyThrows

    public static int getNPCCount() {
        JsonObject jsonObject = gson.fromJson(readJson(new File("./prop.json")), JsonObject.class);
        return jsonObject.get("npc").getAsInt();

    }

    @SneakyThrows

    public static int getPort() {
        JsonObject jsonObject = gson.fromJson(readJson(new File("./prop.json")), JsonObject.class);
        return jsonObject.get("port").getAsInt();

    }




}
