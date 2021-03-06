package com.persona5dex.models.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "skills")
public class Skill {
    @PrimaryKey
    public int id;

    public String name;
    public String effect;
    public String element;
    public int cost;
    public String note;

    @Ignore
    public String costFriendly() {
        final String elementLower = element.toLowerCase();
        if (!Objects.equals(elementLower, "passive") && !Objects.equals(elementLower, "trait")) {
            if (cost < 100) {
                return cost + "% HP";
            }
            else {
                return (cost / 100) + "SP";
            }
        }
        else {
            return "-";
        }
    }
}
