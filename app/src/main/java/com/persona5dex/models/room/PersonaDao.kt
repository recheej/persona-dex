package com.persona5dex.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.persona5dex.models.*
import kotlinx.coroutines.yield

/**
 * Created by Rechee on 10/22/2017.
 */
@Dao
abstract class PersonaDao {
    @get:Query("select id, name, arcana, level, rare, dlc, gameId, party from personas order by name")
    @get:Transaction
    abstract val allPersonasForMainListLiveData: LiveData<List<MainListPersona>>

    @get:Transaction
    @get:Query("select id, name, arcana, level, rare, dlc, gameId, party from personas order by name")
    abstract val allPersonasForMainList: List<MainListPersona>

    @Query("select id, arcana, name, level, endurance, agility, strength, magic, luck, imageUrl, note, max, party " +
            "from personas where id = :personaID " +
            "order by name")
    abstract fun getDetailInfoForPersona(personaID: Int): LiveData<PersonaDetailInfo>

    @Query("select shadow_name as shadowName, isPrimary from personaShadowNames where persona_id = :personaID")
    abstract fun getShadowsForPersona(personaID: Int): LiveData<Array<PersonaShadowDetail>>

    @get:Query("select id, arcana, name, level, rare, dlc, special, gameId, party from personas order by level")
    abstract val personasByLevel: Array<PersonaForFusionService>

    @get:Query("select id, name, arcana, level, rare, dlc, gameId, party from personas where dlc = 1")
    @get:Transaction
    abstract val dLCPersonas: LiveData<List<MainListPersona>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPersonaFusion(personaFusion: PersonaFusion)

    @Query("select * from personaElements where persona_id = :personaID")
    abstract fun getElementsForPersona(personaID: Int): LiveData<List<PersonaElement>>

    @Query("select p1.name as leftPersonaName, p1.id as leftPersonaID, " +
            "p2.name as rightPersonaName, p2.id as rightPersonaID, " +
            "null as resultPersonaName, 0 as resultPersonaID " +
            "from personaFusions " +
            "inner join personas as p1 on p1.id = personaFusions.persona_one " +
            "inner join personas as p2 on p2.id = personaFusions.persona_two " +
            "where personaFusions.result = :personaID")
    abstract fun getEdgesToPersona(personaID: Int): LiveData<List<PersonaEdgeDisplay>>

    @Query("select p1.name as leftPersonaName, p1.id as leftPersonaID, " +
            "p2.name as rightPersonaName, p2.id as rightPersonaID, " +
            "p3.name as resultPersonaName, p3.id as resultPersonaID " +
            "from personaFusions " +
            "inner join personas as p1 on p1.id = personaFusions.persona_one " +
            "inner join personas as p2 on p2.id = personaFusions.persona_two " +
            "inner join personas as p3 on p3.id = personaFusions.result " +
            "where personaFusions.persona_one = :personaID or personaFusions.persona_two == :personaID")
    abstract fun getEdgesFromPersona(personaID: Int): LiveData<List<PersonaEdgeDisplay>>

    @Query("delete from personaFusions")
    abstract fun removeAllFusions()

    @Query("select name from personas where id = :personaID")
    abstract fun getPersonaName(personaID: Int): LiveData<String?>

    @Query("select special from personas where id = :personaID")
    abstract fun personaIsAdvanced(personaID: Int): LiveData<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPersonaFusions(personaFusion: List<PersonaFusion>)

    @Query("delete from personaFusions")
    abstract suspend fun deleteAllFromPersonaFusions()

    @Query("select id, name from personas")
    abstract suspend fun getAllNameViews(): Array<SimplePersonaNameView>

    @Transaction
    open suspend fun deleteAndInsertNewFusions(personaFusions: List<PersonaFusion>) {
        deleteAllFromPersonaFusions()
        yield()
        insertPersonaFusions(personaFusions)
    }
}