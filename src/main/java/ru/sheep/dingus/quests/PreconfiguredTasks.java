package ru.sheep.dingus.quests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minestom.server.coordinate.Pos;
import ru.sheep.dingus.api.GsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreconfiguredTasks {

    private static List<Quest> tasks_towny = new ArrayList<>();
    public static void load(){
        var tasks = new ArrayList<Quest>();

        JsonObject js = new Gson().fromJson(GsonManager.readJson(new File("./run/quests_towny.json")), JsonObject.class);
        JsonArray js2 = js.getAsJsonArray("quests");

        js2.forEach(j ->{
            tasks.add(Quest.fromJson(j.getAsJsonObject()));
        });

        tasks_towny = tasks;
    }
    public static List<Quest> tasks_towny(){
        return new ArrayList<>(tasks_towny);
    }


}
