package es.sdos.formacion.monumentosandaluces.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentDBO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import es.sdos.formacion.monumentosandaluces.data.repository.Repository
import es.sdos.formacion.monumentosandaluces.util.toBO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonumentListViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    /**
     * Get repository by signletone
     */
    //private val repository: Repository = Repository
    //Save monument to delete
    private var _monumentWithImagesAfterDelete : MonumentWithImages?=null

    val monumentWithImagesAfterDelete: LiveData<MonumentWithImages>?=null

    /** Gel all monuments without images */
    val monumentsWithoutImages: LiveData<List<MonumentDBO>> =
        repository.getAllMonuments.asLiveData()

    /**
     * Get fav monuments
     */
    val favMonuments: LiveData<List<MonumentWithImages>> =
        repository.favMonuments.asLiveData()

    /**
     * Get my monuments
     */
    val myMonuments: LiveData<List<MonumentWithImages>> =
        repository.createdMonuments.asLiveData()

    /** Get all monuments with images ordered */
    fun getAllMonumentsWithImages(type: Int): LiveData<List<MonumentWithImages>>{
        return when (type) {
            0 -> {
                repository.getAllMonumentsWithImages.asLiveData()
            }
            1 -> {
                repository.getAllOrderById.asLiveData()
            }
            2 -> {
                repository.getAllOrderByName.asLiveData()
            }
            3 -> {
                repository.getAllOrderByNorthToSouth.asLiveData()
            }
            4 -> {
                repository.getAllOrderByEastToWest.asLiveData()
            }
            else -> {
                repository.getAllMonumentsWithImages.asLiveData()
            }
        }
    }

    /** Update like of monument */
    fun updateMonumentLike(id: Long) =
        viewModelScope.launch {
            repository.updateMonumentLike(id)
        }

    /** Get only one monument with his images */
    fun getOneMonumentWithImage(ownerId: Long) : LiveData<MonumentWithImages> =
        repository.getOneMonumentWithImage(ownerId).asLiveData()

    /** Delete a monument ny id*/
    fun deleteMonument(id: Long) {
        viewModelScope.launch {
            repository.deleteMonument(id)
        }
    }

    /** Save one monument after delete to recover this by snackbar*/
    fun setMonumentAfterDelete(id: Long) {
        viewModelScope.launch {
            _monumentWithImagesAfterDelete = repository.getOneMonumentWithImageStatic(id)
        }
    }

    /**
     * Add one monument after delete. Only if the user press cancel
     */
    fun addMonumentAfterDelete() {
        viewModelScope.launch {
            _monumentWithImagesAfterDelete?.let {
                addMonument(it)
            }
        }
    }

    /** Insert one monument with images*/
    private fun addMonument(monumentWithImages: MonumentWithImages) {
        viewModelScope.launch {
            repository.insertMonument(monumentWithImages.monumentDBO.toBO())
            monumentWithImages.images.forEach {
                repository.insertImage(it.toBO())
            }
        }
    }
}