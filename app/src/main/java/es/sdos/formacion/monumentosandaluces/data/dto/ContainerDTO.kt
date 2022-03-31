package es.sdos.formacion.monumentosandaluces.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** DTO Objects container List*/
data class ContainerDTO(
    @SerializedName("monuments")
    val monuments: List<MonumentDTO>
) :Serializable