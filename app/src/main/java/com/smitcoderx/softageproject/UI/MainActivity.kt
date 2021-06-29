package com.smitcoderx.softageproject.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.smitcoderx.softageproject.Db.ProjectDatabase
import com.smitcoderx.softageproject.R
import com.smitcoderx.softageproject.Repository.ProjectRepository
import com.smitcoderx.softageproject.Utils.ViewModelProviderFactory
import com.smitcoderx.softageproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = ProjectRepository(ProjectDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ProjectViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.apply {
            bottomNav.setupWithNavController(navController)
        }
    }
}