package es.sdos.formacion.monumentosandaluces.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationDTO(
        @SerializedName("latitudE")
        val latitude: Double?,
        @SerializedName("longitude")
        val longitude: Double?
): Serializable
