package es.sdos.formacion.monumentosandaluces.data.dbo

import androidx.room.*

/**
 * Image Domain Entity Class
 * Have m:1 relation with MonumentDBO
 */
@Entity(tableName = "images")
data class ImageDBO (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "url")
    val url: String,
    //Foreign
    val ownerId: Long,
)
