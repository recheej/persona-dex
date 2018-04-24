package com.persona5dex;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;

import com.persona5dex.models.room.PersonaDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MigrationTest {
    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;

    public MigrationTest() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                PersonaDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate2to3() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 2);

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.

        String molochPersonaName = "Moloch";
        db.execSQL("insert into personas (name, level, special, max, dlc, rare)\n" +
                "values (?, 1, 0, 0, 0, 0)", new String[] {molochPersonaName});

        db.execSQL("insert into personas (name, level, special, max, dlc, rare)\n" +
                "values (?, 1, 0, 0, 0, 0)", new String[] {"Yamata-no-Orochi"});

        // Prepare for the next version.
        db.close();

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 3, true, PersonaDatabase.MIGRATION_2_3);

        Cursor cursor = db.query("select psn.shadow_name, ss.suggest_text_1 from personas " +
                "inner join personashadownames psn on psn.persona_id = personas.id " +
                "inner join searchSuggestions ss on ss.suggest_intent_data = personas.id " +
                "where personas.name = ?", new String[] { molochPersonaName });

        cursor.moveToNext();
        String resultShadowName = cursor.getString(0);

        String expectedShadowName = "Sacrificial Pyrekeeper";
        assertEquals(expectedShadowName, resultShadowName);

        String expectedLineOne = String.format("%s (%s)", molochPersonaName, expectedShadowName);
        String suggestionLineOne = cursor.getString(1);

        assertEquals(expectedLineOne, suggestionLineOne);
    }
}
