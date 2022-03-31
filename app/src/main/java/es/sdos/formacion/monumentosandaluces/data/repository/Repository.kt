package es.sdos.formacion.monumentosandaluces.data.repository

import androidx.annotation.WorkerThread
import es.sdos.formacion.monumentosandaluces.data.api.RetrofitInterface
import es.sdos.formacion.monumentosandaluces.data.bbo.ImageBO
import es.sdos.formacion.monumentosandaluces.data.bbo.MonumentBO
import es.sdos.formacion.monumentosandaluces.data.dao.ImageDAO
import es.sdos.formacion.monumentosandaluces.data.dao.MonumentDAO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentDBO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import es.sdos.formacion.monumentosandaluces.data.dto.ContainerDTO
import es.sdos.formacion.monumentosandaluces.util.toDBO
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

/**
 *  Repository by singleton
 */
class Repository @Inject constructor(private val retrofitInterface: RetrofitInterface, private val monumentDAO: MonumentDAO, private val imageDAO: ImageDAO){
    // val monumentApplication: MonumentApplication = MonumentApplication.instance
//    @Inject val database: AppDataBase = DatabaseModule.databaseProvider(@ApplicationContext)
//    // private val database: AppDataBase = AppDataBase.getInstance(monumentApplication)
//    @Inject val monumentDAO = DatabaseModule.provideMonumentDAO(database)
//    private val imageDAO = DatabaseModule.provideImageDAO(database)

    /** Execute by suspends getMonuments */
    suspend fun monumentsFromApi(): Response<ContainerDTO> =
     retrofitInterface.getMonumentsFromApi()

    //Note: Database Query's
    val getAllMonumentsWithImages: Flow<List<MonumentWithImages>> =
        monumentDAO.getMonumentsWithImages()

    val getAllOrderById: Flow<List<MonumentWithImages>> =
        monumentDAO.getAllOrderById()

    val getAllOrderByName: Flow<List<MonumentWithImages>> =
        monumentDAO.getAllOrderByName()

    val getAllOrderByNorthToSouth: Flow<List<MonumentWithImages>> =
        monumentDAO.getAllOrderByNorthToSouth()

    val getAllOrderByEastToWest: Flow<List<MonumentWithImages>> =
        monumentDAO.getAllOrderByEastToWest()

    val getAllMonuments: Flow<List<MonumentDBO>> =
        monumentDAO.getAllMonuments()

    fun getOneMonumentWithImage(ownerId: Long): Flow<MonumentWithImages> =
        monumentDAO.getOneMonumentWithImages(ownerId)

    suspend fun getOneMonumentWithImageStatic(id: Long): MonumentWithImages =
        monumentDAO.getMonumentsWithImagesStatic(id)

    suspend fun getMonumentsOnList() : List<MonumentDBO> =
        monumentDAO.getAllMonumentsOnList()

    val favMonuments : Flow<List<MonumentWithImages>> =
        monumentDAO.getFavMonuments()

    val createdMonuments: Flow<List<MonumentWithImages>> =
        monumentDAO.getCreatedMonuments()

    suspend fun updateMonumentLike(id: Long) =
        monumentDAO.updateLikes(id)

    suspend fun deleteMonument(id: Long) {
        monumentDAO.deleteMonument(id)
        imageDAO.deleteImage(id)
    }

    //Note: Database Insert
    @WorkerThread
    suspend fun insertMonument(monumentBO: MonumentBO): Long=
        monumentDAO.insertMonument(monumentBO.toDBO())

    @WorkerThread
    //Insert Image
    suspend fun insertImage(imageBO: ImageBO) =
        imageDAO.insertOneImages(imageBO.toDBO())

    @WorkerThread
    //Insert ListImages
    suspend fun insertListImages(imageBOList: List<ImageBO>) =
        imageDAO.insertListImages(imageBOList.map { it.toDBO() })
}

