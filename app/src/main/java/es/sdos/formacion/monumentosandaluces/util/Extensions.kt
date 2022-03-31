package es.sdos.formacion.monumentosandaluces.util

import android.view.View
import es.sdos.formacion.monumentosandaluces.data.bbo.ImageBO
import es.sdos.formacion.monumentosandaluces.data.bbo.LocationBO
import es.sdos.formacion.monumentosandaluces.data.bbo.MonumentBO
import es.sdos.formacion.monumentosandaluces.data.bbo.MonumentBOwithImageBO
import es.sdos.formacion.monumentosandaluces.data.dbo.ImageDBO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentDBO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import es.sdos.formacion.monumentosandaluces.data.dto.ImageDTO
import es.sdos.formacion.monumentosandaluces.data.dto.LocationDTO
import es.sdos.formacion.monumentosandaluces.data.dto.MonumentDTO


fun View.setVisibility(value: Boolean) {
    if (value) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

/** Wrapper MonumentDTO to MonumentDBO */
fun MonumentDTO.toDBO(): MonumentDBO {
    val locationToBO: LocationDTO = this.locationDTO ?: LocationDTO(0.0,0.0)
    return MonumentDBO(
        id = this.id?:0L,
        name = this.name?:"",
        city = this.city?:"",
        description = Util.stringToNull(this.description),
        location = LocationBO(locationToBO.latitude?:0.0, locationToBO.longitude?:0.0),
        urlExtraInformation = Util.stringToNull(this.urlExtraInformation),
        isFavorite = false,
        isCreatedByUser = false
    )
}

fun MonumentDTO.toBO(): MonumentBO {
    val locationToBO: LocationDTO = this.locationDTO ?: LocationDTO(0.0,0.0)
    return MonumentBO(
        id = this.id?:0L,
        name = this.name?:"",
        city = this.city?:"",
        description = Util.stringToNull(this.description),
        location = LocationBO(locationToBO.latitude?:0.0,
            locationToBO.longitude?:0.0),
        urlExtraInformation = Util.stringToNull(this.urlExtraInformation),
        isFavorite = false,
        isCreatedByUser = false
    )
}


fun MonumentBO.toDBO(): MonumentDBO {
    return MonumentDBO(
        id = this.id,
        name = this.name,
        city = this.city,
        description = Util.stringToNull(this.description),
        location = this.location,
        urlExtraInformation = Util.stringToNull(this.urlExtraInformation),
        isFavorite = false,
        isCreatedByUser = this.isCreatedByUser
    )
}

fun ImageDTO.toBO(monumentId:Long): ImageBO{
    return ImageBO(
        id = this.id?:0L,
        url = this.url?:"",
        ownerId = monumentId

    )
}

fun ImageBO.toDBO(): ImageDBO {
    return ImageDBO(
        id = this.id,
        url = this.url?:"",
        ownerId = this.ownerId
    )
}

fun MonumentBOwithImageBO.toDBO(): MonumentWithImages {
    return MonumentWithImages(
        monumentDBO = this.monumentBO.toDBO(),
        images = this.imagesBO.map { it.toDBO() }
    )
}


fun ImageDTO.toDBO(monumentId: Long): ImageDBO {
    return ImageDBO(
        id = this.id?:0L,
        url = this.url?:"",
        ownerId = monumentId
    )
}

fun MonumentDBO.toBO(): MonumentBO =
    MonumentBO(
        id = this.id,
        name = this.name,
        city = this.city,
        description = this.description,
        location = this.location,
        urlExtraInformation = this.urlExtraInformation,
        isFavorite = this.isFavorite,
        isCreatedByUser = this.isCreatedByUser
    )

fun ImageDBO.toBO(): ImageBO =
    ImageBO(
        id = this.id,
        url = this.url,
        ownerId = this.ownerId
    )

fun MonumentWithImages.toBO(): MonumentBOwithImageBO {
    return MonumentBOwithImageBO(
        monumentBO = this.monumentDBO.toBO(),
        imagesBO = this.images.map { it.toBO() }
    )
}



