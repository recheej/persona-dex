package com.persona5dex.models.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.huma.room_for_asset.RoomAsset;


/**
 * Created by Rechee on 10/22/2017.
 */

@Database(
        entities = {Persona.class, Skill.class, PersonaSkill.class, PersonaElement.class, SearchSuggestion.class, PersonaFusion.class, PersonaShadowName.class},
        version = 3
)
@TypeConverters({PersonaTypeConverters.class})
public abstract class PersonaDatabase extends RoomDatabase {
    public abstract PersonaDao personaDao();
    public abstract SkillDao skillDao();
    public abstract SearchSuggestionDao searchSuggestionDao();

    private static PersonaDatabase INSTANCE;

    public static PersonaDatabase getPersonaDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = RoomAsset.databaseBuilder(context.getApplicationContext(),
                    PersonaDatabase.class, "persona-db.db")
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {

                        }
                    }, MIGRATION_2_3
                    )
                    .build();
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            //create persona shadow names table
            database.execSQL("CREATE TABLE \"personaShadowNames\" ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `persona_id` INTEGER NOT NULL, `shadow_name` TEXT, `primary` INTEGER NOT NULL DEFAULT 0, `suggestion_id` INTEGER, FOREIGN KEY(`suggestion_id`) REFERENCES `searchSuggestions`(`_id`), FOREIGN KEY(`persona_id`) REFERENCES `personas`(`id`) )");

            //insert persona shadows
            database.execSQL("begin transaction;" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Sacrificial Pyrekeeper', 1\n" +
                    " from personas where lower(personas.name) = 'moloch'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Beguiling Girl', 1\n" +
                    " from personas where lower(personas.name) = 'pixie'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Bedside Brute', 1\n" +
                    " from personas where lower(personas.name) = 'incubus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Gallows Flower', 1\n" +
                    " from personas where lower(personas.name) = 'mandrake'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Dirty Two-Horned Beast', 1\n" +
                    " from personas where lower(personas.name) = 'bicorn'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Apprentice in a Jug', 1\n" +
                    " from personas where lower(personas.name) = 'agathion'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Brutal Calvaryman', 1\n" +
                    " from personas where lower(personas.name) = 'berith'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Troublesome Maid', 1\n" +
                    " from personas where lower(personas.name) = 'silky'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Mad Marsh Horse', 1\n" +
                    " from personas where lower(personas.name) = 'kelpie'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Twilight Prostitute', 1\n" +
                    " from personas where lower(personas.name) = 'succubus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Menacing Owlman', 1\n" +
                    " from personas where lower(personas.name) = 'andras'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'War-Hungry Horseman', 0\n" +
                    " from personas where lower(personas.name) = 'eligor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Guard Captain', 1\n" +
                    " from personas where lower(personas.name) = 'eligor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Guard Captain', 1\n" +
                    " from personas where lower(personas.name) = 'belphegor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Ambassador of Filth', 0\n" +
                    " from personas where lower(personas.name) = 'belphegor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Heavenly Punisher', 1\n" +
                    " from personas where lower(personas.name) = 'archangel'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Night-Walking Warrior', 1\n" +
                    " from personas where lower(personas.name) = 'mokoi'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Waterside Nymph', 1\n" +
                    " from personas where lower(personas.name) = 'apsaras'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Leafy Old Man', 1\n" +
                    " from personas where lower(personas.name) = 'koropokkuru'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Corpse Bird', 1\n" +
                    " from personas where lower(personas.name) = 'onmoraki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Embittered Blacksmith', 1\n" +
                    " from personas where lower(personas.name) = 'ippon-datara'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Night Chimera', 0\n" +
                    " from personas where lower(personas.name) = 'nue'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Security Shadow', 1\n" +
                    " from personas where lower(personas.name) = 'nue'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Mocking Snowman', 1\n" +
                    " from personas where lower(personas.name) = 'jack frost'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Hunting Wolf Spirit', 1\n" +
                    " from personas where lower(personas.name) = 'makami'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Possessive Dog Ghost', 1\n" +
                    " from personas where lower(personas.name) = 'inugami'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Bringer of Misfortune', 1\n" +
                    " from personas where lower(personas.name) = 'shiki-ouji'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Ascended Feline', 1\n" +
                    " from personas where lower(personas.name) = 'nekomata'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Prankster Leader', 1\n" +
                    " from personas where lower(personas.name) = 'high pixie'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Zealous Messenger', 1\n" +
                    " from personas where lower(personas.name) = 'angel'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Twin-Headed Guardian', 1\n" +
                    " from personas where lower(personas.name) = 'orthrus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Equine Sage', 1\n" +
                    " from personas where lower(personas.name) = 'orobas'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Chivalrous Fiend', 0\n" +
                    " from personas where lower(personas.name) = 'oni'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Chivalrous Guard', 1\n" +
                    " from personas where lower(personas.name) = 'oni'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Human-Eating Lady', 1\n" +
                    " from personas where lower(personas.name) = 'yaksini'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Jealous Lover', 1\n" +
                    " from personas where lower(personas.name) = 'leanan sidhe'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Battle Fiend', 1\n" +
                    " from personas where lower(personas.name) = 'rakshasa'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Defeated Avenger', 1\n" +
                    " from personas where lower(personas.name) = 'take-minakata'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Floodbringer Demon', 1\n" +
                    " from personas where lower(personas.name) = 'sui-ki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Tornado Devil', 1\n" +
                    " from personas where lower(personas.name) = 'fuu-ki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Demonic Warrior/ Samurai Killer', 1\n" +
                    " from personas where lower(personas.name) = 'kin-ki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Envoy of Slumber', 1\n" +
                    " from personas where lower(personas.name) = 'sandman'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Thief of Tablets', 1\n" +
                    " from personas where lower(personas.name) = 'anzu'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Cavern Snakeman', 1\n" +
                    " from personas where lower(personas.name) = 'naga'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Slithering Snakewoman', 1\n" +
                    " from personas where lower(personas.name) = 'lamia'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Chanting Baboon', 1\n" +
                    " from personas where lower(personas.name) = 'thoth'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'She of Life and Death', 1\n" +
                    " from personas where lower(personas.name) = 'isis'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Bearer of the Scales', 1\n" +
                    " from personas where lower(personas.name) = 'anubis'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Raging God Bird', 1\n" +
                    " from personas where lower(personas.name) = 'garuda'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Raging Bird God', 0\n" +
                    " from personas where lower(personas.name) = 'garuda'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Coffin Borne God', 1\n" +
                    " from personas where lower(personas.name) = 'mot'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Vicious Pentagram', 1\n" +
                    " from personas where lower(personas.name) = 'decarabia'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Pulsing Mud', 1\n" +
                    " from personas where lower(personas.name) = 'black ooze'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Awakened God', 1\n" +
                    " from personas where lower(personas.name) = 'arahabaki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Rebellious Elephant', 1\n" +
                    " from personas where lower(personas.name) = 'girimehkala'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Vampire Moth', 1\n" +
                    " from personas where lower(personas.name) = 'mothman'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Woman who brings Ruin', 1\n" +
                    " from personas where lower(personas.name) = 'lilim'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Dark Sun', 1\n" +
                    " from personas where lower(personas.name) = 'mithras'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'The Shadowed One', 1\n" +
                    " from personas where lower(personas.name) = 'scathach'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Wishless Star', 1\n" +
                    " from personas where lower(personas.name) = 'kaiwan'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Cruel Leopard', 1\n" +
                    " from personas where lower(personas.name) = 'ose'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Expressionless Beast', 1\n" +
                    " from personas where lower(personas.name) = 'unicorn'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Mountain Girl', 1\n" +
                    " from personas where lower(personas.name) = 'kikuri-hime'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Funerary Warrior', 1\n" +
                    " from personas where lower(personas.name) = 'valkyrie'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Divine Warrior', 1\n" +
                    " from personas where lower(personas.name) = 'power'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Auspicious Pachyderm', 1\n" +
                    " from personas where lower(personas.name) = 'ganesha'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Midnight Queen', 1\n" +
                    " from personas where lower(personas.name) = 'queen mab'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Life-Draining Spirit', 1\n" +
                    " from personas where lower(personas.name) = 'kumbhanda'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Lamenting Sacrifice', 1\n" +
                    " from personas where lower(personas.name) = 'kushinada-hime'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Dancing Witch', 1\n" +
                    " from personas where lower(personas.name) = 'rangda'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Quaking Lady of Shadow', 1\n" +
                    " from personas where lower(personas.name) = 'skadi'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Final Assessor', 1\n" +
                    " from personas where lower(personas.name) = 'norn'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Snake King', 1\n" +
                    " from personas where lower(personas.name) = 'raja naga'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Guard Dog of Hades', 1\n" +
                    " from personas where lower(personas.name) = 'cerberus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Blood-Thirsty Demoness', 1\n" +
                    " from personas where lower(personas.name) = 'dakini'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Strumming Veena Player', 1\n" +
                    " from personas where lower(personas.name) = 'sarasvati'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Self-Infatuated Star', 1\n" +
                    " from personas where lower(personas.name) = 'narcissus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Monarch of Snow', 1\n" +
                    " from personas where lower(personas.name) = 'king frost'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Scandalous Queen', 1\n" +
                    " from personas where lower(personas.name) = 'titania'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Destructive Beauty', 1\n" +
                    " from personas where lower(personas.name) = 'parvati'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Monk of the Valley', 1\n" +
                    " from personas where lower(personas.name) = 'kurama tengu'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Dancing Lion', 1\n" +
                    " from personas where lower(personas.name) = 'barong'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Rhetorician of the Sea', 0\n" +
                    " from personas where lower(personas.name) = 'forneus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Shadow Former Noble', 1\n" +
                    " from personas where lower(personas.name) = 'forneus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Nimble Monkey King', 0\n" +
                    " from personas where lower(personas.name) = 'hanuman'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Shadow TV President', 1\n" +
                    " from personas where lower(personas.name) = 'hanuman'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Heretic Goat', 1\n" +
                    " from personas where lower(personas.name) = 'baphomet'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Shadow IT President', 1\n" +
                    " from personas where lower(personas.name) = 'oberon'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Unfaithful Dream King', 0\n" +
                    " from personas where lower(personas.name) = 'oberon'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Shadow Politician Ooe', 1\n" +
                    " from personas where lower(personas.name) = 'yamata-no-orochi'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Shadow Cleaner', 1\n" +
                    " from personas where lower(personas.name) = 'ongyo-ki'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Harlot of Desire', 1\n" +
                    " from personas where lower(personas.name) = 'lilith'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Wandering Reviver', 1\n" +
                    " from personas where lower(personas.name) = 'nebiros'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Hedonistic Braggart', 1\n" +
                    " from personas where lower(personas.name) = 'dionysus'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Pagan Saviour', 0\n" +
                    " from personas where lower(personas.name) = 'melchizedek'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Pagan Avenger', 1\n" +
                    " from personas where lower(personas.name) = 'melchizedek'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'The Black Avenger', 1\n" +
                    " from personas where lower(personas.name) = 'chernobog'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Reviled Dictator', 1\n" +
                    " from personas where lower(personas.name) = 'baal'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Thunder Emperor', 1\n" +
                    " from personas where lower(personas.name) = 'thor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Missionary of Depravity', 1\n" +
                    " from personas where lower(personas.name) = 'belial'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Throbbing King of Desires', 0\n" +
                    " from personas where lower(personas.name) = 'mara'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Abyssal King of Avarice', 1\n" +
                    " from personas where lower(personas.name) = 'abaddon'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Merciless Inquisitor', 1\n" +
                    " from personas where lower(personas.name) = 'dominion'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'The Blackened Fury', 1\n" +
                    " from personas where lower(personas.name) = 'kali'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Herald of Death', 1\n" +
                    " from personas where lower(personas.name) = 'uriel'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Cleanser of Heaven', 1\n" +
                    " from personas where lower(personas.name) = 'raphael'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Declarer of Anguish', 1\n" +
                    " from personas where lower(personas.name) = 'gabriel'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Apocalyptic Guide', 1\n" +
                    " from personas where lower(personas.name) = 'michael'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'regent'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Viscid Rotting Meat', 1\n" +
                    " from personas where lower(personas.name) = 'slime'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Piggyback Demon', 1\n" +
                    " from personas where lower(personas.name) = 'obariyon'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Hanging Tree Spirit', 1\n" +
                    " from personas where lower(personas.name) = 'kodama'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Foolish Monk', 1\n" +
                    " from personas where lower(personas.name) = 'koppa tengu'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Noisy Mountain Spirit', 1\n" +
                    " from personas where lower(personas.name) = 'sudama'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'queen's necklace'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Gathering Devil', 1\n" +
                    " from personas where lower(personas.name) = 'choronzon'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'stone of scone'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Corpse-Eating Corpse', 1\n" +
                    " from personas where lower(personas.name) = 'pisaca'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'koh-i-noor'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Fused Ghost', 1\n" +
                    " from personas where lower(personas.name) = 'legion'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'orlov'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'emperor's amulet'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'hope diamond'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Treasure Demon', 1\n" +
                    " from personas where lower(personas.name) = 'crystal skull'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Drunken Serpents', 0\n" +
                    " from personas where lower(personas.name) = 'yamata-no-orochi'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Crypt-Dwelling Pyromaniac', 1\n" +
                    " from personas where lower(personas.name) = 'jack-o'-lantern'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Torn King of Desires', 1\n" +
                    " from personas where lower(personas.name) = 'mara'\n" +
                    "insert into personaShadowNames (persona_id, shadow_name, [primary]) select personas.id, 'Girl of the Hanging Tree', 1\n" +
                    " from personas where lower(personas.name) = 'hua po'\n" +
                    "end transaction");

            //insert search suggestions for shadows
            database.execSQL("insert into searchSuggestions (suggest_text_1, suggest_text_2, suggest_intent_data, suggest_intent_extra_data)\n" +
                    "select p.name, psn.shadow_name, p.id, 1 from personaShadowNames psn\n" +
                    "inner join personas p on p.id = psn.persona_id\n");

            //update suggestion_id column on persona shadows
            database.execSQL("update personaShadowNames\n" +
                    "set suggestion_id = (\n" +
                    "\tselect searchSuggestions._id from searchSuggestions\n" +
                    "\tinner join personaShadowNames psn on lower(psn.shadow_name) = lower(searchSuggestions.suggest_text_2)\n" +
                    "\tand psn.id = personaShadowNames.id\n" +
                    ")");
        }

    };
}
