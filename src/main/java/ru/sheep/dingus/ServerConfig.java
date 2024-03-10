package ru.sheep.dingus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.sheep.dingus.api.GsonManager;

import java.io.*;

public class ServerConfig {

   private static int amplifier = 1;
   private static int PORT;
   private static int NPC;

   public static void init(){
       Gson gson = new Gson();
       File file = new File("./run/properties.json");
       JsonObject jsonObject = gson.fromJson(GsonManager.readJson(file), JsonObject.class);

       PORT = jsonObject.get("port").getAsInt();
       NPC = jsonObject.get("npc").getAsInt();
   }

   public static int port(){
       return PORT;
   }

    public static int amplifier(){
        return amplifier;
    }

    public static int npc_number(){
        return NPC;
    }

}
