package com.persona5dex

import android.database.Cursor
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.persona5dex.models.GameType
import com.persona5dex.models.room.MIGRATION_2_3
import com.persona5dex.models.room.PersonaDatabase
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            PersonaDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    /**
     * We don't have migration from 3_4. We just want to use pre-populated data
     */
    @Test
    @Throws(IOException::class)
    fun migrate3to4() {
        var db = helper.createDatabase(TEST_DB, 3)
        db = helper.runMigrationsAndValidate(PersonaDatabase.DB_NAME, 4, true)

        var cursor = db.query("select * from personas")
        Assert.assertTrue(cursor.count > 0)

        // select some royal data to confirm things
        db.query("select gameid from personas where name = 'Cait Sith'").apply {
            moveToNext()
            val gameTypeValue = getInt(0)
            Assert.assertEquals(GameType.ROYAL.value, gameTypeValue)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate2to3() {
        var db = helper.createDatabase(TEST_DB, 2)

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
        val molochPersonaName = "Moloch"
        db.execSQL("""
    insert into personas (name, level, special, max, dlc, rare)
    values (?, 1, 0, 0, 0, 0)
    """.trimIndent(), arrayOf(molochPersonaName))
        db.execSQL("""
    insert into personas (name, level, special, max, dlc, rare)
    values (?, 1, 0, 0, 0, 0)
    """.trimIndent(), arrayOf("Yamata-no-Orochi"))

        // Prepare for the next version.
        db.close()

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)
        val cursor: Cursor = db.query("select psn.shadow_name, ss.suggest_text_1 from personas " +
                "inner join personashadownames psn on psn.persona_id = personas.id " +
                "inner join searchSuggestions ss on ss.suggest_intent_data = personas.id " +
                "where personas.name = ?", arrayOf(molochPersonaName))
        cursor.moveToNext()
        val resultShadowName: String = cursor.getString(0)
        val expectedShadowName = "Sacrificial Pyrekeeper"
        assertEquals(expectedShadowName, resultShadowName)
        val expectedLineOne = String.format("%s (%s)", molochPersonaName, expectedShadowName)
        val suggestionLineOne: String = cursor.getString(1)
        assertEquals(expectedLineOne, suggestionLineOne)
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}