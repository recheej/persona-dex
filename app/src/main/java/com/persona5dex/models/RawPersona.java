package com.persona5dex.models;

import com.persona5dex.PersonaUtilities;
import com.persona5dex.models.Enumerations.Arcana;
import com.persona5dex.models.Enumerations.Element;
import com.persona5dex.models.Enumerations.ElementEffect;
import com.persona5dex.models.Enumerations.Personality;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Rechee on 7/1/2017.
 */

public class RawPersona {
    public String arcana;
    public String personality;
    public int[] stats;
    public String[] elems;
    public String note;
    public String arcanaName;
    public String name;
    public int level;
    public boolean rare;
    public boolean special;
    public boolean dlc;
}
