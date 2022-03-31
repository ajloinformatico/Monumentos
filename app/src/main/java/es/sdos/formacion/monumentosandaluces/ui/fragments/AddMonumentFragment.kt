package es.sdos.formacion.monumentosandaluces.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.bbo.LocationBO
import es.sdos.formacion.monumentosandaluces.databinding.FragmentAddMonumentBinding
import es.sdos.formacion.monumentosandaluces.ui.adapter.MonumentAddImagesAdapter
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.AddMonumentViewModel
import es.sdos.formacion.monumentosandaluces.util.Util
import java.util.*

@AndroidEntryPoint
class AddMonumentFragment : Fragment(), OnMapReadyCallback, MonumentAddImagesAdapter.MonumentAddImagesAdapterListener{

    private val viewModelAdd: AddMonumentViewModel by viewModels()
    private var navController: NavController? = null
    private lateinit var map : GoogleMap
    private  var location: LatLng? = null
    private lateinit var binding: FragmentAddMonumentBinding
    private val safeArgs : AddMonumentFragmentArgs by navArgs()
    private var imageUrl : Uri? = null
    private  val contentValues by lazy {
        ContentValues()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentAddMonumentBinding.bind(
            inflater.inflate(R.layout.fragment_add_monument, container, false)
        )
        this.navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment = SupportMapFragment.newInstance()
        activity?.let { it ->
            it.supportFragmentManager.beginTransaction()
                .add(R.id.add_monument__map, mapFragment)
                .commit()
            mapFragment.getMapAsync(this)
        }

        //Note: Fix map scroll error
        fixMapScrolling(binding.addMonumentMapScroll, binding.addMonumentNatestedScroll)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MonumentAddImagesAdapter(this)
        binding.addMonumentRecyclerImages.layoutManager = linearLayoutManager
        binding.addMonumentRecyclerImages.adapter = adapter
        viewModelAdd.firstImage()

        viewModelAdd.getImageList().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        binding.addMonumentBtnSend.setOnClickListener{
            checkInputsAndUpdate()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.map = p0
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true

        if (this.safeArgs.latitude.isNotBlank() and this.safeArgs.longitude.isNotBlank()) {
            viewModelAdd.setState(false)
            this.location = LatLng(this.safeArgs.latitude.toDouble(), this.safeArgs.longitude.toDouble())
            location?.let { loc ->
                setMarkerAndZoom(map, loc)
            }

        }

        /** onclick to add monument*/
        map.setOnMapClickListener {
            map.clear()
            this.location = it
            location?.let { loc ->
                setMarkerAndZoom(map, loc)
            }
        }
    }

    /** Remove image from the preview */
    override fun onRemoveImage(position: Int) {
        viewModelAdd.deleteImage(position)
    }

    /** Images options dialog */
    override fun onItemAdd() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        builder?.let{
            it.setMessage(R.string.monument_list__alert_message)
                .setTitle(R.string.monument_add__image_intent_title)
                .setPositiveButton(R.string.monument_add__open_camera
                ) {_, _ ->
                    openCamera()
                }
                .setNegativeButton(R.string.monument_add__open_gallery
                ) {_, _ ->
                    openGallery()
                }
            }
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    /** set image */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == Util.SELECT_ACTIVITY_IMAGE && resultCode == Activity.RESULT_OK -> {
                imageUrl = data!!.data
                imageUrl?.let { uri ->
                    viewModelAdd.addNewImage(uri)
                }
            }

            requestCode == Util.TAKE_ACTIVITY_IMAGE && resultCode == Activity.RESULT_OK -> {
                imageUrl?.let { uri ->
                    viewModelAdd.addNewImage(uri)
                }
            }
        }
    }

    /** Insert monument i use the other viewmodel because here is where i work with databases */
    private fun checkInputsAndUpdate() {
        if (location == null) {
            this.location = LatLng(0.0, 0.0)
        }
        if (viewModelAdd.checkInputs(
            binding.addMonumentNameEditText.text.toString(),
            binding.addMonumentCityEditText.text.toString(),
            binding.addMonumentDescriptionEditText.text.toString(),
            binding.addMonumentUrlExtraInformationText.text.toString(),
                location?.latitude?.let {
                    location?.longitude?.let {
                        it1 -> LocationBO(it, it1)
                    }
                },
            binding.root
        )) {
            navController?.popBackStack()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun fixMapScrolling(mapScroll: ImageView, nestedScroll: NestedScrollView) {
        mapScroll.setOnTouchListener {_, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    nestedScroll.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    nestedScroll.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    nestedScroll.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> {
                    false
                }
            }
        }
    }

    /** Set a marker and do zoom transition */
    private fun setMarkerAndZoom(map: GoogleMap, location: LatLng){
        val imageOriginal = BitmapFactory.decodeResource(resources,R.drawable.ic_marker_add_monument)
        val imageFinal = Bitmap.createScaledBitmap(imageOriginal,48,64,false)
        map.addMarker(
                MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(imageFinal))
            )

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(location, 9f),
            2000,
            null
        )

        val message: String = if (binding.addMonumentNameLabel.text.isNotEmpty()){
            binding.addMonumentNameEditText.text.toString()
        } else "Monument"

        //Note: capitalize(Locale.ROOT) SUGGESTED BY IDE
        Snackbar.make(binding.root,
            message.capitalize(Locale.ROOT)+" coordinates added", Snackbar.LENGTH_SHORT).show()
    }

    /** Init gallery intent  */
    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, Util.SELECT_ACTIVITY_IMAGE)
    }

    /** init intent for camera intent */
    private fun openCamera(){
        contentValues.put(MediaStore.Images.Media.TITLE, "")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "")

        imageUrl = activity?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl)
        startActivityForResult(intent, Util.TAKE_ACTIVITY_IMAGE)
    }
}
