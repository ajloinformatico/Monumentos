package es.sdos.formacion.monumentosandaluces.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MonumentDTO (
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("citY")
    val city: String?,
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("urlExtraInformation")
    val urlExtraInformation: String? = "",
    @SerializedName("location")
    val locationDTO: LocationDTO?,
    @SerializedName("images")
    val images: List<ImageDTO>?
): Serializable