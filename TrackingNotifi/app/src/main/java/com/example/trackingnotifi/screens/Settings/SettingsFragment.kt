package com.example.trackingnotifi.screens.Settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.trackingnotifi.R
import com.example.trackingnotifi.databinding.FragmentSettingsBinding
import com.example.trackingnotifi.databinding.NotifiFragmentBinding
import com.example.trackingnotifi.screens.ListOfNotifi.NotifiViewModel

class SettingsFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var tvSettings: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        tvSettings = binding.tvSettings

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        tvSettings.text = "This is settings!"
    }

}