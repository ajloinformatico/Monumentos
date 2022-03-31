package es.sdos.formacion.monumentosandaluces.ui.fragments
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.data.dbo.MonumentWithImages
import es.sdos.formacion.monumentosandaluces.databinding.FragmentMonumentListBinding
import es.sdos.formacion.monumentosandaluces.ui.adapter.MonumentListAdapter
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.MonumentListViewModel
import es.sdos.formacion.monumentosandaluces.util.Util

@AndroidEntryPoint
class MonumentListFragment : Fragment(), MonumentListAdapter.MonumentListAdapterListener {

    private val viewModel: MonumentListViewModel by viewModels()
    private lateinit var binding: FragmentMonumentListBinding
    private var navController: NavController? = null
    private var monumentWithImagesAfterDelete: MonumentWithImages? = null
    private val adapter = MonumentListAdapter(this)
    private var stateOrder : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
        ): View {
        this.binding = FragmentMonumentListBinding.bind(inflater.inflate(R.layout.fragment_monument_list, container, false))
        this.navController = findNavController()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMonumentListBinding.bind(view)
        binding.monumentListRecycler.adapter = adapter
        binding.progressBarMonumentList.visibility = View.VISIBLE
        this.viewModel.getAllMonumentsWithImages(stateOrder?:0).observe(viewLifecycleOwner, { monument ->
                monument?.let {
                    adapter.submitList(monument)
            }
            binding.progressBarMonumentList.visibility = View.GONE
        })

        //Onclick listener for map monumentDTOS fragment
        binding.monumentListFragmentFabOpenMap.setOnClickListener {
            navController?.navigate(R.id.action_monumentListFragment_to_allMonumentsMapFragment,
                    null,
                    Util.NAVIGATE_OPTIONS_RIGHT)
        }

        viewModel.monumentWithImagesAfterDelete?.observe(viewLifecycleOwner, {
            this.monumentWithImagesAfterDelete = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.monument_list_menu,menu)
    }

    /** Order list of monuments */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.stateOrder = when (item.itemId) {
            R.id.monument_list_menu_order_by_id -> {
                1
            }
            R.id.monument_list_menu_order_by_name -> {
                2
            }
            R.id.monument_list_menu__order_by_north_south -> {
                3
            }
            R.id.monument_list_menu__order_by_east_west -> {
                4
            }
            else -> {
                -1
            }

        }
        if (stateOrder == -1){
            return false
        }
        viewModel.getAllMonumentsWithImages(stateOrder!!).observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        return true
    }


    /** Load monument detail by nav comp and listener on monumentList*/
    override fun onItemClicked(id: Long) {
        this.openDetail(id)
    }

    /** Dialog onLongCLick show dialog to open or delete*/
    override fun onLongItemClicked(id: Long) {
        this.createDialog(id)
    }

    /** Open monument detail*/
    private fun openDetail(id:Long){
        val action = MonumentListFragmentDirections
            .actionMonumentListFragmentToMonumentDetailFragment(id)
        navController?.navigate(action,Util.NAVIGATE_OPTIONS_RIGHT)
    }

    /** Fav and un fav click*/
    override fun onFavClick(id: Long) {
        viewModel.updateMonumentLike(id)
    }

    /** Create dialog*/
    private fun createDialog(monumentId: Long){
        viewModel.setMonumentAfterDelete(monumentId)
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            it.setMessage(R.string.monument_list__alert_message)
                .setTitle(R.string.monument_common__alert_title)
                .setPositiveButton(R.string.monument_common__open
                ) { _, _ ->
                    openDetail(monumentId)
                }
                .setNegativeButton(R.string.monument_common__delete,
                ) { _, _ ->
                    deleteMonument(monumentId)
                }
        }
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    /** Delete one*/
    private fun deleteMonument(id: Long){
        val snack: Snackbar = Snackbar.make(binding.root, R.string.monument_common__snackbar_delete, Snackbar.LENGTH_LONG)
        snack.setAction(R.string.cancel) {
            viewModel.addMonumentAfterDelete()
        }
        snack.show()
        viewModel.deleteMonument(id)
    }
}