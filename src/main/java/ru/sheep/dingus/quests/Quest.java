package ru.sheep.dingus.quests;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;

@Getter
@Setter
public class Quest{

        private final Pos pos;
        private final QuestRisk questRisk;
        private final String name;
        private final String description;
        private final int work_duration;

        private boolean isRequired;
        private int hologramEntityId;
        private boolean complete;

        public Quest(Pos pos, QuestRisk risk, String name, String description, int work_duration) {
            this.pos = pos;
            this.questRisk = risk;
            this.name = name;
            this.description = description;
            this.work_duration = work_duration;
            this.hologramEntityId = Entity.generateId();
            this.complete = false;
            this.isRequired = false;
        }

        public static Quest fromJson(JsonObject jsonObject){

            return new Quest(
                     new Gson().fromJson(jsonObject.get("pos"),Pos.class),
                     QuestRisk.valueOfWithIgnoreCase(jsonObject.get("risk").getAsString()),
                     jsonObject.get("location").getAsString(),
                     jsonObject.get("name").getAsString(),
                     jsonObject.get("diff").getAsInt()
            );

        }
    }

