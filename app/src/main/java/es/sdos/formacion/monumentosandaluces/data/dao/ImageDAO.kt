package es.sdos.formacion.monumentosandaluces.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.sdos.formacion.monumentosandaluces.data.dbo.ImageDBO

@Dao
interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOneImages(imageDBO: ImageDBO)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertListImages(imageDBOList: List<ImageDBO>)

    @Query("DELETE FROM images WHERE ownerId = :id")
    suspend fun deleteImage(id: Long)
}