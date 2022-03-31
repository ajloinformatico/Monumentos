package es.sdos.formacion.monumentosandaluces.data.dao

import androidx.room.*
import es.sdos.formacion.monumentosandaluces.data.dbo.*
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface MonumentDAO {

    @Query("SELECT * FROM monuments")
    fun getAllMonuments(): Flow<List<MonumentDBO>>

    @Query("SELECT * FROM monuments")
    suspend fun getAllMonumentsOnList(): List<MonumentDBO>

    @Query("SELECT * FROM monuments WHERE id = :monumentId")
    fun getMonumentById(monumentId : Long): Flow<List<MonumentDBO>>

    @Transaction
    @Query("SELECT * FROM monuments WHERE name = :name")
    suspend fun getMonumentWithImagesStaticByName(name: String): MonumentWithImages

    @Transaction
    @Query("SELECT * FROM monuments WHERE id = :id")
    suspend fun getMonumentsWithImagesStatic(id: Long): MonumentWithImages

    @Transaction
    @Query("SELECT * FROM monuments WHERE is_favorite = 1")
    fun getFavMonuments(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments WHERE is_created_by = 1")
    fun getCreatedMonuments(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments ORDER BY id")
    fun getAllOrderById(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments ORDER BY name")
    fun getAllOrderByName(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments ORDER BY latitude DESC")
    fun getAllOrderByNorthToSouth(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments ORDER BY longitude DESC")
    fun getAllOrderByEastToWest(): Flow<List<MonumentWithImages>>

    @Query("UPDATE MONUMENTS SET is_favorite = CASE WHEN is_favorite = 0 THEN (1) ELSE 0 END WHERE id = :id")
    suspend fun updateLikes(id: Long)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMonument(monument: MonumentDBO): Long

    @Query("SELECT * FROM images WHERE ownerId = :ownerId")
    fun getImagesFromMonumentId(ownerId: Long): Flow<List<ImageDBO>>

    @Transaction
    @Query("SELECT * FROM monuments")
    fun getMonumentsWithImages(): Flow<List<MonumentWithImages>>

    @Transaction
    @Query("SELECT * FROM monuments WHERE id = :id")
    fun getOneMonumentWithImages(id: Long): Flow<MonumentWithImages>

    @Query("DELETE  FROM monuments WHERE id = :id")
    suspend fun deleteMonument(id: Long)
}
