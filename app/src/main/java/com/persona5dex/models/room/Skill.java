package com.persona5dex.models.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.persona5dex.models.Enumerations;

import org.jetbrains.annotations.NotNull;

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
        if (!Objects.equals(element.toLowerCase(), "passive")) {
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
