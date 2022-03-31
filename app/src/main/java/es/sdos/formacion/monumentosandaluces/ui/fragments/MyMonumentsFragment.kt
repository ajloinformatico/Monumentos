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
import es.sdos.formacion.monumentosandaluces.databinding.FragmentMyMonumentsBinding
import es.sdos.formacion.monumentosandaluces.ui.adapter.MyAndLikeMonumentsAdapter
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.MonumentListViewModel
import es.sdos.formacion.monumentosandaluces.util.Util

@AndroidEntryPoint
class MyMonumentsFragment : Fragment(), MyAndLikeMonumentsAdapter.MyAndLikeMonumentsAdapterListener {

    private val viewModel: MonumentListViewModel by viewModels()
    private lateinit var binding: FragmentMyMonumentsBinding
    private var navController: NavController? = null

    /** setHasOptionsMenu to quit the menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.binding = FragmentMyMonumentsBinding.bind(
            inflater.inflate(R.layout.fragment_my_monuments, container, false)
        )
        this.navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMyMonumentsBinding.bind(view)
        binding.myMonumentListProgressbar.visibility = View.GONE
        val adapter = MyAndLikeMonumentsAdapter(this)
        binding.myMonumentListRecycler.adapter = adapter
        viewModel.myMonuments.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            } else {
                binding.myMonumentListNoMonumentsTextView.visibility = View.VISIBLE
            }
            binding.myMonumentListProgressbar.visibility = View.GONE
        })

        binding.myMonumentListFbaAdMonument.setOnClickListener {

            val action = MyMonumentsFragmentDirections
                .actionMyMonumentsFragmentToAddMonumentFragment("","")
            navController?.navigate(action,Util.NAVIGATE_OPTIONS_RIGHT)
        }
    }

    /** Navigate to detail with the id from fragment callback */
    override fun onItemClicked(id: Long) {
        val action = MyMonumentsFragmentDirections
            .actionMyMonumentsFragmentToMonumentDetailFragment(id)
        navController?.navigate(action,Util.NAVIGATE_OPTIONS_RIGHT)
    }
}