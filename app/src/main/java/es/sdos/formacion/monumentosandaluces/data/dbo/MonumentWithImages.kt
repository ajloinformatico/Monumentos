package es.sdos.formacion.monumentosandaluces.data.dbo

import androidx.room.Embedded
import androidx.room.Relation
/**
 * RelationShips 1:m between MonumentBO and image
 */
data class MonumentWithImages (
    @Embedded val monumentDBO: MonumentDBO,
    @Relation(
     parentColumn = "id",
     entityColumn = "ownerId"
    )
    //List of images
    val images: List<ImageDBO>
)
