package com.sotatek.weather.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

typealias BI<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>(private val bi: BI<VB>) :
    AppCompatActivity() {
    private lateinit var binding: VB
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bi(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    abstract fun initView()

}