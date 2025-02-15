package com.valify.registration.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.valify.registration.R
import com.valify.registration.databinding.ActivityHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityHostBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RegistrationSDK)
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(this)

    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(this)
    }
}