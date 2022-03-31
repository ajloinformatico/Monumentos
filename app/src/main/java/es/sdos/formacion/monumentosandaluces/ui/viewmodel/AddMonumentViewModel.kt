package es.sdos.formacion.monumentosandaluces.ui.viewmodel

import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import es.sdos.formacion.monumentosandaluces.data.bbo.ImageBO
import es.sdos.formacion.monumentosandaluces.data.bbo.LocationBO
import es.sdos.formacion.monumentosandaluces.data.bbo.MonumentBO
import es.sdos.formacion.monumentosandaluces.data.repository.Repository
import es.sdos.formacion.monumentosandaluces.util.Util
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMonumentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //private val repository: Repository = Repository
    private val imagesList = MutableLiveData<List<Uri>>()
    fun getImageList() : LiveData<List<Uri>>  = imagesList

    /**
     * Where comes the user if (form my monuments come back to muy monuments
     * if come back from monuments map return back to monument map)
     */
    private val _state = MutableLiveData<Boolean>()

    /**
     * set state to make the action to my monuments or to the map fragment
     * false to the map true to the user monuments
     */
    fun setState(boolean: Boolean) {
        _state.value = boolean
    }

    /** Add images Thanks to cris */
    fun addNewImage(uri: Uri){
        val imagesList = imagesList.value ?: listOf()
        val newImagesList: MutableList<Uri> = mutableListOf()
        newImagesList.addAll(imagesList)
        newImagesList.add(uri)
        this.imagesList.value = newImagesList
    }

    /** Delete an image */
    fun deleteImage(position: Int){
        val imagesList = imagesList.value?: listOf()
        val newImageList: MutableList<Uri> = mutableListOf()
        newImageList.addAll(imagesList)
        newImageList.removeAt(position)
        this.imagesList.value = newImageList
    }

    /** First insert */
    fun firstImage() {
        val uri: Uri = Uri.EMPTY
        this.addNewImage(uri)
    }

    /** Check inputs */
    fun checkInputs(
        name:String, city:String, description:String, urlExtraInformation: String?,
        location: LocationBO?, view:View): Boolean{
        val imageList: List<Uri> = getImageList().value?: emptyList()
        Log.d("TAG:::","HOLA")
        return when {
            name.isEmpty() -> {
                Util.showSnack(view, "Name is empty", Snackbar.LENGTH_LONG)
                false
            }
            name.length < 2 -> {
                Util.showSnack(view, "Name is not valid", Snackbar.LENGTH_LONG)
                false
            }
            city.isEmpty() -> {
                Util.showSnack(view, "City is empty", Snackbar.LENGTH_LONG)
                false
            }
            city.length < 2 -> {
                Util.showSnack(view, "City is not valid", Snackbar.LENGTH_LONG)
                false
            }
            description.isEmpty() -> {
                Util.showSnack(view, "Description is empty", Snackbar.LENGTH_LONG)
                false
            }
            description.length < 2 -> {
                Util.showSnack(view, "Description is not valid", Snackbar.LENGTH_LONG)
                false
            }
            location == null -> {
                Util.showSnack(view, "Location is not valid", Snackbar.LENGTH_LONG)
                false
            }
            location.latitude == 0.0 ||
                location.longitude == 0.0-> {
                Util.showSnack(view, "Location is not valid", Snackbar.LENGTH_LONG)
                false
            }
            imageList.size <= 1 -> {
                Util.showSnack(view, "Monument need images is not valid", Snackbar.LENGTH_LONG)
                false
            }
            else -> {
                //Execute insert
                if (insertMonumentWithImage(name, city, description, urlExtraInformation, location, imageList, view)){
                    true
                } else {
                    Util.showSnack(view, "Something was wrong", Snackbar.LENGTH_LONG)
                    false
                }
            }
        }
    }


    private fun insertMonumentWithImage(name: String, city: String, description: String, urlExtraInfotmation: String?,
                                        locationBO: LocationBO, images: List<Uri>, view: View): Boolean{
        val imagesBO = mutableListOf<ImageBO>()
        val monument = MonumentBO(
            id = 0L,
            name = name,
            city = city,
            description = description,
            urlExtraInformation = urlExtraInfotmation,
            location = locationBO,
            isFavorite = false,
            isCreatedByUser = true
        )
        viewModelScope.launch {
            try {
                val ownerId: Long =  repository.insertMonument(monument)
                images.forEach {
                    if (it.toString().isNotBlank()) {
                    imagesBO.add(
                        ImageBO(
                            id = 0L,
                            url = it.toString(),
                            ownerId = ownerId
                        )
                    )}
                }
                repository.insertListImages(imagesBO.toList())
            } catch (e: SQLiteConstraintException) {
                Util.showSnack(view, name + "is already in use", Snackbar.LENGTH_LONG)
            }
        }
        return true
    }
}