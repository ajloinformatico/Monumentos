package es.sdos.formacion.monumentosandaluces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.databinding.ActivityMainBinding
import es.sdos.formacion.monumentosandaluces.ui.viewmodel.SeedersViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var burgerButton: ActionBarDrawerToggle
    private val retrofitViewModel by lazy {
        ViewModelProvider(this).get(SeedersViewModel::class.java) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retrofitViewModel.getFromApi()
        //Binding and inflate
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Note: Set toolbar
        setSupportActionBar(binding.mainActivityCustomToolBar.customToolBar)
        //Note: Set NavController
        this.navController = Navigation.findNavController(this,
        R.id.main_activity__nav_host_fragment)

        //Set up navigationView by NavigationUI and setting navigation_view and navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.root)
        //Note config burgerButton
        setBurgerButton()
        //Note: Handle change destination to hidden toolba
        destinationChanges()
    }

    /** like OnNavigationItemSelected but with nav host */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            binding.root
        )
    }

    /**
     * Set toggle button on the toolbar to open side menu
     */
    private fun setBurgerButton(){
        this.burgerButton = ActionBarDrawerToggle(
            this,
            binding.root,
            binding.mainActivityCustomToolBar.customToolBar,
            R.string.drawer__open,
            R.string.drawer__closed
        )

        binding.root.addDrawerListener(burgerButton)
        burgerButton.syncState()
    }

    /** I use it to hidden default toolbar on monumentDetail */
    private fun destinationChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->


                if (destination.id == R.id.monumentDetailFragment || destination.id ==
                    R.id.monumentWebViewFragment) {
                    binding.mainActivityCustomToolBar.customToolBar.visibility = View.GONE
                } else {
                    binding.mainActivityCustomToolBar.customToolBar.visibility = View.VISIBLE
                    actionBar?.setHomeAsUpIndicator(R.drawable.ic_burger_menu)
                    actionBar?.title = destination.label
                }
        }
    }
}


