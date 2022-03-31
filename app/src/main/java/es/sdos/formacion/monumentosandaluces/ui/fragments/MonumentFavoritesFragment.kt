package es.sdos.formacion.monumentosandaluces.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R
import es.sdos.formacion.monumentosandaluces.databinding.FragmentMonumentFavoritesBinding
import es.sdos.formacion.monumentosandaluces.ui.adapter.MyAndLikeMonumentsAdapter
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.MonumentListViewModel
import es.sdos.formacion.monumentosandaluces.util.Util

@AndroidEntryPoint
class MonumentFavoritesFragment : Fragment(), MyAndLikeMonumentsAdapter.MyAndLikeMonumentsAdapterListener {

    private val viewModel: MonumentListViewModel by viewModels()
    private lateinit var binding: FragmentMonumentFavoritesBinding
    private var navController: NavController? = null


    /**
     * To quit options menu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.binding = FragmentMonumentFavoritesBinding.bind(
            inflater.inflate(R.layout.fragment_monument_favorites, container, false)
        )
        this.navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MyAndLikeMonumentsAdapter(this)
        with (binding){
            this.monumentListRecyclerFavorites.adapter = adapter
            this.monumentListFavoritesProgressbar.visibility = View.VISIBLE
        }
        //Load fav monuments
        viewModel.favMonuments.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            } else {
                binding.monumentListFavoritesNoMonumentsTextView.visibility = View.VISIBLE
            }
            binding.monumentListFavoritesProgressbar.visibility = View.GONE
        })
    }

    /** Callback from adapter with id to load detail of monument */
    override fun onItemClicked(id: Long) {
        val action = MonumentFavoritesFragmentDirections
            .actionMonumentFavoritesFragmentToMonumentDetailFragment(id)
        navController?.navigate(action,Util.NAVIGATE_OPTIONS_RIGHT)
    }
}