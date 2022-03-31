package es.sdos.formacion.monumentosandaluces.data.bbo

data class MonumentBO (
    val id: Long,
    val name: String,
    val city: String,
    val description: String?=null,
    val location: LocationBO,
    val urlExtraInformation: String?=null,
    val isFavorite: Boolean,
    val isCreatedByUser: Boolean
)