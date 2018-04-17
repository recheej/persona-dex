package com.persona5dex.models.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.huma.room_for_asset.RoomAsset;

import java.io.IOException;


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

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            //if existing database, don't run the rest of the migration steps
            //Cursor cursor = database.query("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='version'");
            Cursor cursor = database.query("select DISTINCT tbl_name from sqlite_master where tbl_name = 'version'", null);
            if(cursor.moveToFirst()){
                //only put in version table to tell difference between new databases and actual migrations.
                //we don't need this check after the first time, let's delete the table
                database.execSQL("delete from version");
                database.execSQL("drop table if exists version");
                cursor.close();
                return;
            }

            //create persona shadow names table
            database.execSQL("CREATE TABLE if not exists \"personaShadowNames\" ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `persona_id` INTEGER NOT NULL, `shadow_name` TEXT, `isPrimary` INTEGER NOT NULL DEFAULT 0, FOREIGN KEY(`persona_id`) REFERENCES `personas`(`id`))");

            //insert persona shadows
            database.beginTransaction();
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Sacrificial Pyrekeeper', 1" +
                    " from personas  where lower(personas.name) = 'moloch'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Beguiling Girl', 1" +
                    " from personas  where lower(personas.name) = 'pixie'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Bedside Brute', 1" +
                    " from personas  where lower(personas.name) = 'incubus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Gallows Flower', 1" +
                    " from personas  where lower(personas.name) = 'mandrake'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Dirty Two-Horned Beast', 1" +
                    " from personas  where lower(personas.name) = 'bicorn'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Apprentice in a Jug', 1" +
                    " from personas  where lower(personas.name) = 'agathion'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Brutal Calvaryman', 1" +
                    " from personas  where lower(personas.name) = 'berith'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Troublesome Maid', 1" +
                    " from personas  where lower(personas.name) = 'silky'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Mad Marsh Horse', 1" +
                    " from personas  where lower(personas.name) = 'kelpie'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Twilight Prostitute', 1" +
                    " from personas  where lower(personas.name) = 'succubus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Menacing Owlman', 1" +
                    " from personas  where lower(personas.name) = 'andras'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'War-Hungry Horseman', 0" +
                    " from personas  where lower(personas.name) = 'eligor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Guard Captain', 1" +
                    " from personas  where lower(personas.name) = 'eligor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Guard Captain', 1" +
                    " from personas  where lower(personas.name) = 'belphegor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Ambassador of Filth', 0" +
                    " from personas  where lower(personas.name) = 'belphegor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Heavenly Punisher', 1" +
                    " from personas  where lower(personas.name) = 'archangel'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Night-Walking Warrior', 1" +
                    " from personas  where lower(personas.name) = 'mokoi'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Waterside Nymph', 1" +
                    " from personas  where lower(personas.name) = 'apsaras'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Leafy Old Man', 1" +
                    " from personas  where lower(personas.name) = 'koropokkuru'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Corpse Bird', 1" +
                    " from personas  where lower(personas.name) = 'onmoraki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Embittered Blacksmith', 1" +
                    " from personas  where lower(personas.name) = 'ippon-datara'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Night Chimera', 0" +
                    " from personas  where lower(personas.name) = 'nue'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Security Shadow', 1" +
                    " from personas  where lower(personas.name) = 'nue'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Mocking Snowman', 1" +
                    " from personas  where lower(personas.name) = 'jack frost'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Hunting Wolf Spirit', 1" +
                    " from personas  where lower(personas.name) = 'makami'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Possessive Dog Ghost', 1" +
                    " from personas  where lower(personas.name) = 'inugami'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Bringer of Misfortune', 1" +
                    " from personas  where lower(personas.name) = 'shiki-ouji'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Ascended Feline', 1" +
                    " from personas  where lower(personas.name) = 'nekomata'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Prankster Leader', 1" +
                    " from personas  where lower(personas.name) = 'high pixie'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Zealous Messenger', 1" +
                    " from personas  where lower(personas.name) = 'angel'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Twin-Headed Guardian', 1" +
                    " from personas  where lower(personas.name) = 'orthrus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Equine Sage', 1" +
                    " from personas  where lower(personas.name) = 'orobas'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Chivalrous Fiend', 0" +
                    " from personas  where lower(personas.name) = 'oni'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Chivalrous Guard', 1" +
                    " from personas  where lower(personas.name) = 'oni'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Human-Eating Lady', 1" +
                    " from personas  where lower(personas.name) = 'yaksini'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Jealous Lover', 1" +
                    " from personas  where lower(personas.name) = 'leanan sidhe'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Battle Fiend', 1" +
                    " from personas  where lower(personas.name) = 'rakshasa'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Defeated Avenger', 1" +
                    " from personas  where lower(personas.name) = 'take-minakata'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Floodbringer Demon', 1" +
                    " from personas  where lower(personas.name) = 'sui-ki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Tornado Devil', 1" +
                    " from personas  where lower(personas.name) = 'fuu-ki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Demonic Warrior/ Samurai Killer', 1" +
                    " from personas  where lower(personas.name) = 'kin-ki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Envoy of Slumber', 1" +
                    " from personas  where lower(personas.name) = 'sandman'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Thief of Tablets', 1" +
                    " from personas  where lower(personas.name) = 'anzu'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Cavern Snakeman', 1" +
                    " from personas  where lower(personas.name) = 'naga'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Slithering Snakewoman', 1" +
                    " from personas  where lower(personas.name) = 'lamia'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Chanting Baboon', 1" +
                    " from personas  where lower(personas.name) = 'thoth'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'She of Life and Death', 1" +
                    " from personas  where lower(personas.name) = 'isis'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Bearer of the Scales', 1" +
                    " from personas  where lower(personas.name) = 'anubis'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Raging God Bird', 1" +
                    " from personas  where lower(personas.name) = 'garuda'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Raging Bird God', 0" +
                    " from personas  where lower(personas.name) = 'garuda'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Coffin Borne God', 1" +
                    " from personas  where lower(personas.name) = 'mot'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Vicious Pentagram', 1" +
                    " from personas  where lower(personas.name) = 'decarabia'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Pulsing Mud', 1" +
                    " from personas  where lower(personas.name) = 'black ooze'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Awakened God', 1" +
                    " from personas  where lower(personas.name) = 'arahabaki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Rebellious Elephant', 1" +
                    " from personas  where lower(personas.name) = 'girimehkala'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Vampire Moth', 1" +
                    " from personas  where lower(personas.name) = 'mothman'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Woman who brings Ruin', 1" +
                    " from personas  where lower(personas.name) = 'lilim'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Dark Sun', 1" +
                    " from personas  where lower(personas.name) = 'mithras'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'The Shadowed One', 1" +
                    " from personas  where lower(personas.name) = 'scathach'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Wishless Star', 1" +
                    " from personas  where lower(personas.name) = 'kaiwan'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Cruel Leopard', 1" +
                    " from personas  where lower(personas.name) = 'ose'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Expressionless Beast', 1" +
                    " from personas  where lower(personas.name) = 'unicorn'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Mountain Girl', 1" +
                    " from personas  where lower(personas.name) = 'kikuri-hime'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Funerary Warrior', 1" +
                    " from personas  where lower(personas.name) = 'valkyrie'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Divine Warrior', 1" +
                    " from personas  where lower(personas.name) = 'power'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Auspicious Pachyderm', 1" +
                    " from personas  where lower(personas.name) = 'ganesha'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Midnight Queen', 1" +
                    " from personas  where lower(personas.name) = 'queen mab'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Life-Draining Spirit', 1" +
                    " from personas  where lower(personas.name) = 'kumbhanda'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Lamenting Sacrifice', 1" +
                    " from personas  where lower(personas.name) = 'kushinada-hime'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Dancing Witch', 1" +
                    " from personas  where lower(personas.name) = 'rangda'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Quaking Lady of Shadow', 1" +
                    " from personas  where lower(personas.name) = 'skadi'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Final Assessor', 1" +
                    " from personas  where lower(personas.name) = 'norn'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Snake King', 1" +
                    " from personas  where lower(personas.name) = 'raja naga'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Guard Dog of Hades', 1" +
                    " from personas  where lower(personas.name) = 'cerberus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Blood-Thirsty Demoness', 1" +
                    " from personas  where lower(personas.name) = 'dakini'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Strumming Veena Player', 1" +
                    " from personas  where lower(personas.name) = 'sarasvati'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Self-Infatuated Star', 1" +
                    " from personas  where lower(personas.name) = 'narcissus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Monarch of Snow', 1" +
                    " from personas  where lower(personas.name) = 'king frost'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Scandalous Queen', 1" +
                    " from personas  where lower(personas.name) = 'titania'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Destructive Beauty', 1" +
                    " from personas  where lower(personas.name) = 'parvati'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Monk of the Valley', 1" +
                    " from personas  where lower(personas.name) = 'kurama tengu'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Dancing Lion', 1" +
                    " from personas  where lower(personas.name) = 'barong'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Rhetorician of the Sea', 0" +
                    " from personas  where lower(personas.name) = 'forneus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Shadow Former Noble', 1" +
                    " from personas  where lower(personas.name) = 'forneus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Nimble Monkey King', 0" +
                    " from personas  where lower(personas.name) = 'hanuman'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Shadow TV President', 1" +
                    " from personas  where lower(personas.name) = 'hanuman'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Heretic Goat', 1" +
                    " from personas  where lower(personas.name) = 'baphomet'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Shadow IT President', 1" +
                    " from personas  where lower(personas.name) = 'oberon'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Unfaithful Dream King', 0" +
                    " from personas  where lower(personas.name) = 'oberon'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Shadow Politician Ooe', 1" +
                    " from personas  where lower(personas.name) = 'yamata-no-orochi'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Shadow Cleaner', 1" +
                    " from personas  where lower(personas.name) = 'ongyo-ki'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Harlot of Desire', 1" +
                    " from personas  where lower(personas.name) = 'lilith'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Wandering Reviver', 1" +
                    " from personas  where lower(personas.name) = 'nebiros'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Hedonistic Braggart', 1" +
                    " from personas  where lower(personas.name) = 'dionysus'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Pagan Saviour', 0" +
                    " from personas  where lower(personas.name) = 'melchizedek'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Pagan Avenger', 1" +
                    " from personas  where lower(personas.name) = 'melchizedek'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'The Black Avenger', 1" +
                    " from personas  where lower(personas.name) = 'chernobog'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Reviled Dictator', 1" +
                    " from personas  where lower(personas.name) = 'baal'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Thunder Emperor', 1" +
                    " from personas  where lower(personas.name) = 'thor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Missionary of Depravity', 1" +
                    " from personas  where lower(personas.name) = 'belial'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Throbbing King of Desires', 0" +
                    " from personas  where lower(personas.name) = 'mara'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Abyssal King of Avarice', 1" +
                    " from personas  where lower(personas.name) = 'abaddon'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Merciless Inquisitor', 1" +
                    " from personas  where lower(personas.name) = 'dominion'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'The Blackened Fury', 1" +
                    " from personas  where lower(personas.name) = 'kali'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Herald of Death', 1" +
                    " from personas  where lower(personas.name) = 'uriel'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Cleanser of Heaven', 1" +
                    " from personas  where lower(personas.name) = 'raphael'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Declarer of Anguish', 1" +
                    " from personas  where lower(personas.name) = 'gabriel'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Apocalyptic Guide', 1" +
                    " from personas  where lower(personas.name) = 'michael'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'regent'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Viscid Rotting Meat', 1" +
                    " from personas  where lower(personas.name) = 'slime'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Piggyback Demon', 1" +
                    " from personas  where lower(personas.name) = 'obariyon'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Hanging Tree Spirit', 1" +
                    " from personas  where lower(personas.name) = 'kodama'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Foolish Monk', 1" +
                    " from personas  where lower(personas.name) = 'koppa tengu'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Noisy Mountain Spirit', 1" +
                    " from personas  where lower(personas.name) = 'sudama'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'queen''s necklace'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Gathering Devil', 1" +
                    " from personas  where lower(personas.name) = 'choronzon'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'stone of scone'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Corpse-Eating Corpse', 1" +
                    " from personas  where lower(personas.name) = 'pisaca'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'koh-i-noor'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Fused Ghost', 1" +
                    " from personas  where lower(personas.name) = 'legion'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'orlov'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'emperor''s amulet'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'hope diamond'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Treasure Demon', 1" +
                    " from personas  where lower(personas.name) = 'crystal skull'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Drunken Serpents', 0" +
                    " from personas  where lower(personas.name) = 'yamata-no-orochi'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Crypt-Dwelling Pyromaniac', 1" +
                    " from personas  where lower(personas.name) = 'jack-o''-lantern'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Torn King of Desires', 1" +
                    " from personas  where lower(personas.name) = 'mara'");
            database.execSQL(" insert into  personaShadowNames (persona_id, shadow_name, isPrimary) select personas.id, 'Girl of the Hanging Tree', 1" +
                    " from personas  where lower(personas.name) = 'hua po'");
            database.setTransactionSuccessful();
            database.endTransaction();

            //remove all search suggestions for current personas
            database.execSQL("delete from searchSuggestions where suggest_intent_extra_data = 1");

            //insert search suggestions for personas with shadow names
            database.execSQL("insert into searchSuggestions (suggest_text_1, suggest_text_2, suggest_intent_data, suggest_intent_extra_data)\n" +
                    "select printf('%s (%s)', personas.name, personaShadowNames.shadow_name), personas.arcanaName, \n" +
                    "personas.id, 1\n" +
                    "from personas\n" +
                    "inner join personaShadowNames on personaShadowNames.persona_id = personas.id\n" +
                    "order by personas.name");

            //insert search suggestions for the rest of the personas without any shadow names
            database.execSQL("\n" +
                    "insert into searchSuggestions (suggest_text_1, suggest_text_2, suggest_intent_data, suggest_intent_extra_data)\n" +
                    "select personas.name, personas.arcanaName, personas.id, '1'\n" +
                    "from personas\n" +
                    "where not exists (\n" +
                    "\tselect 1 from personaShadowNames where personaShadowNames.persona_id = personas.id\n" +
                    ")\n" +
                    "order by personas.name");

            //create unique shadows index
            database.execSQL("create unique index unique_shadows " +
                    "on personaShadowNames (persona_id, shadow_name)");

            //update incorrect spelling of kushi mitama
            database.execSQL("update personas set name = 'Kushi Mitama'\n" +
                    "where name = 'Kusi Mitama'");

            //fix missing link for phoenix
            database.execSQL("update personas set imageurl = 'https://vignette.wikia.nocookie.net/megamitensei/images/d/d2/HoouSMT.jpg'\n" +
                    "where name = 'Phoenix'");

            //fix missing link for yamata
            database.execSQL("update personas set imageurl = 'https://s3-us-west-2.amazonaws.com/en-samurai-gamers-images/wp-content/uploads/2017/03/23064221/YamatanoOrochi_03_23_2017.jpg'\n" +
                    "where name = 'Yamata-no-Orochi'");

            cursor.close();
        }
    };
}
