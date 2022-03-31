package es.sdos.formacion.monumentosandaluces.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageDTO(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("url")
    val url: String? = ""
): Serializable
