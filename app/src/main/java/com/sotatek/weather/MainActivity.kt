package com.sotatek.weather

import android.content.pm.ActivityInfo
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.sotatek.weather.base.BaseActivity
import com.sotatek.weather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    override fun initView() {
        val navController = findNavController(R.id.navHostFragment)
        val navGraph = navController.graph
        navController.graph = navGraph
    }
}

