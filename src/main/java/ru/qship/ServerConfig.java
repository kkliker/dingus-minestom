package ru.qship;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import ru.qship.managers.GsonManager;

import java.io.*;

public class ServerConfig {

   @Getter
   private final int amplifier = 1;

   @Getter
   private final int PORT;

   @Getter
   private final int NPC;

   public ServerConfig(){
       Gson gson = new Gson();
       File file = new File("./properties.json");
       JsonObject jsonObject = gson.fromJson(GsonManager.readJson(file), JsonObject.class);

       this.PORT = jsonObject.get("port").getAsInt();
       this.NPC = jsonObject.get("npc").getAsInt();
   }

}
