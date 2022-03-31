package es.sdos.formacion.monumentosandaluces.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentDBO
import es.sdos.formacion.monumentosandaluces.databinding.FragmentAllMonumentsMapBinding
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.MonumentListViewModel
import es.sdos.formacion.monumentosandaluces.util.Util

@AndroidEntryPoint
class AllMonumentsMapFragment : Fragment(), GoogleMap.OnMarkerClickListener,
    GoogleMap.InfoWindowAdapter, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: FragmentAllMonumentsMapBinding
    private var navController: NavController? = null
    private var monumentSelected: MonumentDBO?=null
    private val mapFragment: SupportMapFragment? by lazy {
        childFragmentManager.findFragmentById(R.id.all_maps_fragment) as SupportMapFragment?
    }
    private val viewModel: MonumentListViewModel by viewModels()
    private var map: GoogleMap?=null
    private val callback = OnMapReadyCallback { googleMap ->
        this.map = googleMap
        checkPermissions()
        map?.let { map ->
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            map.uiSettings.isCompassEnabled = true
            setMarkers()

            //LOad add monument
            map.setOnMapLongClickListener {
                val action =
                    AllMonumentsMapFragmentDirections.actionAllMonumentsMapFragmentToAddMonumentFragment(
                        it.latitude.toString(), it.longitude.toString()
                    )
                navController?.navigate(action, Util.NAVIGATE_OPTIONS_RIGHT)
            }

            map.setOnMyLocationButtonClickListener(this)
            map.setOnMyLocationClickListener(this)
            map.setOnMarkerClickListener(this)
            map.setInfoWindowAdapter(this)
            map.setOnInfoWindowClickListener {
                monumentSelected?.let {
                    loadMonumentDetail(this.monumentSelected!!.id)
                }
            }
            map.setOnInfoWindowLongClickListener {
                createDialog(monumentSelected!!.id, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentAllMonumentsMapBinding.bind(
            inflater.inflate(R.layout.fragment_all_monuments_map, container, false)
        )
        this.navController = findNavController()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment?.getMapAsync(callback)
        binding.allMapsFbaFragmentGoBack.setOnClickListener {
            navController?.popBackStack()
        }

        binding.allMapsFbaFragmentGoDefault.setOnClickListener { setPositionDefault() }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(marker.position, Util.ZOOM_ON_MARKER),
            Util.ZOOM_DURATION,
            null
        )

        marker.showInfoWindow()
        binding.let {
            Util.showSnack(it.root, marker.title?:"",Snackbar.LENGTH_LONG)
        }

        return true
    }

    //Note: first call getInfoWindow. if it is null call getInfoWindow
    @SuppressLint("InflateParams")
    override fun getInfoWindow(marker: Marker): View {
        val v: View = layoutInflater.inflate(R.layout.widget_google_map_marker_info,null)
        this.viewModel.monumentsWithoutImages.observe(this, {
            this.monumentSelected = it.firstOrNull { monument ->
                monument.name == marker.title
            }

            if (monumentSelected != null) {
                v.findViewById<TextView>(R.id.google_map_info__title).text =
                    monumentSelected!!.name
                v.findViewById<TextView>(R.id.google_map_info__city).text =
                    monumentSelected!!.city

            } else {
                v.findViewById<TextView>(R.id.google_map_info__title).text = ""
                v.findViewById<TextView>(R.id.google_map_info__city).text = ""
            }
        })

        return v
    }

    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker): View?
            = layoutInflater.inflate(R.layout.widget_google_map_marker_info,null)

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(context, "${p0.latitude} ${p0.longitude}", Toast.LENGTH_LONG).show()
    }

    /** My location*/
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, R.string.map_load_user_location, Toast.LENGTH_SHORT)
            .show()
        return false
    }

    private fun createDialog(id: Long, marker: Marker) {
        viewModel.setMonumentAfterDelete(id)
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        builder?.let {
            it.setMessage(R.string.monument_list__alert_message)
                .setTitle(R.string.monument_common__alert_title)
                .setPositiveButton(R.string.monument_common__open
                ) {_, _ ->

                    this.loadMonumentDetail(id)
                }
                .setNegativeButton(R.string.monument_common__delete
                ) {_, _ ->

                    this.deleteMonument(id, marker)
                }
        }
        val dialog = builder?.create()
        dialog?.show()

    }



    /** Delete one monument*/
    private fun deleteMonument(id: Long, marker:Marker) {
        binding.let {
            val snack: Snackbar = Snackbar.make(it.root, R.string.monument_common__snackbar_delete, Snackbar.LENGTH_LONG)
            snack.setAction(R.string.cancel) {
                viewModel.addMonumentAfterDelete()
                this.map?.addMarker(MarkerOptions()
                    .position(marker.position)
                    .title(marker.title)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.monument_marker))
                )
            }
            snack.show()
            map?.clear()
            viewModel.deleteMonument(id)
            this.setPositionDefault()
        }
    }

    /** Action to monumentDetail*/
    private fun loadMonumentDetail(id: Long){
        val action = AllMonumentsMapFragmentDirections
            .actionAllMonumentsMapFragmentToMonumentDetailFragment(id)
        navController?.navigate(action)
    }

    private fun setMarkers(){
        this.viewModel.monumentsWithoutImages.observe(this, { listMonument ->
            listMonument.forEach {
                map?.addMarker(MarkerOptions()
                    .position(LatLng(it.location.latitude, it.location.longitude))
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.monument_marker))
                )
            }
        })
    }


    /** User location permission*/
    private fun checkPermissions() {
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } ==
            PackageManager.PERMISSION_GRANTED &&
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } ==
            PackageManager.PERMISSION_GRANTED) {

                map?.let { map ->
                map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
            }

        } else {
            Toast.makeText(activity, R.string.user_location_denied, Toast.LENGTH_SHORT).show()
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Util.LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /** Set default position*/
    private fun setPositionDefault() {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(Util.DEFAULT_LOCATION,Util.ZOOM_DEFAULT),
            Util.ZOOM_DURATION,null
        )
    }
}