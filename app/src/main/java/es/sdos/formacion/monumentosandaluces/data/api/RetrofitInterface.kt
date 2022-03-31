package es.sdos.formacion.monumentosandaluces.data.api

import es.sdos.formacion.monumentosandaluces.data.dto.ContainerDTO
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitInterface {

    /** Get all monuments */
    @GET("v3/be71040c-03e0-4f37-9f97-8604495e9ae1")
    suspend fun getMonumentsFromApi() : Response<ContainerDTO>
}