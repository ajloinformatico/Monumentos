package es.sdos.formacion.monumentosandaluces.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.dbo.ImageDBO
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import es.sdos.formacion.monumentosandaluces.databinding.FragmentMonumentDetailBinding
import es.sdos.formacion.monumentosandaluces.ui.adapter.MonumentImagePageAdapter
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.MonumentListViewModel
import es.sdos.formacion.monumentosandaluces.util.Util
import es.sdos.formacion.monumentosandaluces.util.setVisibility

@AndroidEntryPoint
class MonumentDetailFragment() : Fragment(), OnMapReadyCallback {

    private var monumentId: Long? = null

    private lateinit var monument: MonumentWithImages
    private lateinit var binding: FragmentMonumentDetailBinding
    private var navController: NavController? = null
    private val MARKER_ZOOM: Float = 15f
    private val DURATION_ZOOM: Int = 100
    private lateinit var map: GoogleMap
    private lateinit var monumentMarkerPosition: LatLng
    private val viewModel: MonumentListViewModel by viewModels()
    private val safeArgs: MonumentDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        this.binding = FragmentMonumentDetailBinding.bind(
            inflater.inflate(R.layout.fragment_monument_detail, container, false)
        )
        configToolbar()
        this.navController = findNavController()
        this.monumentId = safeArgs.monumentId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.fragmentMonumentDetailToolbar.setOnClickListener {  }

        monumentId?.let { it ->
            viewModel.getOneMonumentWithImage(it).observe(viewLifecycleOwner, { monument ->
                this.monument = monument
                setContent(monument)
                initViewPager(monument.images)

                binding.fragmentMonumentDetailInfo.setVisibility(
                    !monument.monumentDBO.urlExtraInformation.isNullOrBlank()
                )
                binding.fragmentMonumentDetailFav.setOnClickListener {
                    viewModel.updateMonumentLike(monument.monumentDBO.id)
                }
                monument.monumentDBO.urlExtraInformation?.let { url ->
                    binding.fragmentMonumentDetailInfo.setOnClickListener {
                        val action = MonumentDetailFragmentDirections
                            .actionMonumentDetailFragmentToMonumentWebViewFragment(url)
                        navController?.navigate(action, Util.NAVIGATE_OPTIONS_RIGHT)
                    }
                }

                this.monumentMarkerPosition = LatLng(
                    monument.monumentDBO.location.latitude,
                    monument.monumentDBO.location.longitude
                )


                val mapFragment: SupportMapFragment = SupportMapFragment.newInstance()
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_monument_detail__map_fragment, mapFragment)
                        .commit()
                    mapFragment.getMapAsync(this)
                }
            })
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.map = p0
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        map.addMarker(
            MarkerOptions()
                .position(monumentMarkerPosition)
                .title(monument.monumentDBO.name)
        )

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(monumentMarkerPosition, MARKER_ZOOM)
            ,DURATION_ZOOM,
            null
        )

        map.uiSettings.setAllGesturesEnabled(false)
    }

    private fun configToolbar(){
        val menu = binding.fragmentMonumentDetailToolbar.menu
        menu.findItem(R.id.monument_menu__delete).setOnMenuItemClickListener {
            openDialog(this.monument.monumentDBO.id)
            true
        }
        menu.findItem(R.id.monument_menu__maps).setOnMenuItemClickListener {
            openMaps()
            true
        }
        menu.findItem(R.id.monument_menu__share).setOnMenuItemClickListener {
            sendGmail()
            true
        }
        menu.findItem(R.id.monument_menu__back).setOnMenuItemClickListener {
            navController?.popBackStack()
            true
        }
    }

    private fun initViewPager(images: List<ImageDBO>) {
        activity?.let{
            binding.fragmentMonumentDetailPagerImg.adapter = MonumentImagePageAdapter(images,it.applicationContext)
        }
    }

    private fun openMaps() {
        val mapIntent =  Intent(
            Intent.ACTION_VIEW, Uri.parse(
            getString(R.string.monument_detail__map_uri,
                (this.monument.monumentDBO.location.latitude.toString()),
                (this.monument.monumentDBO.location.longitude.toString()))
        ))
        mapIntent.setPackage(this.getString(R.string.google_map_package))
        if (activity?.let { mapIntent.resolveActivity(it.packageManager) } != null) {
            startActivity(mapIntent)
        }
    }

    private fun sendGmail() {
        val gmailIntent = Intent(Intent.ACTION_SEND)
        gmailIntent.data = Uri.parse(this.getString(R.string.monument_detail__email_uri_parse))
        gmailIntent.type = "text/plain"
        gmailIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.monument_detail__email_subject))
        gmailIntent.putExtra(
            Intent.EXTRA_TEXT,
            this.getString(R.string.monument_detail__email_message,
                this.monument.monumentDBO.name))
        startActivity(gmailIntent)
    }

    private fun openDialog(id: Long) {
        viewModel.setMonumentAfterDelete(id)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.monument_detail__delete_dialog_message)
            .setTitle(R.string.monument_detail__delete_dialog_title)
            .setPositiveButton(R.string.monument_detail__delete_dialog_action_no
            ){_, _ ->
                /*no-op*/
            }
            .setNegativeButton(R.string.monument_detail__delete_dialog_action_yes
            ){_, _ ->
                viewModel.deleteMonument(id)
                navController?.popBackStack()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setContent(monument: MonumentWithImages) {
        binding.fragmentMonumentDetailTitle.text = monument.monumentDBO.name
        binding.fragmentMonumentDetailDescription.text = monument.monumentDBO.description
        binding.fragmentMonumentDetailCity.text = monument.monumentDBO.city
        binding.fragmentMonumentDetailFav.setImageResource(
            if (monument.monumentDBO.isFavorite) {
                R.drawable.ic_start_true_favicon
            } else {
                R.drawable.ic_start_not_favicon
            }
        )
    }
}