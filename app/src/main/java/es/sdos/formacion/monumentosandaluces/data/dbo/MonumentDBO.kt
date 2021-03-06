package es.sdos.formacion.monumentosandaluces.data.dbo

import androidx.annotation.Nullable
import androidx.room.*
import es.sdos.formacion.monumentosandaluces.data.bbo.LocationBO
import java.io.Serializable

/**
 * MonumentDTO Domain Entity class
 * Have 1:m with ImagesDomain
 */
@Entity(tableName = "monuments", indices = [Index(value = ["name"], unique = true)])
data class MonumentDBO (
    //Note: Long primary key autogenerated
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "city")
    val city: String,

    @Nullable
    @ColumnInfo(name = "description")
    val description: String? = null,

    @Embedded val location: LocationBO,

    @Nullable
    @ColumnInfo(name = "url_extra_information")
    val urlExtraInformation: String? = null,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "is_created_by")
    val isCreatedByUser: Boolean
)