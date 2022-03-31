package es.sdos.formacion.monumentosandaluces.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.sdos.formacion.monumentosandaluces.data.repository.Repository
import es.sdos.formacion.monumentosandaluces.util.toBO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeedersViewModel @Inject constructor(private val repository: Repository): ViewModel(){


    /** Get repository by signletone */
    //private val repository: Repository = Repository

    /** Check if images are inserted to make insert */
    fun getFromApi() {
        viewModelScope.launch {
            val monumentsFromApi = repository.monumentsFromApi()
            val monumentsOnList = repository.getMonumentsOnList()
            if (monumentsFromApi.isSuccessful) {
                val allMonumentsFromApi = monumentsFromApi.body()
                val monumentsIdFromDatabase = ArrayList<Long>()
                monumentsOnList.forEach {
                    monumentsIdFromDatabase.add(it.id)
                }
                allMonumentsFromApi?.monuments?.forEach { it ->
                    if (!monumentsIdFromDatabase.contains(it.id)) {
                        repository.insertMonument(it.toBO())
                        val ownerId = it.id

                        it.images?.forEach { imageDTO ->
                            repository.insertImage(imageDTO.toBO(ownerId?:0L))
                        }
                    }
                }
            }
        }
    }
}